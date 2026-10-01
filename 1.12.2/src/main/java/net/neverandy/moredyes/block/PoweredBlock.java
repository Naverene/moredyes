package net.neverandy.moredyes.block;

import net.neverandy.moredyes.utility.BlockInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class PoweredBlock extends BasicBlock
{
	public PoweredBlock(String[] blockColor, BlockInfo info, int set)
	{
		super(blockColor, info, set);
		
	}
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return 15;
    }
}
