package net.neverandy.moredyes.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.neverandy.moredyes.utility.BlockInfo;

/** Dyed concrete powder falls like sand and sets into dyed concrete of the same shade when it touches water. */
public class BlockColoredConcretePowder extends BlockColoredSand
{
	public BlockColoredConcretePowder(BlockInfo info,int group)
	{
		super(info,group);
	}
	private IBlockState concrete(IBlockState powder)
	{
		return MDBlock.concrete[this.group].getStateFromMeta(this.getMetaFromState(powder));
	}
	@Override
	public void onEndFalling(World world,BlockPos pos,IBlockState fallingState,IBlockState hitState)
	{
		if(hitState.getMaterial().isLiquid())
		{
			world.setBlockState(pos,this.concrete(fallingState),3);
		}
	}
	/** Sets into concrete when water is next to it or above it, like vanilla concrete powder. */
	private boolean tryTouchWater(World world,BlockPos pos,IBlockState state)
	{
		for(EnumFacing side:EnumFacing.values())
		{
			if(side!=EnumFacing.DOWN&&world.getBlockState(pos.offset(side)).getMaterial()==Material.WATER)
			{
				world.setBlockState(pos,this.concrete(state),3);
				return true;
			}
		}
		return false;
	}
	@Override
	public void neighborChanged(IBlockState state,World world,BlockPos pos,Block block,BlockPos fromPos)
	{
		if(!this.tryTouchWater(world,pos,state))
		{
			super.neighborChanged(state,world,pos,block,fromPos);
		}
	}
	@Override
	public void onBlockAdded(World world,BlockPos pos,IBlockState state)
	{
		if(!this.tryTouchWater(world,pos,state))
		{
			super.onBlockAdded(world,pos,state);
		}
	}
	/** Unlike sand, plants do not grow on it. */
	@Override
	public boolean canSustainPlant(IBlockState state,IBlockAccess world,BlockPos pos,EnumFacing direction,IPlantable plantable)
	{
		return false;
	}
}
