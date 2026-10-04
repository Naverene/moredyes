package net.neverandy.moredyes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neverandy.moredyes.tileentity.DyedSignBlockEntity;

/** A dyed oak sign on a wall. Its edit screen shows a plain oak sign. */
public class DyedWallSignBlock extends WallSignBlock
{
    public DyedWallSignBlock(Properties properties)
    {
        super(properties, WoodType.OAK);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DyedSignBlockEntity(pos, state);
    }
}
