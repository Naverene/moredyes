package info.kg6jay.moredyes.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.entity.SheepColors;

/**
 * Sends sheep colors to the players who see the sheep. Minecraft 1.4.7 cannot tell mods when a player starts seeing an
 * entity, so the client asks for the color of each sheep that appears, and the server answers (and sends every
 * change).
 */
public class PacketHandler implements IPacketHandler {

    public static final String CHANNEL = "MoreDyes";

    private static final byte ASK_SHEEP = 0, SHEEP_COLOR = 1;

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(packet.data));
        try {
            byte type = in.readByte();
            int entityId = in.readInt();
            if (type == ASK_SHEEP && player instanceof EntityPlayerMP) {
                EntityPlayerMP playerMP = (EntityPlayerMP) player;
                Entity entity = playerMP.worldObj.getEntityByID(entityId);
                if (entity instanceof EntitySheep) {
                    int color = SheepColors.get((EntitySheep) entity);
                    if (color != SheepColors.NONE) {
                        PacketDispatcher.sendPacketToPlayer(sheepColor(entityId, color), player);
                    }
                }
            } else if (type == SHEEP_COLOR) {
                MoreDyes.proxy.onSheepColor(entityId, in.readShort());
            }
        } catch (IOException e) {
            // A broken packet: ignore it.
        }
    }

    public static Packet250CustomPayload askSheep(int entityId) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        try {
            out.writeByte(ASK_SHEEP);
            out.writeInt(entityId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return PacketDispatcher.getPacket(CHANNEL, bytes.toByteArray());
    }

    public static Packet250CustomPayload sheepColor(int entityId, int color) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        try {
            out.writeByte(SHEEP_COLOR);
            out.writeInt(entityId);
            out.writeShort(color);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return PacketDispatcher.getPacket(CHANNEL, bytes.toByteArray());
    }
}
