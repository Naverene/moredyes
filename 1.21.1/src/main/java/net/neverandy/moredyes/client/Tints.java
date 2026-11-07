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

    /**
     * The color a lit lamp is drawn in (tint index 1 of its model): the dye color brightened, keeping its hue, until
     * its brightest channel is full or it is 60% brighter. Dark colors stay darker than light ones, so lamps of
     * different shades still differ.
     */
    public static int glow(int argb) {
        int r = argb >> 16 & 0xFF, g = argb >> 8 & 0xFF, b = argb & 0xFF;
        int max = Math.max(1, Math.max(r, Math.max(g, b)));
        int percent = Math.min(160, 255 * 100 / max);
        return 0xFF000000 | r * percent / 100 << 16 | g * percent / 100 << 8 | b * percent / 100;
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
