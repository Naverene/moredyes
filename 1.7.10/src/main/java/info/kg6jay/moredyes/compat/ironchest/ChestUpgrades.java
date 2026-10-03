package info.kg6jay.moredyes.compat.ironchest;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.ironchest.ChestChangerType;
import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.ItemChestChanger;
import cpw.mods.ironchest.TileEntityIronChest;
import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.block.MDBlockColoredChest;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDBlockColoredChest;
import info.kg6jay.moredyes.utility.ColorIndex;

/**
 * Makes Iron Chests' upgrade items keep the color: an iron to gold upgrade on a dyed iron chest gives a dyed gold chest
 * of the same color, and a wood to iron (or copper) upgrade on a More Dyes dyed chest gives a dyed iron (or copper)
 * chest. An upgrade to a tier that has no dyed version (crystal, obsidian) gives Iron Chests' plain chest, as it does
 * for its own chests.
 * <p>
 * Iron Chests' own handling would put a plain chest's tile entity into our block (or turn a dyed wooden chest into a
 * plain chest), so the upgrade is done here on the server and Iron Chests' is cancelled: the interact event comes
 * before the item's onItemUseFirst. On the client the event is let through, so the click still reaches the server.
 */
public class ChestUpgrades {

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || event.world.isRemote) {
            return;
        }
        EntityPlayer player = event.entityPlayer;
        ItemStack held = player.getCurrentEquippedItem();
        if (held == null || !(held.getItem() instanceof ItemChestChanger changer)) {
            return;
        }
        World world = event.world;
        int x = event.x, y = event.y, z = event.z;
        Block block = world.getBlock(x, y, z);
        TileEntity te = world.getTileEntity(x, y, z);

        IronChestType from;
        int color, facing;
        if (block instanceof DyedIronChestBlock && te instanceof DyedIronChestTile chest) {
            from = chest.getType();
            color = chest.getColor();
            facing = chest.getFacing();
        } else if (block instanceof MDBlockColoredChest && te instanceof TileEntityMDBlockColoredChest chest) {
            from = IronChestType.WOOD;
            color = ColorIndex.of(
                Arrays.asList(MDBlock.chest)
                    .indexOf(block),
                world.getBlockMetadata(x, y, z));
            facing = chest.getFacing();
        } else {
            return;
        }

        ChestChangerType upgrade = changer.getType();
        if (!upgrade.canUpgrade(from)) {
            // Iron Chests does nothing in this case either.
            return;
        }
        event.setCanceled(true);
        if (isOpen(te)) {
            return;
        }

        IronChestType target = IronChestType.values()[upgrade.getTarget()];
        IronChestCompat.Tier tier = IronChestCompat.Tier.of(target);
        IInventory old = (IInventory) te;
        ItemStack[] items = new ItemStack[old.getSizeInventory()];
        for (int slot = 0; slot < items.length; slot++) {
            items[slot] = old.getStackInSlot(slot);
            // Emptied first, so breaking the old block drops nothing.
            old.setInventorySlotContents(slot, null);
        }

        if (tier != null) {
            world.setBlock(x, y, z, tier.block, 0, 3);
        } else {
            world.setBlock(x, y, z, IronChest.ironChestBlock, target.ordinal(), 3);
        }
        TileEntity placed = world.getTileEntity(x, y, z);
        if (!(placed instanceof TileEntityIronChest chest)) {
            return;
        }
        chest.setFacing(facing);
        if (chest instanceof DyedIronChestTile dyed) {
            dyed.setColor(color);
        }
        for (int slot = 0; slot < items.length; slot++) {
            if (items[slot] == null) {
                continue;
            }
            if (slot < chest.getSizeInventory()) {
                chest.setInventorySlotContents(slot, items[slot]);
            } else {
                // Only if the new chest were smaller, which none of Iron Chests' upgrades make.
                world.spawnEntityInWorld(new EntityItem(world, x + 0.5D, y + 1.0D, z + 0.5D, items[slot]));
            }
        }
        chest.markDirty();
        world.markBlockForUpdate(x, y, z);

        if (!player.capabilities.isCreativeMode && --held.stackSize <= 0) {
            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
        }
    }

    /** Iron Chests does not upgrade a chest someone has open either. */
    private static boolean isOpen(TileEntity te) {
        if (te instanceof TileEntityMDBlockColoredChest chest) {
            return chest.numPlayersUsing > 0;
        }
        return te instanceof DyedIronChestTile chest && chest.isOpen();
    }
}
