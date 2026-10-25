package net.neverandy.moredyes.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.TransparentBlock;
import net.neverandy.moredyes.api.MoreDyesAPI;
import net.neverandy.moredyes.color.MixColor;

/** Dyed glass. A beacon beam passing through it takes its color, like vanilla stained glass. */
public class DyedGlassBlock extends TransparentBlock implements DyedBeaconBeamBlock {

    private final int beaconColor;
    private final DyeColor nearest;

    public DyedGlassBlock(Properties properties, MixColor color) {
        super(properties);
        this.beaconColor = beaconColor(color);
        this.nearest = MoreDyesAPI.nearestVanillaColor(color.rgb());
    }

    @Override
    public int getBeaconColor() {
        return beaconColor;
    }

    @Override
    public DyeColor getColor() {
        return nearest;
    }

    /** The color a dye is drawn in, as the opaque ARGB color a beacon beam is tinted with. */
    public static int beaconColor(MixColor color) {
        return 0xFF000000 | MoreDyesAPI.displayColor(color.rgb());
    }
}
