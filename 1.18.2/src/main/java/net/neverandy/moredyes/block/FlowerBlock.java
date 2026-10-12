package net.neverandy.moredyes.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.BlockGetter;
import net.neverandy.moredyes.utility.BlockInfo;

import net.minecraft.world.level.block.state.BlockBehaviour.OffsetType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class FlowerBlock extends BushBlock
{
    private static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);

    public FlowerBlock(BlockInfo info)
    {
        super(Properties.of(info.blockMaterial)
                .strength(info.hardness,info.resistance)
                .sound(info.sound)
                .lightLevel(value -> info.lightlevel)
                // Like vanilla flowers: walk-through and see-through. Without notSolid the flower blocks the light
                // in its own space and is drawn black.
                .noCollission()
                .noOcclusion());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
    {
        Vec3 offset = state.getOffset(world, pos);
        return SHAPE.move(offset.x, offset.y, offset.z);
    }

    @Override
    public BlockBehaviour.OffsetType getOffsetType()
    {
        return OffsetType.XZ;
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
