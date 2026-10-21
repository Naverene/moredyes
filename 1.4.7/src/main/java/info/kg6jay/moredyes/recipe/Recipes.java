package info.kg6jay.moredyes.recipe;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.block.MDBlocks;
import info.kg6jay.moredyes.item.MDItems;

/**
 * The crafting and smelting recipes, the same as in the 1.7.10 version where Minecraft 1.4.7 has the ingredients.
 * Blocks that 1.4.7 does not have yet are replaced: quartz blocks by snow blocks, stained clay by clay blocks, and
 * the coal and redstone blocks by eight coal or redstone dust around a dye (which craft back into eight). The newer
 * blocks with no vanilla 1.4.7 block to dye are made from other dyed blocks or from the nearest vanilla plants (see
 * addNewerBlockRecipes).
 */
public final class Recipes {

    /** Matches any damage value, as OreDictionary.WILDCARD_VALUE does in later versions. */
    private static final int ANY = -1;

    private Recipes() {}

    public static void addCrafting() {
        addDyeMixes();
        for (int color = 0; color < Colors.COUNT; color++) {
            addColorRecipes(color);
        }
        addShapeRecipes();
        addPlainStoneRecipes();
    }

    /** Each More Dyes color is mixed from two vanilla dyes (two of each when the pair already makes a vanilla dye). */
    private static void addDyeMixes() {
        for (int[] mix : Colors.MIXES) {
            int color = Colors.of(mix[0], mix[1]);
            ItemStack a = new ItemStack(Item.dyePowder, 1, mix[2]);
            ItemStack b = new ItemStack(Item.dyePowder, 1, mix[3]);
            if (mix[4] != 0) {
                GameRegistry.addShapelessRecipe(new ItemStack(MDItems.dye, 4, color), a, a, b, b);
            } else {
                GameRegistry.addShapelessRecipe(new ItemStack(MDItems.dye, 2, color), a, b);
            }
        }
    }

