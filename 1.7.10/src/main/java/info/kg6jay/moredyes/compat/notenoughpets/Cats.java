package info.kg6jay.moredyes.compat.notenoughpets;

import net.minecraft.entity.Entity;

import astrotibs.notenoughpets.entity.EntityOcelotNEP;

/** The parts of {@link NotEnoughPetsCompat} that load Not Enough Pets classes. */
final class Cats {

    private Cats() {}

    static boolean isCat(Entity entity) {
        return entity instanceof EntityOcelotNEP;
    }

    static void setCollarColor(Entity cat, int color) {
        ((EntityOcelotNEP) cat).setCollarColor(color);
    }
}
