package info.kg6jay.moredyes.handler;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.item.MDItem;

/**
 * Right-clicking a cauldron that has water in it with a stack of dyed blocks washes the whole stack back to the
 * vanilla block and uses up one level of water, like washing dyed leather armor in vanilla.
 */
public class CauldronWashHandler {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        World world = event.entityPlayer.worldObj;
        // Server only: cancelling on the client would stop the click from reaching the server. On the client the
        // cauldron already takes the click, so the held block is not placed.
        if (world.isRemote || world.getBlock(event.x, event.y, event.z) != Blocks.cauldron) {
            return;
        }
        int water = world.getBlockMetadata(event.x, event.y, event.z);
        EntityPlayer player = event.entityPlayer;
        ItemStack held = player.getCurrentEquippedItem();
        if (water <= 0 || held == null) {
            return;
        }
        ItemStack washed = held.getItem() == MDItem.bed ? new ItemStack(Items.bed)
            : MDBlock.WASHED.get(Block.getBlockFromItem(held.getItem()));
        if (washed == null) {
            return;
        }

        event.setCanceled(true);
        ItemStack result = washed.copy();
        result.stackSize = held.stackSize;
        // Keeps what the item carries, such as the contents of a shulker box or a custom name.
        if (held.hasTagCompound()) {
            result.setTagCompound(
                (NBTTagCompound) held.getTagCompound()
                    .copy());
        }
        player.inventory.setInventorySlotContents(player.inventory.currentItem, result);
        Blocks.cauldron.func_150024_a(world, event.x, event.y, event.z, water - 1);
        player.inventoryContainer.detectAndSendChanges();
    }
}
