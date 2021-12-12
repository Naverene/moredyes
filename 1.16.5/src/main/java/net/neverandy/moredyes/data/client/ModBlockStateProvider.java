package net.neverandy.moredyes.data.client;


import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.*;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ModBlockStateProvider extends BlockStateProvider {
    public static final Logger LOGGER = LogManager.getLogger();

    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MoreDyes.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        LOGGER.info("Starting for loop");
        for(int i=0; i<ColorStrings.ALL.length;i++){
            LOGGER.info(blockGlass(i).toString());
            simpleBlock(blockGlass(i),texture(blockGlass(i), "glass", i));
//            simpleBlock(blockSand(i));
//            simpleBlock(blockLeaf(i));
//            simpleBlock(blockBrick(i));
//            simpleBlock(blockClay(i));
//            simpleBlock(blockWool(i));
//            simpleBlock(blockCobble(i));
//            simpleBlock(blockStoneBrick(i));
//            simpleBlock(blockStoneBrickCracked(i));
//            simpleBlock(blockStoneBrickCarved(i));
//            simpleBlock(blockStone(i));
//            simpleBlock(blockObsidian(i));
//            simpleBlock(blockLapis(i));
//            simpleBlock(blockGlowstone(i));
//            simpleBlock(blockCoal(i));
//            simpleBlock(blockSoulSand(i));
//            simpleBlock(blockPlank(i));
//            simpleBlock(blockRedstone(i));
//            simpleBlock(blockQuartz(i));
//            simpleBlock(blockGlassFoggy(i));
//            simpleBlock(blockSapling(i));
//            simpleBlock(blockTulip(i));
//            simpleBlock(blockWorkbench(i));
            LOGGER.info(i);
        }

    }

    public ModelFile texture(Block block, String blockType, int arrayIndex){
        ResourceLocation name = block.getRegistryName();
        ResourceLocation resourceLocation = new ResourceLocation(name.getNamespace(),
                ModelProvider.BLOCK_FOLDER + "/" + blockType + "/" + ColorStrings.ALL[arrayIndex]);
        return models().cubeAll(block.getRegistryName().getPath(), resourceLocation);
    }

    private Block blockGlass(int arrayIndex){
            return  MDBlock.glassArray[arrayIndex];
    }
//    private Block blockSand(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return   BLOCKS.register("sand_" + color,() -> new BlockFalling(color, Reference.BLOCK_INFO_SAND)).get();
//    }
//    private Block blockLeaf(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("leaf_"+color,() -> new LeafBlock(color, Reference.BLOCK_INFO_LEAVES)).get();
//    }
//    private Block blockBrick(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("brick_"+color,() -> new BasicBlock(color, Reference.BLOCK_INFO_BRICK)).get();
//    }
//    private Block blockClay(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("clay_"+color,() -> new BasicBlock(color, Reference.BLOCK_INFO_CLAY)).get();
//    }
//    private Block blockWool(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("wool_"+color,() -> new BasicBlock(color, Reference.BLOCK_INFO_WOOL)).get();
//    }
//    private Block blockCobble(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("cobble_"+color,() -> new BasicBlock(color, Reference.BLOCK_INFO_COBBLE)).get();
//    }
//    private Block blockStoneBrick(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("stonebrick_"+color,() -> new BasicBlock(color, Reference.BLOCK_INFO_STONE_BRICK)).get();
//    }
//    private Block blockStoneBrickCracked(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("stonebrickcracked_"+color,() -> new BasicBlock(color, Reference.BLOCK_INFO_STONE_BRICK_CRACKED)).get();
//    }
//    private Block blockStoneBrickCarved(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("stonebrickcarved_"+color,() -> new BasicBlock(color, Reference.BLOCK_INFO_STONE_BRICK_CARVED)).get();
//    }
//    private Block blockStone(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("stone_"+color,() -> new BasicBlock(color, Reference.BLOCK_INFO_STONE)).get();
//    }
//    private Block blockObsidian(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("obsidian_"+color,() -> new BasicBlock(color, Reference.BLOCK_INFO_OBSIDIAN)).get();
//    }
//    private Block blockLapis(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("lapis_"+color,() -> new BasicBlock(color, Reference.BLOCK_INFO_LAPIS)).get();
//    }
//    private Block blockGlowstone(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("glowstone_"+color,() -> new BasicBlock(color, Reference.BLOCK_INFO_GLOWSTONE)).get();
//    }
//    private Block blockCoal(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("coal_"+color,() -> new BasicBlock(color, Reference.BLOCK_INFO_COAL)).get();
//    }
//    private Block blockSoulSand(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("soulsand_"+color,() -> new BasicBlock(color, Reference.BLOCK_INFO_SOULSAND)).get();
//    }
//    private Block blockPlank(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("plank_"+color,() -> new BasicBlock(color, Reference.BLOCK_INFO_PLANK)).get();
//    }
//    private Block blockRedstone(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("redstone_"+color,() -> new PoweredBlock(color, Reference.BLOCK_INFO_REDSTONE)).get();
//    }
//    private Block blockQuartz(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("quartz_"+color,() -> new BasicBlock(color, Reference.BLOCK_INFO_QUARTZ)).get();
//    }
//    private Block blockGlassFoggy(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("glassfoggy_"+color,() -> new BlockGlass(color, Reference.BLOCK_INFO_GLASS_FOGGY, true)).get();
//    }
//    private Block blockSapling(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("sapling_"+color,() -> new BlockSapling(color, Reference.BLOCK_INFO_SAPLING)).get();
//    }
//    private Block blockTulip(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("tulip_"+color,() -> new FlowerBlock(color, Reference.BLOCK_INFO_TULIP)).get();
//    }
//    private Block blockWorkbench(int arrayIndex){
//        final String color = ColorStrings.ALL[arrayIndex];
//        return  BLOCKS.register("workbench_"+color,() -> new WorkbenchBlock(color, Reference.BLOCK_INFO_WORKBENCH)).get();
//    }
}
