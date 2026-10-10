package info.kg6jay.moredyes.entity;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;

import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.network.PacketHandler;

/**
 * More Dyes colors on vanilla sheep. A vanilla sheep only has room for the 16 vanilla colors, so the color is kept in
 * the sheep's Forge data (saved with the sheep) and sent to the players who can see it. While a sheep has a color, its
 * vanilla color is the color's set (orange for the orange shades), so it still looks close without this mod.
 */
public final class SheepColors {

    public static final int NONE = -1;

    private static final String TAG_COLOR = "MoreDyesColor";
    /** Set once a new sheep has had its chance of spawning in a More Dyes color. */
    private static final String TAG_CHECKED = "MoreDyesChecked";

    private SheepColors() {}

    public static int get(EntitySheep sheep) {
        NBTTagCompound data = sheep.getEntityData();
        return data.hasKey(TAG_COLOR) ? Colors.clamp(data.getShort(TAG_COLOR)) : NONE;
    }

    /** Stores the color on this side only (used on the client when the server sends it). */
    public static void store(EntitySheep sheep, int color) {
        NBTTagCompound data = sheep.getEntityData();
        if (color == NONE) {
            data.removeTag(TAG_COLOR);
        } else {
            data.setShort(TAG_COLOR, (short) color);
        }
    }

    /** Colors the sheep and tells the players who can see it. Server only. */
    public static void set(EntitySheep sheep, int color) {
        store(sheep, color);
        if (color != NONE) {
            sheep.setFleeceColor(Colors.set(color));
        }
        if (sheep.worldObj instanceof WorldServer) {
            ((WorldServer) sheep.worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(sheep,
                PacketHandler.sheepColor(sheep.entityId, color));
        }
    }

    /** True the first time it is called for a sheep. */
    public static boolean firstCheck(EntitySheep sheep) {
        NBTTagCompound data = sheep.getEntityData();
        if (data.getBoolean(TAG_CHECKED)) {
            return false;
        }
        data.setBoolean(TAG_CHECKED, true);
        return true;
    }
}
