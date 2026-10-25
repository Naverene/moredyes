package info.kg6jay.moredyes.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.nbt.NBTTagCompound;

/**
 * The More Dyes shade of a tamed wolf's collar. Vanilla collars only know the 16 vanilla colors, so the shade is kept
 * in the wolf's Forge data (saved with it), packed the same way as a sheep's (see {@link SheepColor}).
 */
public final class WolfCollar {

    private static final String KEY = "MoreDyesCollar";

    private WolfCollar() {}

    /** The wolf's packed collar shade, or {@link SheepColor#NONE}; NONE for anything that is not a wolf. */
    public static int get(Entity entity) {
        if (!(entity instanceof EntityWolf)) {
            return SheepColor.NONE;
        }
        NBTTagCompound data = entity.getEntityData();
        if (!data.hasKey(KEY)) {
            return SheepColor.NONE;
        }
        NBTTagCompound tag = data.getCompoundTag(KEY);
        return SheepColor.pack(tag.getByte("Set"), tag.getByte("Shade"));
    }

    /** Sets the packed collar shade, or clears it with {@link SheepColor#NONE}. Does not tell anyone. */
    public static void put(EntityWolf wolf, int color) {
        NBTTagCompound data = wolf.getEntityData();
        if (color == SheepColor.NONE) {
            data.removeTag(KEY);
            return;
        }
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte("Set", (byte) SheepColor.setOf(color));
        tag.setByte("Shade", (byte) SheepColor.shadeOf(color));
        data.setTag(KEY, tag);
    }
}
