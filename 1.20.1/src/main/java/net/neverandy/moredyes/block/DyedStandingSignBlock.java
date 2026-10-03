package net.neverandy.moredyes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neverandy.moredyes.tileentity.DyedSignBlockEntity;
import net.neverandy.moredyes.tileentity.ModTileEntities;

/** A dyed oak sign standing on the ground. Its edit screen shows a plain oak sign. */
public class DyedStandingSignBlock extends StandingSignBlock
{
    public DyedStandingSignBlock(Properties properties)
    {
        super(properties, WoodType.OAK);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DyedSignBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        return createTickerHelper(type, ModTileEntities.SIGN.get(), SignBlockEntity::tick);
    }
}
