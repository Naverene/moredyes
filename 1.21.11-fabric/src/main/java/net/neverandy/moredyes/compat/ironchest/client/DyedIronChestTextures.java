package net.neverandy.moredyes.compat.ironchest.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import com.mojang.blaze3d.platform.NativeImage;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;

/**
 * Splits each Iron Chests chest texture into two textures: the metal panels, made grey so the renderer can multiply them
 * by the dye color, and everything else (the frame around each face, the latch and the inside), which keeps the tier's
 * own colors. They are made from whatever textures are loaded, so they follow resource packs, and none of Iron Chests'
 * art is copied into More Dyes.
 *
 * <p>
 * The Fabric port of Iron Chests draws its chests with the vanilla chest model and all-metal textures (no wood, unlike
 * the NeoForge mod). Which pixels are panels is read from the iron chest, whose panels are light grey and whose frame
 * is a darker grey. The other tiers share its layout, but their own colors can't tell panel from frame as reliably.
 */
public final class DyedIronChestTextures implements ResourceManagerReloadListener {

    public static final DyedIronChestTextures INSTANCE = new DyedIronChestTextures();

    /** How bright the grey panels are on average, as in the NeoForge build's tools/textures.py. */
    private static final int BRIGHTNESS = 200;
    /** The iron chest's panels are at least this bright (luminance out of 255); its frame and inside are darker. */
    private static final int PANEL_LUMINANCE = 190;

    private DyedIronChestTextures() {}

    public static Identifier panels(IronChestCompat.Tier tier) {
        return Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dynamic/" + tier.id() + "_chest_panels");
    }

    public static Identifier trim(IronChestCompat.Tier tier) {
        return Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dynamic/" + tier.id() + "_chest_trim");
    }

    @Override
    public void onResourceManagerReload(ResourceManager resources) {
        NativeImage iron = read(resources, IronChestCompat.Tier.IRON);
        boolean[] mask = iron == null ? null : panelMask(iron);
        for (IronChestCompat.Tier tier : IronChestCompat.Tier.values()) {
            NativeImage source = tier == IronChestCompat.Tier.IRON ? iron : read(resources, tier);
            if (source == null) {
                continue;
            }
            boolean[] panels = mask != null && iron.getWidth() == source.getWidth() && iron.getHeight() == source.getHeight()
                ? mask : panelMask(source);
            NativeImage panelImage = new NativeImage(source.getWidth(), source.getHeight(), true);
            NativeImage trimImage = new NativeImage(source.getWidth(), source.getHeight(), true);
            split(source, panels, panelImage, trimImage);
            if (source != iron) {
                source.close();
            }
            Identifier panelsId = panels(tier);
            Identifier trimId = trim(tier);
            Minecraft.getInstance().getTextureManager().register(panelsId, new DynamicTexture(panelsId::toString, panelImage));
            Minecraft.getInstance().getTextureManager().register(trimId, new DynamicTexture(trimId::toString, trimImage));
        }
        if (iron != null) {
            iron.close();
        }
    }

    /** The texture Iron Chests draws a tier's chest with. */
    private static @Nullable NativeImage read(ResourceManager resources, IronChestCompat.Tier tier) {
        Identifier texture = tier.type.texture;
        Identifier location = Identifier.fromNamespaceAndPath(texture.getNamespace(),
            "textures/" + texture.getPath() + ".png");
        Optional<Resource> resource = resources.getResource(location);
        if (resource.isEmpty()) {
            MoreDyes.LOGGER.warn("Missing {}, so dyed {} chests can't be drawn", location, tier.id());
            return null;
        }
        try (InputStream stream = resource.get().open()) {
            return NativeImage.read(stream);
        } catch (IOException e) {
            MoreDyes.LOGGER.warn("Couldn't read {}", location, e);
            return null;
        }
    }

    /** Which pixels are panels: light ones, outside the latch in the top-left corner. */
    private static boolean[] panelMask(NativeImage image) {
        int width = image.getWidth(), height = image.getHeight();
        int latchWidth = width * 6 / 64, latchHeight = height * 5 / 64;
        boolean[] mask = new boolean[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getPixel(x, y);
                mask[y * width + x] = !(x < latchWidth && y < latchHeight) && (pixel >>> 24) != 0
                    && luminance(pixel) >= PANEL_LUMINANCE;
            }
        }
        return mask;
    }

    /** Copies the panels into one image as brightened grey and everything else into the other. */
    private static void split(NativeImage source, boolean[] panels, NativeImage panelImage, NativeImage trimImage) {
        int width = source.getWidth(), height = source.getHeight();
        double sum = 0;
        int count = 0, top = 0;
        for (int i = 0; i < panels.length; i++) {
            if (panels[i]) {
                int lum = luminance(source.getPixel(i % width, i / width));
                sum += lum;
                count++;
                top = Math.max(top, lum);
            }
        }
        double mean = count == 0 ? BRIGHTNESS : sum / count;
        // Lifts the grey so its average is BRIGHTNESS, compressing highlights rather than clipping them.
        double squeeze = Math.min(1.0, (250 - BRIGHTNESS) / Math.max(1.0, top - mean));
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = source.getPixel(x, y);
                if (panels[y * width + x]) {
                    double d = luminance(pixel) - mean;
                    int lum = mean >= BRIGHTNESS ? luminance(pixel)
                        : (int) Math.max(0, Math.min(255, Math.round(BRIGHTNESS + (d > 0 ? d * squeeze : d))));
                    panelImage.setPixel(x, y, (pixel & 0xFF000000) | lum << 16 | lum << 8 | lum);
                    trimImage.setPixel(x, y, 0);
                } else {
                    panelImage.setPixel(x, y, 0);
                    trimImage.setPixel(x, y, pixel);
                }
            }
        }
    }

    /** NativeImage.getPixel is ARGB. */
    private static int luminance(int argb) {
        return Math.min(255, Math.round(0.299F * (argb >> 16 & 255) + 0.587F * (argb >> 8 & 255) + 0.114F * (argb & 255)));
    }
}
