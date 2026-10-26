package net.neverandy.moredyes.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.CryingObsidianBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.GlazedTerracottaBlock;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PoweredBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.neverandy.moredyes.item.ChestItem;
import net.neverandy.moredyes.item.DyedEntityItems;
import net.neverandy.moredyes.item.MDTabs;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.toggle.Toggles;
import net.neverandy.moredyes.world.DyeTrees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Every dyed block, one per color of each type, named "<type>_<color>" such as "wool_334c59". Each block copies the
 * properties of its vanilla block, so it breaks, sounds and burns the same way. The arrays are filled when the blocks
 * are registered, so they can only be read once registration is done. Blocks the config turns off (see Toggles) are not
 * registered and stay null in their arrays.
 */
public class MDBlock
{
    public static final int COLORS = ColorStrings.ALL.length;

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    /**
     * The tool that mines each array of blocks fastest (a mineable/ tag), for the block tags. Arrays are compared by
     * identity; the map keeps the order they were added in, so the generated tags don't reorder between runs.
     */
    public static final Map<Block[], TagKey<Block>> MINEABLE = new LinkedHashMap<>();
    /** Arrays of blocks that need at least a stone, iron or diamond tool to drop, like their vanilla blocks. */
    public static final Map<Block[], TagKey<Block>> NEEDS_TOOL = new LinkedHashMap<>();
    /** The arrays whose blocks the config turned off; they stay full of nulls. */
    private static final Set<Block[]> DISABLED = Collections.newSetFromMap(new IdentityHashMap<>());

    public static Block[] brickArray = new Block[COLORS];
    public static Block[] clayArray = new Block[COLORS];
    public static Block[] woolArray = new Block[COLORS];
    public static Block[] cobbleArray = new Block[COLORS];
    public static Block[] stonebrickArray = new Block[COLORS];
    public static Block[] stonebrickCrackedArray = new Block[COLORS];
    public static Block[] stonebrickCarvedArray = new Block[COLORS];
    public static Block[] stoneArray = new Block[COLORS];
    public static Block[] obsidianArray = new Block[COLORS];
    public static Block[] lapisArray = new Block[COLORS];
    public static Block[] glowstoneArray = new Block[COLORS];
    public static Block[] coalArray = new Block[COLORS];
    public static Block[] soulsandArray = new Block[COLORS];
    public static Block[] redstoneArray = new Block[COLORS];
    public static Block[] quartzArray = new Block[COLORS];
    public static Block[] glassArray = new Block[COLORS];
    public static Block[] glassFoggyArray = new Block[COLORS];
    public static Block[] sandArray = new Block[COLORS];

    public static BlockSapling[] oakSaplingArray = new BlockSapling[COLORS];
    public static BlockSapling[] birchSaplingArray = new BlockSapling[COLORS];
    public static BlockSapling[] acaciaSaplingArray = new BlockSapling[COLORS];
    public static BlockSapling[] darkOakSaplingArray = new BlockSapling[COLORS];
    public static BlockSapling[] jungleSaplingArray = new BlockSapling[COLORS];
    public static BlockSapling[] spruceSaplingArray = new BlockSapling[COLORS];

    public static FlowerBlock[] tulipArray = new FlowerBlock[COLORS];
    public static WorkbenchBlock[] workbenchArray = new WorkbenchBlock[COLORS];

    public static Block[] oakPlankArray = new Block[COLORS];
    public static Block[] birchPlankArray = new Block[COLORS];
    public static Block[] acaciaPlankArray = new Block[COLORS];
    public static Block[] darkOakPlankArray = new Block[COLORS];
    public static Block[] junglePlankArray = new Block[COLORS];
    public static Block[] sprucePlankArray = new Block[COLORS];

    public static LeavesBlock[] oakLeafArray = new LeavesBlock[COLORS];
    public static LeavesBlock[] spruceLeafArray = new LeavesBlock[COLORS];
    public static LeavesBlock[] jungleLeafArray = new LeavesBlock[COLORS];
    public static LeavesBlock[] birchLeafArray = new LeavesBlock[COLORS];
    public static LeavesBlock[] acaciaLeafArray = new LeavesBlock[COLORS];
    public static LeavesBlock[] darkOakLeafArray = new LeavesBlock[COLORS];

    public static Block[] oakLogArray = new Block[COLORS];
    public static Block[] birchLogArray = new Block[COLORS];
    public static Block[] darkOakLogArray = new Block[COLORS];
    public static Block[] jungleLogArray = new Block[COLORS];
    public static Block[] acaciaLogArray = new Block[COLORS];
    public static Block[] spruceLogArray = new Block[COLORS];

