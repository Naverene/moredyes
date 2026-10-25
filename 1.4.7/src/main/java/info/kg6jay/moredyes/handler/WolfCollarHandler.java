package info.kg6jay.moredyes.handler;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.entity.SheepColors;
import info.kg6jay.moredyes.entity.WolfCollars;
import info.kg6jay.moredyes.item.ItemMoreDye;

/**
 * Dyes the collar of a tamed wolf that belongs to the player (see {@link WolfCollars}). This runs before the wolf's
 * own right click, which would otherwise make it sit down: vanilla only lets vanilla dyes through to the collar. A
 * vanilla dye on a More Dyes collar gives it that vanilla color again, even the one hidden underneath.
 */
public class WolfCollarHandler {

    @ForgeSubscribe
    public void onEntityInteract(EntityInteractEvent event) {
        if (!(event.target instanceof EntityWolf)) {
            return;
        }
        EntityWolf wolf = (EntityWolf) event.target;
        EntityPlayer player = event.entityPlayer;
        ItemStack held = player.getCurrentEquippedItem();
        if (held == null || !wolf.isEntityAlive() || !wolf.isTamed()
            || !player.username.equalsIgnoreCase(wolf.getOwnerName())) {
            return;
        }
        int color = WolfCollars.get(wolf);
        int newColor;
        int vanilla = -1;
        if (held.getItem() instanceof ItemMoreDye) {
            newColor = Colors.clamp(held.getItemDamage());
            if (newColor == color) {
                return;
            }
        } else if (held.itemID == Item.dyePowder.itemID && color != SheepColors.NONE) {
            newColor = SheepColors.NONE;
            vanilla = ~held.getItemDamage() & 15;
        } else {
            return;
        }
        event.setCanceled(true);
        if (wolf.worldObj.isRemote) {
            return;
        }
        WolfCollars.set(wolf, newColor);
        if (vanilla >= 0) {
            wolf.setCollarColor(vanilla);
        }
        if (!player.capabilities.isCreativeMode && --held.stackSize <= 0) {
            player.destroyCurrentEquippedItem();
        }
        player.inventoryContainer.detectAndSendChanges();
    }
}
