package net.neverandy.moredyes.block;

import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.neverandy.moredyes.tileentity.DyedSignTileEntity;

/** A dyed oak sign standing on the ground. Its edit screen shows a plain oak sign. */
public class DyedStandingSignBlock extends StandingSignBlock
{
    public DyedStandingSignBlock(Properties properties)
    {
        super(properties, WoodType.OAK);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world)
    {
        return new DyedSignTileEntity();
    }
}
