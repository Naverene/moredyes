package info.kg6jay.moredyes.compat.dynmap;

import java.util.HashMap;

import org.dynmap.hdmap.TexturePack;
import org.dynmap.hdmap.textureprocessor.CustomTextureProcessor;

import info.kg6jay.moredyes.utility.TintPixels;

/**
 * Makes a texture for the web map the way client/TintSources makes the game's: the grey texture a dyed block is tinted
 * from, or the untinted parts drawn over it. Set on a texture line as processor=, with transform= naming what to do
 * (see apply) and an optional crop=x:y:width:height, in pixels of a 64 pixel wide texture, for model textures such as
 * the chest.
 */
public class TintedTextureProcessor extends CustomTextureProcessor {

    private String transform = "grey";
    private int[] crop;

    @Override
    public boolean init(HashMap<String, String> data) {
        String transform = data.get("transform");
        if (transform != null) {
            this.transform = transform;
        }
        String crop = data.get("crop");
        if (crop != null) {
            String[] parts = crop.split(":");
            this.crop = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                this.crop[i] = Integer.parseInt(parts[i]);
            }
        }
        return true;
    }

    @Override
    public int getTextureCount() {
        return 1;
    }

    @Override
    public void patchTextures(TexturePack texturePack, int[] tileToDyntile, int[] argb, int w, int h, int nativeScale) {
        // Animated textures are a vertical strip of frames; only the first frame is used.
        int x0 = 0, y0 = 0, width = w, height = Math.min(w, h);
        if (this.crop != null && this.crop.length == 4) {
            float scale = w / 64.0F;
            x0 = Math.round(this.crop[0] * scale);
            y0 = Math.round(this.crop[1] * scale);
            width = Math.max(1, Math.round(this.crop[2] * scale));
            height = Math.max(1, Math.round(this.crop[3] * scale));
        }

        int[] region = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int sx = Math.min(x0 + x, w - 1), sy = Math.min(y0 + y, h - 1);
                region[y * width + x] = this.apply(argb[sy * w + sx], sx, sy, w);
            }
        }

        int[] tile = new int[nativeScale * nativeScale];
        if (width == height) {
            TexturePack.scaleTerrainPNGSubImage(width, nativeScale, region, tile);
        } else {
            for (int y = 0; y < nativeScale; y++) {
                for (int x = 0; x < nativeScale; x++) {
                    tile[y * nativeScale + x] = region[y * height / nativeScale * width + x * width / nativeScale];
                }
            }
        }
        texturePack.setTileARGB(tileToDyntile[0], tile);
    }

    /** One pixel of the result, from the pixel at x, y of a source texture this many pixels wide. */
    private int apply(int argb, int x, int y, int size) {
        switch (this.transform) {
            case "greyIfGreen":
                return TintPixels.isGreen(argb) ? TintPixels.grey(argb) : 0;
            case "greyIfNotGreen":
                return TintPixels.isGreen(argb) ? 0 : TintPixels.grey(argb);
            case "keepIfGreen":
                return TintPixels.isGreen(argb) ? argb : 0;
            case "keepIfNotGreen":
                return TintPixels.isGreen(argb) ? 0 : argb;
            case "greyBrighter4":
                return TintPixels.greyBrighter(argb, 4.0F);
            case "pistonSideTrim":
                return TintPixels.isPistonTrim(argb, x, y, size, true, false) ? argb : 0;
            case "pistonInnerTrim":
                return TintPixels.isPistonTrim(argb, x, y, size, false, true) ? argb : 0;
            case "pistonBottomTrim":
                return TintPixels.isPistonTrim(argb, x, y, size, false, false) ? argb : 0;
            default:
                return TintPixels.grey(argb);
        }
    }
}
