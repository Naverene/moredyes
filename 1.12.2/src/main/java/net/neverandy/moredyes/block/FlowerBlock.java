package net.neverandy.moredyes.block;

import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.BlockInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FlowerBlock extends BlockBush
{
	private String color;
	public FlowerBlock(String blockColor, BlockInfo info)
	{
    	super();
		this.color = blockColor;
		setHardness(info.hardness);
		setHarvestLevel(info.harvestTool,info.harvestLevel);
		setSoundType(info.sound);
		setCreativeTab(info.tab);
		setResistance(info.resistance);
		setRegistryName(info.blockName+"_"+this.color);
		setUnlocalizedName(info.blockName+"."+this.color);
		ItemBlock itemBlock = new ItemBlock(this);
		//initModel();
	}
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),0,new ModelResourceLocation(Reference.MOD_ID+":"+this.getRegistryName().toString().substring(9),"inventory"));
	}
    /**
     * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
     */
    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType()
    {
        return Block.EnumOffsetType.XZ;
    }
}