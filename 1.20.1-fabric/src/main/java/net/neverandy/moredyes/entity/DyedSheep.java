package net.neverandy.moredyes.entity;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.neverandy.moredyes.ConfigHandler;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.network.SheepColorPacket;
import net.neverandy.moredyes.reference.ColorStrings;

/**
 * Lets vanilla sheep wear any MoreDyes color. Vanilla sheep only know the 16 vanilla colors, so the MoreDyes color is
 * kept in a field the mixin adds to the sheep (saved with it, see mixin/SheepMixin) and sent to the players who can
 * see it. The client draws the wool in that color (client/DyedSheepRenderer), and vanilla wool the sheep drops,
 * sheared or killed, is swapped for the dyed wool (mixin/EntityMixin).
 */
public final class DyedSheep
{
    private DyedSheep() {}

    public static void register()
    {
        UseEntityCallback.EVENT.register((player, level, hand, entity, hit) ->
                entity instanceof Sheep sheep ? undye(sheep, player.getItemInHand(hand), player.getAbilities().instabuild) : InteractionResult.PASS);
    }

    /**
     * A player who starts seeing a dyed sheep is told its color, right after the sheep itself (mixin/ServerEntityMixin).
     * Fabric's START_TRACKING event fires before the player knows about the sheep, so it can't be used for this.
     */
    public static void startedSeeing(Sheep sheep, ServerPlayer player)
    {
        if (getColor(sheep) >= 0)
        {
            SheepColorPacket.send(player, sheep.getId(), getColor(sheep));
        }
    }

    /** The sheep's MoreDyes color as an index into ColorStrings.ALL, or -1 if it has a vanilla color. */
    public static int getColor(Sheep sheep)
    {
        return ((Fleece) sheep).moredyes$getFleece();
    }

    /** Sets the MoreDyes color (-1 for none) and, on the server, tells the players who can see the sheep. */
    public static void setColor(Sheep sheep, int color)
    {
        ((Fleece) sheep).moredyes$setFleece(color);
        if (!sheep.level().isClientSide)
        {
            for (ServerPlayer player : PlayerLookup.tracking(sheep))
            {
                SheepColorPacket.send(player, sheep.getId(), color);
            }
        }
    }

    /** Dyes a sheep with a MoreDyes dye. Called from MDItemDye. */
    public static InteractionResult dye(Sheep sheep, int color, ItemStack stack)
    {
        if (!sheep.isAlive() || sheep.isSheared() || getColor(sheep) == color)
        {
            return InteractionResult.PASS;
        }
        if (!sheep.level().isClientSide)
        {
            setColor(sheep, color);
            stack.shrink(1);
        }
        return InteractionResult.sidedSuccess(sheep.level().isClientSide);
    }

    /** A vanilla dye on a sheep with a MoreDyes color gives it that vanilla color again. */
    private static InteractionResult undye(Sheep sheep, ItemStack stack, boolean creative)
    {
        if (!(stack.getItem() instanceof DyeItem dye) || !sheep.isAlive() || sheep.isSheared() || getColor(sheep) < 0)
        {
            return InteractionResult.PASS;
        }
        if (!sheep.level().isClientSide)
        {
            setColor(sheep, -1);
            sheep.setColor(dye.getDyeColor());
            if (!creative)
            {
                stack.shrink(1);
            }
        }
        return InteractionResult.sidedSuccess(sheep.level().isClientSide);
    }

    /** Vanilla wool dropped by a sheep with a MoreDyes color, sheared or killed, becomes that color's wool. */
    public static ItemStack drop(Sheep sheep, ItemStack stack)
    {
        int color = getColor(sheep);
        if (color < 0 || !stack.is(ItemTags.WOOL) || !"minecraft".equals(BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace()))
        {
            return stack;
        }
        return new ItemStack(MDBlock.woolArray[color], stack.getCount());
    }

    /** Some sheep spawn in the world with a random MoreDyes color, both when a chunk is generated and later. */
    public static void spawned(Sheep sheep, MobSpawnType reason)
    {
        if ((reason == MobSpawnType.NATURAL || reason == MobSpawnType.CHUNK_GENERATION)
                && sheep.getRandom().nextDouble() < ConfigHandler.sheepSpawnChance())
        {
            // Not in the world yet, so nobody needs telling: players are told when they start seeing it.
            ((Fleece) sheep).moredyes$setFleece(sheep.getRandom().nextInt(ColorStrings.ALL.length));
        }
    }

    /** A lamb, not in the world yet, takes the color of one of its parents, picked at random. */
    public static void bred(Sheep a, Sheep b, Sheep child)
    {
        if (getColor(a) < 0 && getColor(b) < 0)
        {
            return;
        }
        Sheep parent = child.getRandom().nextBoolean() ? a : b;
        if (getColor(parent) >= 0)
        {
            ((Fleece) child).moredyes$setFleece(getColor(parent));
        }
        else
        {
            child.setColor(parent.getColor());
        }
    }

    /** What mixin/SheepMixin adds to every sheep. */
    public interface Fleece
    {
        int moredyes$getFleece();

        void moredyes$setFleece(int color);
    }
}
