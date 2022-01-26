package net.neverandy.moredyes.data.client;


import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;


public class ModBlockStateProvider extends BlockStateProvider
{
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper)
    {
        super(gen, Reference.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        MoreDyes.LOGGER.info("Starting for loop");
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            simpleBlock(blockGlass(i), texture(blockGlass(i), "glass", i));
            simpleBlock(blockSand(i), texture(blockSand(i), "sand", i));

            simpleBlock(blockBrick(i), texture(blockBrick(i), "brick", i));
            simpleBlock(blockClay(i), texture(blockClay(i), "clay", i));
            simpleBlock(blockWool(i), texture(blockWool(i), "wool", i));
            simpleBlock(blockCobble(i), texture(blockCobble(i), "cobble", i));
            simpleBlock(blockStonebrick(i), texture(blockStonebrick(i), "stonebrick", i));
            simpleBlock(blockStonebrickCracked(i), texture(blockStonebrickCracked(i), "stonebrickcracked", i));
            simpleBlock(blockStonebrickCarved(i), texture(blockStonebrickCarved(i), "stonebrickcarved", i));
            simpleBlock(blockStone(i), texture(blockStone(i), "stone", i));
            simpleBlock(blockObsidian(i), texture(blockObsidian(i), "obsidian", i));
            simpleBlock(blockLapis(i), texture(blockLapis(i), "lapis", i));
            simpleBlock(blockGlowstone(i), texture(blockGlowstone(i), "glowstone", i));
            simpleBlock(blockCoal(i), texture(blockCoal(i), "coal", i));
            simpleBlock(blockSoulsand(i), texture(blockSoulsand(i), "soulsand", i));
            
            //Planks
            simpleBlock(blockOakPlank(i), texturePlank(blockOakPlank(i), "plankoak", i));
            simpleBlock(blockAcaciaPlank(i), texturePlank(blockAcaciaPlank(i), "plankacacia", i));
            simpleBlock(blockDarkOakPlank(i), texturePlank(blockDarkOakPlank(i), "plankdarkoak", i));
            simpleBlock(blockSprucePlank(i), texturePlank(blockSprucePlank(i), "plankspruce", i));
            simpleBlock(blockJunglePlank(i), texturePlank(blockJunglePlank(i), "plankjungle", i));

            simpleBlock(blockRedstone(i), texture(blockRedstone(i), "redstone", i));
            simpleBlock(blockQuartz(i), texture(blockQuartz(i), "quartz", i));
            simpleBlock(blockGlassFoggy(i), texture(blockGlassFoggy(i), "glassFoggy", i));

            //Sapling
            simpleBlock(blockSapling(i), textureSapling(blockSapling(i), "oaksapling", i));
            simpleBlock(blockSapling(i), textureSapling(blockSapling(i), "acaciasapling", i));
            simpleBlock(blockSapling(i), textureSapling(blockSapling(i), "birchsapling", i));
            simpleBlock(blockSapling(i), textureSapling(blockSapling(i), "junglesapling", i));
            simpleBlock(blockSapling(i), textureSapling(blockSapling(i), "darkoaksapling", i));
            simpleBlock(blockSapling(i), textureSapling(blockSapling(i), "sprucesapling", i));

            simpleBlock(blockTulip(i), texture(blockTulip(i), "tulip", i));

            //Log
            simpleBlock(blockOakLog(i), textureLog(blockOakLog(i), "oaklog", i));
            simpleBlock(blockBirchLog(i), textureLog(blockBirchLog(i), "birchlog", i));
            simpleBlock(blockDarkOakPlank(i), textureLog(blockDarkOakLog(i), "darkoaklog", i));
            simpleBlock(blockJungleLog(i), textureLog(blockJungleLog(i), "junglelog", i));
            simpleBlock(blockAcaciaLog(i), textureLog(blockAcaciaLog(i), "acacialog", i));
            simpleBlock(blockSpruceLog(i), textureLog(blockSpruceLog(i), "sprucelog", i));
            
            //Leaves
            simpleBlock(blockOakLeaf(i), textureLeaves(blockOakLeaf(i), "oakLeaf", i));
            simpleBlock(blockBirchLeaf(i), textureLeaves(blockBirchLeaf(i), "birchLeaf", i));
            simpleBlock(blockDarkOakLeaf(i), textureLeaves(blockDarkOakLeaf(i), "darkoakLeaf", i));
            simpleBlock(blockJungleLeaf(i), textureLeaves(blockJungleLeaf(i), "jungleLeaf", i));
            simpleBlock(blockAcaciaLeaf(i), textureLeaves(blockAcaciaLeaf(i), "acaciaLeaf", i));
            simpleBlock(blockSpruceLeaf(i), textureLeaves(blockSpruceLeaf(i), "spruceLeaf", i));

            simpleBlock(blockWorkbench(i), textureWorkbench(blockWorkbench(i), "workbench", i));
            simpleBlock(blockSandstone(i), textureSandstone(blockSandstone(i), "sandstone", i));
            simpleBlock(blockSandstoneCarved(i), textureSandstone(blockSandstoneCarved(i), "sandstonecarved", i));
            simpleBlock(blockSandstoneSmooth(i), textureSandstone(blockSandstoneSmooth(i), "sandstonesmooth", i));
            simpleBlock(blockAndesite(i), texture(blockAndesite(i), "andesite", i));
            simpleBlock(blockDiorite(i), texture(blockDiorite(i), "diorite", i));
            simpleBlock(blockConcretePowder(i), texture(blockConcretePowder(i), "concretepowder", i));
            simpleBlock(blockConcretePowder(i), texture(blockConcrete(i), "concrete", i));

            //Fence
            fenceBlock(blockOakFence(i), texture(blockOakPlank(i), "plank", i).getLocation());
            fenceBlock(blockDarkOakFence(i), texture(blockDarkOakPlank(i), "plank", i).getLocation());
            fenceBlock(blockAcaciaFence(i), texture(blockAcaciaPlank(i), "plank", i).getLocation());
            fenceBlock(blockBirchFence(i), texture(blockBirchPlank(i), "plank", i).getLocation());
            fenceBlock(blockJungleFence(i), texture(blockJunglePlank(i), "plank", i).getLocation());
            fenceBlock(blockSpruceFence(i), texture(blockSprucePlank(i), "plank", i).getLocation());
        }

    }
    public ModelFile texture(@NotNull Block block, @NotNull String blockType, int arrayIndex)
    {
        ResourceLocation name = block.getRegistryName();
        assert name != null;
        ResourceLocation resourceLocation = new ResourceLocation(name.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/" + ColorStrings.ALL[arrayIndex]);
        return models().cubeAll(block.getRegistryName().getPath(), resourceLocation);
    }

    public ModelFile textureLog(@NotNull Block block, @NotNull String blockType, int arrayIndex)
    {
        ResourceLocation name = block.getRegistryName();
        assert name != null;
        ResourceLocation top = new ResourceLocation(name.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/top/" + ColorStrings.ALL[arrayIndex]);
        ResourceLocation side = new ResourceLocation(name.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/side");
        switch (blockType)
        {
            case "acacialog":
                top = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/log/acacia/top/" + ColorStrings.ALL[arrayIndex]);
                side = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/log/acacia/side");
                break;
            case "birchlog":
                top = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/log/birch/top/" + ColorStrings.ALL[arrayIndex]);
                side = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/log/birch/side");
                break;
            case "darkoaklog":
                top = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/log/darkoak/top/" + ColorStrings.ALL[arrayIndex]);
                side = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/log/darkoak/side");
                break;
            case "sprucelog":
                top = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/log/spruce/top/" + ColorStrings.ALL[arrayIndex]);
                side = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/log/spruce/side");
                break;
            case "junglelog":
                top = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/log/jungle/top/" + ColorStrings.ALL[arrayIndex]);
                side = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/log/jungle/side");
                break;
            case "oaklog":
                top = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/log/oak/top/" + ColorStrings.ALL[arrayIndex]);
                side = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/log/oak/side");
                break;
        }
        return models().cubeColumnHorizontal(block.getRegistryName().getPath(), side, top);
    }
    public ModelFile textureLeaves(@NotNull Block block, @NotNull String blockType, int arrayIndex)
    {
        ResourceLocation name = block.getRegistryName();
        assert name != null;
        ResourceLocation resourceLocation = new ResourceLocation(name.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/top/" + ColorStrings.ALL[arrayIndex]);
        switch (blockType)
        {
            case "acacialeaf":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/leaf/acacia/" + ColorStrings.ALL[arrayIndex]);
                break;
            case "birchleaf":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/leaf/birch/" + ColorStrings.ALL[arrayIndex]);
                break;
            case "darkoakleaf":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/leaf/darkoak/" + ColorStrings.ALL[arrayIndex]);
                break;
            case "spruceleaf":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/leaf/spruce/" + ColorStrings.ALL[arrayIndex]);
                break;
            case "jungleleaf":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/leaf/jungle/" + ColorStrings.ALL[arrayIndex]);
                break;
            case "oakleaf":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/leaf/oak/" + ColorStrings.ALL[arrayIndex]);
                break;
        }
        return models().cubeAll(name.getNamespace(), resourceLocation);
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

        if (blockType.equals("sandstonecarved"))
        {
            side = new ResourceLocation(name.getNamespace(),
                    ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/" + ColorStrings.ALL[arrayIndex]);
            top = new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/sandstone/top/" + ColorStrings.ALL[arrayIndex]);
            bottom = new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/sandstone/bottom/" + ColorStrings.ALL[arrayIndex]);
        } else if (blockType.equals("sandstonesmooth"))
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
        return models().orientableWithBottom(block.getRegistryName().getPath(), side, front, bottom, top);

    }

    public ModelFile textureSapling(Block block, String blockType, int arrayIndex)
    {
        ResourceLocation name = block.getRegistryName();
        assert name != null;
        ResourceLocation resourceLocation = new ResourceLocation(name.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/" + ColorStrings.ALL[arrayIndex]);

        switch(blockType)
        {
            case "oaksapling":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/sapling/oak/" + ColorStrings.ALL[arrayIndex]);
                return models().cubeAll("oaksapling_" + ColorStrings.ALL[arrayIndex], resourceLocation);
            case "birchsapling":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/sapling/birch/" + ColorStrings.ALL[arrayIndex]);
                return models().cubeAll("birchsapling_" + ColorStrings.ALL[arrayIndex], resourceLocation);
            case "darkoaksapling":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/sapling/darkoak/" + ColorStrings.ALL[arrayIndex]);
                return models().cubeAll("darkoakapling_" + ColorStrings.ALL[arrayIndex], resourceLocation);
            case "acaciasapling":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/sapling/acacia/" + ColorStrings.ALL[arrayIndex]);
                return models().cubeAll("acaciaapling_" + ColorStrings.ALL[arrayIndex], resourceLocation);
            case "junglesapling":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/sapling/jungle/" + ColorStrings.ALL[arrayIndex]);
                return models().cubeAll("junglesapling_" + ColorStrings.ALL[arrayIndex], resourceLocation);
            case "sprucesapling":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/sapling/spruce/" + ColorStrings.ALL[arrayIndex]);
                return models().cubeAll("sprucesapling_" + ColorStrings.ALL[arrayIndex], resourceLocation);
        }
        return null;

    }
    public ModelFile texturePlank(@NotNull Block block, @NotNull String blockType, int arrayIndex)
    {
        ResourceLocation name = block.getRegistryName();
        assert name != null;
        ResourceLocation resourceLocation;

        switch (blockType)
        {
            case "plankoak":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/plank/oak/" + ColorStrings.ALL[arrayIndex]);
                return models().cubeAll("oakplank_" + ColorStrings.ALL[arrayIndex], resourceLocation);
            case "plankbirch":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/plank/birch/" + ColorStrings.ALL[arrayIndex]);
                return models().cubeAll("birchplank_" + ColorStrings.ALL[arrayIndex], resourceLocation);
            case "plankdarkoak":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/plank/darkoak/" + ColorStrings.ALL[arrayIndex]);
                return models().cubeAll("darkoakplank_" + ColorStrings.ALL[arrayIndex], resourceLocation);
            case "plankacacia":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/plank/acacia/" + ColorStrings.ALL[arrayIndex]);
                return models().cubeAll("acaciaplank_" + ColorStrings.ALL[arrayIndex], resourceLocation);
            case "plankjungle":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/plank/jungle/" + ColorStrings.ALL[arrayIndex]);
                return models().cubeAll("jungleplank_" + ColorStrings.ALL[arrayIndex], resourceLocation);
            case "plankspruce":
                resourceLocation = new ResourceLocation(name.getNamespace(),
                        ModelProvider.BLOCK_FOLDER + "/plank/spruce/" + ColorStrings.ALL[arrayIndex]);
                return models().cubeAll("spruceplank_" + ColorStrings.ALL[arrayIndex], resourceLocation);
        }
        return null;
    }

    public static Block blockGlass(int arrayIndex)
    {
        return MDBlock.glassArray[arrayIndex];
    }

    //Oak
    public static Block blockOakLog(int arrayIndex)
    {
        return MDBlock.oakLogArray[arrayIndex];
    }

    public static Block blockOakLeaf(int arrayIndex)
    {
        return MDBlock.oakLeafArray[arrayIndex];
    }

    public static Block blockOakPlank(int arrayIndex)
    {
        return MDBlock.oakPlankArray[arrayIndex];
    }
    private FenceBlock blockOakFence(int arrayIndex)
    {
        return MDBlock.oakFenceArray[arrayIndex];
    }

    //Dark Oak
    public static Block blockDarkOakLog(int arrayIndex)
    {
        return MDBlock.darkOakLogArray[arrayIndex];
    }

    public static Block blockDarkOakLeaf(int arrayIndex)
    {
        return MDBlock.darkOakLeafArray[arrayIndex];
    }

    public static Block blockDarkOakPlank(int arrayIndex)
    {
        return MDBlock.darkOakPlankArray[arrayIndex];
    }
    private FenceBlock blockDarkOakFence(int arrayIndex)
    {
        return MDBlock.darkOakFenceArray[arrayIndex];
    }


    //Acacia
    public static Block blockAcaciaLog(int arrayIndex)
    {
        return MDBlock.acaciaPlankArray[arrayIndex];
    }

    public static Block blockAcaciaLeaf(int arrayIndex)
    {
        return MDBlock.acaciaLeafArray[arrayIndex];
    }

    public static Block blockAcaciaPlank(int arrayIndex)
    {
        return MDBlock.acaciaPlankArray[arrayIndex];
    }
    private FenceBlock blockAcaciaFence(int arrayIndex)
    {
        return MDBlock.acaciaFenceArray[arrayIndex];
    }


    //Spruce
    public static Block blockSpruceLog(int arrayIndex)
    {
        return MDBlock.spruceLogArray[arrayIndex];
    }

    public static Block blockSpruceLeaf(int arrayIndex)
    {
        return MDBlock.spruceLafArray[arrayIndex];
    }

    public static Block blockSprucePlank(int arrayIndex)
    {
        return MDBlock.sprucePlankArray[arrayIndex];
    }
    private FenceBlock blockSpruceFence(int arrayIndex)
    {
        return MDBlock.spruceFenceArray[arrayIndex];
    }

    //Birch
    public static Block blockBirchLog(int arrayIndex)
    {
        return MDBlock.birchLogArray[arrayIndex];
    }

    public static Block blockBirchLeaf(int arrayIndex)
    {
        return MDBlock.birchLeafArray[arrayIndex];
    }

    public static Block blockBirchPlank(int arrayIndex)
    {
        return MDBlock.birchPlankArray[arrayIndex];
    }
    private FenceBlock blockBirchFence(int arrayIndex)
    {
        return MDBlock.birchFenceArray[arrayIndex];
    }

    //Jungle
    public static Block blockJungleLog(int arrayIndex)
    {
        return MDBlock.jungleLogArray[arrayIndex];
    }

    public static Block blockJungleLeaf(int arrayIndex)
    {
        return MDBlock.jungleLeafArray[arrayIndex];
    }

    public static Block blockJunglePlank(int arrayIndex)
    {
        return MDBlock.junglePlankArray[arrayIndex];
    }
    private FenceBlock blockJungleFence(int arrayIndex)
    {
        return MDBlock.jungleFenceArray[arrayIndex];
    }


    public static Block blockSand(int arrayIndex)
    {
        return MDBlock.sandArray[arrayIndex];
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
    public static Block blockConcrete(int arrayIndex)
    {
        return MDBlock.concretePowderArray[arrayIndex];
    }


}
