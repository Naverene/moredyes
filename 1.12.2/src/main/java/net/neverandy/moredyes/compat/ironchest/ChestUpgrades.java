package net.neverandy.moredyes.compat.ironchest;

import cpw.mods.ironchest.common.blocks.chest.IronChestType;
import cpw.mods.ironchest.common.items.ChestChangerType;
import cpw.mods.ironchest.common.items.chest.ItemChestChanger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.neverandy.moredyes.block.BlockDyedChest;

/**
 * Iron Chests' upgrade items keep the color: an iron-to-gold upgrade on a dyed iron chest gives a dyed gold chest of
 * the same color, and a wood-to-iron or wood-to-copper upgrade on a More Dyes dyed chest gives a dyed iron or copper
 * chest. Iron Chests' own upgrade code only knows its own chests and would turn a dyed wooden chest into a plain one,
 * so these clicks are handled here first and kept from it.
 */
public final class ChestUpgrades
{
	private ChestUpgrades(){}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		ItemStack stack=event.getItemStack();
		if(!(stack.getItem() instanceof ItemChestChanger))
		{
			return;
		}
		ChestChangerType upgrade=((ItemChestChanger)stack.getItem()).type;
		IronChestCompat.Tier target=IronChestCompat.Tier.of(upgrade.target);
		World world=event.getWorld();
		BlockPos pos=event.getPos();
		IBlockState state=world.getBlockState(pos);
		TileEntity te=world.getTileEntity(pos);
		int color=colorOf(state,te,upgrade);
		if(target==null||color<0||isOpen(te))
		{
			return;
		}
		event.setCanceled(true);
		event.setCancellationResult(EnumActionResult.SUCCESS);
		if(!world.isRemote)
		{
			upgrade(world,pos,state,te,target,color,event.getEntityPlayer(),stack);
		}
	}

	/** The color of a chest this upgrade applies to, or -1 if it is not a dyed chest of the upgrade's source tier. */
	private static int colorOf(IBlockState state,TileEntity te,ChestChangerType upgrade)
	{
		Block block=state.getBlock();
		if(block instanceof BlockDyedIronChest&&te instanceof TileEntityDyedIronChest)
		{
			return upgrade.canUpgrade(((BlockDyedIronChest)block).getTier().type)?((TileEntityDyedIronChest)te).getColor():-1;
		}
		if(block instanceof BlockDyedChest&&te instanceof TileEntityChest&&upgrade.canUpgrade(IronChestType.WOOD))
		{
			return ((BlockDyedChest)block).getColorIndex(0);
		}
		return -1;
	}
	/** A chest someone has open is left alone, as Iron Chests does. */
	private static boolean isOpen(TileEntity te)
	{
		if(te instanceof TileEntityDyedIronChest)
		{
			return ((TileEntityDyedIronChest)te).numPlayersUsing>0;
		}
		return te instanceof TileEntityChest&&((TileEntityChest)te).numPlayersUsing>0;
	}

	private static void upgrade(World world,BlockPos pos,IBlockState state,TileEntity te,IronChestCompat.Tier target,int color,EntityPlayer player,ItemStack upgrade)
	{
		IInventory inventory=(IInventory)te;
		NonNullList<ItemStack> items=NonNullList.withSize(Math.min(inventory.getSizeInventory(),target.type.size),ItemStack.EMPTY);
		for(int slot=0;slot<items.size();slot++)
		{
			items.set(slot,inventory.getStackInSlot(slot));
		}
		EnumFacing facing=te instanceof TileEntityDyedIronChest?((TileEntityDyedIronChest)te).getFacing():state.getValue(BlockChest.FACING);
		String name=((IWorldNameable)te).hasCustomName()?((IWorldNameable)te).getName():null;

		// Removing the tile entity first keeps the old chest from dropping what it held.
		te.updateContainingBlockInfo();
		if(te instanceof TileEntityChest)
		{
			((TileEntityChest)te).checkForAdjacentChests();
		}
		world.removeTileEntity(pos);
		world.setBlockToAir(pos);
		IBlockState chestState=target.block.getDefaultState();
		world.setBlockState(pos,chestState,3);
		TileEntity placed=world.getTileEntity(pos);
		if(placed instanceof TileEntityDyedIronChest)
		{
			TileEntityDyedIronChest chest=(TileEntityDyedIronChest)placed;
			chest.setContents(items);
			chest.setFacing(facing);
			chest.setColor(color);
			if(name!=null)
			{
				chest.setCustomName(name);
			}
			chest.markDirty();
			world.notifyBlockUpdate(pos,chestState,chestState,3);
		}
		if(!player.capabilities.isCreativeMode)
		{
			upgrade.shrink(1);
		}
	}
}
