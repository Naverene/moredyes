package net.neverandy.moredyes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neverandy.moredyes.api.MoreDyesAPI;
import net.neverandy.moredyes.color.MixColor;

/** Dyed glass. A beacon beam passing through it takes its color, like vanilla stained glass. */
public class DyedGlassBlock extends TransparentBlock {

    private final int beaconColor;

    public DyedGlassBlock(Properties properties, MixColor color) {
        super(properties);
        this.beaconColor = beaconColor(color);
    }

    @Override
    public Integer getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
        return beaconColor;
    }

    /** The color a dye is drawn in, as the opaque ARGB color a beacon beam is tinted with. */
    public static int beaconColor(MixColor color) {
        return 0xFF000000 | MoreDyesAPI.displayColor(color.rgb());
    }
}
