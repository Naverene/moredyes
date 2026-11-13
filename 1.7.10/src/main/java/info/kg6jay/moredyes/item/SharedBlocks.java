package info.kg6jay.moredyes.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.ColorIndex;
import info.kg6jay.moredyes.world.feature.MisgeneratedTrees;

/**
 * Every dyed block's item is one item, MDItemSharedBlock, so storage that keeps one item id per slot (such as the
 * Extra Utilities Filing Cabinet) holds all of them. The item damage says which block and which color:
 * {@code kind * COLORS + color}, where kind is the block's place in KINDS and color is its ColorIndex number.
 * <p>
 * The blocks still have their own block ids and their own ItemBlocks (the "old" items), which the shared item hands
 * placing, names and icons to. The old items are hidden, and turn into the shared item wherever they turn up (see
 * SharedBlockHandler and the old items' onUpdate), so worlds from before keep working.
 * <p>
 * The damage values are saved in worlds, so KINDS only ever grows at the end. One shared item fits KINDS_PER_ITEM
 * kinds; past that a second one ("dyedBlock1") is added without changing the damage of the first.
 */
public final class SharedBlocks {

    /** The number of colors; the color part of the damage is a ColorIndex number. */
    public static final int COLORS = 118;
    /** How many kinds fit in one item: damage values go up to 32767 (OreDictionary.WILDCARD_VALUE is reserved). */
    public static final int KINDS_PER_ITEM = 32766 / COLORS;

    /**
     * The blocks, in a fixed order. A name with a capital letter is a block with one block per color set (registered
     * as "white" + "Mix" + name and so on, color in the metadata); the others are one block for every color, with the
     * color in a tile entity and the item damage. Append new blocks at the end; never reorder or remove.
     */
    private static final String[] KINDS = { "Wool", "Carpet", "Stonebrick", "StonebrickCarved", "StonebrickCracked",
        "Stone", "Cobble", "Obsidian", "Soulsand", "Quartz", "Clay", "Coal", "Glowstone", "Lapis", "Redstone", "Plank",
        "Tulip", "Log", "Leaf", "Sapling", "GlassClear", "GlassFoggy", "GlassClearPane", "GlassFoggyPane", "Brick",
        "Sand", "Workbench", "HardenedClay", "Sandstone", "Bookshelf", "Diorite", "Chest", "Granite", "Andesite",
        "PolishedGranite", "SpeckledGranite", "SmoothSpeckledGranite", "PolishedDiorite", "PolishedAndesite",
        "Concrete", "ConcretePowder", "SoulSoil", "Basalt", "PolishedBasalt", "SmoothStone", "SmoothSandstone",
        "SmoothQuartz", "CutSandstone", "MossyCobble", "MossyStonebrick", "NetherBrick", "CrackedNetherBrick",
        "ChiseledNetherBrick", "EndStoneBrick", "BoneBlock", "GlazedTerracotta", "NetheriteBlock", "CryingObsidian",
        "Cornflower", "Chain", "Lamp", "InvertedLamp", "SolidLamp", "InvertedSolidLamp", "EndStone", "Gravel", "Ice",
        "PackedIce", "Snow", "QuartzBricks", "ChiseledQuartz", "QuartzPillar", "ChiseledSandstone", "SprucePlank",
        "BirchPlank", "JunglePlank", "AcaciaPlank", "DarkOakPlank", "Allium", "AzureBluet", "BlueOrchid", "Dandelion",
        "LilyOfTheValley", "OxeyeDaisy", "Poppy", "Rockwool",
        // One block for every color
        "ironTrapdoor", "piston", "stickyPiston", "lilac", "peony", "roseBush", "stoneStairs", "graniteStairs",
        "polishedGraniteStairs", "speckledGraniteStairs", "smoothSpeckledGraniteStairs", "dioriteStairs",
        "polishedDioriteStairs", "andesiteStairs", "polishedAndesiteStairs", "mossyCobbleStairs",
        "mossyStonebrickStairs", "endStoneBrickStairs", "smoothSandstoneStairs", "smoothQuartzStairs",
        "netherBrickStairs", "stoneSlab", "cutSandstoneSlab", "dioriteSlab", "brickWall", "stonebrickWall",
        "mossyStonebrickWall", "graniteWall", "dioriteWall", "andesiteWall", "netherBrickWall", "sandstoneWall",
        "endStoneBrickWall", "cobbleStairs", "stonebrickStairs", "stonebrickCrackedStairs", "stonebrickCarvedStairs",
        "brickStairs", "clayStairs", "coalStairs", "lapisStairs", "redstoneStairs", "quartzStairs", "quartzBrickStairs",
        "chiseledQuartzStairs", "quartzPillarStairs", "obsidianStairs", "glowstoneStairs", "soulsandStairs",
        "sandStairs", "sandstoneStairs", "chiseledSandstoneStairs", "cutSandstoneStairs", "endStoneStairs", "iceStairs",
        "packedIceStairs", "snowStairs", "boneBlockStairs", "glassStairs", "glassFoggyStairs", "woolStairs",
        "concreteStairs", "concretePowderStairs", "oakStairs", "spruceStairs", "birchStairs", "jungleStairs",
        "acaciaStairs", "darkOakStairs", "cobbleSlab", "mossyCobbleSlab", "stonebrickSlab", "stonebrickCrackedSlab",
        "stonebrickCarvedSlab", "mossyStonebrickSlab", "brickSlab", "claySlab", "coalSlab", "lapisSlab", "redstoneSlab",
        "quartzSlab", "quartzBrickSlab", "chiseledQuartzSlab", "quartzPillarSlab", "smoothQuartzSlab", "obsidianSlab",
        "glowstoneSlab", "soulsandSlab", "sandSlab", "sandstoneSlab", "chiseledSandstoneSlab", "graniteSlab",
        "andesiteSlab", "polishedGraniteSlab", "speckledGraniteSlab", "smoothSpeckledGraniteSlab",
        "polishedDioriteSlab", "polishedAndesiteSlab", "endStoneSlab", "iceSlab", "packedIceSlab", "snowSlab",
        "boneBlockSlab", "glassSlab", "glassFoggySlab", "woolSlab", "concreteSlab", "concretePowderSlab", "oakSlab",
        "spruceSlab", "birchSlab", "jungleSlab", "acaciaSlab", "darkOakSlab", "workbenchSlab", "cobbleWall",
        "mossyCobbleWall", "endStoneWall", "oakFence", "spruceFence", "birchFence", "jungleFence", "acaciaFence",
        "darkOakFence" };

