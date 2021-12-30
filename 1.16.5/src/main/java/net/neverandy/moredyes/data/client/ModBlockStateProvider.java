package net.neverandy.moredyes.data.client;


import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;


public class ModBlockStateProvider extends BlockStateProvider
{
    public static final Logger LOGGER = LogManager.getLogger();

    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper)
    {
        super(gen, Reference.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        LOGGER.info("Starting for loop");
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            simpleBlock(blockGlass(i), texture(blockGlass(i), "glass", i));
            simpleBlock(blockSand(i), texture(blockSand(i), "sand", i));
            simpleBlock(blockLeaf(i), texture(blockLeaf(i), "leaf", i));
            simpleBlock(blockBrick(i), texture(blockBrick(i), "brick", i));
            simpleBlock(blockClay(i), texture(blockClay(i), "clay", i));
            simpleBlock(blockWool(i), texture(blockWool(i), "wool", i));
            simpleBlock(blockCobble(i), texture(blockCobble(i), "cobble", i));
            simpleBlock(blockStonebrick(i), texture(blockStonebrick(i), "stonebrick", i));
            simpleBlock(blockStonebrickCracked(i), texture(blockStonebrickCracked(i), "stonebrickCracked", i));
            simpleBlock(blockStonebrickCarved(i), texture(blockStonebrickCarved(i), "stonebrickCarved", i));
            simpleBlock(blockStone(i), texture(blockStone(i), "stone", i));
            simpleBlock(blockObsidian(i), texture(blockObsidian(i), "obsidian", i));
            simpleBlock(blockLapis(i), texture(blockLapis(i), "lapis", i));
            simpleBlock(blockGlowstone(i), texture(blockGlowstone(i), "glowstone", i));
            simpleBlock(blockCoal(i), texture(blockCoal(i), "coal", i));
            simpleBlock(blockSoulsand(i), texture(blockSoulsand(i), "soulsand", i));
            simpleBlock(blockPlank(i), texture(blockPlank(i), "plank", i));
            simpleBlock(blockRedstone(i), texture(blockRedstone(i), "redstone", i));
            simpleBlock(blockQuartz(i), texture(blockQuartz(i), "quartz", i));
            simpleBlock(blockGlassFoggy(i), texture(blockGlassFoggy(i), "glassFoggy", i));
            simpleBlock(blockSapling(i), texture(blockSapling(i), "sapling", i));
            simpleBlock(blockTulip(i), texture(blockTulip(i), "tulip", i));
            simpleBlock(blockLog(i), textureLog(blockLog(i), "log", i));
            simpleBlock(blockWorkbench(i),textureWorkbench(blockWorkbench(i), "workbench", i));
            simpleBlock(blockSandstone(i), textureSandstone(blockSandstone(i), "sandstone", i));
            simpleBlock(blockSandstoneCarved(i), textureSandstone(blockSandstoneCarved(i), "sandstonecarved", i));
            simpleBlock(blockSandstoneSmooth(i), textureSandstone(blockSandstoneSmooth(i), "sandstonesmooth", i));
            simpleBlock(blockAndesite(i), texture(blockAndesite(i), "andesite", i));
            simpleBlock(blockDiorite(i), texture(blockDiorite(i), "diorite", i));
            simpleBlock(blockConcretePowder(i), texture(blockConcretePowder(i), "concretepowder", i));


        }

    }

    public ModelFile texture(@NotNull Block block, @NotNull String blockType, int arrayIndex)
    {
        ResourceLocation name = block.getRegistryName();
        ResourceLocation resourceLocation = new ResourceLocation(name.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/" + ColorStrings.ALL[arrayIndex]);
        return models().cubeAll(block.getRegistryName().getPath(), resourceLocation);
    }
    public ModelFile textureLog(@NotNull Block block, @NotNull String blockType, int arrayIndex)
    {
        ResourceLocation name = block.getRegistryName();
        ResourceLocation top = new ResourceLocation(name.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/top/" + ColorStrings.ALL[arrayIndex]);
        ResourceLocation side = new ResourceLocation(name.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/log_side");
        return models().cubeColumnHorizontal(block.getRegistryName().getPath(), side, top);
    }
    public ModelFile textureSandstone(@NotNull Block block, @NotNull String blockType, int arrayIndex)
    {
        ResourceLocation name = block.getRegistryName();
        ResourceLocation side = new ResourceLocation(name.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/side/" + ColorStrings.ALL[arrayIndex]);
        ResourceLocation top = new ResourceLocation(name.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/top/" + ColorStrings.ALL[arrayIndex]);
        ResourceLocation bottom = new ResourceLocation(name.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/bottom/" + ColorStrings.ALL[arrayIndex]);

        if(blockType.equals("sandstonecarved"))
        {
            side = new ResourceLocation(name.getNamespace(),
                    ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/" + ColorStrings.ALL[arrayIndex]);
            top = new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/sandstone/top/" + ColorStrings.ALL[arrayIndex]);
            bottom = new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/sandstone/bottom/" + ColorStrings.ALL[arrayIndex]);
        }
        else if(blockType.equals("sandstonesmooth"))
        {
            side = new ResourceLocation(name.getNamespace(),
                    ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/" + ColorStrings.ALL[arrayIndex]);
            top = new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/sandstone/top/" + ColorStrings.ALL[arrayIndex]);
            bottom = new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/sandstone/bottom/" + ColorStrings.ALL[arrayIndex]);
        }
        return models().cubeBottomTop(block.getRegistryName().getPath(), side, bottom, top);
    }

    public ModelFile textureWorkbench(@NotNull Block block, @NotNull String blockType, int arrayIndex)
    {
        ResourceLocation name = block.getRegistryName();
        ResourceLocation top = new ResourceLocation(name.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/top/" + ColorStrings.ALL[arrayIndex]);
        ResourceLocation side = new ResourceLocation(name.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/side/" + ColorStrings.ALL[arrayIndex]);
        ResourceLocation bottom = new ResourceLocation(name.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/plank/" + ColorStrings.ALL[arrayIndex]);
        ResourceLocation front = new ResourceLocation(name.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/front/" + ColorStrings.ALL[arrayIndex]);
        return models().orientableWithBottom(block.getRegistryName().getPath(),side, front, bottom, top);

    }

    public static Block blockGlass(int arrayIndex)
    {
        return MDBlock.glassArray[arrayIndex];
    }
    public static Block blockLog(int arrayIndex)
    {
        return MDBlock.logArray[arrayIndex];
    }
    public static Block blockSand(int arrayIndex)
    {
        return MDBlock.sandArray[arrayIndex];
    }

    public static Block blockLeaf(int arrayIndex)
    {
        return MDBlock.leafArray[arrayIndex];
    }

    public static Block blockBrick(int arrayIndex)
    {
        return MDBlock.brickArray[arrayIndex];
    }

    public static Block blockClay(int arrayIndex)
    {
        return MDBlock.clayArray[arrayIndex];
    }

    public static Block blockWool(int arrayIndex)
    {
        return MDBlock.woolArray[arrayIndex];
    }

    public static Block blockCobble(int arrayIndex)
    {
        return MDBlock.cobbleArray[arrayIndex];
    }

    public static Block blockStonebrick(int arrayIndex)
    {
        return MDBlock.stonebrickArray[arrayIndex];
    }

    public static Block blockStonebrickCracked(int arrayIndex)
    {
        return MDBlock.stonebrickCrackedArray[arrayIndex];
    }

    public static Block blockStonebrickCarved(int arrayIndex)
    {
        return MDBlock.stonebrickCarvedArray[arrayIndex];
    }

    public static Block blockStone(int arrayIndex)
    {
        return MDBlock.stoneArray[arrayIndex];
    }

    public static Block blockObsidian(int arrayIndex)
    {
        return MDBlock.obsidianArray[arrayIndex];
    }

    public static Block blockLapis(int arrayIndex)
    {
        return MDBlock.lapisArray[arrayIndex];
    }

    public static Block blockGlowstone(int arrayIndex)
    {
        return MDBlock.glowstoneArray[arrayIndex];
    }

    public static Block blockCoal(int arrayIndex)
    {
        return MDBlock.coalArray[arrayIndex];
    }

    public static Block blockSoulsand(int arrayIndex)
    {
        return MDBlock.soulsandArray[arrayIndex];
    }

    public static Block blockPlank(int arrayIndex)
    {
        return MDBlock.plankArray[arrayIndex];
    }

    public static Block blockRedstone(int arrayIndex)
    {
        return MDBlock.redstoneArray[arrayIndex];
    }

    public static Block blockQuartz(int arrayIndex)
    {
        return MDBlock.quartzArray[arrayIndex];
    }

    public static Block blockGlassFoggy(int arrayIndex)
    {
        return MDBlock.glassFoggyArray[arrayIndex];
    }

    public static Block blockSapling(int arrayIndex)
    {
        return MDBlock.saplingArray[arrayIndex];
    }

    public static Block blockTulip(int arrayIndex)
    {
        return MDBlock.tulipArray[arrayIndex];
    }

    public static Block blockWorkbench(int arrayIndex)
    {
        return MDBlock.workbenchArray[arrayIndex];
    }
    public static Block blockSandstone(int arrayIndex)
    {
        return MDBlock.sandstoneArray[arrayIndex];
    }
    public static Block blockSandstoneCarved(int arrayIndex)
    {
        return MDBlock.sandstoneCarvedArray[arrayIndex];
    }
    public static Block blockSandstoneSmooth(int arrayIndex)
    {
        return MDBlock.sandstoneSmoothArray[arrayIndex];
    }
    public static Block blockAndesite(int arrayIndex)
    {
        return MDBlock.andesiteArray[arrayIndex];
    }
    public static Block blockDiorite(int arrayIndex)
    {
        return MDBlock.dioriteArray[arrayIndex];
    }
    public static Block blockConcretePowder(int arrayIndex)
    {
        return MDBlock.concretePowderArray[arrayIndex];
    }


}
