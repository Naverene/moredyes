package info.kg6jay.moredyes.entity;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.network.PacketHandler;

/**
 * The More Dyes color of a tamed wolf's collar. Vanilla collars only know the 16 vanilla colors, so the color is kept
 * in the wolf's Forge data (saved with it) and sent to the players who can see it, the same way as a sheep's (see
 * {@link SheepColors}).
 */
public final class WolfCollars {

    private static final String TAG_COLOR = "MoreDyesCollar";

    private WolfCollars() {}

    public static int get(EntityWolf wolf) {
        NBTTagCompound data = wolf.getEntityData();
        return data.hasKey(TAG_COLOR) ? Colors.clamp(data.getShort(TAG_COLOR)) : SheepColors.NONE;
    }

    /** Stores the color on this side only (used on the client when the server sends it). */
    public static void store(EntityWolf wolf, int color) {
        NBTTagCompound data = wolf.getEntityData();
        if (color == SheepColors.NONE) {
            data.removeTag(TAG_COLOR);
        } else {
            data.setShort(TAG_COLOR, (short) color);
        }
    }

    /** Colors the collar and tells the players who can see the wolf. Server only. */
    public static void set(EntityWolf wolf, int color) {
        store(wolf, color);
        if (wolf.worldObj instanceof WorldServer) {
            ((WorldServer) wolf.worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(wolf,
                PacketHandler.sheepColor(wolf.entityId, color));
        }
    }
}
