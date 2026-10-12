package info.kg6jay.moredyes.utility;

import java.awt.Color;

/**
 * The pixel operations that turn a source texture into the grey texture a dyed block is tinted from. The game's own
 * textures (client/TintSources) and the web map's (compat/dynmap) both use these, so they look alike. Not client-only,
 * because the web map draws on the server.
 */
public final class TintPixels {

    /** The darkest greys of a piston's cobblestone (the cracks and edges), which stay grey on a dyed piston. */
    private static final int PISTON_DARK = 0x60;

    private TintPixels() {}

    /** The pixel in grey, using its brightness, with its transparency kept. */
    public static int grey(int argb) {
        int r = argb >> 16 & 255, g = argb >> 8 & 255, b = argb & 255;
        int lum = Math.min(255, Math.round(0.299F * r + 0.587F * g + 0.114F * b));
        return argb & 0xFF000000 | lum << 16 | lum << 8 | lum;
    }

    /** The pixel in grey and this many times brighter. */
    public static int greyBrighter(int argb, float factor) {
        int lum = Math.min(255, Math.round((grey(argb) & 255) * factor));
        return argb & 0xFF000000 | lum << 16 | lum << 8 | lum;
    }

    /** True for a visible green pixel, such as the leaves and stems of a plant. */
    public static boolean isGreen(int argb) {
        if ((argb >>> 24) == 0) {
            return false;
        }
        float[] hsb = Color.RGBtoHSB(argb >> 16 & 255, argb >> 8 & 255, argb & 255, null);
        float hue = hsb[0] * 360.0F;
        return hsb[1] > 0.15F && hue >= 65.0F && hue <= 170.0F;
    }

    /**
     * True where a piston texture keeps its own colors on a dyed piston: its wood, its iron and the darkest greys of
     * its cobblestone. The side has a wooden strip along the top; the inside has the iron ring and hole the arm comes
     * out of. The pixel is at x, y in a texture this many pixels wide.
     */
    public static boolean isPistonTrim(int argb, int x, int y, int size, boolean side, boolean inside) {
        int px = x * 16 / size, py = y * 16 / size;
        boolean trim = inside ? px >= 5 && px <= 10 && py >= 5 && py <= 10 : side && py < 4;
        return trim || (grey(argb) & 255) < PISTON_DARK;
    }
}
