package info.kg6jay.moredyes.handler;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import info.kg6jay.moredyes.entity.SheepColor;
import info.kg6jay.moredyes.entity.WolfCollar;
import info.kg6jay.moredyes.item.MDItemDye;
import info.kg6jay.moredyes.network.PacketHandler;

/**
 * Lets tamed wolves wear a collar in a More Dyes shade (see {@link WolfCollar}), sent to the players who can see the
 * wolf the same way as a sheep's shade. The client draws it with RenderColoredWolf.
 */
public class WolfCollarHandler {

    @SubscribeEvent
    public void onStartTracking(PlayerEvent.StartTracking event) {
        int color = WolfCollar.get(event.target);
        if (color != SheepColor.NONE && event.entityPlayer instanceof EntityPlayerMP) {
            PacketHandler.sendColor(event.target, color, (EntityPlayerMP) event.entityPlayer);
        }
    }

    /**
     * Dyes the collar of a tamed wolf that belongs to the player. This runs before the wolf's own right click, which
     * would otherwise make it sit down: vanilla only lets vanilla dyes through to the collar. A vanilla dye on a More
     * Dyes collar gives it that vanilla color again, even the one hidden underneath.
     */
    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        if (!(event.target instanceof EntityWolf)) {
            return;
        }
        EntityWolf wolf = (EntityWolf) event.target;
        EntityPlayer player = event.entityPlayer;
        ItemStack held = player.getCurrentEquippedItem();
        if (held == null || !wolf.isEntityAlive() || !wolf.isTamed() || !wolf.func_152114_e(player)) {
            return;
        }
        int color = WolfCollar.get(wolf);
        int newColor;
        int vanilla = -1;
        if (held.getItem() instanceof MDItemDye) {
            newColor = SheepColor.pack(((MDItemDye) held.getItem()).set, held.getItemDamage());
            if (newColor == SheepColor.NONE || newColor == color) {
                return;
            }
        } else if (held.getItem() == Items.dye && color != SheepColor.NONE) {
            newColor = SheepColor.NONE;
            vanilla = ~held.getItemDamage() & 15;
        } else {
            return;
        }
        event.setCanceled(true);
        if (wolf.worldObj.isRemote) {
            return;
        }
        WolfCollar.put(wolf, newColor);
        if (vanilla >= 0) {
            wolf.setCollarColor(vanilla);
        }
        PacketHandler.sendColor(wolf, newColor);
        if (!player.capabilities.isCreativeMode && --held.stackSize <= 0) {
            player.destroyCurrentEquippedItem();
        }
        player.inventoryContainer.detectAndSendChanges();
    }
}
