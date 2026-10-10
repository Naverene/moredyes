package net.neverandy.moredyes.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.utility.BlockInfo;

/**
 * Dyed chain (Minecraft 1.16): a thin upright post drawn as two crossed planes. The metadata holds the shade, so
 * unlike the 1.16 chain it always hangs upright, as in 1.7.10.
 */
public class BlockColoredChain extends BlockColored
{
	private static final AxisAlignedBB AABB=new AxisAlignedBB(6.5/16,0.0,6.5/16,9.5/16,1.0,9.5/16);

	public BlockColoredChain(BlockInfo info,int group)
	{
		super(info,group);
	}
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos)
	{
		return AABB;
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
	public BlockFaceShape getBlockFaceShape(IBlockAccess world,IBlockState state,BlockPos pos,EnumFacing face)
	{
		return face.getAxis()==EnumFacing.Axis.Y?BlockFaceShape.CENTER_SMALL:BlockFaceShape.UNDEFINED;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
}
