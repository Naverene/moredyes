package net.neverandy.moredyes.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.BlockInfo;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LeafBlock extends BlockLeaves
{
	private String color;
	private int index;
	public LeafBlock(String blockColor, BlockInfo info, int i)
	{
		super();
		this.index=i;
		this.leavesFancy=true;
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
		this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
		
	}
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),0,new ModelResourceLocation(Reference.MOD_ID+":"+this.getRegistryName().toString().substring(9),"inventory"));
	}
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {CHECK_DECAY, DECAYABLE});
    }
    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(DECAYABLE, Boolean.valueOf((meta & 4) == 0)).withProperty(CHECK_DECAY, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        if (!((Boolean)state.getValue(DECAYABLE)).booleanValue())
        {
            i |= 4;
        }
        if (((Boolean)state.getValue(CHECK_DECAY)).booleanValue())
        {
            i |= 8;
        }
        return i;
    }
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		 return java.util.Arrays.asList(new ItemStack(this, 1));
	}

	@Override
	public EnumType getWoodType(int meta)
	{
		// TODO Auto-generated method stub
		return EnumType.OAK;
	}
   @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
	    return Item.getItemFromBlock(MDBlock.sapling[this.index]);
    }

}
