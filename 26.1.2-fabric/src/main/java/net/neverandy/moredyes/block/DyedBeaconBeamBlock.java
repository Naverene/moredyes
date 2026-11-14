package net.neverandy.moredyes.block;

import net.minecraft.world.level.block.BeaconBeamBlock;

/**
 * A dyed block that colors a beacon beam passing through it, like vanilla stained glass. BeaconBeamBlock only allows
 * the 16 vanilla colors, so getColor gives the nearest of those and BeaconBlockEntityMixin puts in the exact color.
 */
public interface DyedBeaconBeamBlock extends BeaconBeamBlock {

    /** The opaque ARGB color the beam is tinted with. */
    int getBeaconColor();
}
