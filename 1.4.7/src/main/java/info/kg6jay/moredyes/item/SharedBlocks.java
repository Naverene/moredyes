package info.kg6jay.moredyes.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

import cpw.mods.fml.common.registry.LanguageRegistry;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.block.MDBlocks;

/**
 * One item ID for every dyed block, so storage that holds a single item ID (such as Extra Utilities' filing cabinet in
 * later versions) holds them all. The shared item's damage is kind * Colors.COUNT + color, where the kind is the
 * block's place in KINDS. The blocks in the world do not change: each still has its own block ID and its own item
 * (the "old" item), which places, names and draws it; the shared item hands everything to it.
 * <p>
 * The old items are hidden from the creative menu and turn into the shared item in a player's inventory, so items from
 * before keep working. Signs keep their own item, since they stack to 16.
 */
public final class SharedBlocks {

    public static final int COLORS = Colors.COUNT;

    /**
     * The block registry names, in the order their damage values are given out. Only ever add to the end: a kind's
     * place is saved in every item stack of it. Startup fails if a dyed block is missing here.
     */
    private static final String[] KINDS = {
        "wool", "stonebrick", "stonebrickCarved", "stonebrickCracked", "stone", "cobble", "obsidian",
        "soulsand", "quartz", "clay", "coal", "glowstone", "lapis", "redstone", "plank", "tulip", "log",
        "leaves", "sapling", "glassClear", "glassFoggy", "glassClearPane", "glassFoggyPane", "brick", "sand",
        "workbench", "chest", "hardenedClay", "sandstone", "bookshelf", "diorite", "granite", "andesite",
        "polishedGranite", "polishedDiorite", "polishedAndesite", "concrete", "concretePowder", "soulSoil",
        "basalt", "polishedBasalt", "smoothStone", "smoothSandstone", "smoothQuartz", "cutSandstone",
        "mossyCobble", "mossyStonebrick", "netherBrick", "crackedNetherBrick", "chiseledNetherBrick",
        "endStoneBrick", "boneBlock", "glazedTerracotta", "netheriteBlock", "cryingObsidian", "cornflower",
        "chain", "endStone", "quartzBricks", "chiseledQuartz", "quartzPillar", "gravel", "ice", "packedIce",
        "snow", "chiseledSandstone", "birchPlank", "sprucePlank", "junglePlank", "acaciaPlank", "darkOakPlank",
        "allium", "azureBluet", "blueOrchid", "dandelion", "lilyOfTheValley", "oxeyeDaisy", "poppy", "lilac",
        "peony", "roseBush", "speckledGranite", "smoothSpeckledGranite", "lamp", "invertedLamp", "solidLamp",
        "invertedSolidLamp", "stoneStairs", "graniteStairs", "polishedGraniteStairs", "dioriteStairs",
        "polishedDioriteStairs", "andesiteStairs", "polishedAndesiteStairs", "mossyCobbleStairs",
        "mossyStonebrickStairs", "endStoneBrickStairs", "smoothSandstoneStairs", "smoothQuartzStairs",
        "netherBrickStairs", "stoneSlab", "cutSandstoneSlab", "dioriteSlab", "brickWall", "stonebrickWall",
        "mossyStonebrickWall", "graniteWall", "dioriteWall", "andesiteWall", "netherBrickWall", "sandstoneWall",
        "endStoneBrickWall", "cobbleStairs", "cobbleSlab", "mossyCobbleSlab", "stonebrickStairs",
        "stonebrickSlab", "stonebrickCrackedStairs", "stonebrickCrackedSlab", "stonebrickCarvedStairs",
        "stonebrickCarvedSlab", "mossyStonebrickSlab", "brickStairs", "brickSlab", "clayStairs", "claySlab",
        "coalStairs", "coalSlab", "lapisStairs", "lapisSlab", "redstoneStairs", "redstoneSlab", "quartzStairs",
        "quartzSlab", "quartzBricksStairs", "quartzBricksSlab", "chiseledQuartzStairs", "chiseledQuartzSlab",
        "quartzPillarStairs", "quartzPillarSlab", "smoothQuartzSlab", "obsidianStairs", "obsidianSlab",
        "glowstoneStairs", "glowstoneSlab", "soulsandStairs", "soulsandSlab", "sandStairs", "sandSlab",
        "sandstoneStairs", "sandstoneSlab", "chiseledSandstoneStairs", "chiseledSandstoneSlab",
        "cutSandstoneStairs", "graniteSlab", "andesiteSlab", "polishedGraniteSlab", "polishedDioriteSlab",
        "polishedAndesiteSlab", "endStoneStairs", "endStoneSlab", "iceStairs", "iceSlab", "packedIceStairs",
        "packedIceSlab", "snowStairs", "snowSlab", "boneBlockStairs", "boneBlockSlab", "glassClearStairs",
        "glassClearSlab", "glassFoggyStairs", "glassFoggySlab", "woolStairs", "woolSlab", "concreteStairs",
        "concreteSlab", "concretePowderStairs", "concretePowderSlab", "plankStairs", "plankSlab", "plankFence",
        "birchPlankStairs", "birchPlankSlab", "birchPlankFence", "sprucePlankStairs", "sprucePlankSlab",
        "sprucePlankFence", "junglePlankStairs", "junglePlankSlab", "junglePlankFence", "acaciaPlankStairs",
        "acaciaPlankSlab", "acaciaPlankFence", "darkOakPlankStairs", "darkOakPlankSlab", "darkOakPlankFence",
        "workbenchSlab", "cobbleWall", "mossyCobbleWall", "endStoneWall", "speckledGraniteStairs",
        "speckledGraniteSlab", "smoothSpeckledGraniteStairs", "smoothSpeckledGraniteSlab", "ironTrapdoor", };

