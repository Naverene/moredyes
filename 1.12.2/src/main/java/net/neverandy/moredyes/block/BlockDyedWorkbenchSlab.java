package net.neverandy.moredyes.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.handler.GuiHandler;
import net.neverandy.moredyes.utility.BlockInfo;

/** Half a dyed crafting table, in every color. Using it opens the crafting grid, like a whole one. */
public class BlockDyedWorkbenchSlab extends BlockDyedSlab
{
	public BlockDyedWorkbenchSlab(Block base,BlockInfo info,String name)
	{
		super(base,info,name);
	}
	@Override
	public boolean onBlockActivated(World world,BlockPos pos,IBlockState state,EntityPlayer player,EnumHand hand,EnumFacing facing,float hitX,float hitY,float hitZ)
	{
		if(!world.isRemote)
		{
			player.openGui(MoreDyes.instance,GuiHandler.COLORED_WORKBENCH_GUI_ID,world,pos.getX(),pos.getY(),pos.getZ());
			player.addStat(StatList.CRAFTING_TABLE_INTERACTION);
		}
		return true;
	}
}
