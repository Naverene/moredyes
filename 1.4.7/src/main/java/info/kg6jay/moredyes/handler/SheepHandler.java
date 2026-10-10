package info.kg6jay.moredyes.handler;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

import cpw.mods.fml.common.network.PacketDispatcher;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.block.MDBlocks;
import info.kg6jay.moredyes.entity.SheepColors;
import info.kg6jay.moredyes.network.PacketHandler;

/**
 * Lets vanilla sheep wear More Dyes colors: gives some new sheep a random color, drops More Dyes wool when a colored
 * sheep is sheared or killed, and lets a vanilla dye take the color off again. More Dyes dyes color sheep themselves
 * (see ItemMoreDye).
 */
public class SheepHandler {

    @ForgeSubscribe
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!(event.entity instanceof EntitySheep)) {
            return;
        }
        EntitySheep sheep = (EntitySheep) event.entity;
        if (event.world.isRemote) {
            // The color is not in the sheep's spawn packet, so ask for it.
            PacketDispatcher.sendPacketToServer(PacketHandler.askSheep(sheep.entityId));
            return;
        }
        if (!SheepColors.firstCheck(sheep) || SheepColors.get(sheep) != SheepColors.NONE) {
            return;
        }
        Random rand = event.world.rand;
        if (rand.nextDouble() < MoreDyes.config.sheepSpawnChance) {
            SheepColors.store(sheep, rand.nextInt(Colors.COUNT));
            sheep.setFleeceColor(Colors.set(SheepColors.get(sheep)));
        }
    }

    @ForgeSubscribe
    public void onEntityInteract(EntityInteractEvent event) {
        if (!(event.target instanceof EntitySheep)) {
            return;
        }
        EntitySheep sheep = (EntitySheep) event.target;
        EntityPlayer player = event.entityPlayer;
        ItemStack held = player.getCurrentEquippedItem();
        // Server only: the server tells the player's game about the new fleece and inventory.
        if (player.worldObj.isRemote || held == null || SheepColors.get(sheep) == SheepColors.NONE) {
            return;
        }
        if (held.itemID == Item.dyePowder.itemID) {
            // A vanilla dye replaces the More Dyes color, even when the sheep's vanilla color is the same.
            if (!sheep.getSheared()) {
                event.setCanceled(true);
                SheepColors.set(sheep, SheepColors.NONE);
                sheep.setFleeceColor(~held.getItemDamage() & 15);
                useHeldItem(player, held, false);
            }
        } else if (held.getItem() instanceof ItemShears) {
            // Vanilla shearing would drop vanilla wool, so shear the sheep here instead.
            if (!sheep.getSheared() && !sheep.isChild()) {
                event.setCanceled(true);
                shear(sheep);
                useHeldItem(player, held, true);
            }
        }
    }

    /** Swaps the vanilla wool a colored sheep drops when killed for More Dyes wool. */
    @ForgeSubscribe
    public void onLivingDrops(LivingDropsEvent event) {
        if (!(event.entityLiving instanceof EntitySheep)) {
            return;
        }
        int color = SheepColors.get((EntitySheep) event.entityLiving);
        if (color == SheepColors.NONE) {
            return;
        }
        for (EntityItem drop : event.drops) {
            ItemStack stack = drop.getEntityItem();
            if (stack != null && stack.itemID == Block.cloth.blockID) {
                drop.func_92058_a(new ItemStack(MDBlocks.wool, stack.stackSize, color));
            }
        }
    }

    /** Shears a sheep like vanilla shears do, dropping 1 to 3 More Dyes wool. */
    private static void shear(EntitySheep sheep) {
        Random rand = sheep.worldObj.rand;
        int color = SheepColors.get(sheep);
        sheep.setSheared(true);
        int count = 1 + rand.nextInt(3);
        for (int i = 0; i < count; i++) {
            EntityItem item = sheep.entityDropItem(new ItemStack(MDBlocks.wool, 1, color), 1.0F);
            item.motionY += rand.nextFloat() * 0.05F;
            item.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
            item.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
        }
        sheep.playSound("mob.sheep.shear", 1.0F, 1.0F);
    }

    /** Takes one dye from the player's hand, or wears down the shears in it. Creative players keep their dye. */
    private static void useHeldItem(EntityPlayer player, ItemStack held, boolean shears) {
        if (shears) {
            held.damageItem(1, player);
        } else if (!player.capabilities.isCreativeMode) {
            held.stackSize--;
        }
        if (held.stackSize <= 0) {
            player.destroyCurrentEquippedItem();
        }
        player.inventoryContainer.detectAndSendChanges();
    }
}
