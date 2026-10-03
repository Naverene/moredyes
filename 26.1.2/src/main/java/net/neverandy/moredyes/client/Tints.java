package net.neverandy.moredyes.client;

import com.mojang.serialization.MapCodec;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.Block;

import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.color.MixColors;

/**
 * Colors every dyed block and item. Their textures are grey, and the game multiplies the faces a model marks with
 * {@code tintindex 0} by the color returned here, the same way it colors grass. The color is the hex code at the end of
 * the registry name, such as {@code wool_334c59}, drawn a little more vividly (see {@link MixColors#vivid}).
 */
public final class Tints {

    private Tints() {}

    /** The opaque ARGB color a dyed color is drawn in. */
    public static int argb(MixColor color) {
        return 0xFF000000 | MixColors.vivid(color.rgb());
    }

    /** The tint for a More Dyes block, or white (no tint) for any other block. */
    public static int blockTint(Block block) {
        return MixColors.of(BuiltInRegistries.BLOCK.getKey(block)).map(Tints::argb).orElse(-1);
    }

    /** The item tint {@code moredyes:dye_color}: the color in the item's registry name. */
    public record DyeColor() implements ItemTintSource {

        public static final MapCodec<DyeColor> MAP_CODEC = MapCodec.unit(new DyeColor());

        @Override
        public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
            return MixColors.of(BuiltInRegistries.ITEM.getKey(stack.getItem())).map(Tints::argb).orElse(-1);
        }

        @Override
        public MapCodec<DyeColor> type() {
            return MAP_CODEC;
        }
    }

    /**
     * The item tint {@code moredyes:state_color}: the color whose position in {@link MixColors#ALL} is the
     * {@code color} property in the item's {@code minecraft:block_state} component. For blocks that keep their color
     * in a block state property, such as the dyed Storage Drawers (compat/storagedrawers).
     */
    public record StateColor() implements ItemTintSource {

        public static final MapCodec<StateColor> MAP_CODEC = MapCodec.unit(new StateColor());

        @Override
        public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
            BlockItemStateProperties state = stack.get(DataComponents.BLOCK_STATE);
            String color = state != null ? state.properties().get("color") : null;
            try {
                return argb(MixColors.ALL.get(color != null ? Integer.parseInt(color) : 0));
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                return -1;
            }
        }

        @Override
        public MapCodec<StateColor> type() {
            return MAP_CODEC;
        }
    }
}
