package info.kg6jay.moredyes.block;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDBlockColoredChest;
import info.kg6jay.moredyes.item.MDItemBlockChest;
import info.kg6jay.moredyes.item.MDItemBlockColored;
import info.kg6jay.moredyes.reference.ColorStrings;
import info.kg6jay.moredyes.reference.Reference;

public class MDBlock {

    public static String[][] colorStrings = new String[][] { ColorStrings.WHITE, ColorStrings.ORANGE,
        ColorStrings.MAGENTA, ColorStrings.LBLUE, ColorStrings.YELLOW, ColorStrings.LIME, ColorStrings.PINK,
        ColorStrings.DGRAY, ColorStrings.LGRAY, ColorStrings.CYAN, ColorStrings.PURPLE, ColorStrings.BLUE,
        ColorStrings.BROWN, ColorStrings.GREEN, ColorStrings.RED };
    public static String[] colors = new String[] { "white", "orange", "magenta", "lightBlue", "yellow", "lime", "pink",
        "darkGray", "lightGray", "cyan", "purple", "blue", "brown", "green", "red" };
    public static Block[] wool;
    public static Block[] stoneBrick;
    public static Block[] stoneBrickCarved;
    public static Block[] stoneBrickCracked;
    public static Block[] obsidian;
    public static Block[] stone;
    public static Block[] cobble;
    public static Block[] soulsand;
    public static Block[] quartz;

    public static Block[] clay;
    public static Block[] coal;
    public static Block[] glowstone;
    public static Block[] lapis;
    public static Block[] redstone;

    public static Block[] plank;

    public static Block[] rockwool;
    public static Block[] tulip;
    public static Block[] log;
    public static Block[] leaf;
    public static Block[] sapling;

    public static Block[] glassClear;
    public static Block[] glassFoggy;
    public static Block[] glassClearPane;
    public static Block[] glassFoggyPane;
    public static Block[] brick;
    public static Block[] sand;
    public static Block[] workbench;
    public static Block[] chest;
    public static Block[] hardenedClay;
    public static Block[] sandstone;
    public static Block[] bookshelf;
    public static Block[] diorite;
    /** Plain diorite, the base for dyed diorite. */
    public static Block dioritePlain;

