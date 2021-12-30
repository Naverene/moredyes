package net.neverandy.moredyes.data.server;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.data.ForgeRecipeProvider;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.reference.ColorStrings;

import java.util.function.Consumer;

public class ModRecipeProvider extends ForgeRecipeProvider
{
    public ModRecipeProvider(DataGenerator gen)
    {
        super(gen);
    }

    @Override
    public void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        //circleRecipe(consumer);
        woodToPlanks(consumer);
        variousStonebrickRecipes(consumer);
        sandToSandstone(consumer);
        craftingTables(consumer);
        smeltingRecipe(consumer);
        washingRecipes(consumer);

    }

    public void circleRecipe(Consumer<IFinishedRecipe> consumer)
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            ShapedRecipeBuilder.shapedRecipe(MDBlock.stoneArray[i], 8)
                    .key('s', Blocks.STONE)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.lapisArray[i], 8)
                    .key('s', Blocks.LAPIS_BLOCK)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
            /* // Not needed anymore because the files are already generated, and I needed to make other files with the same names.
            ShapedRecipeBuilder.shapedRecipe(MDBlock.glassArray[i], 8)
                    .key('s', Blocks.GLASS)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);

             */
            ShapedRecipeBuilder.shapedRecipe(MDBlock.obsidianArray[i], 8)
                    .key('s', Blocks.OBSIDIAN)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.clayArray[i], 8)
                    .key('s', Blocks.CLAY)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.coalArray[i], 8)
                    .key('s', Blocks.COAL_BLOCK)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.brickArray[i], 8)
                    .key('s', Blocks.BRICKS)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.cobbleArray[i], 8)
                    .key('s', Blocks.COBBLESTONE)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.plankArray[i], 8)
                    .key('s', Blocks.OAK_PLANKS)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.logArray[i], 8)
                    .key('s', Blocks.OAK_LOG)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.sandstoneArray[i], 8)
                    .key('s', Blocks.SANDSTONE)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.sandArray[i], 8)
                    .key('s', Blocks.SAND)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.workbenchArray[i], 8)
                    .key('s', Blocks.CRAFTING_TABLE)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .patternLine("sss");
            ShapedRecipeBuilder.shapedRecipe(MDBlock.woolArray[i], 8)
                    .key('s', Blocks.WHITE_WOOL)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.stonebrickCrackedArray[i], 8)
                    .key('s', Blocks.STONE_BRICKS)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.stonebrickArray[i], 8)
                    .key('s', Blocks.STONE_BRICKS)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.soulsandArray[i], 8)
                    .key('s', Blocks.SOUL_SAND)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.stonebrickCarvedArray[i], 8)
                    .key('s', Blocks.CHISELED_STONE_BRICKS)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.dioriteArray[i], 8)
                    .key('s', Blocks.DIORITE)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.andesiteArray[i], 8)
                    .key('s', Blocks.ANDESITE)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);


        }
    }
    public void variousStonebrickRecipes(Consumer<IFinishedRecipe> consumer)
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            ShapedRecipeBuilder.shapedRecipe(MDBlock.stonebrickArray[i], 4)
                    .key('s', MDBlock.stoneArray[i])
                    .patternLine("ss")
                    .patternLine("ss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(MDBlock.stonebrickArray[i]), MDBlock.stonebrickCrackedArray[i], 0.1f, 20)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
        }
    }
    public void craftingTables(Consumer<IFinishedRecipe> consumer)
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            ShapedRecipeBuilder.shapedRecipe(MDBlock.workbenchArray[i], 1)
                    .key('s', MDBlock.plankArray[i])
                    .patternLine("ss")
                    .patternLine("ss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
        }
    }

    public void smeltingRecipe(Consumer<IFinishedRecipe> consumer)
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(MDBlock.sandArray[i]), MDBlock.glassArray[i], 2.0f, 20)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(MDBlock.cobbleArray[i]), MDBlock.stoneArray[i], 0.1f, 20)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
        }

    }
    public void sandToSandstone(Consumer<IFinishedRecipe> consumer)
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            ShapedRecipeBuilder.shapedRecipe(MDBlock.sandstoneArray[i])
                    .key('s', MDBlock.sandstoneArray[i])
                    .patternLine("ss")
                    .patternLine("ss")
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE))
                    .build(consumer);
        }
    }

    public void woodToPlanks(Consumer<IFinishedRecipe> consumer)
    {
        //Shapeless
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            ShapelessRecipeBuilder.shapelessRecipe(MDBlock.plankArray[i], 4).addIngredient(MDBlock.logArray[i])
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
        }

    }
    public void washingRecipes(Consumer<IFinishedRecipe> consumer)
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.STONE, 1)
                    .addIngredient(MDBlock.stoneArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.COBBLESTONE, 1)
                    .addIngredient(MDBlock.cobbleArray[i])
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.STONE_BRICKS, 1)
                    .addIngredient(MDBlock.stonebrickArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.CRACKED_STONE_BRICKS, 1)
                    .addIngredient(MDBlock.stonebrickCrackedArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.CLAY, 1)
                    .addIngredient(MDBlock.clayArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.COAL_BLOCK, 1)
                    .addIngredient(MDBlock.coalArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.BRICKS, 1)
                    .addIngredient(MDBlock.brickArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.OAK_LOG, 1)
                    .addIngredient(MDBlock.logArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.OAK_PLANKS, 1)
                    .addIngredient(MDBlock.plankArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.GLASS, 1)
                    .addIngredient(MDBlock.glassArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.GLOWSTONE, 1)
                    .addIngredient(MDBlock.glowstoneArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.REDSTONE_BLOCK, 1)
                    .addIngredient(MDBlock.redstoneArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.WHITE_WOOL, 1)
                    .addIngredient(MDBlock.woolArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.CRACKED_STONE_BRICKS, 1)
                    .addIngredient(MDBlock.stonebrickCrackedArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.SOUL_SAND, 1)
                    .addIngredient(MDBlock.soulsandArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.LAPIS_BLOCK, 1)
                    .addIngredient(MDBlock.lapisArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.OBSIDIAN, 1)
                    .addIngredient(MDBlock.obsidianArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.QUARTZ_BLOCK, 1)
                    .addIngredient(MDBlock.quartzArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.SOUL_SAND, 1)
                    .addIngredient(MDBlock.soulsandArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.OAK_LEAVES, 1)
                    .addIngredient(MDBlock.leafArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.ANDESITE, 1)
                    .addIngredient(MDBlock.andesiteArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.DIORITE, 1)
                    .addIngredient(MDBlock.dioriteArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.CRAFTING_TABLE, 1)
                    .addIngredient(MDBlock.workbenchArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.SAND, 1)
                    .addIngredient(MDBlock.sandArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.SANDSTONE, 1)
                    .addIngredient(MDBlock.sandstoneArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(Blocks.WHITE_CONCRETE_POWDER, 1)
                    .addIngredient(MDBlock.concretePowderArray[i])
                    .addIngredient(Items.WATER_BUCKET)
                    .addCriterion("stone",InventoryChangeTrigger.Instance.forItems(Blocks.STONE)).build(consumer);
        }


    }
    public void planksToSticks()
    {
        /*
        for(int i = 0; i < blocks.length; i++)
        {
            ShapedRecipeBuilder.shapedRecipe(MDItem.sticksArray[i], 4)
                    .key('s', MDBlock.plankArray[i])
                    .patternLine("s")
                    .patternLine("s");
        }
        */
    }

    public void dyeLogs(Block[] blocks)
    {
        for (int i = 0; i < blocks.length; i++)
        {
            ShapelessRecipeBuilder.shapelessRecipe(MDBlock.logArray[i], 1);
        }
    }
}