    public static FenceBlock[] oakFenceArray = new FenceBlock[COLORS];
    public static FenceBlock[] birchFenceArray = new FenceBlock[COLORS];
    public static FenceBlock[] darkOakFenceArray = new FenceBlock[COLORS];
    public static FenceBlock[] jungleFenceArray = new FenceBlock[COLORS];
    public static FenceBlock[] acaciaFenceArray = new FenceBlock[COLORS];
    public static FenceBlock[] spruceFenceArray = new FenceBlock[COLORS];

    public static Block[] sandstoneArray = new Block[COLORS];
    public static Block[] sandstoneCarvedArray = new Block[COLORS];
    public static Block[] sandstoneSmoothArray = new Block[COLORS];
    public static Block[] andesiteArray = new Block[COLORS];
    public static Block[] dioriteArray = new Block[COLORS];
    public static Block[] concretePowderArray = new Block[COLORS];
    public static Block[] concreteArray = new Block[COLORS];
    public static BlockPiston[] pistonArray = new BlockPiston[COLORS];
    public static BlockPiston[] stickyPistonArray = new BlockPiston[COLORS];
    public static BlockPistonHead[] pistonHeadArray = new BlockPistonHead[COLORS];
    public static Block[] hardenedClayArray = new Block[COLORS];

    public static IronBarsBlock[] glassPaneArray = new IronBarsBlock[COLORS];
    public static IronBarsBlock[] glassFoggyPaneArray = new IronBarsBlock[COLORS];
    public static BlockBookshelf[] bookshelfArray = new BlockBookshelf[COLORS];
    public static BlockChest[] chestArray = new BlockChest[COLORS];
    public static DyedStandingSignBlock[] signArray = new DyedStandingSignBlock[COLORS];
    public static DyedWallSignBlock[] wallSignArray = new DyedWallSignBlock[COLORS];

    // Carpets, beds, banners and shulker boxes. Beds, banners and shulker boxes are vanilla blocks with the white dye
    // color (no color for shulker boxes) that share vanilla's block entities; the client draws them in their own color.
    public static CarpetBlock[] carpetArray = new CarpetBlock[COLORS];
    public static BedBlock[] bedArray = new BedBlock[COLORS];
    // Beds in the vanilla colors, made from vanilla wool and planks other than oak, so they can remember their planks.
    // They have no place in the creative tabs.
    public static BedBlock[] vanillaColorBedArray = new BedBlock[DyeColor.values().length];
    public static BannerBlock[] bannerArray = new BannerBlock[COLORS];
    public static WallBannerBlock[] wallBannerArray = new WallBannerBlock[COLORS];
    public static DyedShulkerBoxBlock[] shulkerBoxArray = new DyedShulkerBoxBlock[COLORS];

    // Full blocks from the sheet's second batch.
    public static Block[] graniteArray = new Block[COLORS];
    public static Block[] polishedAndesiteArray = new Block[COLORS];
    public static Block[] polishedDioriteArray = new Block[COLORS];
    public static Block[] polishedGraniteArray = new Block[COLORS];
    /** Granite with GregTech's red granite textures (LGPL-3.0, see THIRD_PARTY_NOTICES.md), greyed and tinted. */
    public static Block[] speckledGraniteArray = new Block[COLORS];
    public static Block[] smoothSpeckledGraniteArray = new Block[COLORS];
    public static Block[] endstoneArray = new Block[COLORS];
    public static Block[] mossyCobbleArray = new Block[COLORS];
    public static Block[] mossyStonebrickArray = new Block[COLORS];
    public static Block[] quartzBricksArray = new Block[COLORS];
    public static Block[] quartzChiseledArray = new Block[COLORS];
    public static RotatedPillarBlock[] quartzPillarArray = new RotatedPillarBlock[COLORS];
    public static Block[] quartzSmoothArray = new Block[COLORS];
    public static RotatedPillarBlock[] boneBlockArray = new RotatedPillarBlock[COLORS];
    public static Block[] gravelArray = new Block[COLORS];
    public static Block[] iceArray = new Block[COLORS];
    public static Block[] packedIceArray = new Block[COLORS];
    public static Block[] snowArray = new Block[COLORS];

    // The full blocks 1.7.10 More Dyes added later (from vanilla 1.14 to 1.16); each copies its vanilla block.
    public static Block[] soulSoilArray = new Block[COLORS];
    public static RotatedPillarBlock[] basaltArray = new RotatedPillarBlock[COLORS];
    public static RotatedPillarBlock[] polishedBasaltArray = new RotatedPillarBlock[COLORS];
    public static Block[] smoothStoneArray = new Block[COLORS];
    /** Smooth sandstone; "sandstonesmooth" is cut sandstone, named before vanilla had both. */
    public static Block[] smoothSandstoneArray = new Block[COLORS];
    public static Block[] netherBrickArray = new Block[COLORS];
    public static Block[] netherBrickCrackedArray = new Block[COLORS];
    public static Block[] netherBrickChiseledArray = new Block[COLORS];
    public static Block[] endStoneBrickArray = new Block[COLORS];
    public static GlazedTerracottaBlock[] glazedTerracottaArray = new GlazedTerracottaBlock[COLORS];
    public static Block[] netheriteArray = new Block[COLORS];
    public static CryingObsidianBlock[] cryingObsidianArray = new CryingObsidianBlock[COLORS];
    public static ChainBlock[] chainArray = new ChainBlock[COLORS];

