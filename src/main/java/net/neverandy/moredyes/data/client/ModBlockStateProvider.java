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

import java.util.Locale;


public class ModBlockStateProvider extends BlockStateProvider {
    public static final Logger LOGGER = LogManager.getLogger();

    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Reference.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        LOGGER.info("Starting for loop");
        for(int i=0; i<ColorStrings.ALL.length;i++){
            simpleBlock(blockGlass(i),texture(blockGlass(i), "glass", i));
            simpleBlock(blockSand(i),texture(blockSand(i), "sand", i));
            simpleBlock(blockLeaf(i),texture(blockLeaf(i), "leaf", i));
            simpleBlock(blockBrick(i),texture(blockBrick(i), "brick", i));
            simpleBlock(blockClay(i),texture(blockClay(i), "clay", i));
            simpleBlock(blockWool(i),texture(blockWool(i), "wool", i));
            simpleBlock(blockCobble(i),texture(blockCobble(i), "cobble", i));
            simpleBlock(blockStonebrick(i),texture(blockStonebrick(i), "stonebrick", i));
            simpleBlock(blockStonebrickCracked(i),texture(blockStonebrickCracked(i), "stonebrickCracked", i));
            simpleBlock(blockStonebrickCarved(i),texture(blockStonebrickCarved(i), "stonebrickCarved", i));
            simpleBlock(blockStone(i),texture(blockStone(i), "stone", i));
            simpleBlock(blockObsidian(i),texture(blockObsidian(i), "obsidian", i));
            simpleBlock(blockLapis(i),texture(blockLapis(i), "lapis", i));
            simpleBlock(blockGlowstone(i),texture(blockGlowstone(i), "glowstone", i));
            simpleBlock(blockCoal(i),texture(blockCoal(i), "coal", i));
            simpleBlock(blockSoulsand(i),texture(blockSoulsand(i), "soulsand", i));
            simpleBlock(blockPlank(i),texture(blockPlank(i), "plank", i));
            simpleBlock(blockRedstone(i),texture(blockRedstone(i), "redstone", i));
            simpleBlock(blockQuartz(i),texture(blockQuartz(i), "quartz", i));
            simpleBlock(blockGlassFoggy(i),texture(blockGlassFoggy(i), "glassFoggy", i));
            simpleBlock(blockSapling(i),texture(blockSapling(i), "sapling", i));
            simpleBlock(blockTulip(i),texture(blockTulip(i), "tulip", i));
            //simpleBlock(blockWorkbench(i),texture(blockWorkbench(i), "workbench", i));
        }

    }

    public ModelFile texture(Block block, String blockType, int arrayIndex){
        ResourceLocation name = block.getRegistryName();
        ResourceLocation resourceLocation = new ResourceLocation(name.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + blockType.toLowerCase(Locale.ROOT) + "/" + ColorStrings.ALL[arrayIndex]);
        return models().cubeAll(block.getRegistryName().getPath(), resourceLocation);
    }

    private Block blockGlass(int arrayIndex){
            return  MDBlock.glassArray[arrayIndex];
    }
    private Block blockSand(int arrayIndex){return  MDBlock.sandArray[arrayIndex];}
    private Block blockLeaf(int arrayIndex){
        return  MDBlock.leafArray[arrayIndex];
    }
    private Block blockBrick(int arrayIndex){
        return  MDBlock.brickArray[arrayIndex];
    }
    private Block blockClay(int arrayIndex){
        return  MDBlock.clayArray[arrayIndex];
    }
    private Block blockWool(int arrayIndex){
        return  MDBlock.woolArray[arrayIndex];
    }
    private Block blockCobble(int arrayIndex){
        return  MDBlock.cobbleArray[arrayIndex];
    }
    private Block blockStonebrick(int arrayIndex){
        return  MDBlock.stonebrickArray[arrayIndex];
    }
    private Block blockStonebrickCracked(int arrayIndex){
        return  MDBlock.stonebrickCrackedArray[arrayIndex];
    }
    private Block blockStonebrickCarved(int arrayIndex){
        return  MDBlock.stonebrickCarvedArray[arrayIndex];
    }
    private Block blockStone(int arrayIndex){
        return  MDBlock.stoneArray[arrayIndex];
    }
    private Block blockObsidian(int arrayIndex){
        return  MDBlock.obsidianArray[arrayIndex];
    }
    private Block blockLapis(int arrayIndex){
        return  MDBlock.lapisArray[arrayIndex];
    }
    private Block blockGlowstone(int arrayIndex){
        return  MDBlock.glowstoneArray[arrayIndex];
    }
    private Block blockCoal(int arrayIndex){
        return  MDBlock.coalArray[arrayIndex];
    }
    private Block blockSoulsand(int arrayIndex){
        return  MDBlock.soulsandArray[arrayIndex];
    }
    private Block blockPlank(int arrayIndex){
        return  MDBlock.plankArray[arrayIndex];
    }
    private Block blockRedstone(int arrayIndex){
        return  MDBlock.redstoneArray[arrayIndex];
    }
    private Block blockQuartz(int arrayIndex){
        return  MDBlock.quartzArray[arrayIndex];
    }
    private Block blockGlassFoggy(int arrayIndex){
        return  MDBlock.glassFoggyArray[arrayIndex];
    }
    private Block blockSapling(int arrayIndex){
        return  MDBlock.saplingArray[arrayIndex];
    }
    private Block blockTulip(int arrayIndex){
        return  MDBlock.tulipArray[arrayIndex];
    }
    private Block blockWorkbench(int arrayIndex){
        return  MDBlock.workbenchArray[arrayIndex];
    }



}
