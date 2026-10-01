package net.neverandy.moredyes.block;

import java.util.List;

import net.neverandy.moredyes.item.MDItemBlock;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.BlockInfo;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BasicBlock extends Block implements IColoredBlock
{
	private String[] colors;
	public String blockName;
	public BasicBlock(String[] blockColor, BlockInfo info, int set)
	{
		super(info.blockMaterial);
		this.colors = blockColor;
		this.blockName=info.blockName;
		setHardness(info.hardness);
		setHarvestLevel(info.harvestTool,info.harvestLevel);
		setSoundType(info.sound);
		setCreativeTab(info.tab);
		setResistance(info.resistance);
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
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),i,new ModelResourceLocation(Reference.MOD_ID+":"+name+"_"+ this.colors[i],"inventory"));
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