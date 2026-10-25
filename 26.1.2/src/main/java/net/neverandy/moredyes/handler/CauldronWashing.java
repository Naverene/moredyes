package net.neverandy.moredyes.handler;

import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.neoforged.neoforge.event.RegisterCauldronInteractionEvent;
import net.neoforged.neoforge.registries.DeferredItem;

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

    private static final Identifier WATER = Identifier.withDefaultNamespace("water");

    private CauldronWashing() {}

    public static void register(RegisterCauldronInteractionEvent.Interaction event) {
        for (Kind kind : Kind.values()) {
            if (!kind.hasItem() || !kind.enabled() || kind.washed().isEmpty()) {
                continue;
            }
            Item washed = kind.washed().get();
            for (MixColor color : MixColors.ALL) {
                DeferredItem<?> dyed = ModItems.get(kind, color);
                if (kind == Kind.SHULKER_BOX) {
                    // A shulker box keeps its contents and loses its color, as vanilla's dyed ones do.
                    event.register(WATER, dyed.get(), CauldronInteractions::shulkerBoxInteraction);
                    continue;
                }
                event.register(WATER, dyed.get(), (state, level, pos, player, hand, held) -> {
                    // A banner with patterns loses its top pattern, as vanilla's do; one with none washes white.
                    if (kind == Kind.BANNER && !held.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
                        .layers().isEmpty()) {
                        return CauldronInteractions.bannerInteraction(state, level, pos, player, hand, held);
                    }
                    if (!level.isClientSide()) {
                        player.setItemInHand(hand, new ItemStack(washed, held.getCount()));
                        player.awardStat(Stats.USE_CAULDRON);
                        LayeredCauldronBlock.lowerFillLevel(state, level, pos);
                    }
                    return InteractionResult.SUCCESS;
                });
            }
        }
    }
}
