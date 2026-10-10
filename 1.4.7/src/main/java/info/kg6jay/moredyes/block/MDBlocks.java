package info.kg6jay.moredyes.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.Configuration;

import cpw.mods.fml.common.registry.GameRegistry;
import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.Textures;
import info.kg6jay.moredyes.item.ItemBlockDyed;
import info.kg6jay.moredyes.item.ItemBlockDyedSlab;

/**
 * Every block of the mod. Each kind of dyed block is one block ID for all of its colors: the color is in the block's
 * tile entity, so the metadata stays free and 4096 block IDs are enough. The IDs are set in the config.
 */
public final class MDBlocks {

    private static final StepSound STONE = Block.soundStoneFootstep, WOOD = Block.soundWoodFootstep,
        GLASS = Block.soundGlassFootstep, SAND = Block.soundSandFootstep, GRASS = Block.soundGrassFootstep,
        METAL = Block.soundMetalFootstep, CLOTH = Block.soundClothFootstep;
    private static final String PICKAXE = "pickaxe", AXE = "axe", SHOVEL = "shovel";

    /** The first block ID used by default; the others follow in the order the blocks are made. */
    private static final int FIRST_ID = 3400;

    public static Block wool, stonebrick, stonebrickCarved, stonebrickCracked, stone, cobble, obsidian, soulsand,
        quartz, clay, coal, glowstone, lapis, redstone, plank, tulip, log, leaves, sapling, glassClear, glassFoggy,
        glassClearPane, glassFoggyPane, brick, sand, workbench, chest, hardenedClay, sandstone, bookshelf, diorite,
        granite, andesite, polishedGranite, polishedDiorite, polishedAndesite, concrete, concretePowder, soulSoil,
        basalt, polishedBasalt, smoothStone, smoothSandstone, smoothQuartz, cutSandstone, mossyCobble,
        mossyStonebrick, netherBrick, crackedNetherBrick, chiseledNetherBrick, endStoneBrick, boneBlock,
        glazedTerracotta, netheriteBlock, cryingObsidian, cornflower, chain;

    /** Plain granite, diorite and andesite, the bases for the dyed versions. */
    public static Block granitePlain, dioritePlain, andesitePlain;

    public static Block ironTrapdoor;

    /** A stair, slab or wall block and the dyed block it is crafted from. */
    public static final class Shape {

        public static final int STAIRS = 0, SLAB = 1, WALL = 2;

        public final Block block;
        public final Block base;
        public final int kind;

        Shape(Block block, Block base, int kind) {
            this.block = block;
            this.base = base;
            this.kind = kind;
        }
    }

    public static final List<Shape> SHAPES = new ArrayList<Shape>();

    /** Every dyed block that is a full kind of its own (not a stair, slab or wall), in creative menu order. */
    public static final List<Block> DYED = new ArrayList<Block>();

    private static Configuration config;
    private static int nextId = FIRST_ID;

    private MDBlocks() {}