    private static void addColorRecipes(int color) {
        ItemStack dye = new ItemStack(MDItems.dye, 1, color);

        // Dyeing a block of wool, and getting dyes back from flowers.
        GameRegistry.addShapelessRecipe(stack(MDBlocks.wool, 1, color), dye, new ItemStack(Block.cloth, 1, ANY));
        GameRegistry.addShapelessRecipe(dye.copy(), stack(MDBlocks.tulip, 1, color));
        GameRegistry.addShapelessRecipe(dye.copy(), stack(MDBlocks.cornflower, 1, color));

        // Eight vanilla blocks around a dye make eight dyed blocks.
        surround(MDBlocks.stonebrick, color, new ItemStack(Block.stoneBrick, 1, 0), dye);
        surround(MDBlocks.mossyStonebrick, color, new ItemStack(Block.stoneBrick, 1, 1), dye);
        surround(MDBlocks.stonebrickCracked, color, new ItemStack(Block.stoneBrick, 1, 2), dye);
        surround(MDBlocks.stonebrickCarved, color, new ItemStack(Block.stoneBrick, 1, 3), dye);
        surround(MDBlocks.stone, color, new ItemStack(Block.stone), dye);
        surround(MDBlocks.cobble, color, new ItemStack(Block.cobblestone), dye);
        surround(MDBlocks.mossyCobble, color, new ItemStack(Block.cobblestoneMossy), dye);
        surround(MDBlocks.lapis, color, new ItemStack(Block.blockLapis), dye);
        surround(MDBlocks.glowstone, color, new ItemStack(Block.glowStone), dye);
        surround(MDBlocks.obsidian, color, new ItemStack(Block.obsidian), dye);
        surround(MDBlocks.soulsand, color, new ItemStack(Block.slowSand), dye);
        surround(MDBlocks.quartz, color, new ItemStack(Block.blockSnow), dye);
        surround(MDBlocks.clay, color, new ItemStack(Block.blockClay), dye);
        surround(MDBlocks.glassClear, color, new ItemStack(Block.glass), dye);
        surround(MDBlocks.glassClearPane, color, new ItemStack(Block.thinGlass), dye);
        surround(MDBlocks.sand, color, new ItemStack(Block.sand), dye);
        surround(MDBlocks.brick, color, new ItemStack(Block.brick), dye);
        surround(MDBlocks.sandstone, color, new ItemStack(Block.sandStone, 1, 0), dye);
        surround(MDBlocks.cutSandstone, color, new ItemStack(Block.sandStone, 1, 2), dye);
        surround(MDBlocks.bookshelf, color, new ItemStack(Block.bookShelf), dye);
        surround(MDBlocks.netherBrick, color, new ItemStack(Block.netherBrick), dye);
        // End stone bricks come from dyed end stone, as in vanilla (see addNewerBlockRecipes).
        surround(MDBlocks.granite, color, new ItemStack(MDBlocks.granitePlain), dye);
        surround(MDBlocks.diorite, color, new ItemStack(MDBlocks.dioritePlain), dye);
        surround(MDBlocks.andesite, color, new ItemStack(MDBlocks.andesitePlain), dye);
        GameRegistry.addRecipe(new ShapedOreRecipe(stack(MDBlocks.plank, 8, color), "SSS", "SDS", "SSS", 'S',
            "plankWood", 'D', dye));

        // Coal and redstone blocks do not exist yet: eight coal or redstone dust around a dye, and back.
        surround(MDBlocks.coal, color, new ItemStack(Item.coal, 1, 0), dye, 1);
        GameRegistry.addShapelessRecipe(new ItemStack(Item.coal, 8, 0), stack(MDBlocks.coal, 1, color));
        surround(MDBlocks.redstone, color, new ItemStack(Item.redstone), dye, 1);
        GameRegistry.addShapelessRecipe(new ItemStack(Item.redstone, 8), stack(MDBlocks.redstone, 1, color));
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, 9, 4), stack(MDBlocks.lapis, 1, color));

        // Same shapes as the vanilla recipes.
        square(MDBlocks.stonebrick, MDBlocks.stone, color);
        square(MDBlocks.polishedGranite, MDBlocks.granite, color);
        // Speckled granite: eight dyed granite around a flint. It smelts into smooth speckled granite.
        GameRegistry.addRecipe(stack(MDBlocks.speckledGranite, 8, color), "GGG", "GFG", "GGG", 'G',
            stack(MDBlocks.granite, 1, color), 'F', Item.flint);
        square(MDBlocks.polishedDiorite, MDBlocks.diorite, color);
        square(MDBlocks.polishedAndesite, MDBlocks.andesite, color);
        square(MDBlocks.polishedBasalt, MDBlocks.basalt, color);
        square(MDBlocks.cutSandstone, MDBlocks.sandstone, color);
        GameRegistry.addShapelessRecipe(stack(MDBlocks.plank, 4, color), stack(MDBlocks.log, 1, color));
        GameRegistry.addShapelessRecipe(stack(MDBlocks.sapling, 1, color), dye, new ItemStack(Block.sapling, 1, ANY));
        GameRegistry.addRecipe(stack(MDBlocks.glassClearPane, 16, color), "GGG", "GGG", 'G',
            stack(MDBlocks.glassClear, 1, color));
        GameRegistry.addRecipe(stack(MDBlocks.glassFoggyPane, 16, color), "GGG", "GGG", 'G',
            stack(MDBlocks.glassFoggy, 1, color));

        // Dyed planks count as "plankWood", so the vanilla chest and crafting table recipes would take them too.
        // These go first, so planks of one color make a chest or crafting table of that color.
        ItemStack plank = stack(MDBlocks.plank, 1, color);
        addFirst(new ShapedOreRecipe(stack(MDBlocks.chest, 1, color), "PPP", "P P", "PPP", 'P', plank));
        addFirst(new ShapedOreRecipe(stack(MDBlocks.workbench, 1, color), "PP", "PP", 'P', plank));
        GameRegistry.addShapelessRecipe(stack(MDBlocks.chest, 1, color), Block.chest, dye);
        GameRegistry.addShapelessRecipe(stack(MDBlocks.workbench, 1, color), Block.workbench, dye);

        // The blocks from newer Minecraft versions.
        GameRegistry.addShapelessRecipe(stack(MDBlocks.concretePowder, 8, color), dye, Block.sand, Block.sand,
            Block.sand, Block.sand, Block.gravel, Block.gravel, Block.gravel, Block.gravel);
        GameRegistry.addShapelessRecipe(stack(MDBlocks.soulSoil, 2, color), stack(MDBlocks.soulsand, 1, color),
            Block.dirt);
        GameRegistry.addRecipe(stack(MDBlocks.chiseledNetherBrick, 1, color), "N", "N", 'N',
            stack(MDBlocks.netherBrick, 1, color));
        GameRegistry.addRecipe(stack(MDBlocks.boneBlock, 1, color), "BBB", "BDB", "BBB", 'B',
            new ItemStack(Item.dyePowder, 1, 15), 'D', dye);
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, 8, 15), stack(MDBlocks.boneBlock, 1, color));
        GameRegistry.addRecipe(stack(MDBlocks.cryingObsidian, 8, color), "OOO", "OTO", "OOO", 'O',
            stack(MDBlocks.obsidian, 1, color), 'T', Item.ghastTear);
        GameRegistry.addRecipe(stack(MDBlocks.netheriteBlock, 4, color), "OGO", "GDG", "OGO", 'O', Block.obsidian,
            'G', Item.ingotGold, 'D', dye);
        GameRegistry.addShapelessRecipe(stack(MDBlocks.ironTrapdoor, 1, color), Item.ingotIron, Item.ingotIron,
            Item.ingotIron, Item.ingotIron, dye);
        GameRegistry.addRecipe(stack(MDBlocks.chain, 4, color), "I", "D", "I", 'I', Item.ingotIron, 'D', dye);

        addNewerBlockRecipes(color, dye);
    }

    /**
     * The blocks the modern versions have that this one lacked. Where Minecraft 1.4.7 has the vanilla block, eight of
     * it around a dye make eight dyed ones; otherwise the dyed block comes from other dyed blocks, the way vanilla
     * makes it (quartz bricks from quartz blocks), or from the nearest vanilla plants.
     */
    private static void addNewerBlockRecipes(int color, ItemStack dye) {
        surround(MDBlocks.endStone, color, new ItemStack(Block.whiteStone), dye);
        square(MDBlocks.endStoneBrick, MDBlocks.endStone, color);
        surround(MDBlocks.gravel, color, new ItemStack(Block.gravel), dye);
        surround(MDBlocks.ice, color, new ItemStack(Block.ice), dye);
        GameRegistry.addRecipe(stack(MDBlocks.packedIce, 1, color), "III", "III", "III", 'I',
            stack(MDBlocks.ice, 1, color));
        // Eight snowballs make two snow blocks, so a dye in them makes two dyed ones. Eight snow blocks around a dye
        // already make the dyed quartz blocks.
        surround(MDBlocks.snow, color, new ItemStack(Item.snowball), dye, 2);
        surround(MDBlocks.chiseledSandstone, color, new ItemStack(Block.sandStone, 1, 1), dye);
        GameRegistry.addRecipe(stack(MDBlocks.chiseledSandstone, 1, color), "S", "S", 'S',
            stack(MDBlocks.sandstoneSlab, 1, color));
        square(MDBlocks.quartzBricks, MDBlocks.quartz, color);
        GameRegistry.addRecipe(stack(MDBlocks.quartzPillar, 2, color), "Q", "Q", 'Q', stack(MDBlocks.quartz, 1, color));
        GameRegistry.addRecipe(stack(MDBlocks.chiseledQuartz, 1, color), "S", "S", 'S',
            stack(MDBlocks.quartzSlab, 1, color));

        // Planks of the other woods. These go first, or the dyed oak plank recipe, which takes any planks, would win.
        // Acacia and dark oak do not exist yet: two dyed oak planks with two dyed jungle or spruce planks make them.
        addFirst(new ShapedOreRecipe(stack(MDBlocks.sprucePlank, 8, color), "SSS", "SDS", "SSS", 'S',
            new ItemStack(Block.planks, 1, 1), 'D', dye));
        addFirst(new ShapedOreRecipe(stack(MDBlocks.birchPlank, 8, color), "SSS", "SDS", "SSS", 'S',
            new ItemStack(Block.planks, 1, 2), 'D', dye));
        addFirst(new ShapedOreRecipe(stack(MDBlocks.junglePlank, 8, color), "SSS", "SDS", "SSS", 'S',
            new ItemStack(Block.planks, 1, 3), 'D', dye));
        // Also first: any four planks in a square make a crafting table.
        ItemStack oak = stack(MDBlocks.plank, 1, color);
        addFirst(new ShapedOreRecipe(stack(MDBlocks.acaciaPlank, 4, color), "OJ", "JO", 'O', oak, 'J',
            stack(MDBlocks.junglePlank, 1, color)));
        addFirst(new ShapedOreRecipe(stack(MDBlocks.darkOakPlank, 4, color), "OS", "SO", 'O', oak, 'S',
            stack(MDBlocks.sprucePlank, 1, color)));

        // Signs from dyed planks of any wood, ahead of the vanilla sign recipe, which takes any planks too.
        for (Block plank : new Block[] { MDBlocks.plank, MDBlocks.birchPlank, MDBlocks.sprucePlank,
            MDBlocks.junglePlank, MDBlocks.acaciaPlank, MDBlocks.darkOakPlank }) {
            addFirst(new ShapedOreRecipe(stack(MDBlocks.sign, 3, color), "PPP", "PPP", " S ", 'P',
                stack(plank, 1, color), 'S', Item.stick));
        }
        GameRegistry.addShapelessRecipe(stack(MDBlocks.sign, 1, color), Item.sign, dye);

        // Flowers: eight vanilla flowers around a dye. Minecraft 1.4.7 only has the dandelion and the rose (poppy);
        // the others come from the plants nearest them: alliums from roses and dandelions, azure bluets from grass,
        // oxeye daisies from ferns, orchids from lily pads (both grow in swamps), lilies of the valley from vines.
        // The tall flowers are two small ones of the same color: lilacs from alliums, peonies from tulips and rose
        // bushes from poppies.
        surround(MDBlocks.dandelion, color, new ItemStack(Block.plantYellow), dye);
        surround(MDBlocks.poppy, color, new ItemStack(Block.plantRed), dye);
        GameRegistry.addRecipe(stack(MDBlocks.allium, 8, color), "RYR", "YDY", "RYR", 'R', Block.plantRed, 'Y',
            Block.plantYellow, 'D', dye);
        surround(MDBlocks.azureBluet, color, new ItemStack(Block.tallGrass, 1, 1), dye);
        surround(MDBlocks.oxeyeDaisy, color, new ItemStack(Block.tallGrass, 1, 2), dye);
        surround(MDBlocks.blueOrchid, color, new ItemStack(Block.waterlily), dye);
        surround(MDBlocks.lilyOfTheValley, color, new ItemStack(Block.vine), dye);
        tallFlower(MDBlocks.lilac, MDBlocks.allium, color);
        tallFlower(MDBlocks.peony, MDBlocks.tulip, color);
        tallFlower(MDBlocks.roseBush, MDBlocks.poppy, color);
        for (Block flower : new Block[] { MDBlocks.allium, MDBlocks.azureBluet, MDBlocks.blueOrchid,
            MDBlocks.dandelion, MDBlocks.lilyOfTheValley, MDBlocks.oxeyeDaisy, MDBlocks.poppy }) {
            GameRegistry.addShapelessRecipe(dye.copy(), stack(flower, 1, color));
        }
        for (Block flower : new Block[] { MDBlocks.lilac, MDBlocks.peony, MDBlocks.roseBush }) {
            ItemStack dyes = dye.copy();
            dyes.stackSize = 2;
            GameRegistry.addShapelessRecipe(dyes, stack(flower, 1, color));
        }
    }

    private static void tallFlower(Block tall, Block small, int color) {
        GameRegistry.addRecipe(stack(tall, 1, color), "F", "F", 'F', stack(small, 1, color));
    }

    /**
     * Stairs, slabs, walls and fences from dyed blocks of one color, with the vanilla shapes and amounts (fences as in
     * Minecraft 1.8 and later: planks and sticks, three at a time).
     */
    private static void addShapeRecipes() {
        for (MDBlocks.Shape shape : MDBlocks.SHAPES) {
            for (int color = 0; color < Colors.COUNT; color++) {
                ItemStack base = stack(shape.base, 1, color);
                IRecipe recipe;
                if (shape.kind == MDBlocks.Shape.STAIRS) {
                    recipe = new ShapedOreRecipe(stack(shape.block, 4, color), "S  ", "SS ", "SSS", 'S', base);
                } else if (shape.kind == MDBlocks.Shape.SLAB) {
                    recipe = new ShapedOreRecipe(stack(shape.block, 6, color), "SSS", 'S', base);
                } else if (shape.kind == MDBlocks.Shape.FENCE) {
                    recipe = new ShapedOreRecipe(stack(shape.block, 3, color), "PSP", "PSP", 'P', base, 'S',
                        Item.stick);
                } else {
                    recipe = new ShapedOreRecipe(stack(shape.block, 6, color), "SSS", "SSS", 'S', base);
                }
                // Dyed planks count as any planks, so the vanilla wooden stairs and slab recipes would take them.
                if (shape.base.blockMaterial == Material.wood) {
                    addFirst(recipe);
                } else {
                    GameRegistry.addRecipe(recipe);
                }
            }
        }
    }

    /**
     * Plain diorite, granite and andesite. Minecraft 1.8 makes them with nether quartz, which 1.4.7 does not have, so
     * diorite is cobblestone and bone meal, granite is diorite and a clay brick, andesite is diorite and cobblestone.
     */
    private static void addPlainStoneRecipes() {
        ItemStack boneMeal = new ItemStack(Item.dyePowder, 1, 15);
        GameRegistry.addRecipe(new ItemStack(MDBlocks.dioritePlain, 2), "CB", "BC", 'C', Block.cobblestone, 'B',
            boneMeal);
        GameRegistry.addShapelessRecipe(new ItemStack(MDBlocks.granitePlain), MDBlocks.dioritePlain, Item.brick);
        GameRegistry.addShapelessRecipe(new ItemStack(MDBlocks.andesitePlain, 2), MDBlocks.dioritePlain,
            Block.cobblestone);
    }

    public static void addSmelting() {
        FurnaceRecipes furnace = FurnaceRecipes.smelting();
        for (int color = 0; color < Colors.COUNT; color++) {
            furnace.addSmelting(MDBlocks.log.blockID, color, new ItemStack(Item.coal, 1, 1), 0.15F);
            smelt(MDBlocks.cobble, MDBlocks.stone, color, 0.1F);
            smelt(MDBlocks.stonebrick, MDBlocks.stonebrickCracked, color, 0.1F);
            smelt(MDBlocks.sand, MDBlocks.glassClear, color, 0.1F);
            smelt(MDBlocks.glassClear, MDBlocks.glassFoggy, color, 0.1F);
            smelt(MDBlocks.clay, MDBlocks.hardenedClay, color, 0.35F);
            // Minecraft 1.12 to 1.16 smelting recipes
            smelt(MDBlocks.stone, MDBlocks.smoothStone, color, 0.1F);
            smelt(MDBlocks.sandstone, MDBlocks.smoothSandstone, color, 0.1F);
            smelt(MDBlocks.quartz, MDBlocks.smoothQuartz, color, 0.1F);
            smelt(MDBlocks.netherBrick, MDBlocks.crackedNetherBrick, color, 0.1F);
            smelt(MDBlocks.hardenedClay, MDBlocks.glazedTerracotta, color, 0.1F);
            smelt(MDBlocks.speckledGranite, MDBlocks.smoothSpeckledGranite, color, 0.1F);
            // Basalt forms from lava over soul soil in 1.16, so it is smelted from dyed soul soil here.
            smelt(MDBlocks.soulSoil, MDBlocks.basalt, color, 0.1F);
        }
    }

    private static ItemStack stack(Block block, int count, int color) {
        return new ItemStack(block, count, color);
    }

    private static void surround(Block output, int color, ItemStack surround, ItemStack dye) {
        surround(output, color, surround, dye, 8);
    }

    private static void surround(Block output, int color, ItemStack surround, ItemStack dye, int count) {
        GameRegistry.addRecipe(stack(output, count, color), "SSS", "SDS", "SSS", 'S', surround, 'D', dye);
    }

    /** Four of a dyed block in a square make four of another in the same color. */
    private static void square(Block output, Block input, int color) {
        GameRegistry.addRecipe(stack(output, 4, color), "SS", "SS", 'S', stack(input, 1, color));
    }

    private static void smelt(Block input, Block output, int color, float xp) {
        FurnaceRecipes.smelting().addSmelting(input.blockID, color, stack(output, 1, color), xp);
    }

    @SuppressWarnings("unchecked")
    private static void addFirst(IRecipe recipe) {
        CraftingManager.getInstance().getRecipeList().add(0, recipe);
    }
}
