package net.neverandy.moredyes.block;

import java.util.List;

import net.neverandy.moredyes.item.MDItemBlock;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.BlockInfo;
import net.minecraft.block.BlockFalling;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class FallingBlock extends BlockFalling implements IColoredBlock
{
	private String[] colors;
	private int set;
	public String blockName;
	public FallingBlock(String[] blockColors, BlockInfo info, int set)
	{
		super(info.blockMaterial);
		this.set=set;
		this.colors = blockColors;
		this.blockName = info.blockName;
		this.setHardness(info.hardness);
		this.setHarvestLevel(info.harvestTool,info.harvestLevel);
		this.setSoundType(info.sound);
		this.setCreativeTab(info.tab);
		this.setResistance(info.resistance);
		this.setRegistryName(info.blockName+"_"+set);
		this.setUnlocalizedName(info.blockName+"."+set);
		MDItemBlock itemBlock = new MDItemBlock(this);
		//initModel(info.blockName);
	}
	@SideOnly(Side.CLIENT)
	public void initModel(String name)
	{	
		for(int i=0;i<this.colors.length;i++)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),i,new ModelResourceLocation(Reference.MOD_ID+":"+name+"_"+this.colors[i],"inventory"));
		}
	}
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
	       for (int i = 0; i < this.colors.length; ++i)
	       {
	    	   list.add(new ItemStack(itemIn, 1, i));
	       }
    }
	@Override
	public String[] getColors()
	{
		return this.colors;
	}
	@Override
	public int getColorCount()
	{
		return this.colors.length;
	}
	@Override
	public String getColorName(ItemStack stack)
	{
		return this.colors[stack.getItemDamage()];
	}
}
