package net.neverandy.moredyes.compat.ironshulkerbox.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.compat.ironshulkerbox.IronShulkerBoxCompat;
import net.neverandy.moredyes.reference.Reference;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Splits each white Iron Shulker Boxes texture into two textures: the shell, made grey so the renderer can multiply it
 * by the dye color, and the metal trim, which keeps its own colors. The shell is the part that differs between the
 * white and the red box of a tier; the trim is the same on both. They are made from whatever textures are loaded, so
 * they follow resource packs, and none of Iron Shulker Boxes' art is copied into More Dyes.
 */
public final class DyedIronShulkerBoxTextures implements ResourceManagerReloadListener
{
    public static final DyedIronShulkerBoxTextures INSTANCE = new DyedIronShulkerBoxTextures();

    private DyedIronShulkerBoxTextures() {}

    public static ResourceLocation shell(IronShulkerBoxCompat.Tier tier)
    {
        return new ResourceLocation(Reference.MOD_ID, "dynamic/" + tier.id() + "_shulker_box_shell");
    }

    public static ResourceLocation trim(IronShulkerBoxCompat.Tier tier)
    {
        return new ResourceLocation(Reference.MOD_ID, "dynamic/" + tier.id() + "_shulker_box_trim");
    }

    @Override
    public void onResourceManagerReload(ResourceManager resources)
    {
        for (IronShulkerBoxCompat.Tier tier : IronShulkerBoxCompat.Tier.values())
        {
            NativeImage white = read(resources, tier, "white");
            NativeImage red = read(resources, tier, "red");
            if (white == null)
            {
                if (red != null)
                {
                    red.close();
                }
                continue;
            }
            boolean compare = red != null && red.getWidth() == white.getWidth() && red.getHeight() == white.getHeight();
            NativeImage shell = new NativeImage(white.getWidth(), white.getHeight(), true);
            NativeImage trim = new NativeImage(white.getWidth(), white.getHeight(), true);
            for (int y = 0; y < white.getHeight(); y++)
            {
                for (int x = 0; x < white.getWidth(); x++)
                {
                    int pixel = white.getPixelRGBA(x, y);
                    // Without a red texture to compare with (a resource pack that changes the sizes), all of it is shell.
                    if (!compare || red.getPixelRGBA(x, y) != pixel)
                    {
                        int lum = luminance(pixel);
                        shell.setPixelRGBA(x, y, (pixel & 0xFF000000) | lum << 16 | lum << 8 | lum);
                        trim.setPixelRGBA(x, y, 0);
                    }
                    else
                    {
                        shell.setPixelRGBA(x, y, 0);
                        trim.setPixelRGBA(x, y, pixel);
                    }
                }
            }
            white.close();
            if (red != null)
            {
                red.close();
            }
            Minecraft.getInstance().getTextureManager().register(shell(tier), new DynamicTexture(shell));
            Minecraft.getInstance().getTextureManager().register(trim(tier), new DynamicTexture(trim));
        }
    }

    private static NativeImage read(ResourceManager resources, IronShulkerBoxCompat.Tier tier, String color)
    {
        ResourceLocation location = new ResourceLocation(IronShulkerBoxCompat.MOD_ID,
                "textures/model/" + color + "/shulker_" + color + "_" + tier.id() + ".png");
        Optional<Resource> resource = resources.getResource(location);
        if (resource.isEmpty())
        {
            MoreDyes.LOGGER.warn("Missing {}, so dyed {} shulker boxes can't be drawn", location, tier.id());
            return null;
        }
        try (InputStream stream = resource.get().open())
        {
            return NativeImage.read(stream);
        }
        catch (IOException e)
        {
            MoreDyes.LOGGER.warn("Couldn't read {}", location, e);
            return null;
        }
    }

    /** NativeImage pixels are ABGR. */
    private static int luminance(int abgr)
    {
        return Math.min(255, Math.round(0.299F * (abgr & 255) + 0.587F * (abgr >> 8 & 255) + 0.114F * (abgr >> 16 & 255)));
    }
}
