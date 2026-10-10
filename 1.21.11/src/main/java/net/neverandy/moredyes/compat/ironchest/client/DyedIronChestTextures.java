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
 * Splits each Iron Chests chest texture into two textures: the wood, made grey so the renderer can multiply it by the
 * dye color, and everything else (the metal trim, the latch and the inside), which keeps its own color. They are made
 * from whatever textures are loaded, so they follow resource packs, and none of Iron Chests' art is copied into More
 * Dyes.
 *
 * <p>
 * Which pixels are wood is read from the iron chest, whose trim is grey and wood is brown. The other tiers share its
 * layout, but some of their trims are brown or orange themselves, so their own colors can't tell.
 */
public final class DyedIronChestTextures implements ResourceManagerReloadListener {

    public static final DyedIronChestTextures INSTANCE = new DyedIronChestTextures();

    /** How bright the grey wood is on average, as in tools/textures.py. */
    private static final int BRIGHTNESS = 200;

    private DyedIronChestTextures() {}

    public static Identifier wood(IronChestCompat.Tier tier) {
        return Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dynamic/" + tier.id() + "_chest_wood");
    }

    public static Identifier trim(IronChestCompat.Tier tier) {
        return Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dynamic/" + tier.id() + "_chest_trim");
    }

    @Override
    public void onResourceManagerReload(ResourceManager resources) {
        NativeImage iron = read(resources, IronChestCompat.Tier.IRON);
        boolean[] mask = iron == null ? null : woodMask(iron);
        for (IronChestCompat.Tier tier : IronChestCompat.Tier.values()) {
            NativeImage source = tier == IronChestCompat.Tier.IRON ? iron : read(resources, tier);
            if (source == null) {
                continue;
            }
            boolean[] wood = mask != null && iron.getWidth() == source.getWidth() && iron.getHeight() == source.getHeight()
                ? mask : woodMask(source);
            NativeImage woodImage = new NativeImage(source.getWidth(), source.getHeight(), true);
            NativeImage trimImage = new NativeImage(source.getWidth(), source.getHeight(), true);
            split(source, wood, woodImage, trimImage);
            if (source != iron) {
                source.close();
            }
            Identifier woodId = wood(tier);
            Identifier trimId = trim(tier);
            Minecraft.getInstance().getTextureManager().register(woodId, new DynamicTexture(woodId::toString, woodImage));
            Minecraft.getInstance().getTextureManager().register(trimId, new DynamicTexture(trimId::toString, trimImage));
        }
        if (iron != null) {
            iron.close();
        }
    }

    private static @Nullable NativeImage read(ResourceManager resources, IronChestCompat.Tier tier) {
        Identifier location = Identifier.fromNamespaceAndPath(IronChestCompat.MOD_ID,
            "textures/model/" + tier.id() + "_chest.png");
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

    /** Which pixels are wood: brown ones, outside the latch in the top-left corner. */
    private static boolean[] woodMask(NativeImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int latchWidth = width * 6 / 64;
        int latchHeight = height * 5 / 64;
        boolean[] mask = new boolean[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                mask[y * width + x] = !(x < latchWidth && y < latchHeight) && isBrown(image.getPixel(x, y));
            }
        }
        return mask;
    }

    /** NativeImage.getPixel is ARGB. */
    private static boolean isBrown(int argb) {
        if ((argb >>> 24) == 0) {
            return false;
        }
        float r = (argb >> 16 & 255) / 255.0F;
        float g = (argb >> 8 & 255) / 255.0F;
        float b = (argb & 255) / 255.0F;
        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        if (max == 0.0F || (max - min) / max <= 0.25F || max != r) {
            return false;
        }
        float hue = ((g - b) / (max - min) + 6.0F) % 6.0F * 60.0F;
        return hue >= 15.0F && hue <= 50.0F;
    }

    /** Copies the wood into one image as brightened grey and everything else into the other. */
    private static void split(NativeImage source, boolean[] wood, NativeImage woodImage, NativeImage trimImage) {
        int width = source.getWidth();
        int height = source.getHeight();
        double sum = 0;
        int count = 0;
        int top = 0;
        for (int i = 0; i < wood.length; i++) {
            if (wood[i]) {
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
                if (wood[y * width + x]) {
                    double d = luminance(pixel) - mean;
                    int lum = mean >= BRIGHTNESS ? luminance(pixel)
                        : (int) Math.max(0, Math.min(255, Math.round(BRIGHTNESS + (d > 0 ? d * squeeze : d))));
                    woodImage.setPixel(x, y, (pixel & 0xFF000000) | lum << 16 | lum << 8 | lum);
                    trimImage.setPixel(x, y, 0);
                } else {
                    woodImage.setPixel(x, y, 0);
                    trimImage.setPixel(x, y, pixel);
                }
            }
        }
    }

    private static int luminance(int argb) {
        return Math.min(255, Math.round(0.299F * (argb >> 16 & 255) + 0.587F * (argb >> 8 & 255) + 0.114F * (argb & 255)));
    }
}
