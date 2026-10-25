package net.neverandy.moredyes.entity;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.neverandy.moredyes.item.MDItemDye;
import net.neverandy.moredyes.network.SheepColorPacket;

/**
 * Lets tamed wolves and cats wear a collar in any MoreDyes color. Vanilla collars only know the 16 vanilla colors, so
 * the MoreDyes color is kept in a field the mixin adds to the pet (saved with it, see mixin/TamableAnimalMixin) and
 * sent to the players who can see it, the same way as for sheep (see DyedSheep). The client draws the collar in that
 * color (client/DyedCollarRenderers).
 */
public final class DyedCollars
{
    private DyedCollars() {}

    public static void register()
    {
        UseEntityCallback.EVENT.register((player, level, hand, entity, hit) -> dye(entity, player, player.getItemInHand(hand)));
    }

    /** Wolves and cats are the pets with a collar. */
    public static boolean hasCollar(Entity entity)
    {
        return entity instanceof Wolf || entity instanceof Cat;
    }

    /** A player who starts seeing a pet with a MoreDyes collar is told its color (see mixin/ServerEntityMixin). */
    public static void startedSeeing(TamableAnimal pet, ServerPlayer player)
    {
        if (getColor(pet) >= 0)
        {
            SheepColorPacket.send(player, pet.getId(), getColor(pet));
        }
    }

    /** The pet's MoreDyes collar color as an index into ColorStrings.ALL, or -1 if it has a vanilla color. */
    public static int getColor(TamableAnimal pet)
    {
        return ((Collar) pet).moredyes$getCollar();
    }

    /** Sets the MoreDyes collar color (-1 for none) and, on the server, tells the players who can see the pet. */
    public static void setColor(TamableAnimal pet, int color)
    {
        ((Collar) pet).moredyes$setCollar(color);
        if (!pet.level().isClientSide)
        {
            for (ServerPlayer player : PlayerLookup.tracking(pet))
            {
                SheepColorPacket.send(player, pet.getId(), color);
            }
        }
    }

    /**
     * Dyes the collar of a tamed wolf or cat that belongs to the player. This runs before the pet's own right click,
     * which would otherwise make it sit down: vanilla only lets vanilla dyes through to the collar. A vanilla dye on a
     * MoreDyes collar gives it that vanilla color again, even the one hidden underneath.
     */
    private static InteractionResult dye(Entity entity, Player player, ItemStack stack)
    {
        if (!(entity instanceof TamableAnimal pet) || !hasCollar(pet) || !pet.isAlive() || !pet.isTame() || !pet.isOwnedBy(player))
        {
            return InteractionResult.PASS;
        }
        boolean server = !pet.level().isClientSide;
        if (stack.getItem() instanceof MDItemDye dye)
        {
            if (getColor(pet) == dye.colorIndex())
            {
                return InteractionResult.PASS;
            }
            if (server)
            {
                setColor(pet, dye.colorIndex());
            }
        }
        else if (stack.getItem() instanceof DyeItem dye && getColor(pet) >= 0)
        {
            if (server)
            {
                setColor(pet, -1);
                setVanillaColor(pet, dye.getDyeColor());
            }
        }
        else
        {
            return InteractionResult.PASS;
        }
        if (server && !player.getAbilities().instabuild)
        {
            stack.shrink(1);
        }
        return InteractionResult.sidedSuccess(!server);
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

    /** Added to every tamable animal by mixin/TamableAnimalMixin; only wolves and cats use it. */
    public interface Collar
    {
        int moredyes$getCollar();

        void moredyes$setCollar(int color);
    }
}
