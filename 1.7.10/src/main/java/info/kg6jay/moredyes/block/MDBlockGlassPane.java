package info.kg6jay.moredyes.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.BlockInfo;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class MDBlockGlassPane extends BlockPane implements IBlockColored
{

    protected String[] blockColors;
    protected String blockName,colorSet,variant;
	private int blockIndex;
    @SideOnly(Side.CLIENT)
    protected IIcon[] iconsBlock,iconsPane;


    protected MDBlockGlassPane(String[] colors, BlockInfo info, String colorSet,int index,String variant)
    {
        super("","",info.blockMaterial,true);
		this.blockIndex=index;
        this.blockColors=colors;
        this.blockName=info.blockName;
        this.colorSet = colorSet;
        this.variant=variant;
        this.setHardness(info.hardness);
        this.setHarvestLevel(info.harvestTool, info.harvestLevel);
        this.setStepSound(info.sound);
        this.setResistance(info.resistance);
        char tmp = (char)(((int)this.blockName.charAt(0))-32);
        char tmpVar = (char)(((int)this.variant.charAt(0))-32);
        this.setBlockName(colorSet+"Mix"+tmp+this.blockName.substring(1)+tmpVar+this.variant.substring(1));
        this.setCreativeTab(info.tab);
    }
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return this.blockMaterial == Material.glass ? 41 : 18;
    }
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 1;
    }
    @SideOnly(Side.CLIENT)
    public IIcon func_150097_e()
    {
        return this.iconsPane[0];
    }

    /**
     * Return true if a player with Silk Touch can harvest this block directly, and not its normal drops.
     */
    protected boolean canSilkHarvest()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.iconsBlock = new IIcon[blockColors.length];
        this.iconsPane = new IIcon[blockColors.length];

        for (int i = 0; i < this.iconsBlock.length; ++i)
        {
            this.iconsBlock[i] = iconRegister.registerIcon(Reference.MOD_ID + ":"+blockName+"/" + this.variant + "/" + blockColors[i]);
            this.iconsPane[i] = iconRegister.registerIcon(Reference.MOD_ID + ":"+blockName+"/pane/" + blockColors[i]);
        }
    }

    public boolean canPaneConnectTo(IBlockAccess world, int x, int y, int z, ForgeDirection dir)
    {
        return canPaneConnectToBlock(world.getBlock(x, y, z)) ||
                world.isSideSolid(x, y, z, dir.getOpposite(), false);
    }
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        for (int i = 0; i < blockColors.length; ++i)
        {
        	list.add(new ItemStack(item, 1, i));
        }
    }
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
    	if(meta>=this.iconsBlock.length)
    	{
    		meta=0;
    	}
        return this.iconsBlock[meta];
    }
    public String getColorSet()
    {
    	return this.colorSet;
    }
    public int getMaxMeta()
    {
    	return this.blockColors.length-1;
    }
}
