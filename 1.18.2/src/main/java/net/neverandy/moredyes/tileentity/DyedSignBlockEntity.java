package net.neverandy.moredyes.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A vanilla sign, with its own type so dyed signs get their own renderer (client/DyedSignRenderer). The vanilla
 * sign always passes the vanilla sign type to its parent, so the type is given here instead.
 */
public class DyedSignBlockEntity extends SignBlockEntity
{
    public DyedSignBlockEntity(BlockPos pos, BlockState state)
    {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType()
    {
        return ModTileEntities.SIGN.get();
    }
}
