package net.neverandy.moredyes.paint.client;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.reference.Reference;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Grey copies of the vanilla block textures, so paint shows its own color: a red stone block is red, not the dark red
 * that multiplying stone's grey-brown by red gives. They are added to the block atlas by a sprite source
 * (assets/minecraft/atlases/blocks.json, type "moredyes:paint_grey") as "moredyes:paint/minecraft/block/stone" and
 * so on. Textures of other mods have no copy and are painted by multiplying; More Dyes' own are grey already.
 */
public final class GreySprites
{
    private static final String PREFIX = "paint/";
    private static final Map<ResourceLocation, Optional<TextureAtlasSprite>> CACHE = new ConcurrentHashMap<>();
    private static SpriteSourceType type;

    private GreySprites() {}

    /** Adds the sprite source type. Called while the mod is constructed, before any textures load. */
    public static void registerSourceType()
    {
        try
        {
            Method register = ObfuscationReflectionHelper.findMethod(SpriteSources.class, "m_260887_", String.class, Codec.class);
            type = (SpriteSourceType) register.invoke(null, Reference.MOD_ID + ":paint_grey", MapCodec.unit(Source.INSTANCE).codec());
        }
        catch (ReflectiveOperationException | RuntimeException e)
        {
            MoreDyes.LOGGER.error("Couldn't add grey block textures for paint; painted blocks will be tinted instead", e);
        }
    }

    /** Forgets the sprites, after the textures reload. */
    public static void clear()
    {
        CACHE.clear();
    }

    /** The grey copy of a block texture, or null if it has none. */
    @Nullable
    public static TextureAtlasSprite find(TextureAtlasSprite sprite)
    {
        ResourceLocation name = sprite.contents().name();
        return CACHE.computeIfAbsent(name, key ->
        {
            ResourceLocation id = new ResourceLocation(Reference.MOD_ID, PREFIX + key.getNamespace() + "/" + key.getPath());
            TextureAtlasSprite grey = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(id);
            return grey.contents().name().equals(MissingTextureAtlasSprite.getLocation()) ? Optional.empty() : Optional.of(grey);
        }).orElse(null);
    }

    /** Lists textures/block in the minecraft namespace and adds a grey copy of each. */
    private static final class Source implements SpriteSource
    {
        static final Source INSTANCE = new Source();
        private static final FileToIdConverter BLOCK_TEXTURES = new FileToIdConverter("textures/block", ".png");

        @Override
        public void run(ResourceManager resources, Output output)
        {
            BLOCK_TEXTURES.listMatchingResources(resources).forEach((file, resource) ->
            {
                if (!file.getNamespace().equals("minecraft"))
                {
                    return;
                }
                ResourceLocation name = BLOCK_TEXTURES.fileToId(file);
                ResourceLocation id = new ResourceLocation(Reference.MOD_ID, PREFIX + name.getNamespace() + "/block/" + name.getPath());
                output.add(id, () ->
                {
                    SpriteContents contents = SpriteLoader.loadSprite(id, resource);
                    if (contents != null)
                    {
                        greyscale(contents.getOriginalImage());
                    }
                    return contents;
                });
            });
        }

        @Override
        public SpriteSourceType type()
        {
            return type;
        }
    }

    /**
     * Turns an image grey, then brightens it so its average is light (about 80%) without clipping its brightest
     * pixel. That keeps the texture's pattern while letting a paint color come through bright.
     */
    static void greyscale(NativeImage image)
    {
        int width = image.getWidth();
        int height = image.getHeight();
        double sum = 0;
        int count = 0;
        int max = 1;
        int[] luma = new int[width * height];
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int abgr = image.getPixelRGBA(x, y);
                int r = abgr & 0xFF;
                int g = (abgr >> 8) & 0xFF;
                int b = (abgr >> 16) & 0xFF;
                int l = (r * 299 + g * 587 + b * 114) / 1000;
                luma[y * width + x] = l;
                if ((abgr >>> 24) > 0)
                {
                    sum += l;
                    count++;
                    max = Math.max(max, l);
                }
            }
        }
        double mean = count == 0 ? 255 : Math.max(1, sum / count);
        double scale = Math.min(204.0 / mean, 255.0 / max);
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int abgr = image.getPixelRGBA(x, y);
                int l = Math.min(255, (int) Math.round(luma[y * width + x] * scale));
                image.setPixelRGBA(x, y, (abgr & 0xFF000000) | (l << 16) | (l << 8) | l);
            }
        }
    }
}
