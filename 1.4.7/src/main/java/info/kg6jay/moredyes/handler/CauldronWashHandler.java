package info.kg6jay.moredyes.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import info.kg6jay.moredyes.block.MDBlocks;

/**
 * Right-clicking a cauldron that has water in it with a stack of dyed blocks washes the whole stack back to the
 * vanilla block and uses up one level of water, like washing dyed leather armor in vanilla.
 */
public class CauldronWashHandler {

    /** The vanilla block each dyed block turns back into, by block ID. */
    private final Map<Integer, ItemStack> washed = new HashMap<Integer, ItemStack>();

    public CauldronWashHandler() {
        this.put(MDBlocks.wool, Block.cloth, 0);
        this.put(MDBlocks.stonebrick, Block.stoneBrick, 0);
        this.put(MDBlocks.mossyStonebrick, Block.stoneBrick, 1);
        this.put(MDBlocks.stonebrickCracked, Block.stoneBrick, 2);
        this.put(MDBlocks.stonebrickCarved, Block.stoneBrick, 3);
        this.put(MDBlocks.stone, Block.stone, 0);
        this.put(MDBlocks.cobble, Block.cobblestone, 0);
        this.put(MDBlocks.obsidian, Block.obsidian, 0);
        this.put(MDBlocks.soulsand, Block.slowSand, 0);
        this.put(MDBlocks.glowstone, Block.glowStone, 0);
        this.put(MDBlocks.lapis, Block.blockLapis, 0);
        this.put(MDBlocks.plank, Block.planks, 0);
        this.put(MDBlocks.log, Block.wood, 0);
        this.put(MDBlocks.leaves, Block.leaves, 0);
        this.put(MDBlocks.sapling, Block.sapling, 0);
        this.put(MDBlocks.glassClear, Block.glass, 0);
        this.put(MDBlocks.glassClearPane, Block.thinGlass, 0);
        this.put(MDBlocks.brick, Block.brick, 0);
        this.put(MDBlocks.sand, Block.sand, 0);
        this.put(MDBlocks.workbench, Block.workbench, 0);
        this.put(MDBlocks.chest, Block.chest, 0);
        this.put(MDBlocks.sandstone, Block.sandStone, 0);
        this.put(MDBlocks.cutSandstone, Block.sandStone, 2);
        this.put(MDBlocks.bookshelf, Block.bookShelf, 0);
        this.put(MDBlocks.mossyCobble, Block.cobblestoneMossy, 0);
        this.put(MDBlocks.netherBrick, Block.netherBrick, 0);
        this.put(MDBlocks.diorite, MDBlocks.dioritePlain, 0);
        this.put(MDBlocks.granite, MDBlocks.granitePlain, 0);
        this.put(MDBlocks.andesite, MDBlocks.andesitePlain, 0);
    }

    private void put(Block dyed, Block vanilla, int meta) {
        this.washed.put(dyed.blockID, new ItemStack(vanilla, 1, meta));
    }

    @ForgeSubscribe
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        World world = event.entityPlayer.worldObj;
        // Server only: cancelling on the client would stop the click from reaching the server. On the client the
        // cauldron already takes the click, so the held block is not placed.
        if (world.isRemote || world.getBlockId(event.x, event.y, event.z) != Block.cauldron.blockID) {
            return;
        }
        int water = world.getBlockMetadata(event.x, event.y, event.z);
        EntityPlayer player = event.entityPlayer;
        ItemStack held = player.getCurrentEquippedItem();
        if (water <= 0 || held == null) {
            return;
        }
        ItemStack washed = this.washed.get(held.itemID);
        if (washed == null) {
            return;
        }
        event.setCanceled(true);
        ItemStack result = washed.copy();
        result.stackSize = held.stackSize;
        player.inventory.setInventorySlotContents(player.inventory.currentItem, result);
        world.setBlockMetadataWithNotify(event.x, event.y, event.z, water - 1);
        player.inventoryContainer.detectAndSendChanges();
    }
}
