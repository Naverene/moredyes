package net.neverandy.moredyes.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.IntUnaryOperator;

/**
 * Picks the More Dyes color a sheep spawning in the wild gets, if any. By default about one sheep in five gets a
 * natural brown, beige or grey, and one in fifty a rare burgundy, next to the colors vanilla sheep spawn with.
 */
public final class WildSheepColors {
    /** The natural greys, beiges and browns wild sheep get by default. */
    public static final List<String> NATURAL = Collections.unmodifiableList(Arrays.asList(
        "cccccc", "727272", "595959", "333333",
        "b3a699", "998c79", "927f66", "b29272", "b98c66",
        "9f8c85", "807366", "594c40",
        "92663f", "7f5933", "403326"));
    /** The rare color wild sheep get by default: burgundy. */
    public static final List<String> RARE = Collections.singletonList("592626");
    public static final double NATURAL_CHANCE = 0.2D;
    public static final double RARE_CHANCE = 0.02D;

    private WildSheepColors() {}

    /**
     * The color (hex) a sheep spawning in the wild gets, or null to keep its vanilla color.
     *
     * @param roll       a random number from 0 to 1
     * @param nextInt    gives a random number from 0 to n - 1
     * @param all        every More Dyes color (hex)
     * @param chance     chance to get one of {@code colors}
     * @param colors     the colors to pick from; empty means any More Dyes color
     * @param rareChance chance to get one of {@code rareColors} instead
     * @param rareColors the rare colors; empty means any More Dyes color
     */
    public static String pick(double roll, IntUnaryOperator nextInt, List<String> all,
        double chance, List<? extends String> colors, double rareChance, List<? extends String> rareColors) {
        if (roll < rareChance) {
            return choose(nextInt, all, rareColors);
        }
        if (roll < rareChance + chance) {
            return choose(nextInt, all, colors);
        }
        return null;
    }

    private static String choose(IntUnaryOperator nextInt, List<String> all, List<? extends String> colors) {
        if (colors.isEmpty()) {
            return all.get(nextInt.applyAsInt(all.size()));
        }
        // Names that aren't More Dyes colors are skipped.
        List<String> known = new ArrayList<>();
        for (String color : colors) {
            String hex = normalize(color);
            if (all.contains(hex)) {
                known.add(hex);
            }
        }
        return known.isEmpty() ? null : known.get(nextInt.applyAsInt(known.size()));
    }

    /** "#7F5933 " becomes "7f5933". */
    public static String normalize(String color) {
        String hex = color.trim().toLowerCase(Locale.ROOT);
        return hex.startsWith("#") ? hex.substring(1) : hex;
    }
}