    /** Dyed blocks that keep their own item. */
    private static final String[] NOT_SHARED = { "sign", "wallSign", "flowerPot" };

    public static Item item;

    private static Block[] blocks;
    private static final Map<Integer, Integer> KIND_OF_BLOCK = new HashMap<Integer, Integer>();

    private SharedBlocks() {}

    /** Makes the shared item. Call after the blocks are made, with the config the item IDs are in. */
    public static void init(Configuration config) {
        Map<String, Block> byName = new HashMap<String, Block>();
        for (Block block : dyedBlocks()) {
            byName.put(MDBlocks.registryName(block), block);
        }
        blocks = new Block[KINDS.length];
        for (int kind = 0; kind < KINDS.length; kind++) {
            Block block = byName.remove(KINDS[kind]);
            blocks[kind] = block;
            if (block != null) {
                KIND_OF_BLOCK.put(block.blockID, kind);
            }
        }
        for (String name : NOT_SHARED) {
            byName.remove(name);
        }
        if (!byName.isEmpty()) {
            List<String> missing = new ArrayList<String>();
            for (Block block : dyedBlocks()) {
                if (byName.containsValue(block)) {
                    missing.add('"' + MDBlocks.registryName(block) + '"');
                }
            }
            throw new IllegalStateException("Dyed blocks missing from SharedBlocks.KINDS: " + missing);
        }
        if (KINDS.length * COLORS > Short.MAX_VALUE) {
            throw new IllegalStateException("Too many kinds for one shared item");
        }
        item = new ItemSharedBlock(config.getItem("dyedBlock", 25601).getInt());
        LanguageRegistry.addName(item, "Dyed Block");
    }

    /** Every dyed block, in the order they are made. */
    private static List<Block> dyedBlocks() {
        List<Block> list = new ArrayList<Block>(MDBlocks.DYED);
        for (MDBlocks.Shape shape : MDBlocks.SHAPES) {
            list.add(shape.block);
        }
        list.add(MDBlocks.ironTrapdoor);
        list.add(MDBlocks.sign);
        list.add(MDBlocks.wallSign);
        list.add(MDBlocks.flowerPot);
        return list;
    }

    /** The kinds that are present, for the creative menu. */
    static Block[] blocks() {
        return blocks;
    }

    /** A stack of this dyed block in this color: the shared item, or the block's own item if it keeps it. */
    public static ItemStack stack(Block block, int count, int color) {
        Integer kind = block == null ? null : KIND_OF_BLOCK.get(block.blockID);
        if (kind == null || item == null) {
            return new ItemStack(block, count, color);
        }
        return new ItemStack(item, count, kind * COLORS + Colors.clamp(color));
    }

    public static boolean isShared(ItemStack stack) {
        return stack != null && item != null && stack.itemID == item.itemID;
    }

    /** True if this is the old item of a block that has a shared one. */
    public static boolean isOld(ItemStack stack) {
        return stack != null && KIND_OF_BLOCK.containsKey(stack.itemID);
    }

    /** The block of a shared stack, null if the stack is not one. */
    public static Block sharedBlock(ItemStack stack) {
        if (!isShared(stack)) {
            return null;
        }
        int kind = stack.getItemDamage() / COLORS;
        return kind >= 0 && kind < blocks.length ? blocks[kind] : null;
    }

    /** The color of a shared stack. */
    public static int sharedColor(ItemStack stack) {
        return Colors.clamp(stack.getItemDamage() % COLORS);
    }

    /** The block of any block item, shared or not; null for other items. */
    public static Block blockOf(ItemStack stack) {
        if (stack == null) {
            return null;
        }
        if (isShared(stack)) {
            return sharedBlock(stack);
        }
        return stack.itemID < Block.blocksList.length ? Block.blocksList[stack.itemID] : null;
    }

    /** The old item stack a shared stack stands for (a new stack), or null if it stands for none. */
    public static ItemStack toOld(ItemStack stack) {
        Block block = sharedBlock(stack);
        if (block == null) {
            return null;
        }
        ItemStack old = new ItemStack(block.blockID, stack.stackSize, sharedColor(stack));
        old.setTagCompound(stack.getTagCompound());
        return old;
    }

    /** Turns an old item stack into the shared item, in place. */
    public static void convert(ItemStack stack) {
        if (isOld(stack) && item != null) {
            int kind = KIND_OF_BLOCK.get(stack.itemID);
            int color = Colors.clamp(stack.getItemDamage());
            stack.itemID = item.itemID;
            stack.setItemDamage(kind * COLORS + color);
        }
    }
}
