package info.kg6jay.moredyes.compat.ae2;

/**
 * The vanilla dye color a More Dyes color looks closest to, by CIEDE2000 distance in CIELAB, for mods that only know
 * the 16 vanilla colors. The newer versions put the same choice in tags (tools/make_dye_tags.py in 1.16.5 to 1.20.1).
 */
public class NearestDye {

    /** The vanilla dye colors, in wool metadata order (white first, black last). */
    private static final int[] VANILLA = { 0xF9FFFE, 0xF9801D, 0xC74EBD, 0x3AB3DA, 0xFED83D, 0x80C71F, 0xF38BAA,
        0x474F52, 0x9D9D97, 0x169C9C, 0x8932B8, 0x3C44AA, 0x835432, 0x5E7C16, 0xB02E26, 0x1D1D21 };

    /** The wool metadata of the vanilla color nearest a hex color such as "c56685". */
    public static int of(String hex) {
        double[] lab = lab(Integer.parseInt(hex, 16));
        int best = 0;
        double bestDistance = Double.MAX_VALUE;
        for (int i = 0; i < VANILLA.length; i++) {
            double distance = ciede2000(lab, lab(VANILLA[i]));
            if (distance < bestDistance) {
                best = i;
                bestDistance = distance;
            }
        }
        return best;
    }

    private static double linear(double u) {
        return u > 0.04045 ? Math.pow((u + 0.055) / 1.055, 2.4) : u / 12.92;
    }

    private static double f(double t) {
        return t > 0.008856 ? Math.cbrt(t) : 7.787 * t + 16.0 / 116;
    }

    private static double[] lab(int rgb) {
        double r = linear(((rgb >> 16) & 255) / 255.0), g = linear(((rgb >> 8) & 255) / 255.0),
            b = linear((rgb & 255) / 255.0);
        double x = (0.4124 * r + 0.3576 * g + 0.1805 * b) / 0.95047;
        double y = 0.2126 * r + 0.7152 * g + 0.0722 * b;
        double z = (0.0193 * r + 0.1192 * g + 0.9505 * b) / 1.08883;
        return new double[] { 116 * f(y) - 16, 500 * (f(x) - f(y)), 200 * (f(y) - f(z)) };
    }

    private static double ciede2000(double[] c1, double[] c2) {
        double l1 = c1[0], a1 = c1[1], b1 = c1[2], l2 = c2[0], a2 = c2[1], b2 = c2[2];
        double cbar = (Math.hypot(a1, b1) + Math.hypot(a2, b2)) / 2;
        double g = 0.5 * (1 - Math.sqrt(Math.pow(cbar, 7) / (Math.pow(cbar, 7) + Math.pow(25, 7))));
        double a1p = a1 * (1 + g), a2p = a2 * (1 + g);
        double c1p = Math.hypot(a1p, b1), c2p = Math.hypot(a2p, b2);
        double h1 = mod360(Math.toDegrees(Math.atan2(b1, a1p))), h2 = mod360(Math.toDegrees(Math.atan2(b2, a2p)));
        double dl = l2 - l1, dc = c2p - c1p, dh;
        if (c1p * c2p == 0) dh = 0;
        else if (Math.abs(h2 - h1) <= 180) dh = h2 - h1;
        else dh = h2 > h1 ? h2 - h1 - 360 : h2 - h1 + 360;
        double dhh = 2 * Math.sqrt(c1p * c2p) * Math.sin(Math.toRadians(dh / 2));
        double lbar = (l1 + l2) / 2, cbarp = (c1p + c2p) / 2, hbar;
        if (c1p * c2p == 0) hbar = h1 + h2;
        else if (Math.abs(h1 - h2) <= 180) hbar = (h1 + h2) / 2;
        else hbar = h1 + h2 < 360 ? (h1 + h2 + 360) / 2 : (h1 + h2 - 360) / 2;
        double t = 1 - 0.17 * Math.cos(Math.toRadians(hbar - 30))
            + 0.24 * Math.cos(Math.toRadians(2 * hbar))
            + 0.32 * Math.cos(Math.toRadians(3 * hbar + 6))
            - 0.2 * Math.cos(Math.toRadians(4 * hbar - 63));
        double sl = 1 + 0.015 * (lbar - 50) * (lbar - 50) / Math.sqrt(20 + (lbar - 50) * (lbar - 50));
        double sc = 1 + 0.045 * cbarp;
        double sh = 1 + 0.015 * cbarp * t;
        double rt = -2 * Math.sqrt(Math.pow(cbarp, 7) / (Math.pow(cbarp, 7) + Math.pow(25, 7)))
            * Math.sin(Math.toRadians(60 * Math.exp(-Math.pow((hbar - 275) / 25, 2))));
        return Math
            .sqrt(Math.pow(dl / sl, 2) + Math.pow(dc / sc, 2) + Math.pow(dhh / sh, 2) + rt * (dc / sc) * (dhh / sh));
    }

    private static double mod360(double degrees) {
        double d = degrees % 360;
        return d < 0 ? d + 360 : d;
    }
}