    /** The shared items, KINDS_PER_ITEM kinds each. */
    public static Item[] items;

    /** Per kind: the old item of each color set (one block per set), or a single old item in [0] (one block). */
    private static Item[][] oldItems;
    private static boolean[] perSet;
    /** Old item to {kind, set} (set is -1 for the one-block kinds). */
    private static final Map<Item, int[]> OLD = new HashMap<>();

    private SharedBlocks() {}

    /** Creates and registers the shared items. Call after every dyed block is registered. */
    public static void register() {
        if (ColorIndex.count() != COLORS) {
            throw new IllegalStateException("SharedBlocks.COLORS must be " + ColorIndex.count());
        }
        oldItems = new Item[KINDS.length][];
        perSet = new boolean[KINDS.length];
        for (int kind = 0; kind < KINDS.length; kind++) {
            String name = KINDS[kind];
            perSet[kind] = Character.isUpperCase(name.charAt(0));
            if (perSet[kind]) {
                oldItems[kind] = new Item[MDBlock.colors.length];
                for (int set = 0; set < MDBlock.colors.length; set++) {
                    Item item = GameRegistry.findItem(Reference.MOD_ID, MDBlock.colors[set] + "Mix" + name);
                    oldItems[kind][set] = item;
                    if (item != null) {
                        OLD.put(item, new int[] { kind, set });
                    }
                }
            } else {
                Item item = GameRegistry.findItem(Reference.MOD_ID, name);
                oldItems[kind] = new Item[] { item };
                if (item != null) {
                    OLD.put(item, new int[] { kind, -1 });
                }
            }
        }
        checkEveryBlockIsListed();

        items = new Item[(KINDS.length + KINDS_PER_ITEM - 1) / KINDS_PER_ITEM];
        for (int i = 0; i < items.length; i++) {
            items[i] = new MDItemSharedBlock(i);
            GameRegistry.registerItem(items[i], i == 0 ? "dyedBlock" : "dyedBlock" + i);
        }
    }

    /** Stops the game in development when a new dyed block was not added to KINDS. */
    private static void checkEveryBlockIsListed() {
        for (Object o : Item.itemRegistry) {
            if (o instanceof MDItemBlockColored || o instanceof MDItemBlockTileColored) {
                if (!OLD.containsKey(o)) {
                    throw new IllegalStateException(
                        "Add " + Item.itemRegistry.getNameForObject(o) + " to SharedBlocks.KINDS");
                }
            }
        }
    }

    /** True for an item of a dyed block that the shared item replaces. */
    public static boolean isOld(Item item) {
        return item != null && OLD.containsKey(item);
    }

    public static boolean isShared(Item item) {
        return item instanceof MDItemSharedBlock;
    }

    /** The shared stack for a dyed block and its item damage (the shade, or the color number); null if none. */
    public static ItemStack of(Block block, int damage, int size) {
        Item item = Item.getItemFromBlock(block);
        return item == null ? null : toShared(new ItemStack(item, size, damage));
    }

