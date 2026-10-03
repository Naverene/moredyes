package info.kg6jay.moredyes.client;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.reference.Reference;

/**
 * Where each tinted texture comes from. To give a new block a texture, add a line here and call
 * TintedTextures.register(iconRegister, key) from the block's registerBlockIcons.
 * <p>
 * The generated texture keeps the source's brightness (so dark materials like obsidian stay dark) and drops its
 * color; the block's dye color is then multiplied on top when it is drawn.
 */
@SideOnly(Side.CLIENT)
public final class TintSources {

    /** Changes one ARGB pixel of the source texture. */
    public interface PixelTransform {

        int apply(int argb);

        /** The same with the pixel's position in a texture this many pixels wide, for transforms that need it. */
        default int apply(int argb, int x, int y, int size) {
            return this.apply(argb);
        }

        /**
         * The transform to use for this source image, for transforms that need to look at the whole image first.
         * Returns itself by default.
         */
        default PixelTransform forImage(BufferedImage image) {
            return this;
        }
    }

    public static final class Source {

        public final ResourceLocation location;
        public final PixelTransform transform;
        /** Model textures are not square and are used whole. */
        public final boolean model;

        Source(ResourceLocation location, PixelTransform transform, boolean model) {
            this.location = location;
            this.transform = transform;
            this.model = model;
        }
    }

    /** Converts to grey using the pixel's brightness, keeping its transparency. */
    public static final PixelTransform GREY = TintSources::grey;
    /** Grey where the pixel is green, transparent elsewhere (the leaves of a sapling). */
    public static final PixelTransform GREY_IF_GREEN = argb -> isGreen(argb) ? grey(argb) : 0;
    /** Grey where the pixel is not green, transparent elsewhere (the petals of a flower). */
    public static final PixelTransform GREY_IF_NOT_GREEN = argb -> isGreen(argb) ? 0 : grey(argb);
    /** Unchanged where the pixel is green, transparent elsewhere (the stem of a flower). */
    public static final PixelTransform KEEP_IF_GREEN = argb -> isGreen(argb) ? argb : 0;
    /** Unchanged where the pixel is not green, transparent elsewhere (the trunk of a sapling). */
    public static final PixelTransform KEEP_IF_NOT_GREEN = argb -> isGreen(argb) ? 0 : argb;

    /** The darkest greys of a piston's cobblestone (the cracks and edges), which stay grey on a dyed piston. */
    private static final int PISTON_DARK = 0x60;

    /**
     * Unchanged where the pixel is not cobblestone or is one of its darkest greys, transparent elsewhere: the wood,
     * iron and cracks of a piston texture, drawn untinted over the dyed piston. The side has a wooden strip along the
     * top; the inside has the iron ring and hole the arm comes out of.
     */
    public static PixelTransform keepIfNotPistonCobblestone(boolean side, boolean inside) {
        return new PixelTransform() {

            @Override
            public int apply(int argb) {
                return argb;
            }

            @Override
            public int apply(int argb, int x, int y, int size) {
                int px = x * 16 / size, py = y * 16 / size;
                boolean trim = inside ? px >= 5 && px <= 10 && py >= 5 && py <= 10 : side && py < 4;
                boolean dark = (grey(argb) & 255) < PISTON_DARK;
                return trim || dark ? argb : 0;
            }
        };
    }

    /**
     * Grey and this many times brighter, for textures that are dark because of their color rather than their material
     * (vanilla nether brick is dark red), so the dye still shows.
     */
    public static PixelTransform greyBrighter(float factor) {
        return argb -> {
            int lum = Math.min(255, Math.round((grey(argb) & 255) * factor));
            return argb & 0xFF000000 | lum << 16 | lum << 8 | lum;
        };
    }

    /**
     * The Iron Chests tiers that have dyed versions (compat/ironchest), named as their model textures are. Plain names,
     * so nothing here loads Iron Chests' classes.
     */
    public static final String[] IRON_CHEST_TIERS = { "iron", "gold", "diamond", "copper", "silver" };

    /** The parts of Storage Drawers' oak drawer textures that the dyed drawers (compat/storagedrawers) use. */
    public static final String[] DRAWER_PARTS = { "front_1", "front_2", "front_4", "side", "side_h", "side_v", "trim" };

    /** How bright the grey panels of a dyed Iron Chests chest are at least on average, so the dye still shows. */
    private static final int PANEL_BRIGHTNESS = 200;

