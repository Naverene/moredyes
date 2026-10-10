package net.neverandy.moredyes.client;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.color.MixColors;

/**
 * Colors every dyed block and item. Their textures are grey, and the game multiplies the faces a model marks with
 * {@code tintindex 0} (and layer 0 of a flat item model) by the color returned here, the same way it colors grass. The
 * color is the hex code at the end of the registry name, such as {@code wool_334c59}, drawn a little more vividly (see
 * {@link MixColors#vivid}).
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

    /** The tint for a More Dyes item, or white (no tint) for any other item. */
    public static int itemTint(Item item) {
        return MixColors.of(BuiltInRegistries.ITEM.getKey(item)).map(Tints::argb).orElse(-1);
    }
}
