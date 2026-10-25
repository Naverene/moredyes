package info.kg6jay.moredyes.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.nbt.NBTTagCompound;

import info.kg6jay.moredyes.compat.notenoughpets.NotEnoughPetsCompat;

/**
 * The More Dyes shade of a tamed pet's collar. Vanilla 1.7.10 only gives wolves a collar, and Not Enough Pets gives
 * its cats one too (see {@link NotEnoughPetsCompat}). Both only know the 16 vanilla colors, so the shade is kept in
 * the pet's Forge data (saved with it), packed the same way as a sheep's (see {@link SheepColor}).
 */
public final class PetCollar {

    private static final String KEY = "MoreDyesCollar";

    private PetCollar() {}

    /** Whether the entity is a pet with a dyeable collar: a wolf, or a Not Enough Pets cat. */
    public static boolean hasCollar(Entity entity) {
        return entity instanceof EntityWolf || NotEnoughPetsCompat.isCat(entity);
    }

    /** The pet's packed collar shade, or {@link SheepColor#NONE}; NONE for anything without a collar. */
    public static int get(Entity entity) {
        if (!hasCollar(entity)) {
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
    public static void put(Entity pet, int color) {
        NBTTagCompound data = pet.getEntityData();
        if (color == SheepColor.NONE) {
            data.removeTag(KEY);
            return;
        }
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte("Set", (byte) SheepColor.setOf(color));
        tag.setByte("Shade", (byte) SheepColor.shadeOf(color));
        data.setTag(KEY, tag);
    }

    /** Gives the pet a vanilla collar color (0 to 15, as wool metadata). */
    public static void setVanillaColor(EntityTameable pet, int color) {
        if (pet instanceof EntityWolf) {
            ((EntityWolf) pet).setCollarColor(color);
        } else {
            NotEnoughPetsCompat.setCollarColor(pet, color);
        }
    }
}
