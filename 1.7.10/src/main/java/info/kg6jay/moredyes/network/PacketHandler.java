package info.kg6jay.moredyes.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import info.kg6jay.moredyes.entity.SheepColor;
import info.kg6jay.moredyes.reference.Reference;

public class PacketHandler {

    public static SimpleNetworkWrapper channel;

    public static void initialize() {
        channel = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
        channel.registerMessage(MessageSheepColor.Handler.class, MessageSheepColor.class, 0, Side.CLIENT);
    }

    /** Sends a sheep's shade to every player who can see the sheep. */
    public static void sendSheepColor(Entity sheep, SheepColor color) {
        sendColor(sheep, color.get());
    }

    /** Sends a sheep's shade to one player who has just started seeing the sheep. */
    public static void sendSheepColor(Entity sheep, SheepColor color, EntityPlayerMP player) {
        sendColor(sheep, color.get(), player);
    }

    /** Sends the packed shade of a sheep's fleece or a wolf's collar to every player who can see it. */
    public static void sendColor(Entity entity, int color) {
        if (entity.worldObj instanceof WorldServer) {
            MessageSheepColor message = new MessageSheepColor(entity.getEntityId(), color);
            ((WorldServer) entity.worldObj).getEntityTracker()
                .func_151247_a(entity, channel.getPacketFrom(message));
        }
    }

    /** Sends the packed shade of a sheep's fleece or a wolf's collar to one player who has just started seeing it. */
    public static void sendColor(Entity entity, int color, EntityPlayerMP player) {
        channel.sendTo(new MessageSheepColor(entity.getEntityId(), color), player);
    }
}