    public static void initialize() {
        int l = colors.length;
        dioritePlain = new MDBlockDiorite(Reference.BLOCK_INFO_DIORITE);

        wool = new Block[l];
        stoneBrick = new Block[l];
        stoneBrickCarved = new Block[l];
        stoneBrickCracked = new Block[l];
        stone = new Block[l];
        cobble = new Block[l];
        obsidian = new Block[l];
        soulsand = new Block[l];
        quartz = new Block[l];
        clay = new Block[l];
        coal = new Block[l];
        glowstone = new Block[l];
        lapis = new Block[l];
        redstone = new Block[l];
        plank = new Block[l];
        chest = new Block[l];

        tulip = new Block[l];
        log = new Block[l];
        leaf = new Block[l];
        sapling = new Block[l];
        glassClear = new Block[l];
        glassFoggy = new Block[l];
        glassClearPane = new Block[l];
        glassFoggyPane = new Block[l];
        brick = new Block[l];
        sand = new Block[l];

        workbench = new Block[l];
        hardenedClay = new Block[l];
        sandstone = new Block[l];
        bookshelf = new Block[l];
        diorite = new Block[l];

        for (int i = 0; i < colors.length; i++) {
            String[] shades = colorStrings[i];
            String set = colors[i];
            wool[i] = new MDBlockColored(shades, Reference.BLOCK_INFO_WOOL, set);
            stoneBrick[i] = new MDBlockColored(shades, Reference.BLOCK_INFO_STONE_BRICK, set);
            stoneBrickCarved[i] = new MDBlockColored(shades, Reference.BLOCK_INFO_STONE_BRICK_CARVED, set);
            stoneBrickCracked[i] = new MDBlockColored(shades, Reference.BLOCK_INFO_STONE_BRICK_CRACKED, set);
            stone[i] = new MDBlockStone(shades, Reference.BLOCK_INFO_STONE, set, i);
            cobble[i] = new MDBlockColored(shades, Reference.BLOCK_INFO_COBBLE, set);
            obsidian[i] = new MDBlockObsidian(shades, Reference.BLOCK_INFO_OBSIDIAN, set);
            soulsand[i] = new MDBlockSoulSand(shades, Reference.BLOCK_INFO_SOULSAND, set);
            quartz[i] = new MDBlockColoredMulti(shades, Reference.BLOCK_INFO_QUARTZ, set);
            clay[i] = new MDBlockColored(shades, Reference.BLOCK_INFO_CLAY, set);
            coal[i] = new MDBlockColored(shades, Reference.BLOCK_INFO_COAL, set);
            glowstone[i] = new MDBlockGlowstone(shades, Reference.BLOCK_INFO_GLOWSTONE, set);
            lapis[i] = new MDBlockColored(shades, Reference.BLOCK_INFO_LAPIS, set);
            redstone[i] = new MDBlockColoredPowered(shades, Reference.BLOCK_INFO_REDSTONE, set);
            plank[i] = new MDBlockColored(shades, Reference.BLOCK_INFO_PLANK, set);
            tulip[i] = new MDBlockFlower(shades, Reference.BLOCK_INFO_TULIP, set);
            log[i] = new MDBlockLog(shades, Reference.BLOCK_INFO_LOG, set);
            leaf[i] = new MDBlockLeaf(shades, Reference.BLOCK_INFO_LEAVES, set, i);
            sapling[i] = new MDBlockSapling(shades, Reference.BLOCK_INFO_SAPLING, set, i);
            glassClear[i] = new MDBlockGlass(shades, Reference.BLOCK_INFO_GLASS, set, i, "clear");
            glassFoggy[i] = new MDBlockGlass(shades, Reference.BLOCK_INFO_GLASS, set, i, "foggy");
            glassClearPane[i] = new MDBlockGlassPane(shades, Reference.BLOCK_INFO_GLASS, set, i, "clear");
            glassFoggyPane[i] = new MDBlockGlassPane(shades, Reference.BLOCK_INFO_GLASS, set, i, "foggy");
            brick[i] = new MDBlockColored(shades, Reference.BLOCK_INFO_BRICK, set);
            sand[i] = new MDBlockColoredSand(shades, Reference.BLOCK_INFO_SAND, set);
            workbench[i] = new MDBlockWorkbench(shades, Reference.BLOCK_INFO_WORKBENCH, set);
            chest[i] = new MDBlockColoredChest(shades, Reference.BLOCK_INFO_CHEST, set);
            hardenedClay[i] = new MDBlockColored(shades, Reference.BLOCK_INFO_HARDENED_CLAY, set);
            sandstone[i] = new MDBlockColoredMulti(shades, Reference.BLOCK_INFO_SANDSTONE, set);
            bookshelf[i] = new MDBlockBookshelf(shades, Reference.BLOCK_INFO_BOOKSHELF, set);
            diorite[i] = new MDBlockColored(shades, Reference.BLOCK_INFO_DIORITE, set);
        }
    }

    public static void register() {
        register(wool, "Wool");
        register(stoneBrick, "Stonebrick");
        register(stoneBrickCarved, "StonebrickCarved");
        register(stoneBrickCracked, "StonebrickCracked");
        register(stone, "Stone");
        register(cobble, "Cobble");
        register(obsidian, "Obsidian");
        register(soulsand, "Soulsand");
        register(quartz, "Quartz");
        register(clay, "Clay");
        register(coal, "Coal");
        register(glowstone, "Glowstone");
        register(lapis, "Lapis");
        register(redstone, "Redstone");
        register(plank, "Plank");
        register(tulip, "Tulip");
        register(log, "Log");
        register(leaf, "Leaf");
        register(sapling, "Sapling");
        register(glassClear, "GlassClear");
        register(glassFoggy, "GlassFoggy");
        register(glassClearPane, "GlassClearPane");
        register(glassFoggyPane, "GlassFoggyPane");
        register(brick, "Brick");
        register(sand, "Sand");
        register(workbench, "Workbench");
        register(hardenedClay, "HardenedClay");
        register(sandstone, "Sandstone");
        register(bookshelf, "Bookshelf");
        register(diorite, "Diorite");
        GameRegistry.registerBlock(dioritePlain, "diorite");
        for (Block block : chest) {
            GameRegistry
                .registerBlock(block, MDItemBlockChest.class, ((IBlockColored) block).getColorSet() + "MixChest");
        }
    }

