package net.neverandy.moredyes.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neverandy.moredyes.reference.Reference;

/** Tells a client the MoreDyes color of a sheep (-1 for a vanilla color). The client reads it in client/ClientPackets. */
public class SheepColorPacket
{
    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "sheep_color");

    public final int entityId;
    public final int color;

    public SheepColorPacket(int entityId, int color)
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

    public static SheepColorPacket decode(FriendlyByteBuf buffer)
    {
        return new SheepColorPacket(buffer.readVarInt(), buffer.readVarInt());
    }
}
