package net.neverandy.moredyes.data.server;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeRecipeProvider;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.item.MDItem;
import org.jetbrains.annotations.NotNull;

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
        circleRecipe(MDBlock.stoneArray);
        circleRecipe(MDBlock.brickArray);
        circleRecipe(MDBlock.clayArray);
        circleRecipe(MDBlock.coalArray);
        circleRecipe(MDBlock.cobbleArray);
        circleRecipe(MDBlock.glassArray);
        circleRecipe(MDBlock.glassFoggyArray);
        circleRecipe(MDBlock.stonebrickArray);
        circleRecipe(MDBlock.stonebrickCarvedArray);
        circleRecipe(MDBlock.stonebrickCrackedArray);
        circleRecipe(MDBlock.glowstoneArray);
        circleRecipe(MDBlock.redstoneArray);
        circleRecipe(MDBlock.woolArray);
        circleRecipe(MDBlock.lapisArray);
        //circleRecipe(MDBlock.logArray);
        circleRecipe(MDBlock.obsidianArray);
        circleRecipe(MDBlock.plankArray);
        circleRecipe(MDBlock.quartzArray);
        circleRecipe(MDBlock.workbenchArray);
        circleRecipe(MDBlock.sandArray);
        circleRecipe(MDBlock.soulsandArray);
    }
    public void circleRecipe(Block @NotNull [] blocks)
    {
        for(int i = 0; i < blocks.length; i++)
        {
            if(Tags.Blocks.STONE.contains(blocks[i]))
            {
                ShapedRecipeBuilder.shapedRecipe(blocks[i], 8)
                        .key('s', Items.STONE)
                        .key('d', MDItem.dye[i])
                        .patternLine("sss")
                        .patternLine("sds")
                        .patternLine("sss");
            }
            if(Tags.Blocks.STORAGE_BLOCKS_LAPIS.contains(blocks[i]))
            {
                ShapedRecipeBuilder.shapedRecipe(blocks[i], 8)
                        .key('s', Items.LAPIS_BLOCK)
                        .key('d', MDItem.dye[i])
                        .patternLine("sss")
                        .patternLine("sds")
                        .patternLine("sss");
            }
            if(Tags.Blocks.GLASS.contains(blocks[i]))
            {
                ShapedRecipeBuilder.shapedRecipe(blocks[i], 8)
                        .key('s', Items.GLASS)
                        .key('d', MDItem.dye[i])
                        .patternLine("sss")
                        .patternLine("sds")
                        .patternLine("sss");
            }
            if(Tags.Blocks.OBSIDIAN.contains(blocks[i]))
            {
                ShapedRecipeBuilder.shapedRecipe(blocks[i], 8)
                        .key('s', Items.OBSIDIAN)
                        .key('d', MDItem.dye[i])
                        .patternLine("sss")
                        .patternLine("sds")
                        .patternLine("sss");
            }
        }
    }
    public void woodToPlanks(Block[] blocks)
    {
        for(int i = 0; i < blocks.length; i++)
        {
            ShapedRecipeBuilder.shapedRecipe(MDBlock.plankArray[i], 4)
                    .key('s', MDBlock.logArray[i])
                    .patternLine("ss")
                    .patternLine("ss");
        }
    }
    public void planksToSticks(Block[] blocks)
    {
        for(int i = 0; i < blocks.length; i++)
        {
            ShapedRecipeBuilder.shapedRecipe(MDBlock.plankArray[i], 4)
                    .key('s', MDBlock.plankArray[i])
                    .patternLine("s")
                    .patternLine("s");
        }
    }
    public void dyeLogs(Block[] blocks)
    {
        for(int i = 0; i < blocks.length; i++)
        {
            ShapelessRecipeBuilder.shapelessRecipe(MDBlock.logArray[i], 1);
        }
    }
}