    /**
     * Registers one block per color set as "colorSet + Mix + suffix". These names are saved in worlds, so they must
     * not change.
     */
    public static void register(Block[] blocks, String suffix) {
        for (Block block : blocks) {
            String name = ((IBlockColored) block).getColorSet() + "Mix" + suffix;
            GameRegistry.registerBlock(block, MDItemBlockColored.class, name);
        }
    }

    /** The vanilla block each dyed block turns back into when washed in a cauldron. */
    public static final Map<Block, ItemStack> WASHED = new HashMap<>();

    public static void registerWashing() {
        for (int i = 0; i < colors.length; i++) {
            WASHED.put(wool[i], new ItemStack(Blocks.wool, 1, 0));
            WASHED.put(stoneBrick[i], new ItemStack(Blocks.stonebrick, 1, 0));
            WASHED.put(stoneBrickCracked[i], new ItemStack(Blocks.stonebrick, 1, 2));
            WASHED.put(stoneBrickCarved[i], new ItemStack(Blocks.stonebrick, 1, 3));
            WASHED.put(stone[i], new ItemStack(Blocks.stone));
            WASHED.put(cobble[i], new ItemStack(Blocks.cobblestone));
            WASHED.put(obsidian[i], new ItemStack(Blocks.obsidian));
            WASHED.put(soulsand[i], new ItemStack(Blocks.soul_sand));
            WASHED.put(quartz[i], new ItemStack(Blocks.quartz_block));
            WASHED.put(clay[i], new ItemStack(Blocks.stained_hardened_clay, 1, 0));
            WASHED.put(hardenedClay[i], new ItemStack(Blocks.hardened_clay));
            WASHED.put(coal[i], new ItemStack(Blocks.coal_block));
            WASHED.put(glowstone[i], new ItemStack(Blocks.glowstone));
            WASHED.put(lapis[i], new ItemStack(Blocks.lapis_block));
            WASHED.put(redstone[i], new ItemStack(Blocks.redstone_block));
            WASHED.put(plank[i], new ItemStack(Blocks.planks, 1, 0));
            WASHED.put(log[i], new ItemStack(Blocks.log, 1, 0));
            WASHED.put(leaf[i], new ItemStack(Blocks.leaves, 1, 0));
            WASHED.put(sapling[i], new ItemStack(Blocks.sapling, 1, 0));
            WASHED.put(glassClear[i], new ItemStack(Blocks.glass));
            WASHED.put(glassClearPane[i], new ItemStack(Blocks.glass_pane));
            WASHED.put(brick[i], new ItemStack(Blocks.brick_block));
            WASHED.put(sand[i], new ItemStack(Blocks.sand, 1, 0));
            WASHED.put(workbench[i], new ItemStack(Blocks.crafting_table));
            WASHED.put(chest[i], new ItemStack(Blocks.chest));
            WASHED.put(sandstone[i], new ItemStack(Blocks.sandstone, 1, 0));
            WASHED.put(bookshelf[i], new ItemStack(Blocks.bookshelf));
            WASHED.put(diorite[i], new ItemStack(dioritePlain));
        }
    }