    /** Small flowers, as {registry type, vanilla flower}; each array in smallFlowerArrays matches one row. */
    public static final String[][] SMALL_FLOWERS = {
        {"allium", "allium"}, {"azurebluet", "azure_bluet"}, {"cornflower", "cornflower"}, {"dandelion", "dandelion"},
        {"lilyofthevalley", "lily_of_the_valley"}, {"orchid", "blue_orchid"}, {"oxeyedaisy", "oxeye_daisy"}, {"poppy", "poppy"}};
    public static final FlowerBlock[][] smallFlowerArrays = new FlowerBlock[SMALL_FLOWERS.length][COLORS];
    /** Two-block-tall flowers, as {registry type, vanilla flower}. */
    public static final String[][] TALL_FLOWERS = {{"lilac", "lilac"}, {"peony", "peony"}, {"rosebush", "rose_bush"}};
    public static final TallFlowerBlock[][] tallFlowerArrays = new TallFlowerBlock[TALL_FLOWERS.length][COLORS];

    /**
     * Flower pots holding a dyed flower or sapling: one row per plant type, one pot per color. The vanilla flower pot
     * learns them in common setup (see {@link #registerPottedPlants}), so a dyed plant can be put in an empty pot.
     */
    public static final List<PottedPlants> POTTED = new ArrayList<>();

    /** The potted forms of one type of plant. petals and stem are its tinted and untinted textures in block/tinted. */
    public static final class PottedPlants
    {
        public final String type;
        public final String petals;
        public final String stem;
        public final Block[] plants;
        public final FlowerPotBlock[] pots = new FlowerPotBlock[COLORS];

        PottedPlants(String type, String petals, String stem, Block[] plants)
        {
            this.type = type;
            this.petals = petals;
            this.stem = stem;
            this.plants = plants;
        }
    }

    private static final TagKey<Block> PICKAXE = BlockTags.MINEABLE_WITH_PICKAXE;
    private static final TagKey<Block> AXE = BlockTags.MINEABLE_WITH_AXE;
    private static final TagKey<Block> SHOVEL = BlockTags.MINEABLE_WITH_SHOVEL;
    private static final TagKey<Block> HOE = BlockTags.MINEABLE_WITH_HOE;

    public static void register(IEventBus modBus)
    {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);

