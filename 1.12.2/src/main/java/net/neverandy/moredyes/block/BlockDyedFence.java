package net.neverandy.moredyes.block;

import javax.annotation.Nullable;

import net.minecraft.block.BlockFence;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityColor;
import net.neverandy.moredyes.utility.BlockInfo;

/**
 * A dyed wooden fence in every color, made from dyed planks of one wood. The color is kept in a tile entity (see
 * TileColor). It joins other wooden fences, fence gates and solid sides like the vanilla fence.
 */
public class BlockDyedFence extends BlockFence implements ITileColoredBlock
{
	private final String typeName;

	/** info is the one of the planks it is made of; name is the block's name ("birch_fence"). */
	public BlockDyedFence(BlockInfo info,String name)
	{
		super(info.blockMaterial,MapColor.WOOD);
		this.typeName=name;
		this.setHardness(info.hardness);
		if(info.resistance>=0.0f)
		{
			this.setResistance(info.resistance);
		}
		if(info.harvestTool!=null)
		{
			this.setHarvestLevel(info.harvestTool,info.harvestLevel);
		}
		this.setSoundType(info.sound);
		this.setRegistryName(Reference.MOD_ID,name);
		this.setUnlocalizedName(Reference.MOD_ID+"."+name);
		this.setCreativeTab(MoreDyes.tabShapes);
	}
	@Override
	public String getTypeName()
	{
		return this.typeName;
	}
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	@Override
	public TileEntity createTileEntity(World world,IBlockState state)
	{
		return new TileEntityColor();
	}
	@Override
	public void getDrops(NonNullList<ItemStack> drops,IBlockAccess world,BlockPos pos,IBlockState state,int fortune)
	{
		drops.add(TileColor.stack(this,world,pos,1));
	}
	@Override
	public boolean removedByPlayer(IBlockState state,World world,BlockPos pos,EntityPlayer player,boolean willHarvest)
	{
		if(willHarvest)
		{
			this.onBlockHarvested(world,pos,state,player);
			return true;
		}
		return super.removedByPlayer(state,world,pos,player,false);
	}
	@Override
	public void harvestBlock(World world,EntityPlayer player,BlockPos pos,IBlockState state,@Nullable TileEntity te,ItemStack stack)
	{
		super.harvestBlock(world,player,pos,state,te,stack);
		TileColor.harvested(world,pos);
	}
	@Override
	public ItemStack getItem(World world,BlockPos pos,IBlockState state)
	{
		return TileColor.stack(this,world,pos,1);
	}
	@Override
	public void getSubBlocks(CreativeTabs tab,NonNullList<ItemStack> items)
	{
		TileColor.getSubBlocks(this,items);
	}
}
