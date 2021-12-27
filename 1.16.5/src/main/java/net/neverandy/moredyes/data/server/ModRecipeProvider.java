package net.neverandy.moredyes.data.server;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
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
        circleRecipe(consumer);
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
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.glassArray[i], 8)
                    .key('s', Blocks.GLASS)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.obsidianArray[i], 8)
                    .key('s', Blocks.OBSIDIAN)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.clayArray[i], 8)
                    .key('s', Blocks.CLAY)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.coalArray[i], 8)
                    .key('s', Blocks.COAL_BLOCK)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.brickArray[i], 8)
                    .key('s', Blocks.BRICKS)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.cobbleArray[i], 8)
                    .key('s', Blocks.COBBLESTONE)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.plankArray[i], 8)
                    .key('s', Blocks.OAK_PLANKS)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.logArray[i], 8)
                    .key('s', Blocks.OAK_LOG)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.sandstoneArray[i], 8)
                    .key('s', Blocks.SANDSTONE)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.sandArray[i], 8)
                    .key('s', Blocks.SAND)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.workbenchArray[i], 8)
                    .key('s', Blocks.CRAFTING_TABLE)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss");
            ShapedRecipeBuilder.shapedRecipe(MDBlock.woolArray[i], 8)
                    .key('s', Blocks.WHITE_WOOL)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.stonebrickCrackedArray[i], 8)
                    .key('s', Blocks.STONE_BRICKS)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.stonebrickArray[i], 8)
                    .key('s', Blocks.STONE_BRICKS)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.soulsandArray[i], 8)
                    .key('s', Blocks.SOUL_SAND)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .build(consumer);
            ShapedRecipeBuilder.shapedRecipe(MDBlock.stonebrickCarvedArray[i], 8)
                    .key('s', Blocks.CHISELED_STONE_BRICKS)
                    .key('d', MDItem.dye[i])
                    .patternLine("sss")
                    .patternLine("sds")
                    .patternLine("sss")
                    .build(consumer);

        }
    }
    /*
    public void smeltingRecipe()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            CookingRecipeBuilder.smeltingRecipe(, MDBlock.stoneArray[i], 2.0f, 5);
            CookingRecipeBuilder.smeltingRecipe(new Ingredient(MDBlock.sandArray[i], MDBlock.glassArray[i], 2.0f, 2);
        }

    }

     */

    public void woodToPlanks(Consumer<IFinishedRecipe> consumer)
    {
        //Shapeless
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            ShapelessRecipeBuilder.shapelessRecipe(MDBlock.plankArray[i], 4).addIngredient(MDBlock.logArray[i]).build(consumer);
            ShapelessRecipeBuilder.shapelessRecipe(MDBlock.glassArray[i], 1).addIngredient(MDBlock.sandArray[i]).build(consumer);
        }

    }

    public void planksToSticks(Block[] blocks)
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
