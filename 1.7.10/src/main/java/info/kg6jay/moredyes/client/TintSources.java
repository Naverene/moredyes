package info.kg6jay.moredyes.client;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.TintPixels;

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
    public static final PixelTransform GREY = TintPixels::grey;
    /** Grey where the pixel is green, transparent elsewhere (the leaves of a sapling). */
    public static final PixelTransform GREY_IF_GREEN = argb -> TintPixels.isGreen(argb) ? TintPixels.grey(argb) : 0;
    /** Grey where the pixel is not green, transparent elsewhere (the petals of a flower). */
    public static final PixelTransform GREY_IF_NOT_GREEN = argb -> TintPixels.isGreen(argb) ? 0 : TintPixels.grey(argb);
    /** Unchanged where the pixel is green, transparent elsewhere (the stem of a flower). */
    public static final PixelTransform KEEP_IF_GREEN = argb -> TintPixels.isGreen(argb) ? argb : 0;
    /** Unchanged where the pixel is not green, transparent elsewhere (the trunk of a sapling). */
    public static final PixelTransform KEEP_IF_NOT_GREEN = argb -> TintPixels.isGreen(argb) ? 0 : argb;

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
                return TintPixels.isPistonTrim(argb, x, y, size, side, inside) ? argb : 0;
            }
        };
    }

    /**
     * Grey and this many times brighter, for textures that are dark because of their color rather than their material
     * (vanilla nether brick is dark red), so the dye still shows.
     */
    public static PixelTransform greyBrighter(float factor) {
        return argb -> TintPixels.greyBrighter(argb, factor);
    }

    /**
     * The Iron Chests tiers that have dyed versions (compat/ironchest), named as their model textures are. Plain names,
     * so nothing here loads Iron Chests' classes.
     */
    public static final String[] IRON_CHEST_TIERS = { "iron", "gold", "diamond", "copper", "silver" };

    /** The parts of Storage Drawers' oak drawer textures that the dyed drawers (compat/storagedrawers) use. */
    public static final String[] DRAWER_PARTS = { "front_1", "front_2", "front_4", "side", "side_h", "side_v", "trim" };

    /**
     * Splits an Iron Chests chest texture (see TintPixels.MetalChest). With panels true this keeps the panels as grey
     * and makes the rest transparent; with panels false it keeps the frame and latch in their own colors and makes the
     * panels transparent.
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
                TintPixels.MetalChest chest = new TintPixels.MetalChest(
                    image.getRGB(0, 0, width, height, null, 0, width),
                    width,
                    height);
                return new PixelTransform() {

                    @Override
                    public int apply(int argb) {
                        return argb;
                    }

                    @Override
                    public int apply(int argb, int x, int y, int size) {
                        return panels ? chest.panel(argb, x, y) : chest.trim(argb, x, y);
                    }
                };
            }
        };
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
        // Drawn for More Dyes after GregTech's granite.
        base("speckledGranite");
        base("smoothSpeckledGranite");
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

        // More full blocks, the planks of the other woods, and the flowers. Lily of the valley and quartz bricks are
        // not
        // in 1.7, so they have grey textures of their own (the lily's stem keeps its green).
        block("endStone", "end_stone");
        block("gravel", "gravel");
        block("ice", "ice");
        block("packedIce", "ice_packed");
        block("snow", "snow");
        base("quartzBricks");
        block("chiseledQuartz/top", "quartz_block_chiseled_top");
        block("chiseledQuartz/side", "quartz_block_chiseled");
        block("chiseledQuartz/bottom", "quartz_block_chiseled_top");
        block("quartzPillar/top", "quartz_block_lines_top");
        block("quartzPillar/side", "quartz_block_lines");
        block("quartzPillar/bottom", "quartz_block_lines_top");
        block("chiseledSandstone/top", "sandstone_top");
        block("chiseledSandstone/side", "sandstone_carved");
        block("chiseledSandstone/bottom", "sandstone_top");
        block("sprucePlank", "planks_spruce");
        block("birchPlank", "planks_birch");
        block("junglePlank", "planks_jungle");
        block("acaciaPlank", "planks_acacia");
        block("darkOakPlank", "planks_big_oak");
        flower("allium", "flower_allium");
        flower("azureBluet", "flower_houstonia");
        flower("blueOrchid", "flower_blue_orchid");
        flower("dandelion", "flower_dandelion");
        flower("oxeyeDaisy", "flower_oxeye_daisy");
        flower("poppy", "flower_rose");
        base("lilyOfTheValley/petals");
        add("lilyOfTheValley/stem", modTexture("blocks/base/lilyOfTheValley/stem"), argb -> argb, false);
        flower("lilac/top", "double_plant_syringa_top");
        flower("lilac/bottom", "double_plant_syringa_bottom");
        flower("peony/top", "double_plant_paeonia_top");
        flower("peony/bottom", "double_plant_paeonia_bottom");
        flower("roseBush/top", "double_plant_rose_top");
        flower("roseBush/bottom", "double_plant_rose_bottom");
        // Signs: the item, and the model the sign renderer draws
        add("sign", new ResourceLocation("minecraft", "textures/items/sign.png"), GREY, false);
        add("sign/model", new ResourceLocation("minecraft", "textures/entity/sign.png"), GREY, true);

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

    /**
     * A flower's petals (tinted) and stem (in its own colors) from one vanilla texture, as "key/petals" and "key/stem".
     * For a half of a tall flower, such as "lilac/top", they are "lilac/topPetals" and "lilac/topStem".
     */
    private static void flower(String key, String vanillaName) {
        boolean half = key.contains("/");
        add(key + (half ? "Petals" : "/petals"), vanillaBlock(vanillaName), GREY_IF_NOT_GREEN, false);
        add(key + (half ? "Stem" : "/stem"), vanillaBlock(vanillaName), KEEP_IF_GREEN, false);
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

}
