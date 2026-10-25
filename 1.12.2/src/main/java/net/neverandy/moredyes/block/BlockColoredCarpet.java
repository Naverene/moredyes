package net.neverandy.moredyes.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.utility.BlockInfo;

/**
 * Dyed carpet, like the vanilla carpet: one pixel thick, and it needs a block under it. A tamed llama wears it too
 * (see entity/LlamaCarpets).
 */
public class BlockColoredCarpet extends BlockColored
{
	protected static final AxisAlignedBB CARPET_AABB=new AxisAlignedBB(0.0,0.0,0.0,1.0,0.0625,1.0);

	public BlockColoredCarpet(BlockInfo info,int group)
	{
		super(info,group);
	}
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos)
	{
		return CARPET_AABB;
	}
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	@Override
	public boolean canPlaceBlockAt(World world,BlockPos pos)
	{
		return super.canPlaceBlockAt(world,pos)&&!world.isAirBlock(pos.down());
	}
	@Override
	public void neighborChanged(IBlockState state,World world,BlockPos pos,Block block,BlockPos fromPos)
	{
		if(world.isAirBlock(pos.down()))
		{
			this.dropBlockAsItem(world,pos,state,0);
			world.setBlockToAir(pos);
		}
	}
	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockState state,IBlockAccess world,BlockPos pos,EnumFacing side)
	{
		return side==EnumFacing.UP||world.getBlockState(pos.offset(side)).getBlock() instanceof BlockColoredCarpet
				||super.shouldSideBeRendered(state,world,pos,side);
	}
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world,IBlockState state,BlockPos pos,EnumFacing face)
	{
		return face==EnumFacing.DOWN?BlockFaceShape.SOLID:BlockFaceShape.UNDEFINED;
	}
}
