package net.neverandy.moredyes.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.neverandy.moredyes.item.MDItemDye;
import net.neverandy.moredyes.network.ModNetwork;
import net.neverandy.moredyes.network.SheepColorPacket;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

/**
 * Lets tamed wolves and cats wear a collar in any MoreDyes color. Vanilla collars only know the 16 vanilla colors, so
 * the MoreDyes color is kept in the pet's Forge data (saved with the pet) and sent to the players who can see it, the
 * same way as for sheep (see DyedSheep). The client draws the collar in that color (client/DyedCollarRenderers).
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class DyedCollars
{
    private static final String KEY = Reference.MOD_ID + ":collar";

    private DyedCollars() {}

    /** Wolves and cats are the pets with a collar. */
    public static boolean hasCollar(Object entity)
    {
        return entity instanceof Wolf || entity instanceof Cat;
    }

    /** The pet's MoreDyes collar color as an index into ColorStrings.ALL, or -1 if it has a vanilla color. */
    public static int getColor(TamableAnimal pet)
    {
        CompoundTag data = pet.getPersistentData();
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
    public static void setColor(TamableAnimal pet, int color)
    {
        if (color < 0)
        {
            pet.getPersistentData().remove(KEY);
        }
        else
        {
            pet.getPersistentData().putString(KEY, ColorStrings.ALL[color]);
        }
        if (!pet.level.isClientSide && pet.isAddedToWorld())
        {
            ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> pet), new SheepColorPacket(pet.getId(), color));
        }
    }

    /**
     * Dyes the collar of a tamed wolf or cat that belongs to the player. This runs before the pet's own right click,
     * which would otherwise make it sit down: vanilla only lets vanilla dyes through to the collar.
     */
    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.EntityInteract event)
    {
        if (!(event.getTarget() instanceof TamableAnimal pet) || !hasCollar(pet))
        {
            return;
        }
        ItemStack stack = event.getItemStack();
        Player player = event.getPlayer();
        if (!pet.isAlive() || !pet.isTame() || !pet.isOwnedBy(player))
        {
            return;
        }
        if (stack.getItem() instanceof MDItemDye dye)
        {
            if (getColor(pet) == dye.colorIndex())
            {
                return;
            }
            if (!pet.level.isClientSide)
            {
                setColor(pet, dye.colorIndex());
                if (!player.getAbilities().instabuild)
                {
                    stack.shrink(1);
                }
            }
        }
        else if (stack.getItem() instanceof DyeItem dye && getColor(pet) >= 0)
        {
            // A vanilla dye on a MoreDyes collar gives it that vanilla color again, even the one hidden underneath.
            if (!pet.level.isClientSide)
            {
                setColor(pet, -1);
                setVanillaColor(pet, dye.getDyeColor());
                if (!player.getAbilities().instabuild)
                {
                    stack.shrink(1);
                }
            }
        }
        else
        {
            return;
        }
        event.setCancellationResult(InteractionResult.sidedSuccess(pet.level.isClientSide));
        event.setCanceled(true);
    }

    private static void setVanillaColor(TamableAnimal pet, DyeColor color)
    {
        if (pet instanceof Wolf wolf)
        {
            wolf.setCollarColor(color);
        }
        else if (pet instanceof Cat cat)
        {
            cat.setCollarColor(color);
        }
    }

    /** A player who starts seeing a pet with a MoreDyes collar is told its color. */
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event)
    {
        if (event.getTarget() instanceof TamableAnimal pet && hasCollar(pet) && event.getPlayer() instanceof ServerPlayer player)
        {
            int color = getColor(pet);
            if (color >= 0)
            {
                ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SheepColorPacket(pet.getId(), color));
            }
        }
    }
}
