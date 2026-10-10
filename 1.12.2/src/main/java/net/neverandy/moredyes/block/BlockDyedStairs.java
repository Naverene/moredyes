package net.neverandy.moredyes.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
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

/** Dyed stairs in every color, made from a dyed block. The color is kept in a tile entity (see TileColor). */
public class BlockDyedStairs extends BlockStairs implements ITileColoredBlock
{
	private final String typeName;

	/** base is one block of the dyed kind the stairs are made of; it gives their hardness, sound and look. */
	public BlockDyedStairs(Block base,BlockInfo info,String name)
	{
		super(base.getDefaultState());
		this.typeName=name;
		if(info.harvestTool!=null)
		{
			this.setHarvestLevel(info.harvestTool,info.harvestLevel);
		}
		this.setRegistryName(Reference.MOD_ID,name);
		this.setUnlocalizedName(Reference.MOD_ID+"."+name);
		this.setCreativeTab(MoreDyes.tabShapes);
		this.useNeighborBrightness=true;
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
