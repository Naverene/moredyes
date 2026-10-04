package net.neverandy.moredyes.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** A vanilla sign, with its own type so dyed signs get their own renderer (client/DyedSignRenderer). */
public class DyedSignBlockEntity extends SignBlockEntity
{
    public DyedSignBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModTileEntities.SIGN.get(), pos, state);
    }
}
