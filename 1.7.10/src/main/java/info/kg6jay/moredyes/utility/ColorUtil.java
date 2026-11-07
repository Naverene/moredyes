package info.kg6jay.moredyes.utility;

public final class ColorUtil {

    public static final int WHITE = 0xFFFFFF;

    private ColorUtil() {}

    /** Parses a hex color such as "ecbf99" into 0xRRGGBB. */
    public static int fromHex(String hex) {
        return Integer.parseInt(hex, 16) & 0xFFFFFF;
    }

    /** The color of the shade stored in the given metadata, falling back to the first shade. */
    public static int shade(String[] colors, int meta) {
        if (meta < 0 || meta >= colors.length) {
            meta = 0;
        }
        return fromHex(colors[meta]);
    }

    /** How much a lit lamp's color may be brightened (see glow). */
    private static final int GLOW_MAX_PERCENT = 160;

    /**
     * The color a lit lamp is drawn in: the dye color brightened, keeping its hue, until its brightest channel is full
     * or it is 60% brighter. Dark colors stay darker than light ones, so lamps of different shades still differ.
     */
    public static int glow(int rgb) {
        int r = rgb >> 16 & 0xFF, g = rgb >> 8 & 0xFF, b = rgb & 0xFF;
        int max = Math.max(1, Math.max(r, Math.max(g, b)));
        int percent = Math.min(GLOW_MAX_PERCENT, 255 * 100 / max);
        return r * percent / 100 << 16 | g * percent / 100 << 8 | b * percent / 100;
    }
}
