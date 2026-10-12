package info.kg6jay.moredyes.handler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.block.Dyed;
import info.kg6jay.moredyes.block.MDBlocks;

/**
 * Right-clicking an empty vanilla flower pot with a dyed flower or sapling puts the plant in it: the pot becomes a
 * dyed flower pot (BlockDyedFlowerPot) holding that plant in that color. Breaking it gives back the pot and the plant.
 */
public class FlowerPotHandler {

    @ForgeSubscribe
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        World world = event.entityPlayer.worldObj;
        // Server only, like CauldronWashHandler: the client sends the click on, and gets the new block back.
        if (world.isRemote || world.getBlockId(event.x, event.y, event.z) != Block.flowerPot.blockID
            || world.getBlockMetadata(event.x, event.y, event.z) != 0) {
            return;
        }
        EntityPlayer player = event.entityPlayer;
        ItemStack held = player.getCurrentEquippedItem();
        if (held == null || held.itemID >= Block.blocksList.length) {
            return;
        }
        int plant = MDBlocks.POTTABLE.indexOf(Block.blocksList[held.itemID]);
        if (plant < 0) {
            return;
        }
        event.setCanceled(true);
        Dyed.place(world, event.x, event.y, event.z, MDBlocks.flowerPot, plant, Colors.clamp(held.getItemDamage()),
            true);
        if (!player.capabilities.isCreativeMode && --held.stackSize <= 0) {
            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
        }
        player.inventoryContainer.detectAndSendChanges();
    }
}
