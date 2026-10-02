package net.neverandy.moredyes.handler;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neverandy.moredyes.reference.Reference;

/**
 * Right-clicking a cauldron that has water in it with a stack of dyed blocks washes the whole stack back to the
 * vanilla block and uses up one level of water, like washing dyed leather armor. The vanilla block is the result of
 * the block's "washing/" crafting recipe (dyed block plus water bucket), so a data pack that changes that recipe
 * changes the cauldron too.
 */
public final class CauldronWashing
{
    private CauldronWashing() {}

    public static void register()
    {
        UseBlockCallback.EVENT.register((player, level, hand, hit) -> onRightClickBlock(player, level, hand, hit.getBlockPos()));
    }

    private static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockPos pos)
    {
        BlockState state = level.getBlockState(pos);
        ItemStack held = player.getItemInHand(hand);
        if (player.isSpectator() || !state.is(Blocks.WATER_CAULDRON) || held.isEmpty())
        {
            return InteractionResult.PASS;
        }
        ItemStack washed = washed(level, held);
        if (washed.isEmpty())
        {
            return InteractionResult.PASS;
        }

        // Handled on both sides so the held block is not placed; the client still sends the click to the server.
        if (level.isClientSide)
        {
            return InteractionResult.SUCCESS;
        }
        ItemStack result = washed.copy();
        result.setCount(held.getCount());
        player.setItemInHand(hand, result);
        LayeredCauldronBlock.lowerFillLevel(state, level, pos);
        player.awardStat(Stats.USE_CAULDRON);
        return InteractionResult.CONSUME;
    }

    /** The vanilla block a dyed block washes back into, or an empty stack if it isn't a dyed block. */
    private static ItemStack washed(Level level, ItemStack dyed)
    {
        ResourceLocation item = BuiltInRegistries.ITEM.getKey(dyed.getItem());
        if (!Reference.MOD_ID.equals(item.getNamespace()))
        {
            return ItemStack.EMPTY;
        }
        return level.getRecipeManager().byKey(new ResourceLocation(Reference.MOD_ID, "washing/" + item.getPath()))
                .map((Recipe<?> recipe) -> recipe.getResultItem(level.registryAccess())).orElse(ItemStack.EMPTY);
    }
}