    public static void init(Configuration cfg) {
        config = cfg;
        CreativeTabs blocks = MoreDyes.tabBlocks, plants = MoreDyes.tabPlants, trees = MoreDyes.tabTrees;

        wool = dyed(new BlockDyed(id("wool"), info("wool", "Wool", Material.cloth, 0.8F, CLOTH, null, 0, 1.0F,
            blocks), Faces.all(Textures.WOOL)));
        stonebrick = dyed(new BlockDyed(id("stonebrick"), info("stonebrick", "Stone Brick", Material.rock, 1.5F,
            STONE, PICKAXE, 1, 10.0F, blocks), Faces.all(Textures.STONEBRICK)));
        stonebrickCarved = dyed(new BlockDyed(id("stonebrickCarved"), info("stonebrickCarved", "Carved Stone Brick",
            Material.rock, 1.5F, STONE, PICKAXE, 1, 10.0F, blocks), Faces.all(Textures.STONEBRICK_CARVED)));
        stonebrickCracked = dyed(new BlockDyed(id("stonebrickCracked"), info("stonebrickCracked",
            "Cracked Stone Brick", Material.rock, 1.5F, STONE, PICKAXE, 1, 10.0F, blocks),
            Faces.all(Textures.STONEBRICK_CRACKED)));
        stone = dyed(new BlockDyedStone(id("stone"), info("stone", "Stone", Material.rock, 1.5F, STONE, PICKAXE, 1,
            10.0F, blocks), Faces.all(Textures.STONE)));
        cobble = dyed(new BlockDyed(id("cobble"), info("cobble", "Cobblestone", Material.rock, 1.5F, STONE, PICKAXE,
            1, 10.0F, blocks), Faces.all(Textures.COBBLE)));
        obsidian = dyed(new BlockDyed(id("obsidian"), info("obsidian", "Obsidian", Material.rock, 50.0F, STONE,
            PICKAXE, 3, 2000.0F, blocks), Faces.all(Textures.OBSIDIAN)));
        soulsand = dyed(new BlockDyedSoulSand(id("soulsand"), info("soulsand", "Soulsand", Material.sand, 0.5F, SAND,
            SHOVEL, 0, 1.0F, blocks), Faces.all(Textures.SOULSAND)));
        quartz = dyed(new BlockDyed(id("quartz"), info("quartz", "Quartz Block", Material.rock, 0.8F, STONE, PICKAXE,
            2, 1.0F, blocks), Faces.of(Textures.QUARTZ_TOP, Textures.QUARTZ_SIDE, Textures.QUARTZ_BOTTOM)));
        clay = dyed(new BlockDyed(id("clay"), info("clay", "Hardened Stained Clay", Material.rock, 1.25F, STONE,
            PICKAXE, 1, 7.0F, blocks), Faces.all(Textures.CLAY)));
        coal = dyed(new BlockDyed(id("coal"), info("coal", "Coal Block", Material.rock, 5.0F, STONE, PICKAXE, 1,
            10.0F, blocks), Faces.all(Textures.COAL)));
        glowstone = dyed(new BlockDyedGlowstone(id("glowstone"), info("glowstone", "Glowstone", Material.glass, 0.3F,
            GLASS, PICKAXE, 1, 1.0F, blocks), Faces.all(Textures.GLOWSTONE)));
        lapis = dyed(new BlockDyed(id("lapis"), info("lapis", "Lapis Block", Material.rock, 3.0F, STONE, PICKAXE, 2,
            5.0F, blocks), Faces.all(Textures.LAPIS)));
        redstone = dyed(new BlockDyedPowered(id("redstone"), info("redstone", "Redstone Block", Material.iron, 5.0F,
            METAL, PICKAXE, 1, 10.0F, blocks), Faces.all(Textures.REDSTONE)));
        plank = dyed(new BlockDyed(id("plank"), info("plank", "Plank", Material.wood, 2.0F, WOOD, AXE, 0, 5.0F,
            trees), Faces.all(Textures.PLANK)));
        tulip = dyed(new BlockDyedFlower(id("tulip"), info("tulip", "Tulip", Material.plants, 0.0F, GRASS, null, 0,
            0.0F, plants), Textures.TULIP_PETALS, Textures.TULIP_STEM));
        log = dyed(new BlockDyedLog(id("log"), info("log", "Log", Material.wood, 2.0F, WOOD, AXE, 0, 1.0F, trees)));
        leaves = dyed(new BlockDyedLeaves(id("leaves"), info("leaves", "Leaves", Material.leaves, 0.2F, GRASS, null,
            0, 1.0F, trees), Faces.all(Textures.LEAF)));
        sapling = dyed(new BlockDyedSapling(id("sapling"), info("sapling", "Sapling", Material.plants, 0.0F, GRASS,
            null, 0, 0.0F, trees), Textures.SAPLING_LEAVES, Textures.SAPLING_TRUNK));
        glassClear = dyed(new BlockDyedGlass(id("glassClear"), info("glassClear", "Glass", Material.glass, 0.4F,
            GLASS, null, 0, 1.0F, blocks), Faces.all(Textures.GLASS_CLEAR), "Clear"));
        glassFoggy = dyed(new BlockDyedGlass(id("glassFoggy"), info("glassFoggy", "Glass", Material.glass, 0.4F,
            GLASS, null, 0, 1.0F, blocks), Faces.all(Textures.GLASS_FOGGY), "Foggy"));
        glassClearPane = dyed(new BlockDyedPane(id("glassClearPane"), info("glassClearPane", "Glass Pane",
            Material.glass, 0.4F, GLASS, null, 0, 1.0F, blocks), Textures.GLASS_CLEAR, Textures.GLASS_PANE, "Clear"));
        glassFoggyPane = dyed(new BlockDyedPane(id("glassFoggyPane"), info("glassFoggyPane", "Glass Pane",
            Material.glass, 0.4F, GLASS, null, 0, 1.0F, blocks), Textures.GLASS_FOGGY, Textures.GLASS_PANE, "Foggy"));
        brick = dyed(new BlockDyed(id("brick"), info("brick", "Brick", Material.rock, 2.0F, STONE, PICKAXE, 1, 1.0F,
            blocks), Faces.all(Textures.BRICK)));
        sand = dyed(new BlockDyedSand(id("sand"), info("sand", "Sand", Material.sand, 0.5F, SAND, SHOVEL, 0, 1.0F,
            blocks), Faces.all(Textures.SAND)));
        workbench = dyed(new BlockDyedWorkbench(id("workbench"), info("workbench", "Crafting Table", Material.wood,
            2.5F, WOOD, AXE, 0, 1.0F, blocks), Faces.of(Textures.WORKBENCH_TOP, Textures.WORKBENCH_SIDE,
            Textures.PLANK, Textures.WORKBENCH_FRONT)));
        chest = dyed(new BlockDyedChest(id("chest"), info("chest", "Chest", Material.wood, 2.5F, WOOD, AXE, 0, 1.0F,
            blocks)));
        hardenedClay = dyed(new BlockDyed(id("hardenedClay"), info("hardenedClay", "Hardened Clay", Material.rock,
            1.5F, STONE, PICKAXE, 1, 10.0F, blocks), Faces.all(Textures.HARDENED_CLAY)));
        sandstone = dyed(new BlockDyed(id("sandstone"), info("sandstone", "Sandstone", Material.rock, 2.0F, STONE,
            PICKAXE, 1, 6.0F, blocks), Faces.of(Textures.SANDSTONE_TOP, Textures.SANDSTONE_SIDE,
            Textures.SANDSTONE_BOTTOM)));
        bookshelf = dyed(new BlockDyedBookshelf(id("bookshelf"), info("bookshelf", "Bookshelf", Material.wood, 1.5F,
            WOOD, AXE, 0, 1.0F, trees), Faces.of(Textures.PLANK, Textures.BOOKSHELF, Textures.PLANK)));
        diorite = dyed(stoneLike("diorite", "Diorite", 1.5F, 6.0F, Textures.DIORITE));
        granite = dyed(stoneLike("granite", "Granite", 1.5F, 10.0F, Textures.GRANITE));
        andesite = dyed(stoneLike("andesite", "Andesite", 1.5F, 10.0F, Textures.ANDESITE));
        polishedGranite = dyed(stoneLike("polishedGranite", "Polished Granite", 1.5F, 10.0F,
            Textures.POLISHED_GRANITE));
        polishedDiorite = dyed(stoneLike("polishedDiorite", "Polished Diorite", 1.5F, 10.0F,
            Textures.POLISHED_DIORITE));
        polishedAndesite = dyed(stoneLike("polishedAndesite", "Polished Andesite", 1.5F, 10.0F,
            Textures.POLISHED_ANDESITE));
        concrete = dyed(stoneLike("concrete", "Concrete", 1.8F, 3.0F, Textures.CONCRETE));
        concretePowder = dyed(new BlockDyedConcretePowder(id("concretePowder"), info("concretePowder",
            "Concrete Powder", Material.sand, 0.5F, SAND, SHOVEL, 0, 1.0F, blocks),
            Faces.all(Textures.CONCRETE_POWDER)));
        soulSoil = dyed(new BlockDyed(id("soulSoil"), info("soulSoil", "Soul Soil", Material.ground, 0.5F, SAND,
            SHOVEL, 0, 1.0F, blocks), Faces.all(Textures.SOUL_SOIL)));
        basalt = dyed(new BlockDyed(id("basalt"), info("basalt", "Basalt", Material.rock, 1.25F, STONE, PICKAXE, 1,
            7.0F, blocks), Faces.of(Textures.BASALT_TOP, Textures.BASALT_SIDE, Textures.BASALT_BOTTOM)));
        polishedBasalt = dyed(new BlockDyed(id("polishedBasalt"), info("polishedBasalt", "Polished Basalt",
            Material.rock, 1.25F, STONE, PICKAXE, 1, 7.0F, blocks), Faces.of(Textures.POLISHED_BASALT_TOP,
            Textures.POLISHED_BASALT_SIDE, Textures.POLISHED_BASALT_BOTTOM)));
        smoothStone = dyed(stoneLike("smoothStone", "Smooth Stone", 2.0F, 10.0F, Textures.SMOOTH_STONE));
        smoothSandstone = dyed(stoneLike("smoothSandstone", "Smooth Sandstone", 2.0F, 6.0F,
            Textures.SMOOTH_SANDSTONE));
        smoothQuartz = dyed(stoneLike("smoothQuartz", "Smooth Quartz", 2.0F, 6.0F, Textures.SMOOTH_QUARTZ));
        cutSandstone = dyed(new BlockDyed(id("cutSandstone"), info("cutSandstone", "Cut Sandstone", Material.rock,
            0.8F, STONE, PICKAXE, 1, 1.0F, blocks), Faces.of(Textures.CUT_SANDSTONE_TOP, Textures.CUT_SANDSTONE_SIDE,
            Textures.CUT_SANDSTONE_BOTTOM)));
        mossyCobble = dyed(stoneLike("mossyCobble", "Mossy Cobblestone", 2.0F, 10.0F, Textures.MOSSY_COBBLE));
        mossyStonebrick = dyed(stoneLike("mossyStonebrick", "Mossy Stone Brick", 1.5F, 10.0F,
            Textures.MOSSY_STONEBRICK));
        netherBrick = dyed(stoneLike("netherBrick", "Nether Brick", 2.0F, 10.0F, Textures.NETHER_BRICK));
        crackedNetherBrick = dyed(stoneLike("crackedNetherBrick", "Cracked Nether Brick", 2.0F, 10.0F,
            Textures.CRACKED_NETHER_BRICK));
        chiseledNetherBrick = dyed(stoneLike("chiseledNetherBrick", "Chiseled Nether Brick", 2.0F, 10.0F,
            Textures.CHISELED_NETHER_BRICK));
        endStoneBrick = dyed(stoneLike("endStoneBrick", "End Stone Brick", 3.0F, 15.0F, Textures.END_STONE_BRICK));
        boneBlock = dyed(new BlockDyed(id("boneBlock"), info("boneBlock", "Bone Block", Material.rock, 2.0F, STONE,
            PICKAXE, 1, 3.0F, blocks), Faces.of(Textures.BONE_BLOCK_TOP, Textures.BONE_BLOCK_SIDE,
            Textures.BONE_BLOCK_BOTTOM)));
        glazedTerracotta = dyed(stoneLike("glazedTerracotta", "Glazed Terracotta", 1.4F, 7.0F,
            Textures.GLAZED_TERRACOTTA));
        netheriteBlock = dyed(new BlockDyed(id("netheriteBlock"), info("netheriteBlock", "Block of Netherite",
            Material.iron, 50.0F, METAL, PICKAXE, 3, 2000.0F, blocks), Faces.all(Textures.NETHERITE_BLOCK)));
        cryingObsidian = dyed(new BlockDyed(id("cryingObsidian"), info("cryingObsidian", "Crying Obsidian",
            Material.rock, 50.0F, STONE, PICKAXE, 3, 2000.0F, blocks), Faces.all(Textures.CRYING_OBSIDIAN))
            .setLightValue(10.0F / 15.0F));
        cornflower = dyed(new BlockDyedFlower(id("cornflower"), info("cornflower", "Cornflower", Material.plants,
            0.0F, GRASS, null, 0, 0.0F, plants), Textures.CORNFLOWER_PETALS, Textures.CORNFLOWER_STEM));
        chain = dyed(new BlockDyedChain(id("chain"), info("chain", "Chain", Material.iron, 5.0F, METAL, PICKAXE, 1,
            10.0F, blocks), Faces.all(Textures.CHAIN)));

        granitePlain = new BlockPlainStone(id("granitePlain"), info("granitePlain", "Granite", Material.rock, 1.5F,
            STONE, PICKAXE, 1, 10.0F, blocks), Textures.GRANITE, 0xC89680);
        dioritePlain = new BlockPlainStone(id("dioritePlain"), info("dioritePlain", "Diorite", Material.rock, 1.5F,
            STONE, PICKAXE, 1, 6.0F, blocks), Textures.DIORITE, 0xFFFFFF);
        andesitePlain = new BlockPlainStone(id("andesitePlain"), info("andesitePlain", "Andesite", Material.rock,
            1.5F, STONE, PICKAXE, 1, 10.0F, blocks), Textures.ANDESITE, 0xFFFFFF);

        CreativeTabs shapes = MoreDyes.tabShapes;
        stairs(stone, "stoneStairs", shapes);
        stairs(granite, "graniteStairs", shapes);
        stairs(polishedGranite, "polishedGraniteStairs", shapes);
        stairs(diorite, "dioriteStairs", shapes);
        stairs(polishedDiorite, "polishedDioriteStairs", shapes);
        stairs(andesite, "andesiteStairs", shapes);
        stairs(polishedAndesite, "polishedAndesiteStairs", shapes);
        stairs(mossyCobble, "mossyCobbleStairs", shapes);
        stairs(mossyStonebrick, "mossyStonebrickStairs", shapes);
        stairs(endStoneBrick, "endStoneBrickStairs", shapes);
        stairs(smoothSandstone, "smoothSandstoneStairs", shapes);
        stairs(smoothQuartz, "smoothQuartzStairs", shapes);
        stairs(netherBrick, "netherBrickStairs", shapes);

        slab(stone, "stoneSlab", shapes);
        slab(cutSandstone, "cutSandstoneSlab", shapes);
        slab(diorite, "dioriteSlab", shapes);

        wall(brick, "brickWall", shapes);
        wall(stonebrick, "stonebrickWall", shapes);
        wall(mossyStonebrick, "mossyStonebrickWall", shapes);
        wall(granite, "graniteWall", shapes);
        wall(diorite, "dioriteWall", shapes);
        wall(andesite, "andesiteWall", shapes);
        wall(netherBrick, "netherBrickWall", shapes);
        wall(sandstone, "sandstoneWall", shapes);
        wall(endStoneBrick, "endStoneBrickWall", shapes);

        ironTrapdoor = new BlockDyedTrapdoor(id("ironTrapdoor"), info("ironTrapdoor", "Iron Trapdoor", Material.iron,
            5.0F, METAL, PICKAXE, 1, 5.0F, shapes));
    }

