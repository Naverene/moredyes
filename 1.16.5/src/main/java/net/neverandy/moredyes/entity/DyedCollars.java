package net.neverandy.moredyes.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.neverandy.moredyes.item.MDItemDye;
import net.neverandy.moredyes.network.ModNetwork;
import net.neverandy.moredyes.network.SheepColorPacket;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

/**
 * Lets tamed wolves and cats wear a collar in any MoreDyes color. Vanilla collars only know the 16 vanilla colors, so
 * the MoreDyes color is kept in the pet's Forge data (saved with the pet) and sent to the players who can see it, the
 * same way as for sheep (see DyedSheep). The client draws the collar in that color (client/DyedCollarLayers).
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class DyedCollars
{
    private static final String KEY = Reference.MOD_ID + ":collar";

    private DyedCollars() {}

    /** Wolves and cats are the pets with a collar. */
    public static boolean hasCollar(Entity entity)
    {
        return entity instanceof WolfEntity || entity instanceof CatEntity;
    }

    /** The pet's MoreDyes collar color as an index into ColorStrings.ALL, or -1 if it has a vanilla color. */
    public static int getColor(TameableEntity pet)
    {
        CompoundNBT data = pet.getPersistentData();
        if (!data.contains(KEY))
        {
            return -1;
        }
        String hex = data.getString(KEY);
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            if (ColorStrings.ALL[i].equals(hex))
            {
                return i;
            }
        }
        return -1;
    }

    /** Sets the MoreDyes collar color (-1 for none) and, on the server, tells the players who can see the pet. */
    public static void setColor(TameableEntity pet, int color)
    {
        if (color < 0)
        {
            pet.getPersistentData().remove(KEY);
        }
        else
        {
            pet.getPersistentData().putString(KEY, ColorStrings.ALL[color]);
        }
        if (!pet.world.isRemote && pet.isAddedToWorld())
        {
            ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> pet), new SheepColorPacket(pet.getEntityId(), color));
        }
    }

    /**
     * Dyes the collar of a tamed wolf or cat that belongs to the player. This runs before the pet's own right click,
     * which would otherwise make it sit down: vanilla only lets vanilla dyes through to the collar.
     */
    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.EntityInteract event)
    {
        if (!hasCollar(event.getTarget()))
        {
            return;
        }
        TameableEntity pet = (TameableEntity) event.getTarget();
        ItemStack stack = event.getItemStack();
        PlayerEntity player = event.getPlayer();
        if (!pet.isAlive() || !pet.isTamed() || !pet.isOwner(player))
        {
            return;
        }
        if (stack.getItem() instanceof MDItemDye)
        {
            int color = ((MDItemDye) stack.getItem()).colorIndex();
            if (getColor(pet) == color)
            {
                return;
            }
            if (!pet.world.isRemote)
            {
                setColor(pet, color);
                if (!player.abilities.isCreativeMode)
                {
                    stack.shrink(1);
                }
            }
        }
        else if (stack.getItem() instanceof DyeItem && getColor(pet) >= 0)
        {
            // A vanilla dye on a MoreDyes collar gives it that vanilla color again, even the one hidden underneath.
            if (!pet.world.isRemote)
            {
                setColor(pet, -1);
                setVanillaColor(pet, ((DyeItem) stack.getItem()).getDyeColor());
                if (!player.abilities.isCreativeMode)
                {
                    stack.shrink(1);
                }
            }
        }
        else
        {
            return;
        }
        event.setCancellationResult(ActionResultType.func_233537_a_(pet.world.isRemote));
        event.setCanceled(true);
    }

    private static void setVanillaColor(TameableEntity pet, DyeColor color)
    {
        if (pet instanceof WolfEntity)
        {
            ((WolfEntity) pet).setCollarColor(color);
        }
        else if (pet instanceof CatEntity)
        {
            ((CatEntity) pet).setCollarColor(color);
        }
    }

    /** A player who starts seeing a pet with a MoreDyes collar is told its color. */
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event)
    {
        if (hasCollar(event.getTarget()) && event.getPlayer() instanceof ServerPlayerEntity)
        {
            int color = getColor((TameableEntity) event.getTarget());
            if (color >= 0)
            {
                ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()),
                        new SheepColorPacket(event.getTarget().getEntityId(), color));
            }
        }
    }
}
