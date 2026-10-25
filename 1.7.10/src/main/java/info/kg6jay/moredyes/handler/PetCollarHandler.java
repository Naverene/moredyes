package info.kg6jay.moredyes.handler;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import info.kg6jay.moredyes.entity.PetCollar;
import info.kg6jay.moredyes.entity.SheepColor;
import info.kg6jay.moredyes.item.MDItemDye;
import info.kg6jay.moredyes.network.PacketHandler;

/**
 * Lets tamed wolves, and Not Enough Pets cats, wear a collar in a More Dyes shade (see {@link PetCollar}), sent to
 * the players who can see the pet the same way as a sheep's shade. The client draws it with RenderColoredWolf (and
 * the Not Enough Pets renderers in compat/notenoughpets).
 */
public class PetCollarHandler {

    @SubscribeEvent
    public void onStartTracking(PlayerEvent.StartTracking event) {
        int color = PetCollar.get(event.target);
        if (color != SheepColor.NONE && event.entityPlayer instanceof EntityPlayerMP) {
            PacketHandler.sendColor(event.target, color, (EntityPlayerMP) event.entityPlayer);
        }
    }

    /**
     * Dyes the collar of a tamed pet that belongs to the player. This runs before the pet's own right click, which
     * would otherwise make it sit down, or (with Not Enough Pets) give it the nearest vanilla color. A vanilla dye on a
     * More Dyes collar gives it that vanilla color again, even the one hidden underneath.
     */
    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        if (!PetCollar.hasCollar(event.target)) {
            return;
        }
        EntityTameable pet = (EntityTameable) event.target;
        EntityPlayer player = event.entityPlayer;
        ItemStack held = player.getCurrentEquippedItem();
        if (held == null || !pet.isEntityAlive() || !pet.isTamed() || !pet.func_152114_e(player)) {
            return;
        }
        int color = PetCollar.get(pet);
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
        if (pet.worldObj.isRemote) {
            return;
        }
        PetCollar.put(pet, newColor);
        if (vanilla >= 0) {
            PetCollar.setVanillaColor(pet, vanilla);
        }
        PacketHandler.sendColor(pet, newColor);
        if (!player.capabilities.isCreativeMode && --held.stackSize <= 0) {
            player.destroyCurrentEquippedItem();
        }
        player.inventoryContainer.detectAndSendChanges();
    }

    /**
     * Not Enough Pets swaps vanilla wolves for its own and back by spawning a copy in the same spot and removing the
     * old one, which only copies the vanilla collar color. A tamed pet that joins the world without a More Dyes shade
     * takes the shade of a just-removed pet of the same owner standing exactly where it is.
     */
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.world.isRemote || !PetCollar.hasCollar(event.entity)) {
            return;
        }
        EntityTameable pet = (EntityTameable) event.entity;
        if (!pet.isTamed() || PetCollar.get(pet) != SheepColor.NONE) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<Entity> near = event.world
            .getEntitiesWithinAABBExcludingEntity(pet, pet.boundingBox.expand(0.01D, 0.01D, 0.01D));
        for (Entity other : near) {
            if (other.isDead && other.posX == pet.posX
                && other.posY == pet.posY
                && other.posZ == pet.posZ
                && PetCollar.get(other) != SheepColor.NONE
                && pet.func_152113_b()
                    .equals(((EntityTameable) other).func_152113_b())) {
                PetCollar.put(pet, PetCollar.get(other));
                return;
            }
        }
    }
}
