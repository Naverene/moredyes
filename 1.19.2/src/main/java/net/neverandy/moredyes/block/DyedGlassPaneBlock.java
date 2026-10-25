package net.neverandy.moredyes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;

/** A dyed glass pane. A beacon beam passing through it takes its color, like vanilla stained glass panes. */
public class DyedGlassPaneBlock extends IronBarsBlock
{
    private final float[] beaconColor;

    /** hex is the dye color in the registry name, such as "334c59". */
    public DyedGlassPaneBlock(Properties properties, String hex)
    {
        super(properties);
        this.beaconColor = DyedGlassBlock.beaconColor(hex);
    }

    @Override
    public float[] getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos)
    {
        return this.beaconColor;
    }
}
