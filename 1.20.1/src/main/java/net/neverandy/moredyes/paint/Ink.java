package net.neverandy.moredyes.paint;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neverandy.moredyes.api.MoreDyesAPI;

import java.util.OptionalInt;

/**
 * The paint gun's five ink tanks: red, green, blue, white and black.
 *
 * <p>A color is split into a white part (the smallest of R, G and B), a black part (how far the largest is below
 * 255) and what is left of R, G and B. Painting a block uses that much of each tank, so a pure red block uses 255
 * red, a grey #808080 block uses 128 white and 127 black, and a yellow block uses 255 red and 255 green.
 *
 * <p>Loading a dye splits its color the same way, times {@link #BLOCKS_PER_DYE}: vanilla's red, green, blue, white
 * and black dyes each fill one tank, and every other dye (vanilla or More Dyes) adds a mix.
 */
public final class Ink
{
    public static final int RED = 0;
    public static final int GREEN = 1;
    public static final int BLUE = 2;
    public static final int WHITE = 3;
    public static final int BLACK = 4;
    public static final int TANKS = 5;
    public static final String[] NAMES = {"red", "green", "blue", "white", "black"};
    /** The color each tank is drawn in. */
    public static final int[] TANK_COLORS = {0xFF3030, 0x30D030, 0x3060FF, 0xF0F0F0, 0x000000};

    /** How many blocks one dye paints in its own color. */
    public static final int BLOCKS_PER_DYE = 32;
    /** One dye's worth of ink in a tank. */
    public static final int PER_DYE = 255 * BLOCKS_PER_DYE;
    /** Each tank holds this much, 64 dyes. */
    public static final int CAPACITY = PER_DYE * 64;

    private Ink() {}

    /** How much of each tank painting one block in a color (0xRRGGBB) uses. */
    public static int[] cost(int rgb)
    {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        int white = Math.min(r, Math.min(g, b));
        int black = 255 - Math.max(r, Math.max(g, b));
        return new int[]{r - white, g - white, b - white, white, black};
    }

    /** The color of a dye, or empty if the stack is not a dye. */
    public static OptionalInt dyeColor(ItemStack stack)
    {
        if (MoreDyesAPI.isDye(stack))
        {
            return MoreDyesAPI.getColor(stack);
        }
        DyeColor dye = DyeColor.getColor(stack);
        // The text colors are the pure ones: red is #FF0000, white #FFFFFF, black #000000.
        return dye == null ? OptionalInt.empty() : OptionalInt.of(dye.getTextColor());
    }

    /** The ink one dye of a color adds to each tank. */
    public static int[] perDye(int rgb)
    {
        int[] ink = cost(rgb);
        for (int i = 0; i < TANKS; i++)
        {
            ink[i] *= BLOCKS_PER_DYE;
        }
        return ink;
    }

    /** Whether the tanks hold at least the given amounts. */
    public static boolean has(int[] tanks, int[] amounts)
    {
        for (int i = 0; i < TANKS; i++)
        {
            if (tanks[i] < amounts[i])
            {
                return false;
            }
        }
        return true;
    }

    /** How many dyes of a color fit before a tank it adds to is full, up to max. */
    public static int dyesThatFit(int[] tanks, int rgb, int max)
    {
        int[] add = perDye(rgb);
        int fit = max;
        for (int i = 0; i < TANKS; i++)
        {
            if (add[i] > 0)
            {
                fit = Math.min(fit, (CAPACITY - tanks[i]) / add[i]);
            }
        }
        return Math.max(fit, 0);
    }
}
