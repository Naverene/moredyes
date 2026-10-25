package net.neverandy.moredyes.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Tells a client which dyed carpet a llama wears, as a color index (-1 for none or a vanilla carpet). */
public class LlamaCarpetPacket
{
    public final int entityId;
    public final int color;

    public LlamaCarpetPacket(int entityId, int color)
    {
        this.entityId = entityId;
        this.color = color;
    }

    public static void encode(LlamaCarpetPacket packet, FriendlyByteBuf buffer)
    {
        buffer.writeVarInt(packet.entityId);
        buffer.writeVarInt(packet.color);
    }

    public static LlamaCarpetPacket decode(FriendlyByteBuf buffer)
    {
        return new LlamaCarpetPacket(buffer.readVarInt(), buffer.readVarInt());
    }

    public static void handle(LlamaCarpetPacket packet, Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> net.neverandy.moredyes.client.ClientPackets.llamaCarpet(packet)));
        context.get().setPacketHandled(true);
    }
}
