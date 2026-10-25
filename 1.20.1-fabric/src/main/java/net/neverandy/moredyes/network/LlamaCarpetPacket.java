package net.neverandy.moredyes.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neverandy.moredyes.reference.Reference;

/**
 * Tells a client which dyed carpet a llama wears, as a color index (-1 for none or a vanilla carpet). The client reads
 * it in client/ClientPackets.
 */
public class LlamaCarpetPacket
{
    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "llama_carpet");

    public final int entityId;
    public final int color;

    public LlamaCarpetPacket(int entityId, int color)
    {
        this.entityId = entityId;
        this.color = color;
    }

    public static void send(ServerPlayer player, int entityId, int color)
    {
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeVarInt(entityId);
        buffer.writeVarInt(color);
        ServerPlayNetworking.send(player, ID, buffer);
    }

    public static LlamaCarpetPacket decode(FriendlyByteBuf buffer)
    {
        return new LlamaCarpetPacket(buffer.readVarInt(), buffer.readVarInt());
    }
}