    /** Same fire settings as the vanilla blocks (encouragement, flammability). */
    public static void registerFlammability() {
        for (int i = 0; i < colors.length; i++) {
            Blocks.fire.setFireInfo(wool[i], 30, 60);
            Blocks.fire.setFireInfo(plank[i], 5, 20);
            Blocks.fire.setFireInfo(log[i], 5, 5);
            Blocks.fire.setFireInfo(leaf[i], 30, 60);
            Blocks.fire.setFireInfo(bookshelf[i], 30, 20);
            Blocks.fire.setFireInfo(tulip[i], 60, 100);
            Blocks.fire.setFireInfo(coal[i], 5, 5);
        }
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileEntityMDBlockColoredChest.class, "MDBlockColoredChest");
    }

    /**
     * Ore dictionary names, matching the names Forge gives the vanilla blocks (and GregTech's where Forge has none),
     * so recipes from other mods accept the dyed blocks wherever they accept the vanilla ones.
     */
    public static void registerOreDictionary() {
        for (int i = 0; i < 16; i++) {
            OreDictionary.registerOre("wool", new ItemStack(Blocks.wool, 1, i));
            OreDictionary.registerOre("blockWool", new ItemStack(Blocks.wool, 1, i));
            OreDictionary.registerOre("stainedClay", new ItemStack(Blocks.stained_hardened_clay, 1, i));
        }
        OreDictionary.registerOre("soulsand", new ItemStack(Blocks.soul_sand));
        OreDictionary.registerOre("bookshelf", new ItemStack(Blocks.bookshelf));
        OreDictionary.registerOre("chest", new ItemStack(Blocks.chest));
        OreDictionary.registerOre("chestWood", new ItemStack(Blocks.chest));
        OreDictionary.registerOre("craftingTableWood", new ItemStack(Blocks.crafting_table));
        OreDictionary.registerOre("stoneDiorite", new ItemStack(dioritePlain));
        OreDictionary.registerOre("blockDiorite", new ItemStack(dioritePlain));

        for (int a = 0; a < colors.length; a++) {
            for (int i = 0; i <= ((IBlockColored) wool[a]).getMaxMeta(); i++) {
                ore(wool[a], i, "wool", "blockWool");
                ore(stoneBrick[a], i, "bricksStone", "stoneBricks");
                ore(stoneBrickCracked[a], i, "bricksStoneCracked");
                ore(stoneBrickCarved[a], i, "bricksStoneCarved");
                ore(obsidian[a], i, "blockObsidian", "stoneObsidian");
                ore(stone[a], i, "stone");
                ore(cobble[a], i, "cobblestone");
                ore(soulsand[a], i, "soulsand");
                ore(quartz[a], i, "blockQuartz");
                ore(redstone[a], i, "blockRedstone");
                ore(coal[a], i, "blockCoal");
                ore(plank[a], i, "plankWood");
                ore(log[a], i, "logWood");
                ore(leaf[a], i, "treeLeaves");
                ore(sapling[a], i, "treeSapling");
                ore(glowstone[a], i, "glowstone");
                ore(lapis[a], i, "blockLapis");
                ore(clay[a], i, "stainedClay");
                ore(hardenedClay[a], i, "hardenedClay");
                ore(glassClear[a], i, "blockGlass");
                ore(glassFoggy[a], i, "blockGlass");
                ore(glassClearPane[a], i, "paneGlass");
                ore(glassFoggyPane[a], i, "paneGlass");
                ore(workbench[a], i, "craftingTableWood", "workbench");
                ore(chest[a], i, "chestWood", "chest");
                ore(sand[a], i, "sand");
                ore(sandstone[a], i, "sandstone");
                ore(bookshelf[a], i, "bookshelf");
                ore(diorite[a], i, "stoneDiorite", "blockDiorite");
            }
        }
    }

    private static void ore(Block block, int meta, String... names) {
        for (String name : names) {
            OreDictionary.registerOre(name, new ItemStack(block, 1, meta));
        }
    }

    /**
     * True when no other mod provides diorite, so this mod generates it in the world and adds its crafting recipe.
     * Decided in postInit, once every mod has registered its ore names.
     */
    public static boolean useOwnDiorite = true;

    public static void detectDiorite() {
        for (ItemStack stack : OreDictionary.getOres("stoneDiorite")) {
            Block block = Block.getBlockFromItem(stack.getItem());
            if (block != dioritePlain && !(block instanceof IBlockColored)) {
                useOwnDiorite = false;
                return;
            }
        }
    }
}
