package net.neverandy.moredyes.handler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumActionResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.tileentity.TileEntityDyedBed;

/**
 * Right-clicking a cauldron holding water with dyed blocks washes the whole stack for one level of water. A dyed banner
 * with patterns loses its last pattern instead, one banner at a time, like the vanilla banner.
 */
public class CauldronWashHandler
{
	/** What a stack of dyed things washes into, or null for something that does not wash. */
	private static ItemStack washed(ItemStack held)
	{
		if(held.getItem()==MDItem.bed)
		{
			// A bed of planks other than oak keeps them, as a white bed.
			String wood=TileEntityDyedBed.woodOf(held);
			if(wood==null)
			{
				return new ItemStack(Items.BED,1,0);
			}
			ItemStack white=TileEntityDyedBed.vanillaColorBed(EnumDyeColor.WHITE);
			TileEntityDyedBed.setWood(white,wood);
			return white;
		}
		if(held.getItem()==MDItem.banner)
		{
			return new ItemStack(Items.BANNER,1,15);
		}
		Block block=Block.getBlockFromItem(held.getItem());
		ItemStack washed=MDBlock.WASHED.get(block);
		if(washed!=null&&(block instanceof BlockShulkerBox||MDBlock.WASH_KEEPS_NBT.contains(block))&&held.hasTagCompound())
		{
			// The box keeps its contents.
			washed=washed.copy();
			washed.setTagCompound(held.getTagCompound().copy());
		}
		return washed;
	}

	@SubscribeEvent
	public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		World world=event.getWorld();
		IBlockState state=world.getBlockState(event.getPos());
		if(state.getBlock()!=Blocks.CAULDRON)
		{
			return;
		}
		int water=state.getValue(BlockCauldron.LEVEL);
		ItemStack held=event.getItemStack();
		if(water<=0||held.isEmpty())
		{
			return;
		}
		ItemStack washed=washed(held);
		if(washed==null)
		{
			return;
		}
		// Cancelled on both sides, so the client does not place the held block; it still tells the server.
		event.setCanceled(true);
		event.setCancellationResult(EnumActionResult.SUCCESS);
		if(world.isRemote)
		{
			return;
		}
		EntityPlayer player=event.getEntityPlayer();
		if(held.getItem()==MDItem.banner&&TileEntityBanner.getPatterns(held)>0)
		{
			ItemStack banner=held.copy();
			banner.setCount(1);
			TileEntityBanner.removeBannerData(banner);
			if(!player.capabilities.isCreativeMode)
			{
				held.shrink(1);
			}
			if(held.isEmpty())
			{
				player.setHeldItem(event.getHand(),banner);
			}
			else if(!player.inventory.addItemStackToInventory(banner))
			{
				player.dropItem(banner,false);
			}
		}
		else
		{
			ItemStack result=washed.copy();
			result.setCount(held.getCount());
			player.setHeldItem(event.getHand(),result);
		}
		Blocks.CAULDRON.setWaterLevel(world,event.getPos(),state,water-1);
		player.inventoryContainer.detectAndSendChanges();
	}
}
