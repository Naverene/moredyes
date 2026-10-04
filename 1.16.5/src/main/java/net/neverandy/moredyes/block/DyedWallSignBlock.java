package net.neverandy.moredyes.block;

import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.neverandy.moredyes.tileentity.DyedSignTileEntity;

/** A dyed oak sign on a wall. Its edit screen shows a plain oak sign. */
public class DyedWallSignBlock extends WallSignBlock
{
    public DyedWallSignBlock(Properties properties)
    {
        super(properties, WoodType.OAK);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world)
    {
        return new DyedSignTileEntity();
    }
}
