package info.kg6jay.moredyes;

import java.util.Locale;

/**
 * The More Dyes colors. Each is a mix of two vanilla dyes, grouped in fifteen sets named after the first dye of the
 * mix. Every color has a number from 0 upward (the white set's colors first, then orange's, and so on), which is the
 * damage of its dye and of every dyed block item, and what a dyed block keeps in its tile entity.
 */
public final class Colors {

    public static final String[] SET_NAMES = { "white", "orange", "magenta", "lightBlue", "yellow", "lime", "pink",
        "darkGray", "lightGray", "cyan", "purple", "blue", "brown", "green", "red" };

    /** The colors of each set, as hex codes. */
    public static final String[][] SETS = {
        { "ecbf99", "d9a6ec", "b3ccec", "f2f299", "bfe68c", "f9bfd2", "cccccc", "a6bfcc", "bf9fd9", "99a6d9", "b3a699", "b3bf99", "cc9999" },
        { "c56685", "9f8c85", "deb233", "aca526", "e57f6c", "92663f", "b98c66", "927f66", "ac5f72", "866672", "9f6633", "9f7f33", "b95933", "794c26" },
        { "8c72d8", "cb9886", "998c79", "d265bf", "7f4c92", "a672b9", "7f65b9", "9946c5", "734cc5", "8c4c86", "8c6586", "a64086", "663379" },
        { "a5bf86", "72b279", "ac8cbf", "597392", "7f99b9", "598cb9", "726cc5", "4d73c5", "667386", "668c86", "7f6686", "405979" },
        { "b2d926", "ebb26c", "99993f", "bfbf66", "99b266", "b29272", "8c9972", "a69933", "a6b233", "bf8c33", "7f7f26" },
        { "b8a65f", "668c32", "8cb359", "66a659", "7f5665", "598c65", "738c26", "73a626", "8c8026", "4c7319" },
        { "864c5f", "c6596c", "ac7f6c", "ac666c", "9366ab", "b95fab", "9f7f9f", "c68c9f", "9f6679" },
        { "727272", "4c6572", "65467f", "404c7f", "594c40", "596540", "724040", "333333" },
        { "738c99", "8c6ca5", "6673a5", "807366", "808c66", "996666", "595959" },
        { "655fa5", "4066a5", "596666", "597f66", "725966", "334c59" },
        { "5945b2", "734573", "735f73", "8c3973", "4c2c66" },
        { "4c4c73", "4c6573", "664073", "263366" },
        { "403326", "7f4033", "666533" },
        { "7f5933", "404c26" },
        { "592626" },
    };

