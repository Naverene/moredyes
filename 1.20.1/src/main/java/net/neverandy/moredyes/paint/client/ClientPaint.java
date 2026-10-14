package net.neverandy.moredyes.paint.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.neverandy.moredyes.paint.PaintData;
import net.neverandy.moredyes.paint.PaintPacket;
import net.neverandy.moredyes.reference.Reference;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The painted blocks the client knows about, by chunk. The chunk renderer reads it from its worker threads (see
 * {@link PaintedModel}), so the maps are concurrent.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public final class ClientPaint
{
    private static final Map<Long, Map<Long, PaintData.Paint>> CHUNKS = new ConcurrentHashMap<>();

    private ClientPaint() {}

    /** The paint color (0xRRGGBB) of the block at a position, or -1 if it isn't painted. */
    public static int colorAt(BlockPos pos, BlockState state)
    {
        Map<Long, PaintData.Paint> chunk = CHUNKS.get(ChunkPos.asLong(pos.getX() >> 4, pos.getZ() >> 4));
        PaintData.Paint paint = chunk == null ? null : chunk.get(pos.asLong());
        return paint != null && state.is(paint.block()) ? paint.rgb() : -1;
    }

    public static void receive(PaintPacket packet)
    {
        Map<Long, PaintData.Paint> chunk = CHUNKS.computeIfAbsent(packet.chunk, key -> new ConcurrentHashMap<>());
        if (packet.full)
        {
            chunk.keySet().forEach(ClientPaint::redraw);
            chunk.clear();
        }
        packet.paints.forEach((pos, paint) ->
        {
            if (paint == null)
            {
                chunk.remove(pos);
            }
            else
            {
                chunk.put(pos, paint);
            }
            redraw(pos);
        });
    }

    private static void redraw(long pos)
    {
        BlockPos block = BlockPos.of(pos);
        Minecraft.getInstance().levelRenderer.setBlocksDirty(block.getX(), block.getY(), block.getZ(), block.getX(), block.getY(), block.getZ());
    }

    @SubscribeEvent
    public static void chunkUnloaded(ChunkEvent.Unload event)
    {
        if (event.getLevel().isClientSide())
        {
            CHUNKS.remove(event.getChunk().getPos().toLong());
        }
    }

    @SubscribeEvent
    public static void levelUnloaded(LevelEvent.Unload event)
    {
        if (event.getLevel().isClientSide())
        {
            CHUNKS.clear();
        }
    }
}
