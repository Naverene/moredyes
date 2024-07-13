package info.kg6jay.moredyes.block;

import java.util.List;

import info.kg6jay.moredyes.handler.ConfigHandler;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.BlockInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class MDBlockWorkbench extends BlockWorkbench implements IBlockColored
{
    protected String[] blockColors;
    protected String blockName,colorSet;
	public IIcon[] topIcons,sideIcons,frontIcons,bottomIcons;
	public MDBlockWorkbench(String[] colors, BlockInfo info,String colorSet)
	{
		this.blockColors=colors;
		this.colorSet=colorSet;
        this.blockColors=colors;
        this.blockName=info.blockName;
        this.colorSet = colorSet;
        this.setHardness(info.hardness);
        this.setHarvestLevel(info.harvestTool, info.harvestLevel);
        this.setStepSound(info.sound);
        this.setResistance(info.resistance);
        char tmp = (char)(((int)this.blockName.charAt(0))-32);
        this.setBlockName(colorSet+"Mix"+tmp+this.blockName.substring(1));
        this.setCreativeTab(info.tab);
	}
    public int damageDropped(int meta)
    {
        return meta;
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

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        for (int i = 0; i < blockColors.length; ++i)
        {
        	list.add(new ItemStack(item, 1, i));
        }
    }
    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
    	if(meta>=this.sideIcons.length)
    	{
    		meta=0;
    	}
        return switch (side) {
            case 0 -> this.bottomIcons[meta];
            case 1 -> this.topIcons[meta];
            case 2 -> this.frontIcons[meta];
            default -> this.sideIcons[meta];
        };
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.topIcons = new IIcon[blockColors.length];
        this.bottomIcons = new IIcon[blockColors.length];
        this.sideIcons = new IIcon[blockColors.length];
        this.frontIcons = new IIcon[blockColors.length];

        for (int i = 0; i < this.topIcons.length; ++i)
        {
            this.topIcons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":"+blockName+"/top/"+ blockColors[i]);
            this.bottomIcons[i] = iconRegister.registerIcon(Reference.MOD_ID + "plank/top/"+ blockColors[i]);
            this.frontIcons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":"+blockName+"/front/"+ blockColors[i]);
            this.sideIcons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":"+blockName+"/side/"+ blockColors[i]);
        }
    }
	@Override
	public String getColorSet()
	{
		return this.colorSet;
	}

	@Override
    public int getMaxMeta()
    {
    	return this.blockColors.length-1;
    }
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (world.isRemote)
        {
            player.displayGUIWorkbench(x, y, z);
        }
        return true;
    }
}
