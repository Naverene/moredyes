package info.kg6jay.moredyes.handler;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import info.kg6jay.moredyes.item.SharedBlocks;

/**
 * Turns the dyed blocks' own items into the shared dyed block item (see SharedBlocks) as they come into the world:
 * in the drops of a broken block, and on any item dropped on the ground (shears, explosions, old items thrown out).
 * While a player has a Chisel GUI open it goes the other way, since Chisel only takes block items (see
 * SharedBlocks.inChiselGui). Registered on both the Forge and the FML event bus.
 */
public class SharedBlockHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
        if (event.drops != null) {
            SharedBlocks.convertAll(event.drops);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.world.isRemote || !(event.entity instanceof EntityItem item)) {
            return;
        }
        ItemStack shared = SharedBlocks.toShared(item.getEntityItem());
        if (shared != null) {
            item.setEntityItemStack(shared);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.worldObj.isRemote
            && SharedBlocks.inChiselGui(event.player)) {
            SharedBlocks.chiselInventory(event.player);
        }
    }
}