    public static void register() {
        for (Block block : DYED) {
            GameRegistry.registerBlock(block, ItemBlockDyed.class, registryName(block));
        }
        GameRegistry.registerBlock(granitePlain, registryName(granitePlain));
        GameRegistry.registerBlock(dioritePlain, registryName(dioritePlain));
        GameRegistry.registerBlock(andesitePlain, registryName(andesitePlain));
        for (Shape shape : SHAPES) {
            GameRegistry.registerBlock(shape.block, shape.kind == Shape.SLAB ? ItemBlockDyedSlab.class
                : ItemBlockDyed.class, registryName(shape.block));
        }
        GameRegistry.registerBlock(ironTrapdoor, ItemBlockDyed.class, registryName(ironTrapdoor));
        GameRegistry.registerTileEntity(TileEntityDyed.class, "MoreDyesColor");
        GameRegistry.registerTileEntity(TileEntityDyedChest.class, "MoreDyesChest");

        // Same fire settings as the vanilla blocks (encouragement, flammability).
        Block.setBurnProperties(wool.blockID, 30, 60);
        Block.setBurnProperties(plank.blockID, 5, 20);
        Block.setBurnProperties(log.blockID, 5, 5);
        Block.setBurnProperties(leaves.blockID, 30, 60);
        Block.setBurnProperties(bookshelf.blockID, 30, 20);
        Block.setBurnProperties(tulip.blockID, 60, 100);
        Block.setBurnProperties(cornflower.blockID, 60, 100);
        Block.setBurnProperties(coal.blockID, 5, 5);
    }

