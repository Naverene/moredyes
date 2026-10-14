package net.neverandy.moredyes.paint;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Tells a client about painted blocks in one chunk. A full packet replaces everything the client knows about the
 * chunk (sent when the player starts seeing it); otherwise it changes only the listed positions, and a null paint
 * means the paint was removed.
 */
public class PaintPacket
{
    public final long chunk;
    public final boolean full;
    public final Map<Long, PaintData.Paint> paints;

    public PaintPacket(ChunkPos chunk, boolean full, Map<Long, PaintData.Paint> paints)
    {
        this.chunk = chunk.toLong();
        this.full = full;
        this.paints = paints;
    }

    private PaintPacket(long chunk, boolean full, Map<Long, PaintData.Paint> paints)
    {
        this.chunk = chunk;
        this.full = full;
        this.paints = paints;
    }

    public static void encode(PaintPacket packet, FriendlyByteBuf buffer)
    {
        buffer.writeLong(packet.chunk);
        buffer.writeBoolean(packet.full);
        buffer.writeVarInt(packet.paints.size());
        packet.paints.forEach((pos, paint) ->
        {
            buffer.writeLong(pos);
            buffer.writeBoolean(paint != null);
            if (paint != null)
            {
                buffer.writeInt(paint.rgb());
                buffer.writeRegistryId(ForgeRegistries.BLOCKS, paint.block());
            }
        });
    }

    public static PaintPacket decode(FriendlyByteBuf buffer)
    {
        long chunk = buffer.readLong();
        boolean full = buffer.readBoolean();
        int size = buffer.readVarInt();
        Map<Long, PaintData.Paint> paints = new HashMap<>();
        for (int i = 0; i < size; i++)
        {
            long pos = buffer.readLong();
            if (buffer.readBoolean())
            {
                int rgb = buffer.readInt();
                Block block = buffer.readRegistryId();
                paints.put(pos, new PaintData.Paint(rgb, block));
            }
            else
            {
                paints.put(pos, null);
            }
        }
        return new PaintPacket(chunk, full, paints);
    }

    public static void handle(PaintPacket packet, Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> net.neverandy.moredyes.paint.client.ClientPaint.receive(packet)));
        context.get().setPacketHandled(true);
    }
}