    /** Like of, but falls back to the block's own item, so callers always get a stack. */
    public static ItemStack stack(Block block, int damage, int size) {
        ItemStack shared = of(block, damage, size);
        return shared != null ? shared : new ItemStack(block, size, damage);
    }

    /**
     * The shared stack for an old item's stack, keeping size and NBT; null if the stack is not an old item, or has a
     * shade its block does not have. A misgenerated tree's item (see MisgeneratedTrees) is fixed first.
     */
    public static ItemStack toShared(ItemStack stack) {
        if (stack == null || !isOld(stack.getItem())) {
            return null;
        }
        ItemStack fixed = MisgeneratedTrees.fix(stack);
        if (fixed != null) {
            stack = fixed;
        }
        int[] where = OLD.get(stack.getItem());
        if (where == null) {
            return null;
        }
        int kind = where[0], set = where[1], damage = stack.getItemDamage(), color;
        if (set >= 0) {
            if (damage < 0 || damage >= MDBlock.colorStrings[set].length) {
                return null;
            }
            color = ColorIndex.of(set, damage);
        } else {
            if (damage < 0 || damage >= COLORS) {
                return null;
            }
            color = damage;
        }
        ItemStack shared = new ItemStack(
            items[kind / KINDS_PER_ITEM],
            stack.stackSize,
            kind % KINDS_PER_ITEM * COLORS + color);
        if (stack.hasTagCompound()) {
            shared.setTagCompound(
                (net.minecraft.nbt.NBTTagCompound) stack.getTagCompound()
                    .copy());
        }
        return shared;
    }

    /**
     * The old item's stack a shared stack stands for (same size, NBT shared, not copied), so that code made for the
     * blocks' own items works on it. Any other stack is returned as it is; null for an unknown damage.
     */
    public static ItemStack toOld(ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof MDItemSharedBlock shared)) {
            return stack;
        }
        int damage = stack.getItemDamage();
        int kind = shared.index * KINDS_PER_ITEM + damage / COLORS, color = damage % COLORS;
        if (damage < 0 || kind >= KINDS.length) {
            return null;
        }
        Item item;
        int oldDamage;
        if (perSet[kind]) {
            item = oldItems[kind][ColorIndex.set(color)];
            oldDamage = ColorIndex.shade(color);
        } else {
            item = oldItems[kind][0];
            oldDamage = color;
        }
        if (item == null) {
            return null;
        }
        ItemStack old = new ItemStack(item, stack.stackSize, oldDamage);
        old.setTagCompound(stack.getTagCompound());
        return old;
    }

    /** Like toOld, but returns the stack itself for an unknown damage. */
    public static ItemStack unwrap(ItemStack stack) {
        ItemStack old = toOld(stack);
        return old != null ? old : stack;
    }

    /** The block of a stack, looking through the shared item. */
    public static Block blockOf(ItemStack stack) {
        ItemStack old = unwrap(stack);
        return old == null ? null : Block.getBlockFromItem(old.getItem());
    }

    /** The old items of every kind, in KINDS order, for listing the shared item's variants. */
    static List<ItemBlock> oldItemsInOrder(int sharedIndex) {
        List<ItemBlock> list = new ArrayList<>();
        int from = sharedIndex * KINDS_PER_ITEM, to = Math.min(KINDS.length, from + KINDS_PER_ITEM);
        for (int kind = from; kind < to; kind++) {
            for (Item item : oldItems[kind]) {
                if (item instanceof ItemBlock block) {
                    list.add(block);
                }
            }
        }
        return list;
    }

    /** Turns old items in a list into shared ones, in place. */
    public static <T extends List<ItemStack>> T convertAll(T stacks) {
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack shared = toShared(stacks.get(i));
            if (shared != null) {
                stacks.set(i, shared);
            }
        }
        return stacks;
    }

    /**
     * NEI lists items it finds no variants of by trying damage values, which would show the old items next to the
     * shared one, so they are hidden there. NEI is optional, so it is called by name.
     */
    public static void hideOldItemsInNEI() {
        if (!cpw.mods.fml.common.Loader.isModLoaded("NotEnoughItems")) {
            return;
        }
        try {
            java.lang.reflect.Method hide = Class.forName("codechicken.nei.api.API")
                .getMethod("hideItem", ItemStack.class);
            for (Item item : OLD.keySet()) {
                hide.invoke(null, new ItemStack(item, 1, net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE));
            }
        } catch (ReflectiveOperationException e) {
            cpw.mods.fml.common.FMLLog.warning("More Dyes could not hide the old block items in NEI: %s", e);
        }
    }
}
