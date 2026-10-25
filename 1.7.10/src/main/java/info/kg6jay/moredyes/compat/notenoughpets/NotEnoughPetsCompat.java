package info.kg6jay.moredyes.compat.notenoughpets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;

import cpw.mods.fml.common.Loader;
import info.kg6jay.moredyes.reference.Reference;

/**
 * Not Enough Pets (AstroTibs) gives 1.7.10 cats a dyeable collar, like modern Minecraft. With it installed, More Dyes
 * dyes recolor those collars too (see entity/PetCollar). Its wolves are subclasses of the vanilla wolf, so they work
 * like vanilla ones; only their renderer needs swapping (see client/NotEnoughPetsClient). Its classes are only
 * touched through {@link Cats} when the mod is loaded.
 */
public final class NotEnoughPetsCompat {

    public static final boolean LOADED = Loader.isModLoaded(Reference.NOT_ENOUGH_PETS);

    private NotEnoughPetsCompat() {}

    /** Whether the entity is a Not Enough Pets cat (they all have a collar once tamed). */
    public static boolean isCat(Entity entity) {
        return LOADED && Cats.isCat(entity);
    }

    /** Gives a Not Enough Pets cat a vanilla collar color (0 to 15, as wool metadata). */
    public static void setCollarColor(EntityTameable pet, int color) {
        if (isCat(pet)) {
            Cats.setCollarColor(pet, color);
        }
    }
}
