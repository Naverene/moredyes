package net.neverandy.moredyes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A dyed small flower. It copies a vanilla flower's properties, so it is walk-through, see-through and set off-center
 * like one, but it is not a vanilla FlowerBlock, so it makes no suspicious stew.
 */
public class FlowerBlock extends BushBlock
{
    private static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);

    public FlowerBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        Vec3 offset = state.getOffset(level, pos);
        return SHAPE.move(offset.x, offset.y, offset.z);
    }

    /** Like vanilla flowers, and also on sand, dyed or not. */
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
    {
        return super.mayPlaceOn(state, level, pos) || isSand(state);
    }

    /** Sand, including the dyed sand, which isn't in the minecraft:sand tag. Dyed flowers can grow on it. */
    public static boolean isSand(BlockState state)
    {
        if (state.is(BlockTags.SAND))
        {
            return true;
        }
        for (Block sand : MDBlock.sandArray)
        {
            if (state.is(sand))
            {
                return true;
            }
        }
        return false;
    }
}
