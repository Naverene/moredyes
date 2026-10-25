package net.neverandy.moredyes.compat.ironchest;

import cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType;
import cpw.mods.ironchest.common.items.ShulkerBoxChangerType;
import cpw.mods.ironchest.common.items.shulker.ItemShulkerBoxChanger;
import cpw.mods.ironchest.common.tileentity.shulker.TileEntityIronShulkerBox;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.neverandy.moredyes.block.BlockDyedShulkerBox;
import net.neverandy.moredyes.tileentity.TileEntityDyedShulkerBox;

/**
 * Iron Chests' shulker box upgrades keep the color: an iron-to-gold upgrade on a dyed iron box gives a dyed gold box
 * of the same color, and a vanilla-to-iron upgrade on a More Dyes dyed shulker box gives a dyed iron box. Iron Chests'
 * own upgrade code knows only its 16 colors and would make the box purple, so these clicks are handled here first and
 * kept from it. The box keeps its contents, name and facing.
 */
public final class ShulkerBoxUpgrades
{
	private ShulkerBoxUpgrades(){}

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		ItemStack stack=event.getItemStack();
		if(!(stack.getItem() instanceof ItemShulkerBoxChanger))
		{
			return;
		}
		ShulkerBoxChangerType upgrade=((ItemShulkerBoxChanger)stack.getItem()).type;
		IronShulkerBoxCompat.Tier target=IronShulkerBoxCompat.Tier.of(upgrade.target);
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

	/** The color of a box this upgrade applies to, or -1 if it is not a dyed box of the upgrade's source tier. */
	private static int colorOf(IBlockState state,TileEntity te,ShulkerBoxChangerType upgrade)
	{
		if(state.getBlock() instanceof BlockDyedIronShulkerBox&&te instanceof TileEntityDyedIronShulkerBox)
		{
			return upgrade.canUpgrade(((BlockDyedIronShulkerBox)state.getBlock()).getTier().type)?((TileEntityDyedIronShulkerBox)te).getDyeIndex():-1;
		}
		if(state.getBlock() instanceof BlockDyedShulkerBox&&te instanceof TileEntityDyedShulkerBox&&upgrade.canUpgrade(IronShulkerBoxType.VANILLA))
		{
			return ((TileEntityDyedShulkerBox)te).getColorIndex();
		}
		return -1;
	}
	/** A box someone has open is left alone. */
	private static boolean isOpen(TileEntity te)
	{
		if(te instanceof TileEntityIronShulkerBox)
		{
			return ((TileEntityIronShulkerBox)te).getAnimationStatus()!=TileEntityIronShulkerBox.AnimationStatus.CLOSED;
		}
		return te instanceof TileEntityShulkerBox&&((TileEntityShulkerBox)te).getAnimationStatus()!=TileEntityShulkerBox.AnimationStatus.CLOSED;
	}

	private static void upgrade(World world,BlockPos pos,IBlockState state,TileEntity te,IronShulkerBoxCompat.Tier target,int color,EntityPlayer player,ItemStack upgrade)
	{
		NonNullList<ItemStack> items;
		EnumFacing facing;
		String name;
		if(te instanceof TileEntityDyedIronShulkerBox)
		{
			TileEntityDyedIronShulkerBox box=(TileEntityDyedIronShulkerBox)te;
			items=copy(box.getItems());
			facing=box.getFacing();
			name=box.hasCustomName()?box.getName():null;
			box.setHasBeenUpgraded();
			box.clear();
		}
		else
		{
			TileEntityShulkerBox box=(TileEntityShulkerBox)te;
			items=NonNullList.withSize(box.getSizeInventory(),ItemStack.EMPTY);
			for(int slot=0;slot<items.size();slot++)
			{
				items.set(slot,box.getStackInSlot(slot));
			}
			facing=state.getValue(BlockShulkerBox.FACING);
			name=box.hasCustomName()?box.getName():null;
			box.clear();
		}

		// Emptying and removing the old box first keeps it from dropping itself or what it held.
		te.updateContainingBlockInfo();
		world.removeTileEntity(pos);
		world.setBlockToAir(pos);
		IBlockState boxState=target.block.getDefaultState();
		world.setBlockState(pos,boxState,3);
		TileEntity placed=world.getTileEntity(pos);
		if(placed instanceof TileEntityDyedIronShulkerBox)
		{
			TileEntityDyedIronShulkerBox box=(TileEntityDyedIronShulkerBox)placed;
			// Every upgrade is to a larger box, so everything fits.
			box.setContents(items);
			box.setFacing(facing);
			box.setColor(color);
			if(name!=null)
			{
				box.setCustomName(name);
			}
			box.markDirty();
			world.notifyBlockUpdate(pos,boxState,boxState,3);
		}
		if(!player.capabilities.isCreativeMode)
		{
			upgrade.shrink(1);
		}
	}

	private static NonNullList<ItemStack> copy(NonNullList<ItemStack> items)
	{
		NonNullList<ItemStack> copy=NonNullList.withSize(items.size(),ItemStack.EMPTY);
		for(int slot=0;slot<items.size();slot++)
		{
			copy.set(slot,items.get(slot));
		}
		return copy;
	}
}
