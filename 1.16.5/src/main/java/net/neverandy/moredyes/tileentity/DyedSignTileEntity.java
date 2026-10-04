package net.neverandy.moredyes.tileentity;

import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;

/**
 * A vanilla sign, with its own type so dyed signs get their own renderer (client/DyedSignRenderer). The vanilla
 * sign always passes the vanilla sign type to its parent, so the type is given here instead.
 */
public class DyedSignTileEntity extends SignTileEntity
{
    @Override
    public TileEntityType<?> getType()
    {
        return ModTileEntities.SIGN.get();
    }
}
