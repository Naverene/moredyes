package info.kg6jay.moredyes.utility;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * The pixel operations that turn a source texture into the grey texture a dyed block is tinted from. The game's own
 * textures (client/TintSources) and the web map's (compat/dynmap) both use these, so they look alike. Not client-only,
 * because the web map draws on the server.
 */
public final class TintPixels {

    /** The darkest greys of a piston's cobblestone (the cracks and edges), which stay grey on a dyed piston. */
    private static final int PISTON_DARK = 0x60;
    /** How bright the grey panels of a dyed Iron Chests chest are at least on average, so the dye still shows. */
    private static final int PANEL_BRIGHTNESS = 200;

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
    /**
     * True for the red sheet of a vanilla bed (block and item textures), as opposed to its white pillow and wooden
     * frame: red with little green or blue.
     */
    public static boolean isBedding(int argb) {
        int r = argb >> 16 & 255, g = argb >> 8 & 255, b = argb & 255;
        return (argb >>> 24) != 0 && r >= 0x40 && g < r * 0.53F && b < r * 0.53F;
    }

    /**
     * The red sheet of a bed as grey, from its red channel made about as bright as white wool, so the dye shows as
     * strongly as on dyed wool.
     */
    public static int beddingGrey(int argb) {
        int lum = Math.min(255, Math.round((argb >> 16 & 255) * 1.45F));
        return argb & 0xFF000000 | lum << 16 | lum << 8 | lum;
    }

    /**
     * True where the vanilla bookshelf shows a book rather than wood: the frame, the shelves and the dark wall behind
     * the books are all dull browns.
     */
    public static boolean isBook(int argb) {
        if ((argb >>> 24) == 0) {
            return false;
        }
        float[] hsb = Color.RGBtoHSB(argb >> 16 & 255, argb >> 8 & 255, argb & 255, null);
        float hue = hsb[0] * 360.0F;
        return !(hue >= 25.0F && hue <= 50.0F && hsb[1] <= 0.7F);
    }

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

    /**
     * An Iron Chests chest texture (the Minecraft 1.7.10 ones), split into its panels and the rest. These chests are
     * metal all over, with no wood: each face is a flat panel inside a one pixel frame of a single darker color, with
     * the latch in the top left corner. The frame color is read from each texture (the most common color along the
     * edges of the faces), so resource packs that keep the layout work too.
     */
    public static final class MetalChest {

        private final int width, height;
        private final boolean[] panel;
        private final double mean, squeeze;

        /** Reads the texture, given as its pixels row by row. */
        public MetalChest(int[] pixels, int width, int height) {
            this.width = width;
            this.height = height;
            int latchWidth = width * 6 / 64, latchHeight = height * 5 / 64;
            Map<Integer, Integer> edgeColors = new HashMap<>();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int argb = pixels[y * width + x];
                    if ((argb >>> 24) != 0 && !(x < latchWidth && y < latchHeight)
                        && (this.isClear(pixels, x - 1, y) || this.isClear(pixels, x + 1, y)
                            || this.isClear(pixels, x, y - 1)
                            || this.isClear(pixels, x, y + 1))) {
                        edgeColors.merge(argb, 1, Integer::sum);
                    }
                }
            }
            int frame = edgeColors.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0);

            this.panel = new boolean[width * height];
            double sum = 0;
            int count = 0, top = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int argb = pixels[y * width + x];
                    if ((argb >>> 24) != 0 && !(x < latchWidth && y < latchHeight) && !nearColor(argb, frame)) {
                        this.panel[y * width + x] = true;
                        int lum = grey(argb) & 255;
                        sum += lum;
                        count++;
                        top = Math.max(top, lum);
                    }
                }
            }
            this.mean = count == 0 ? PANEL_BRIGHTNESS : sum / count;
            // Lifts the grey so its average is PANEL_BRIGHTNESS, compressing highlights rather than clipping them.
            this.squeeze = Math.min(1.0, (250 - PANEL_BRIGHTNESS) / Math.max(1.0, top - this.mean));
        }

        private boolean isPanel(int x, int y) {
            return x >= 0 && y >= 0 && x < this.width && y < this.height && this.panel[y * this.width + x];
        }

        /** The pixel at x, y as grey (lifted to PANEL_BRIGHTNESS on average) on a panel, or clear elsewhere. */
        public int panel(int argb, int x, int y) {
            if (!this.isPanel(x, y)) {
                return 0;
            }
            int lum = grey(argb) & 255;
            if (this.mean < PANEL_BRIGHTNESS) {
                double d = lum - this.mean;
                lum = (int) Math.max(0, Math.min(255, Math.round(PANEL_BRIGHTNESS + (d > 0 ? d * this.squeeze : d))));
            }
            return argb & 0xFF000000 | lum << 16 | lum << 8 | lum;
        }

        /** The pixel at x, y in its own color on the frame and latch, or clear on a panel. */
        public int trim(int argb, int x, int y) {
            return this.isPanel(x, y) ? 0 : argb;
        }

        private boolean isClear(int[] pixels, int x, int y) {
            return x < 0 || y < 0 || x >= this.width || y >= this.height || (pixels[y * this.width + x] >>> 24) == 0;
        }

        /** True if two colors differ by at most a few steps per channel, so a slightly noisy frame still counts. */
        private static boolean nearColor(int a, int b) {
            return Math.abs((a >> 16 & 255) - (b >> 16 & 255)) <= 4 && Math.abs((a >> 8 & 255) - (b >> 8 & 255)) <= 4
                && Math.abs((a & 255) - (b & 255)) <= 4;
        }
    }
}