        // The same order as the 1.16.5 port, which is the order the creative tabs list them in.
        colorsByHex("glass", glassArray, hex -> new DyedGlassBlock(copy(Blocks.GLASS), hex), MDTabs.BLOCKS, null);
        colors("sand", sandArray, () -> new FallingBlock(copy(Blocks.SAND)), MDTabs.BLOCKS, SHOVEL);
        colors("leaf", oakLeafArray, () -> new LeavesBlock(copy(Blocks.OAK_LEAVES)), MDTabs.TREES, HOE);
        colors("brick", brickArray, () -> new Block(copy(Blocks.BRICKS)), MDTabs.BLOCKS, PICKAXE);
        colors("clay", clayArray, () -> new Block(copy(Blocks.CLAY)), MDTabs.BLOCKS, SHOVEL);
        colors("wool", woolArray, () -> new Block(copy(Blocks.WHITE_WOOL)), MDTabs.BLOCKS, null);
        colors("cobble", cobbleArray, () -> new Block(copy(Blocks.COBBLESTONE)), MDTabs.BLOCKS, PICKAXE);
        colors("stonebrick", stonebrickArray, () -> new Block(copy(Blocks.STONE_BRICKS)), MDTabs.BLOCKS, PICKAXE);
        colors("stonebrickcracked", stonebrickCrackedArray, () -> new Block(copy(Blocks.CRACKED_STONE_BRICKS)), MDTabs.BLOCKS, PICKAXE);
        colors("stonebrickcarved", stonebrickCarvedArray, () -> new Block(copy(Blocks.CHISELED_STONE_BRICKS)), MDTabs.BLOCKS, PICKAXE);
        colors("stone", stoneArray, () -> new Block(copy(Blocks.STONE)), MDTabs.BLOCKS, PICKAXE);
        colors("obsidian", obsidianArray, () -> new Block(copy(Blocks.OBSIDIAN)), MDTabs.BLOCKS, PICKAXE);
        NEEDS_TOOL.put(obsidianArray, BlockTags.NEEDS_DIAMOND_TOOL);
        colors("lapis", lapisArray, () -> new Block(copy(Blocks.LAPIS_BLOCK)), MDTabs.BLOCKS, PICKAXE);
        NEEDS_TOOL.put(lapisArray, BlockTags.NEEDS_STONE_TOOL);
        colors("glowstone", glowstoneArray, () -> new Block(copy(Blocks.GLOWSTONE)), MDTabs.BLOCKS, null);
        colors("coal", coalArray, () -> new Block(copy(Blocks.COAL_BLOCK)), MDTabs.BLOCKS, PICKAXE);
        // A full block, unlike vanilla soul sand, so it doesn't slow anyone down.
        colors("soulsand", soulsandArray, () -> new Block(copy(Blocks.SOUL_SAND).speedFactor(1.0F)), MDTabs.BLOCKS, SHOVEL);
        colors("plank", oakPlankArray, () -> new Block(copy(Blocks.OAK_PLANKS)), MDTabs.BLOCKS, AXE);
        colorsByHex("glassfoggy", glassFoggyArray, hex -> new DyedGlassBlock(copy(Blocks.GLASS), hex), MDTabs.BLOCKS, null);
        colors("quartz", quartzArray, () -> new Block(copy(Blocks.QUARTZ_BLOCK)), MDTabs.BLOCKS, PICKAXE);
        colors("workbench", workbenchArray, () -> new WorkbenchBlock(copy(Blocks.CRAFTING_TABLE)), MDTabs.BLOCKS, AXE);
        colors("chest", chestArray, () -> new BlockChest(copy(Blocks.CHEST)), MDTabs.BLOCKS, AXE, ChestItem::new);
        if (!Toggles.panes)
        {
            disable(glassPaneArray);
            disable(glassFoggyPaneArray);
        }
        colorsByHex("glasspane", glassPaneArray, hex -> new DyedGlassPaneBlock(copy(Blocks.GLASS_PANE), hex), MDTabs.BLOCKS, null);
        colorsByHex("glassfoggypane", glassFoggyPaneArray, hex -> new DyedGlassPaneBlock(copy(Blocks.GLASS_PANE), hex), MDTabs.BLOCKS, null);
        colors("bookshelf", bookshelfArray, () -> new BlockBookshelf(copy(Blocks.BOOKSHELF)), MDTabs.BLOCKS, AXE);
        colors("tulip", tulipArray, () -> new FlowerBlock(copy(Blocks.RED_TULIP)), MDTabs.PLANTS, null);
        colors("redstone", redstoneArray, () -> new PoweredBlock(copy(Blocks.REDSTONE_BLOCK)), MDTabs.BLOCKS, PICKAXE);
        colors("oaklog", oakLogArray, () -> new Block(copy(Blocks.OAK_LOG)), MDTabs.TREES, AXE);
        colors("sandstone", sandstoneArray, () -> new Block(copy(Blocks.SANDSTONE)), MDTabs.BLOCKS, PICKAXE);
        colors("sandstonecarved", sandstoneCarvedArray, () -> new Block(copy(Blocks.CHISELED_SANDSTONE)), MDTabs.BLOCKS, PICKAXE);
        colors("sandstonesmooth", sandstoneSmoothArray, () -> new Block(copy(Blocks.CUT_SANDSTONE)), MDTabs.BLOCKS, PICKAXE);
        colors("andesite", andesiteArray, () -> new Block(copy(Blocks.ANDESITE)), MDTabs.BLOCKS, PICKAXE);
        colors("diorite", dioriteArray, () -> new Block(copy(Blocks.DIORITE)), MDTabs.BLOCKS, PICKAXE);
        colors("concretepowder", concretePowderArray, () -> new FallingBlock(copy(Blocks.WHITE_CONCRETE_POWDER)), MDTabs.BLOCKS, SHOVEL);
        colors("hardendclay", hardenedClayArray, () -> new Block(copy(Blocks.TERRACOTTA)), MDTabs.BLOCKS, PICKAXE);
        colors("concrete", concreteArray, () -> new Block(copy(Blocks.WHITE_CONCRETE)), MDTabs.BLOCKS, PICKAXE);

        wood("acacia", acaciaLogArray, acaciaLeafArray, acaciaPlankArray, Blocks.ACACIA_LOG, Blocks.ACACIA_LEAVES, Blocks.ACACIA_PLANKS);
        wood("birch", birchLogArray, birchLeafArray, birchPlankArray, Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, Blocks.BIRCH_PLANKS);
        fence("birch", birchFenceArray, Blocks.BIRCH_FENCE);
        wood("jungle", jungleLogArray, jungleLeafArray, junglePlankArray, Blocks.JUNGLE_LOG, Blocks.JUNGLE_LEAVES, Blocks.JUNGLE_PLANKS);
        wood("spruce", spruceLogArray, spruceLeafArray, sprucePlankArray, Blocks.SPRUCE_LOG, Blocks.SPRUCE_LEAVES, Blocks.SPRUCE_PLANKS);
        wood("darkoak", darkOakLogArray, darkOakLeafArray, darkOakPlankArray, Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_LEAVES, Blocks.DARK_OAK_PLANKS);
        fence("jungle", jungleFenceArray, Blocks.JUNGLE_FENCE);
        fence("acacia", acaciaFenceArray, Blocks.ACACIA_FENCE);
        fence("darkoak", darkOakFenceArray, Blocks.DARK_OAK_FENCE);
        fence("spruce", spruceFenceArray, Blocks.SPRUCE_FENCE);
        fence("oak", oakFenceArray, Blocks.OAK_FENCE);
        sapling("oak", oakSaplingArray);
        sapling("acacia", acaciaSaplingArray);
        sapling("jungle", jungleSaplingArray);
        sapling("spruce", spruceSaplingArray);
        sapling("birch", birchSaplingArray);
        sapling("dark_oak", darkOakSaplingArray);