    /** The registry name of a block named "moredyes.name" is "name". */
    private static String registryName(Block block) {
        return block.getBlockName().substring("tile.moredyes.".length());
    }

    private static int id(String name) {
        return config.getBlock(name, nextId++).getInt();
    }

    private static BlockInfo info(String name, String displayName, Material material, float hardness,
        StepSound sound, String tool, int level, float resistance, CreativeTabs tab) {
        return new BlockInfo(name, displayName, material, hardness, sound, tool, level, resistance).tab(tab);
    }

    /** A dyed stone-like block mined with a pickaxe, with one texture on every side. */
    private static Block stoneLike(String name, String displayName, float hardness, float resistance, int texture) {
        return new BlockDyed(id(name), info(name, displayName, Material.rock, hardness, STONE, PICKAXE, 1, resistance,
            MoreDyes.tabBlocks), Faces.all(texture));
    }

    private static Block dyed(Block block) {
        DYED.add(block);
        return block;
    }

    private static void stairs(Block base, String name, CreativeTabs tab) {
        SHAPES.add(new Shape(new BlockDyedStairs(id(name), (BlockDyed) base, name, tab), base, Shape.STAIRS));
    }

    private static void slab(Block base, String name, CreativeTabs tab) {
        SHAPES.add(new Shape(new BlockDyedSlab(id(name), (BlockDyed) base, name, tab), base, Shape.SLAB));
    }

    private static void wall(Block base, String name, CreativeTabs tab) {
        SHAPES.add(new Shape(new BlockDyedWall(id(name), (BlockDyed) base, name, tab), base, Shape.WALL));
    }
}
