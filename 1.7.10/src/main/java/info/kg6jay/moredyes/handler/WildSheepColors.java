package info.kg6jay.moredyes.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Picks the More Dyes color a sheep spawning in the wild gets, if any. By default about one sheep in five gets a
 * natural brown, beige or grey, and one in fifty a rare burgundy, next to the colors vanilla sheep spawn with.
 */
public final class WildSheepColors {

    /** The natural greys, beiges and browns wild sheep get by default. */
    public static final String[] NATURAL = { "cccccc", "727272", "595959", "333333", "b3a699", "998c79", "927f66",
        "b29272", "b98c66", "9f8c85", "807366", "594c40", "92663f", "7f5933", "403326" };
    /** The rare color wild sheep get by default: burgundy. */
    public static final String[] RARE = { "592626" };
    public static final double NATURAL_CHANCE = 0.2D;
    public static final double RARE_CHANCE = 0.02D;

    private WildSheepColors() {}

    /**
     * The color a sheep spawning in the wild gets, as an index into {@code all}, or -1 to keep its vanilla color.
     * An empty color list means any More Dyes color; names that aren't More Dyes colors are skipped.
     *
     * @param all every More Dyes color (lowercase hex)
     */
    public static int pick(Random rand, List<String> all, double chance, String[] colors, double rareChance,
        String[] rareColors) {
        double roll = rand.nextDouble();
        if (roll < rareChance) {
            return choose(rand, all, rareColors);
        }
        if (roll < rareChance + chance) {
            return choose(rand, all, colors);
        }
        return -1;
    }

    private static int choose(Random rand, List<String> all, String[] colors) {
        if (colors.length == 0) {
            return rand.nextInt(all.size());
        }
        List<Integer> known = new ArrayList<Integer>();
        for (String color : colors) {
            String hex = color.trim()
                .toLowerCase(Locale.ROOT);
            if (hex.startsWith("#")) {
                hex = hex.substring(1);
            }
            int index = all.indexOf(hex);
            if (index >= 0) {
                known.add(index);
            }
        }
        return known.isEmpty() ? -1 : known.get(rand.nextInt(known.size()));
    }
}