        registerPistons();
        registerSigns();
        registerFurnishings();
        registerMoreBlocks();
        registerNewerBlocks();
        registerFlowers();
        registerPots();
        DyedShapes.registerAll();
    }

    /**
     * The vanilla block's properties. Its map color is fixed to its default one, since logs and pillars pick theirs from
     * an axis that not every dyed block has.
     */
    static BlockBehaviour.Properties copy(Block vanilla)
    {
        return BlockBehaviour.Properties.copy(vanilla).color(vanilla.defaultMaterialColor());
    }

    private static void wood(String wood, Block[] logs, LeavesBlock[] leaves, Block[] planks, Block log, Block leaf, Block plank)
    {
        colors(wood + "log", logs, () -> new Block(copy(log)), MDTabs.TREES, AXE);
        colors(wood + "leaves", leaves, () -> new LeavesBlock(copy(leaf)), MDTabs.TREES, HOE);
        colors(wood + "planks", planks, () -> new Block(copy(plank)), MDTabs.TREES, AXE);
    }

    private static void fence(String wood, FenceBlock[] fences, Block vanilla)
    {
        if (!Toggles.fences)
        {
            disable(fences);
        }
        colors(wood + "fence", fences, () -> new FenceBlock(copy(vanilla)), MDTabs.TREES, AXE);
    }

    /** "dark_oak" saplings are named "saplingdarkoak_<color>". */
    private static void sapling(String wood, BlockSapling[] saplings)
    {
        for (int i = 0; i < COLORS; i++)
        {
            final int color = i;
            String name = "sapling" + wood.replace("_", "") + "_" + ColorStrings.ALL[i];
            register(name, saplings, i, () -> new BlockSapling(DyeTrees.grower(wood, color), copy(Blocks.OAK_SAPLING)), MDTabs.TREES, BlockItem::new);
        }
    }

    private static void registerMoreBlocks()
    {
        colors("granite", graniteArray, () -> new Block(copy(Blocks.GRANITE)), MDTabs.BLOCKS, PICKAXE);
        colors("polishedandesite", polishedAndesiteArray, () -> new Block(copy(Blocks.POLISHED_ANDESITE)), MDTabs.BLOCKS, PICKAXE);
        colors("polisheddiorite", polishedDioriteArray, () -> new Block(copy(Blocks.POLISHED_DIORITE)), MDTabs.BLOCKS, PICKAXE);
        colors("polishedgranite", polishedGraniteArray, () -> new Block(copy(Blocks.POLISHED_GRANITE)), MDTabs.BLOCKS, PICKAXE);
        colors("speckledgranite", speckledGraniteArray, () -> new Block(copy(Blocks.GRANITE)), MDTabs.BLOCKS, PICKAXE);
        colors("smoothspeckledgranite", smoothSpeckledGraniteArray, () -> new Block(copy(Blocks.POLISHED_GRANITE)), MDTabs.BLOCKS, PICKAXE);
        colors("endstone", endstoneArray, () -> new Block(copy(Blocks.END_STONE)), MDTabs.BLOCKS, PICKAXE);
        colors("mossycobble", mossyCobbleArray, () -> new Block(copy(Blocks.MOSSY_COBBLESTONE)), MDTabs.BLOCKS, PICKAXE);
        colors("mossystonebrick", mossyStonebrickArray, () -> new Block(copy(Blocks.MOSSY_STONE_BRICKS)), MDTabs.BLOCKS, PICKAXE);
        colors("quartzbricks", quartzBricksArray, () -> new Block(copy(Blocks.QUARTZ_BRICKS)), MDTabs.BLOCKS, PICKAXE);
        colors("quartzchiseled", quartzChiseledArray, () -> new Block(copy(Blocks.CHISELED_QUARTZ_BLOCK)), MDTabs.BLOCKS, PICKAXE);
        colors("quartzpillar", quartzPillarArray, () -> new RotatedPillarBlock(copy(Blocks.QUARTZ_PILLAR)), MDTabs.BLOCKS, PICKAXE);
        colors("quartzsmooth", quartzSmoothArray, () -> new Block(copy(Blocks.SMOOTH_QUARTZ)), MDTabs.BLOCKS, PICKAXE);
        colors("boneblock", boneBlockArray, () -> new RotatedPillarBlock(copy(Blocks.BONE_BLOCK)), MDTabs.BLOCKS, PICKAXE);
        colors("gravel", gravelArray, () -> new FallingBlock(copy(Blocks.GRAVEL)), MDTabs.BLOCKS, SHOVEL);
        colors("ice", iceArray, () -> new IceBlock(copy(Blocks.ICE)), MDTabs.BLOCKS, PICKAXE);
        colors("packedice", packedIceArray, () -> new Block(copy(Blocks.PACKED_ICE)), MDTabs.BLOCKS, PICKAXE);
        colors("snow", snowArray, () -> new Block(copy(Blocks.SNOW_BLOCK)), MDTabs.BLOCKS, SHOVEL);
    }

    private static void registerNewerBlocks()
    {
        colors("soulsoil", soulSoilArray, () -> new Block(copy(Blocks.SOUL_SOIL)), MDTabs.BLOCKS, SHOVEL);
        colors("basalt", basaltArray, () -> new RotatedPillarBlock(copy(Blocks.BASALT)), MDTabs.BLOCKS, PICKAXE);
        colors("polishedbasalt", polishedBasaltArray, () -> new RotatedPillarBlock(copy(Blocks.POLISHED_BASALT)), MDTabs.BLOCKS, PICKAXE);
        colors("smoothstone", smoothStoneArray, () -> new Block(copy(Blocks.SMOOTH_STONE)), MDTabs.BLOCKS, PICKAXE);
        colors("smoothsandstone", smoothSandstoneArray, () -> new Block(copy(Blocks.SMOOTH_SANDSTONE)), MDTabs.BLOCKS, PICKAXE);
        colors("netherbrick", netherBrickArray, () -> new Block(copy(Blocks.NETHER_BRICKS)), MDTabs.BLOCKS, PICKAXE);
        colors("netherbrickcracked", netherBrickCrackedArray, () -> new Block(copy(Blocks.CRACKED_NETHER_BRICKS)), MDTabs.BLOCKS, PICKAXE);
        colors("netherbrickchiseled", netherBrickChiseledArray, () -> new Block(copy(Blocks.CHISELED_NETHER_BRICKS)), MDTabs.BLOCKS, PICKAXE);
        colors("endstonebrick", endStoneBrickArray, () -> new Block(copy(Blocks.END_STONE_BRICKS)), MDTabs.BLOCKS, PICKAXE);
        colors("glazedterracotta", glazedTerracottaArray, () -> new GlazedTerracottaBlock(copy(Blocks.WHITE_GLAZED_TERRACOTTA)), MDTabs.BLOCKS, PICKAXE);
        colors("netherite", netheriteArray, () -> new Block(copy(Blocks.NETHERITE_BLOCK)), MDTabs.BLOCKS, PICKAXE);
        NEEDS_TOOL.put(netheriteArray, BlockTags.NEEDS_DIAMOND_TOOL);
        colors("cryingobsidian", cryingObsidianArray, () -> new CryingObsidianBlock(copy(Blocks.CRYING_OBSIDIAN)), MDTabs.BLOCKS, PICKAXE);
        NEEDS_TOOL.put(cryingObsidianArray, BlockTags.NEEDS_DIAMOND_TOOL);
        colors("chain", chainArray, () -> new ChainBlock(copy(Blocks.CHAIN)), MDTabs.BLOCKS, PICKAXE);
    }

    private static void registerFlowers()
    {
        for (int f = 0; f < SMALL_FLOWERS.length; f++)
        {
            colors(SMALL_FLOWERS[f][0], smallFlowerArrays[f], () -> new FlowerBlock(copy(Blocks.POPPY)), MDTabs.PLANTS, null);
        }
        for (int f = 0; f < TALL_FLOWERS.length; f++)
        {
            colors(TALL_FLOWERS[f][0], tallFlowerArrays[f], () -> new DyedTallFlowerBlock(copy(Blocks.LILAC)), MDTabs.PLANTS, null);
        }
    }

    /** One flower pot per dyed plant and color, which has no item: it is placed by using the plant on an empty pot. */
    private static void registerPots()
    {
        POTTED.add(new PottedPlants("tulip", "tulip_petals", "tulip_stem", tulipArray));
        for (int f = 0; f < SMALL_FLOWERS.length; f++)
        {
            String type = SMALL_FLOWERS[f][0];
            POTTED.add(new PottedPlants(type, type + "_petals", type + "_stem", smallFlowerArrays[f]));
        }
        for (String wood : DyeTrees.WOODS)
        {
            // Saplings are registered as "saplingdarkoak" and so on.
            String type = "sapling" + wood.replace("_", "");
            POTTED.add(new PottedPlants(type, wood + "_sapling_leaves", wood + "_sapling_trunk", DyeTrees.saplings(wood)));
        }
        for (PottedPlants potted : POTTED)
        {
            if (isDisabled(potted.plants))
            {
                disable(potted.pots);
            }
            for (int i = 0; i < COLORS; i++)
            {
                final int color = i;
                register("potted" + potted.type + "_" + ColorStrings.ALL[i], potted.pots, i,
                        () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> potted.plants[color],
                                copy(Blocks.POTTED_WHITE_TULIP)), null, null);
            }
        }
    }

    /** Lets the vanilla flower pot take the dyed plants. Must run after registration, in common setup. */
    public static void registerPottedPlants()
    {
        FlowerPotBlock emptyPot = (FlowerPotBlock) Blocks.FLOWER_POT;
        for (PottedPlants potted : POTTED)
        {
            for (int i = 0; i < COLORS; i++)
            {
                FlowerPotBlock pot = potted.pots[i];
                if (pot == null)
                {
                    continue;
                }
                emptyPot.addPlant(ForgeRegistries.BLOCKS.getKey(potted.plants[i]), () -> pot);
            }
        }
    }

    /** A piston and a sticky piston for each color, sharing one head block that has both kinds like vanilla. */
    private static void registerPistons()
    {
        MINEABLE.put(pistonArray, PICKAXE);
        MINEABLE.put(stickyPistonArray, PICKAXE);
        MINEABLE.put(pistonHeadArray, PICKAXE);
        // A piston needs its head, so leaving out either leaves out both (sticky pistons can go on their own).
        if (!Toggles.typeEnabled("piston") || !Toggles.typeEnabled("pistonhead"))
        {
            disable(pistonArray);
            disable(stickyPistonArray);
            disable(pistonHeadArray);
        }
        for (int i = 0; i < COLORS; i++)
        {
            final int color = i;
            String hex = ColorStrings.ALL[i];
            register("piston_" + hex, pistonArray, i, () -> new BlockPiston(false, color), MDTabs.BLOCKS, BlockItem::new);
            register("stickypiston_" + hex, stickyPistonArray, i, () -> new BlockPiston(true, color), MDTabs.BLOCKS, BlockItem::new);
            register("pistonhead_" + hex, pistonHeadArray, i, () -> new BlockPistonHead(color), null, null);
        }
    }

    /** Oak signs in each color. The wall sign has no item; it is placed by the sign's item and drops it. */
    private static void registerSigns()
    {
        MINEABLE.put(signArray, AXE);
        MINEABLE.put(wallSignArray, AXE);
        if (!Toggles.typeEnabled("sign") || !Toggles.typeEnabled("wallsign"))
        {
            disable(signArray);
            disable(wallSignArray);
        }
        for (int i = 0; i < COLORS; i++)
        {
            final int color = i;
            String hex = ColorStrings.ALL[i];
            register("sign_" + hex, signArray, i, () -> new DyedStandingSignBlock(copy(Blocks.OAK_SIGN)), MDTabs.BLOCKS,
                    (block, properties) -> new SignItem(properties.stacksTo(16), block, wallSignArray[color]));
            register("wallsign_" + hex, wallSignArray, i,
                    () -> new DyedWallSignBlock(copy(Blocks.OAK_WALL_SIGN).dropsLike(signArray[color])), null, null);
        }
    }

    /** Carpets, beds, banners and shulker boxes in each color. The wall banner has no item, like the wall sign. */
    private static void registerFurnishings()
    {
        colors("carpet", carpetArray, () -> new CarpetBlock(copy(Blocks.WHITE_CARPET)), MDTabs.BLOCKS, null);
        for (int i = 0; i < COLORS; i++)
        {
            register("bed_" + ColorStrings.ALL[i], bedArray, i, () -> new DyedBedBlock(DyeColor.WHITE, copy(Blocks.WHITE_BED)),
                    MDTabs.BLOCKS, DyedEntityItems.Bed::new);
        }
        if (!isDisabled(bedArray))
        {
            for (DyeColor color : DyeColor.values())
            {
                int i = color.getId();
                BLOCKS.register("bed_" + color.getName(), () -> vanillaColorBedArray[i] = new DyedBedBlock(color,
                        copy(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(color.getName() + "_bed")))));
                ITEMS.register("bed_" + color.getName(), () -> new DyedEntityItems.Bed(vanillaColorBedArray[i], new Item.Properties()));
            }
        }
        MINEABLE.put(bannerArray, AXE);
        MINEABLE.put(wallBannerArray, AXE);
        if (!Toggles.typeEnabled("banner") || !Toggles.typeEnabled("wallbanner"))
        {
            disable(bannerArray);
            disable(wallBannerArray);
        }
        for (int i = 0; i < COLORS; i++)
        {
            final int color = i;
            String hex = ColorStrings.ALL[i];
            register("banner_" + hex, bannerArray, i, () -> new BannerBlock(DyeColor.WHITE, copy(Blocks.WHITE_BANNER)), MDTabs.BLOCKS,
                    (block, properties) -> new DyedEntityItems.Banner(block, wallBannerArray[color], properties));
            register("wallbanner_" + hex, wallBannerArray, i,
                    () -> new WallBannerBlock(DyeColor.WHITE, copy(Blocks.WHITE_WALL_BANNER).dropsLike(bannerArray[color])), null, null);
        }
        colors("shulkerbox", shulkerBoxArray, () -> new DyedShulkerBoxBlock(DyedShulkerBoxBlock.properties(copy(Blocks.WHITE_SHULKER_BOX))),
                MDTabs.BLOCKS, PICKAXE, DyedEntityItems.ShulkerBox::new);
    }

    /** Every dyed bed, banner and shulker box block: the blocks that share a vanilla block entity. */
    public static List<Block> sharingVanillaBlockEntities()
    {
        List<Block> blocks = new ArrayList<>();
        for (Block[] array : new Block[][] {bedArray, bannerArray, wallBannerArray, shulkerBoxArray})
        {
            for (Block block : array)
            {
                if (block != null)
                {
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }

    /** Every More Dyes bed: the dyed ones and the ones in the vanilla colors. */
    public static Block[] beds()
    {
        List<Block> beds = new ArrayList<>(Arrays.asList(bedArray));
        beds.addAll(Arrays.asList(vanillaColorBedArray));
        return beds.toArray(new Block[0]);
    }

    /** The More Dyes bed in a vanilla color, or null if the config turned beds off. */
    public static BedBlock vanillaColorBed(DyeColor color)
    {
        return vanillaColorBedArray[color.getId()];
    }

    /** Every dyed sign block, standing and on a wall. */
    public static Block[] signs()
    {
        Block[] signs = new Block[COLORS * 2];
        System.arraycopy(signArray, 0, signs, 0, COLORS);
        System.arraycopy(wallSignArray, 0, signs, COLORS, COLORS);
        return signs;
    }

    private static <B extends Block> void colors(String type, B[] array, Supplier<B> factory, MDTabs tab, TagKey<Block> tool)
    {
        colors(type, array, factory, tab, tool, BlockItem::new);
    }

    /** Makes one block of a type per color, named "<type>_<color>", with an item in the given tab. */
    private static <B extends Block> void colors(String type, B[] array, Supplier<B> factory, MDTabs tab, TagKey<Block> tool, ItemFactory item)
    {
        if (tool != null)
        {
            MINEABLE.put(array, tool);
        }
        for (int i = 0; i < COLORS; i++)
        {
            register(type + "_" + ColorStrings.ALL[i], array, i, factory, tab, item);
        }
    }

    /** Like colors, for blocks that need their color: the factory gets the hex code, such as "334c59". */
    private static <B extends Block> void colorsByHex(String type, B[] array, Function<String, B> factory, MDTabs tab, TagKey<Block> tool)
    {
        if (tool != null)
        {
            MINEABLE.put(array, tool);
        }
        for (int i = 0; i < COLORS; i++)
        {
            String hex = ColorStrings.ALL[i];
            register(type + "_" + hex, array, i, () -> factory.apply(hex), tab, BlockItem::new);
        }
    }

    /** Leaves out every block of an array: they are not registered (see Toggles). */
    static void disable(Block[] array)
    {
        DISABLED.add(array);
    }

    /** Whether the config turned off the blocks of an array, which are then all null. */
    public static boolean isDisabled(Block[] array)
    {
        return DISABLED.contains(array);
    }

    /**
     * Registers one block, and its item in the tab unless the tab is null. Returns null without registering anything
     * when the config turns off the block's type or its array.
     */
    static <B extends Block> RegistryObject<B> register(String name, B[] array, int index, Supplier<B> factory, MDTabs tab, ItemFactory item)
    {
        if (!Toggles.typeEnabled(name.substring(0, name.lastIndexOf('_'))))
        {
            disable(array);
        }
        if (isDisabled(array))
        {
            Toggles.leftOut(name);
            return null;
        }
        RegistryObject<B> block = BLOCKS.register(name, () -> array[index] = factory.get());
        if (tab != null)
        {
            tab.add(ITEMS.register(name, () -> item.create(array[index], new Item.Properties().tab(tab.tab))));
        }
        return block;
    }

    /** All the dyed blocks, once they are registered. */
    public static List<Block> all()
    {
        List<Block> blocks = new ArrayList<>();
        for (RegistryObject<Block> entry : BLOCKS.getEntries())
        {
            blocks.add(entry.get());
        }
        return blocks;
    }

    @FunctionalInterface
    public interface ItemFactory
    {
        Item create(Block block, Item.Properties properties);
    }
}
