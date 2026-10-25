package net.neverandy.moredyes.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.IronBarsBlock;
import net.neverandy.moredyes.api.MoreDyesAPI;

/** A dyed glass pane. A beacon beam passing through it takes its color, like vanilla stained glass panes. */
public class DyedGlassPaneBlock extends IronBarsBlock implements DyedBeaconBeamBlock
{
    private final float[] beaconColor;
    private final DyeColor nearest;

    /** hex is the dye color in the registry name, such as "334c59". */
    public DyedGlassPaneBlock(Properties properties, String hex)
    {
        super(properties);
        this.beaconColor = DyedGlassBlock.beaconColor(hex);
        this.nearest = MoreDyesAPI.nearestVanillaColor(Integer.parseInt(hex, 16));
    }

    @Override
    public float[] getBeaconColor()
    {
        return this.beaconColor;
    }

    @Override
    public DyeColor getColor()
    {
        return this.nearest;
    }
}
