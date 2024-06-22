package info.kg6jay.moredyes.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.BlockInfo;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class MDBlockColored extends Block implements IBlockColored
{
    @SideOnly(Side.CLIENT)
    protected IIcon[] icons;
    protected String[] blockColors;
    protected String blockName,colorSet;

    public MDBlockColored(String[] colors,BlockInfo info,String colorSet)
    {
        super(info.blockMaterial);
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

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
    	if(meta>this.getMaxMeta())
    	{
    		meta=0;
    	}
        return this.icons[meta];
    }
    
    public String getColorSet()
    {
    	return this.colorSet;
    }
    public int getMaxMeta()
    {
    	return this.blockColors.length-1;
    }
    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int meta)
    {
        return meta;
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

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.icons = new IIcon[blockColors.length];

        for (int i = 0; i < this.icons.length; ++i)
        {
            this.icons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":"+blockName+"/"+ blockColors[i]);
        }
    }
}