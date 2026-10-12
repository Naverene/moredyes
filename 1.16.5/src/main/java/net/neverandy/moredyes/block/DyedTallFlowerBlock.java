package net.neverandy.moredyes.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

/** A dyed tall flower. Unlike vanilla ones it can also be planted on sand, vanilla or dyed. */
public class DyedTallFlowerBlock extends TallFlowerBlock
{
    public DyedTallFlowerBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader world, BlockPos pos)
    {
        return super.isValidGround(state, world, pos) || FlowerBlock.isSand(state);
    }
}
