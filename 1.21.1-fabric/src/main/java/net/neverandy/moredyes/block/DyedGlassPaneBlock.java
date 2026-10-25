package net.neverandy.moredyes.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.IronBarsBlock;
import net.neverandy.moredyes.api.MoreDyesAPI;
import net.neverandy.moredyes.color.MixColor;

/** A dyed glass pane. A beacon beam passing through it takes its color, like vanilla stained glass panes. */
public class DyedGlassPaneBlock extends IronBarsBlock implements DyedBeaconBeamBlock {

    private final int beaconColor;
    private final DyeColor nearest;

    public DyedGlassPaneBlock(Properties properties, MixColor color) {
        super(properties);
        this.beaconColor = DyedGlassBlock.beaconColor(color);
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
}
