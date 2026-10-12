package net.neverandy.moredyes.handler;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.neverandy.moredyes.block.BlockDyedFlowerPot;

/**
 * Puts a dyed flower or sapling in an empty vanilla flower pot, which turns into a dyed flower pot (see
 * BlockDyedFlowerPot). The vanilla pot only takes the vanilla plants it can draw.
 */
public class FlowerPotHandler
{
	@SubscribeEvent
	public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		if(event.getWorld().getBlockState(event.getPos()).getBlock()!=Blocks.FLOWER_POT)
		{
			return;
		}
		ItemStack held=event.getItemStack();
		Block plant=Block.getBlockFromItem(held.getItem());
		if(held.isEmpty()||BlockDyedFlowerPot.Plant.of(plant)==null)
		{
			return;
		}
		TileEntity tile=event.getWorld().getTileEntity(event.getPos());
		if(!(tile instanceof TileEntityFlowerPot)||!((TileEntityFlowerPot)tile).getFlowerItemStack().isEmpty())
		{
			return;
		}
		// The client does the same, so it does not try to place the plant, and the server then does it for real.
		event.setCanceled(true);
		event.setCancellationResult(EnumActionResult.SUCCESS);
		if(!event.getWorld().isRemote&&BlockDyedFlowerPot.pot(event.getWorld(),event.getPos(),plant,held.getMetadata()))
		{
			event.getEntityPlayer().addStat(StatList.FLOWER_POTTED);
			if(!event.getEntityPlayer().capabilities.isCreativeMode)
			{
				held.shrink(1);
			}
		}
	}
}
