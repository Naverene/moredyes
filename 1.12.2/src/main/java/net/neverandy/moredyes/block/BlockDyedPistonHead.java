package net.neverandy.moredyes.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.neverandy.moredyes.reference.Reference;

/**
 * The head of a dyed piston. It looks like the vanilla head (the dye only colors the piston's cobblestone), but
 * belongs to the dyed piston behind it: vanilla's head removes itself when the piston behind it is not a vanilla one.
 */
public class BlockDyedPistonHead extends BlockPistonExtension
{
	public BlockDyedPistonHead()
	{
		this.setRegistryName(Reference.MOD_ID,"piston_head");
		this.setUnlocalizedName(Reference.MOD_ID+".piston_head");
	}
	private static BlockPos basePos(BlockPos pos,IBlockState state)
	{
		return pos.offset(state.getValue(FACING).getOpposite());
	}
	@Override
	public void onBlockHarvested(World world,BlockPos pos,IBlockState state,EntityPlayer player)
	{
		BlockPos base=basePos(pos,state);
		if(player.capabilities.isCreativeMode&&world.getBlockState(base).getBlock() instanceof BlockDyedPiston)
		{
			world.setBlockToAir(base);
		}
		super.onBlockHarvested(world,pos,state,player);
	}
	/** Breaking the head breaks the piston behind it, which drops in its color. */
	@Override
	public void breakBlock(World world,BlockPos pos,IBlockState state)
	{
		super.breakBlock(world,pos,state);
		BlockPos base=basePos(pos,state);
		IBlockState baseState=world.getBlockState(base);
		if(baseState.getBlock() instanceof BlockDyedPiston&&baseState.getValue(BlockPistonBase.EXTENDED))
		{
			baseState.getBlock().dropBlockAsItem(world,base,baseState,0);
			world.setBlockToAir(base);
		}
	}
	@Override
	public void neighborChanged(IBlockState state,World world,BlockPos pos,Block block,BlockPos fromPos)
	{
		BlockPos base=basePos(pos,state);
		IBlockState baseState=world.getBlockState(base);
		if(baseState.getBlock() instanceof BlockDyedPiston)
		{
			baseState.neighborChanged(world,base,block,fromPos);
		}
		else
		{
			world.setBlockToAir(pos);
		}
	}
	/** Picking the head gives the piston behind it, in its color. */
	@Override
	public ItemStack getItem(World world,BlockPos pos,IBlockState state)
	{
		Block piston=state.getValue(TYPE)==EnumPistonType.STICKY?MDBlock.stickyPiston:MDBlock.piston;
		return TileColor.stack(piston,world,basePos(pos,state),1);
	}
}
