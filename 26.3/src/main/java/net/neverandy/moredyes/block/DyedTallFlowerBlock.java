package net.neverandy.moredyes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;

/** A dyed tall flower. Unlike vanilla flowers it can also be planted on sand, dyed sand included. */
public class DyedTallFlowerBlock extends TallFlowerBlock {

    public DyedTallFlowerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return super.mayPlaceOn(state, level, pos) || state.is(BlockTags.SAND);
    }
}
