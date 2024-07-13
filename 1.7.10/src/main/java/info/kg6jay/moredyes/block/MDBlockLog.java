package info.kg6jay.moredyes.block;

import java.util.Random;

import info.kg6jay.moredyes.handler.ConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.BlockInfo;

public class MDBlockLog extends MDBlockColoredMulti
{
    @SideOnly(Side.CLIENT)
    protected IIcon[] field_150167_a;
    @SideOnly(Side.CLIENT)
    protected IIcon[] field_150166_b;
	public MDBlockLog(String[] colors, BlockInfo info, String colorSet)
	{
		super(colors, info, colorSet);
	}
    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        byte b0 = 4;
        int i1 = b0 + 1;

        if (world.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1))
        {
            for (int j1 = -b0; j1 <= b0; ++j1)
            {
                for (int k1 = -b0; k1 <= b0; ++k1)
                {
                    for (int l1 = -b0; l1 <= b0; ++l1)
                    {
                        Block b = world.getBlock(x + j1, y + k1, z + l1);
                        if (b.isLeaves(world, x + j1, y + k1, z + l1))
                        {
                            b.beginLeavesDecay(world, x + j1, y + k1, z + l1);
                        }
                    }
                }
            }
        }
    }
    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z)
    {
        if(ConfigHandler.overrideDefaultMobSpawning)
        {
            return ConfigHandler.mobSpawnOnBlock;
        }
        else
        {
            return super.canCreatureSpawn(type, world, x, y, z);
        }
    }
    @Override
    public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public boolean isWood(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.topIcons = new IIcon[this.blockColors.length];
        this.sideIcons = new IIcon[this.blockColors.length];
        this.bottomIcons = new IIcon[this.blockColors.length];

        for (int i = 0; i < this.blockColors.length; ++i)
        {
            this.topIcons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":"+blockName+"/top/"+ blockColors[i]);
            this.sideIcons[i] = iconRegister.registerIcon(Reference.MOD_ID+":"+blockName+"/log_side");
        }
        this.bottomIcons=this.topIcons;
        
    }
}