    /**
     * Splits an Iron Chests chest texture (the Minecraft 1.7.10 ones). These chests are metal all over, with no wood:
     * each face is a flat panel inside a one pixel frame of a single darker color, with the latch in the top left
     * corner. With panels true this keeps the panels as grey (lifted to PANEL_BRIGHTNESS on average) and makes the rest
     * transparent; with panels false it keeps the frame and latch in their own colors and makes the panels
     * transparent. The frame color is read from each texture (the most common color along the edges of the faces), so
     * resource packs that keep the layout work too.
     */
    public static PixelTransform metalChest(boolean panels) {
        return new PixelTransform() {

            @Override
            public int apply(int argb) {
                return argb;
            }

            @Override
            public PixelTransform forImage(BufferedImage image) {
                int width = image.getWidth(), height = image.getHeight();
                int latchWidth = width * 6 / 64, latchHeight = height * 5 / 64;
                Map<Integer, Integer> edgeColors = new HashMap<>();
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int argb = image.getRGB(x, y);
                        if ((argb >>> 24) != 0 && !(x < latchWidth && y < latchHeight)
                            && (isClear(image, x - 1, y) || isClear(image, x + 1, y)
                                || isClear(image, x, y - 1)
                                || isClear(image, x, y + 1))) {
                            edgeColors.merge(argb, 1, Integer::sum);
                        }
                    }
                }
                int frame = edgeColors.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(0);

                boolean[] panel = new boolean[width * height];
                double sum = 0;
                int count = 0, top = 0;
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int argb = image.getRGB(x, y);
                        if ((argb >>> 24) != 0 && !(x < latchWidth && y < latchHeight) && !nearColor(argb, frame)) {
                            panel[y * width + x] = true;
                            int lum = grey(argb) & 255;
                            sum += lum;
                            count++;
                            top = Math.max(top, lum);
                        }
                    }
                }
                double mean = count == 0 ? PANEL_BRIGHTNESS : sum / count;
                // Lifts the grey so its average is PANEL_BRIGHTNESS, compressing highlights rather than clipping them.
                double squeeze = Math.min(1.0, (250 - PANEL_BRIGHTNESS) / Math.max(1.0, top - mean));

                return new PixelTransform() {

                    @Override
                    public int apply(int argb) {
                        return argb;
                    }

                    @Override
                    public int apply(int argb, int x, int y, int size) {
                        boolean isPanel = x < width && y < height && panel[y * width + x];
                        if (!panels) {
                            return isPanel ? 0 : argb;
                        }
                        if (!isPanel) {
                            return 0;
                        }
                        int lum = grey(argb) & 255;
                        if (mean < PANEL_BRIGHTNESS) {
                            double d = lum - mean;
                            lum = (int) Math
                                .max(0, Math.min(255, Math.round(PANEL_BRIGHTNESS + (d > 0 ? d * squeeze : d))));
                        }
                        return argb & 0xFF000000 | lum << 16 | lum << 8 | lum;
                    }
                };
            }
        };
    }

    private static boolean isClear(BufferedImage image, int x, int y) {
        return x < 0 || y < 0 || x >= image.getWidth() || y >= image.getHeight() || (image.getRGB(x, y) >>> 24) == 0;
    }

    /** True if two colors differ by at most a few steps per channel, so a slightly noisy frame still counts. */
    private static boolean nearColor(int a, int b) {
        return Math.abs((a >> 16 & 255) - (b >> 16 & 255)) <= 4 && Math.abs((a >> 8 & 255) - (b >> 8 & 255)) <= 4
            && Math.abs((a & 255) - (b & 255)) <= 4;
    }

    private static final Map<String, Source> SOURCES = new HashMap<>();

    static {
        block("wool", "wool_colored_white");
        block("woolRock", "wool_colored_white");
        block("stone", "stone");
        block("cobble", "cobblestone");
        block("stonebrick", "stonebrick");
        block("stonebrickCarved", "stonebrick_carved");
        block("stonebrickCracked", "stonebrick_cracked");
        block("obsidian", "obsidian");
        block("soulsand", "soul_sand");
        block("quartz/top", "quartz_block_top");
        block("quartz/side", "quartz_block_side");
        block("quartz/bottom", "quartz_block_bottom");
        block("clay", "hardened_clay_stained_white");
        block("hardenedClay", "hardened_clay");
        block("coal", "coal_block");
        block("glowstone", "glowstone");
        block("lapis", "lapis_block");
        block("redstone", "redstone_block");
        block("plank", "planks_oak");
        block("brick", "brick");
        block("sand", "sand");
        block("sandstone/top", "sandstone_top");
        block("sandstone/side", "sandstone_normal");
        block("sandstone/bottom", "sandstone_bottom");
        block("bookshelf", "bookshelf");
        block("workbench/top", "crafting_table_top");
        block("workbench/side", "crafting_table_side");
        block("workbench/front", "crafting_table_front");
        block("log/top", "log_oak_top");
        block("leaf", "leaves_oak");
        block("glass/clear", "glass");
        block("glass/pane", "glass_pane_top");
        add("glass/foggy", modTexture("blocks/base/glass_foggy"), GREY, false);
        add("diorite", modTexture("blocks/base/diorite"), GREY, false);

        // Blocks from newer Minecraft versions. Where 1.7 has a texture that fits, that is used; the rest have a grey
        // texture of their own in textures/blocks/base.
        block("smoothStone", "stone_slab_top");
        block("smoothSandstone", "sandstone_top");
        block("smoothQuartz", "quartz_block_bottom");
        block("cutSandstone/top", "sandstone_top");
        block("cutSandstone/side", "sandstone_smooth");
        block("cutSandstone/bottom", "sandstone_top");
        block("mossyCobble", "cobblestone_mossy");
        block("mossyStonebrick", "stonebrick_mossy");
        add("netherBrick", vanillaBlock("nether_brick"), greyBrighter(4.0F), false);
        base("granite");
        base("andesite");
        base("polishedGranite");
        base("polishedDiorite");
        base("polishedAndesite");
        base("concrete");
        base("concretePowder");
        base("soulSoil");
        base("basalt/top");
        base("basalt/side");
        add("basalt/bottom", modTexture("blocks/base/basalt/top"), GREY, false);
        base("polishedBasalt/top");
        base("polishedBasalt/side");
        add("polishedBasalt/bottom", modTexture("blocks/base/polishedBasalt/top"), GREY, false);
        base("crackedNetherBrick");
        base("chiseledNetherBrick");
        base("endStoneBrick");
        base("boneBlock/top");
        base("boneBlock/side");
        add("boneBlock/bottom", modTexture("blocks/base/boneBlock/top"), GREY, false);
        base("glazedTerracotta");
        base("netheriteBlock");
        base("cryingObsidian");
        base("ironTrapdoor");
        base("chain");
        // Only the cobblestone of a dyed piston takes the dye: its wood, iron and cracks are drawn over it from the
        // overlays, and its face is the vanilla texture (see MDBlockDyedPiston).
        block("piston/side", "piston_side");
        block("piston/inner", "piston_inner");
        block("piston/bottom", "piston_bottom");
        add("piston/sideOverlay", vanillaBlock("piston_side"), keepIfNotPistonCobblestone(true, false), false);
        add("piston/innerOverlay", vanillaBlock("piston_inner"), keepIfNotPistonCobblestone(false, true), false);
        add("piston/bottomOverlay", vanillaBlock("piston_bottom"), keepIfNotPistonCobblestone(false, false), false);
        add("piston/blank", vanillaBlock("piston_bottom"), argb -> 0, false);
        add("cornflower/petals", modTexture("blocks/base/cornflower"), GREY_IF_NOT_GREEN, false);
        add("cornflower/stem", modTexture("blocks/base/cornflower"), KEEP_IF_GREEN, false);

        add("tulip/petals", vanillaBlock("flower_tulip_white"), GREY_IF_NOT_GREEN, false);
        add("tulip/stem", vanillaBlock("flower_tulip_white"), KEEP_IF_GREEN, false);
        add("sapling/leaves", vanillaBlock("sapling_oak"), GREY_IF_GREEN, false);
        add("sapling/trunk", vanillaBlock("sapling_oak"), KEEP_IF_NOT_GREEN, false);

        add("dye", new ResourceLocation("minecraft", "textures/items/dye_powder_white.png"), GREY, false);

        add("chest/normal", new ResourceLocation("minecraft", "textures/entity/chest/normal.png"), GREY, true);
        add("chest/double", new ResourceLocation("minecraft", "textures/entity/chest/normal_double.png"), GREY, true);

        // Optional compat: these are only requested when Iron Chests or Storage Drawers is installed.
        for (String tier : IRON_CHEST_TIERS) {
            ResourceLocation model = new ResourceLocation("ironchest", "textures/model/" + tier + "chest.png");
            add("ironchest/" + tier + "_panels", model, metalChest(true), true);
            add("ironchest/" + tier + "_trim", model, metalChest(false), true);
        }
        for (String part : DRAWER_PARTS) {
            ResourceLocation oak = new ResourceLocation(
                "storagedrawers",
                "textures/blocks/drawers_oak_" + part + ".png");
            add("drawers/" + part, oak, GREY, false);
        }
    }

    private TintSources() {}

    public static Source get(String key) {
        return SOURCES.get(key);
    }

    private static void block(String key, String vanillaName) {
        add(key, vanillaBlock(vanillaName), GREY, false);
    }

    /** A texture of this mod's own, at textures/blocks/base/&lt;key&gt;.png. */
    private static void base(String key) {
        add(key, modTexture("blocks/base/" + key), GREY, false);
    }

    private static void add(String key, ResourceLocation location, PixelTransform transform, boolean model) {
        SOURCES.put(key, new Source(location, transform, model));
    }

    private static ResourceLocation vanillaBlock(String name) {
        return new ResourceLocation("minecraft", "textures/blocks/" + name + ".png");
    }

    private static ResourceLocation modTexture(String path) {
        return new ResourceLocation(Reference.MOD_ID, "textures/" + path + ".png");
    }

    private static int grey(int argb) {
        int r = argb >> 16 & 255, g = argb >> 8 & 255, b = argb & 255;
        int lum = Math.min(255, Math.round(0.299F * r + 0.587F * g + 0.114F * b));
        return argb & 0xFF000000 | lum << 16 | lum << 8 | lum;
    }

    private static boolean isGreen(int argb) {
        if ((argb >>> 24) == 0) {
            return false;
        }
        float[] hsb = java.awt.Color.RGBtoHSB(argb >> 16 & 255, argb >> 8 & 255, argb & 255, null);
        float hue = hsb[0] * 360.0F;
        return hsb[1] > 0.15F && hue >= 65.0F && hue <= 170.0F;
    }
}
