package net.neverandy.moredyes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neverandy.moredyes.color.MixColor;

/** A dyed glass pane. A beacon beam passing through it takes its color, like vanilla stained glass panes. */
public class DyedGlassPaneBlock extends IronBarsBlock {

    private final int beaconColor;

    public DyedGlassPaneBlock(Properties properties, MixColor color) {
        super(properties);
        this.beaconColor = DyedGlassBlock.beaconColor(color);
    }

    @Override
    public Integer getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
        return beaconColor;
    }
}
