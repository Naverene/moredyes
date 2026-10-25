package net.neverandy.moredyes.handler;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stats;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LayeredCauldronBlock;

import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.registry.ModItems;

/**
 * Using a stack of dyed blocks on a cauldron with water in it washes the whole stack back to the vanilla block and
 * uses up one level of water, like washing dyed leather armor. It is the same as crafting the block with a water
 * bucket (the {@code washing/} recipes), but for a whole stack at once.
 */
public final class CauldronWashing {

    private CauldronWashing() {}

    /** Adds the washing to vanilla's water cauldron interactions. Call after the items are registered. */
    public static void register() {
        for (Kind kind : Kind.values()) {
            if (!kind.hasItem() || !kind.enabled() || kind.washed().isEmpty()) {
                continue;
            }
            Item washed = kind.washed().get();
            CauldronInteraction wash = (state, level, pos, player, hand, held) -> {
                if (!level.isClientSide()) {
                    player.setItemInHand(hand, new ItemStack(washed, held.getCount()));
                    player.awardStat(Stats.USE_CAULDRON);
                    LayeredCauldronBlock.lowerFillLevel(state, level, pos);
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide());
            };
            if (kind == Kind.SHULKER_BOX) {
                // A shulker box keeps its contents and loses its color, as vanilla's dyed ones do.
                wash = CauldronInteraction.SHULKER_BOX;
            } else if (kind == Kind.BANNER) {
                // A banner with patterns loses its top pattern, as vanilla's do; one with none washes white.
                CauldronInteraction plain = wash;
                wash = (state, level, pos, player, hand, held) -> {
                    ItemInteractionResult result = CauldronInteraction.BANNER.interact(state, level, pos, player, hand, held);
                    return result == ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
                        ? plain.interact(state, level, pos, player, hand, held) : result;
                };
            }
            for (MixColor color : MixColors.ALL) {
                CauldronInteraction.WATER.map().put(ModItems.get(kind, color), wash);
            }
        }
    }
}