    /**
     * How each color is mixed: { set, shade, first vanilla dye, second vanilla dye, doubled }. The vanilla dyes are
     * their item damage (15 is bone meal, 0 ink). A mix whose two dyes already make a vanilla dye together (bone meal
     * and lapis make light blue) takes two of each instead, so the vanilla recipe still works; it makes four.
     */
    public static final int[][] MIXES = {
        { 0, 0, 15, 14, 0 },
        { 0, 1, 15, 13, 0 },
        { 0, 2, 15, 12, 0 },
        { 0, 3, 15, 11, 0 },
        { 0, 4, 15, 10, 0 },
        { 0, 5, 15, 9, 0 },
        { 0, 6, 15, 7, 0 },
        { 0, 7, 15, 6, 0 },
        { 0, 8, 15, 5, 0 },
        { 0, 9, 15, 4, 1 },
        { 0, 10, 15, 3, 0 },
        { 0, 11, 15, 2, 1 },
        { 0, 12, 15, 1, 1 },
        { 1, 0, 14, 13, 0 },
        { 1, 1, 14, 12, 0 },
        { 1, 2, 14, 11, 0 },
        { 1, 3, 14, 10, 0 },
        { 1, 4, 14, 9, 0 },
        { 1, 5, 14, 8, 0 },
        { 1, 6, 14, 7, 0 },
        { 1, 7, 14, 6, 0 },
        { 1, 8, 14, 5, 0 },
        { 1, 9, 14, 4, 0 },
        { 1, 10, 14, 3, 0 },
        { 1, 11, 14, 2, 0 },
        { 1, 12, 14, 1, 0 },
        { 1, 13, 14, 0, 0 },
        { 2, 0, 13, 12, 0 },
        { 2, 1, 13, 11, 0 },
        { 2, 2, 13, 10, 0 },
        { 2, 3, 13, 9, 0 },
        { 2, 4, 13, 8, 0 },
        { 2, 5, 13, 7, 0 },
        { 2, 6, 13, 6, 0 },
        { 2, 7, 13, 5, 0 },
        { 2, 8, 13, 4, 0 },
        { 2, 9, 13, 3, 0 },
        { 2, 10, 13, 2, 0 },
        { 2, 11, 13, 1, 0 },
        { 2, 12, 13, 0, 0 },
        { 3, 0, 12, 11, 0 },
        { 3, 1, 12, 10, 0 },
        { 3, 2, 12, 9, 0 },
        { 3, 3, 12, 8, 0 },
        { 3, 4, 12, 7, 0 },
        { 3, 5, 12, 6, 0 },
        { 3, 6, 12, 5, 0 },
        { 3, 7, 12, 4, 0 },
        { 3, 8, 12, 3, 0 },
        { 3, 9, 12, 2, 0 },
        { 3, 10, 12, 1, 0 },
        { 3, 11, 12, 0, 0 },
        { 4, 0, 11, 10, 0 },
        { 4, 1, 11, 9, 0 },
        { 4, 2, 11, 8, 0 },
        { 4, 3, 11, 7, 0 },
        { 4, 4, 11, 6, 0 },
        { 4, 5, 11, 5, 0 },
        { 4, 6, 11, 4, 0 },
        { 4, 7, 11, 3, 0 },
        { 4, 8, 11, 2, 0 },
        { 4, 9, 11, 1, 1 },
        { 4, 10, 11, 0, 0 },
        { 5, 0, 10, 9, 0 },
        { 5, 1, 10, 8, 0 },
        { 5, 2, 10, 7, 0 },
        { 5, 3, 10, 6, 0 },
        { 5, 4, 10, 5, 0 },
        { 5, 5, 10, 4, 0 },
        { 5, 6, 10, 3, 0 },
        { 5, 7, 10, 2, 0 },
        { 5, 8, 10, 1, 0 },
        { 5, 9, 10, 0, 0 },
        { 6, 0, 9, 8, 0 },
        { 6, 1, 9, 7, 0 },
        { 6, 2, 9, 6, 0 },
        { 6, 3, 9, 5, 1 },
        { 6, 4, 9, 4, 0 },
        { 6, 5, 9, 3, 0 },
        { 6, 6, 9, 2, 0 },
        { 6, 7, 9, 1, 0 },
        { 6, 8, 9, 0, 0 },
        { 7, 0, 8, 7, 0 },
        { 7, 1, 8, 6, 0 },
        { 7, 2, 8, 5, 0 },
        { 7, 3, 8, 4, 0 },
        { 7, 4, 8, 3, 0 },
        { 7, 5, 8, 2, 0 },
        { 7, 6, 8, 1, 0 },
        { 7, 7, 8, 0, 0 },
        { 8, 0, 7, 6, 0 },
        { 8, 1, 7, 5, 0 },
        { 8, 2, 7, 4, 0 },
        { 8, 3, 7, 3, 0 },
        { 8, 4, 7, 2, 0 },
        { 8, 5, 7, 1, 0 },
        { 8, 6, 7, 0, 0 },
        { 9, 0, 6, 5, 0 },
        { 9, 1, 6, 4, 0 },
        { 9, 2, 6, 3, 0 },
        { 9, 3, 6, 2, 0 },
        { 9, 4, 6, 1, 0 },
        { 9, 5, 6, 0, 0 },
        { 10, 0, 5, 4, 0 },
        { 10, 1, 5, 3, 0 },
        { 10, 2, 5, 2, 0 },
        { 10, 3, 5, 1, 0 },
        { 10, 4, 5, 0, 0 },
        { 11, 0, 4, 3, 0 },
        { 11, 1, 4, 2, 1 },
        { 11, 2, 4, 1, 1 },
        { 11, 3, 4, 0, 0 },
        { 12, 0, 3, 2, 0 },
        { 12, 1, 3, 1, 0 },
        { 12, 2, 3, 0, 0 },
        { 13, 0, 2, 1, 0 },
        { 13, 1, 2, 0, 0 },
        { 14, 0, 1, 0, 0 },
    };

    public static final int COUNT;
    private static final int[] SET, SHADE, RGB;
    private static final int[][] INDEX;

    static {
        int count = 0;
        for (String[] set : SETS) {
            count += set.length;
        }
        COUNT = count;
        SET = new int[count];
        SHADE = new int[count];
        RGB = new int[count];
        INDEX = new int[SETS.length][];
        int index = 0;
        for (int set = 0; set < SETS.length; set++) {
            INDEX[set] = new int[SETS[set].length];
            for (int shade = 0; shade < SETS[set].length; shade++) {
                SET[index] = set;
                SHADE[index] = shade;
                RGB[index] = Integer.parseInt(SETS[set][shade], 16);
                INDEX[set][shade] = index++;
            }
        }
    }

    private Colors() {}

    public static int of(int set, int shade) {
        return INDEX[set][shade];
    }

    public static int set(int color) {
        return SET[clamp(color)];
    }

    public static int shade(int color) {
        return SHADE[clamp(color)];
    }

    /** The color as 0xRRGGBB. */
    public static int rgb(int color) {
        return RGB[clamp(color)];
    }

    /** The color's hex code as the item names show it ("ECBF99"). */
    public static String hex(int color) {
        return SETS[set(color)][shade(color)].toUpperCase(Locale.ROOT);
    }

    /** Anything that is not a color counts as the first one. */
    public static int clamp(int color) {
        return color < 0 || color >= COUNT ? 0 : color;
    }
}
