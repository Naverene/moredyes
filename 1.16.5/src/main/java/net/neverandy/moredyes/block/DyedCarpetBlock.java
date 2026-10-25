package net.neverandy.moredyes.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

/**
 * A dyed carpet. It behaves like vanilla's CarpetBlock but isn't one: in 1.16 a llama draws any CarpetBlock in that
 * block's dye color, which a dyed carpet doesn't have, so client/DyedLlamaDecorLayer draws dyed carpets instead.
 */
public class DyedCarpetBlock extends Block
{
    private static final VoxelShape SHAPE = Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    public DyedCarpetBlock(AbstractBlock.Properties properties)
    {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Override
    public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos)
    {
        return !state.isValidPosition(world, pos) ? Blocks.AIR.getDefaultState()
                : super.updatePostPlacement(state, facing, facingState, world, pos, facingPos);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos)
    {
        return !world.isAirBlock(pos.down());
    }
}
