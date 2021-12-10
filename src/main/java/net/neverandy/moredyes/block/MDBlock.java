package net.neverandy.moredyes.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.setup.ModSetup;

public class MDBlock
{
    private static final int totalColorCount = 118;
    public static BasicBlock[] brickArray = new BasicBlock[totalColorCount];
    public static BlockItem[] brickItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] clayArray = new BasicBlock[totalColorCount];
    public static BlockItem[] clayItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] woolArray = new BasicBlock[totalColorCount];
    public static BlockItem[] woolItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] cobbleArray = new BasicBlock[totalColorCount];
    public static BlockItem[] cobbleItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] stonebrickArray = new BasicBlock[totalColorCount];
    public static BlockItem[] stonebrickItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] stonebrickCrackedArray = new BasicBlock[totalColorCount];
    public static BlockItem[] stonebrickCrackedItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] stonebrickCarvedArray = new BasicBlock[totalColorCount];
    public static BlockItem[] stonebrickCarvedItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] stoneArray = new BasicBlock[totalColorCount];
    public static BlockItem[] stoneItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] obsidianArray = new BasicBlock[totalColorCount];
    public static BlockItem[] obsidianItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] lapisArray = new BasicBlock[totalColorCount];
    public static BlockItem[] lapisItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] glowstoneArray = new BasicBlock[totalColorCount];
    public static BlockItem[] glowstoneItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] coalArray = new BasicBlock[totalColorCount];
    public static BlockItem[] coalItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] soulsandArray = new BasicBlock[totalColorCount];
    public static BlockItem[] soulsandItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] plankArray = new BasicBlock[totalColorCount];
    public static BlockItem[] plankItemBlockArray = new BlockItem[totalColorCount];
    public static PoweredBlock[] redstoneArray = new PoweredBlock[totalColorCount];
    public static BlockItem[] redstoneItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] quartzArray = new BasicBlock[totalColorCount];
    public static BlockItem[] quartzItemBlockArray = new BlockItem[totalColorCount];
    public static BlockGlass[] glassArray = new BlockGlass[totalColorCount];
    public static BlockItem[] glassItemBlockArray = new BlockItem[totalColorCount];
    public static BlockGlass[] glassFoggyArray = new BlockGlass[totalColorCount];
    public static BlockItem[] glassFoggyItemBlockArray = new BlockItem[totalColorCount];
    //public static Block[] log;
    public static BlockFalling[] sandArray = new BlockFalling[totalColorCount];
    public static BlockItem[] sandItemBlockArray = new BlockItem[totalColorCount];
    public static LeafBlock[] leafArray = new LeafBlock[totalColorCount];
    public static BlockItem[] leafItemBlockArray = new BlockItem[totalColorCount];
    public static BlockSapling[] saplingArray = new BlockSapling[totalColorCount];
    public static BlockItem[] saplingItemBlockArray = new BlockItem[totalColorCount];
    public static FlowerBlock[] tulipArray = new FlowerBlock[totalColorCount];
    public static BlockItem[] tuLipItemBlockArray = new BlockItem[totalColorCount];
    public static WorkbenchBlock[] workbenchArray = new WorkbenchBlock[totalColorCount];
    public static BlockItem[] workbenchItemBlockArray = new BlockItem[totalColorCount];

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    //public static BlockFalling[] SAND = new BlockFalling[totalColorCount];


    public static void initialize()
    {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        String[] all = ColorStrings.ALL;

        for (int i = 0; i < all.length; i++)
        {
            String color = all[i];
            String glass_name = "glass_" + color;
            final BlockGlass glass = new BlockGlass(color, Reference.BLOCK_INFO_GLASS, true);
            final BlockItem glassItem = new BlockItem(glass, new Item.Properties().tab(ModSetup.tabBlocks));
            glassArray[i] = glass;
            //glassArray[i].initModel(glass_name);
            glassItemBlockArray[i] = glassItem;
            BLOCKS.register(glass_name,() -> glass);
            ITEMS.register(glass_name, () -> glassItem);

            String sand_name = "sand_" + color;
            final BlockFalling sand = new BlockFalling(color, Reference.BLOCK_INFO_SAND);
            final BlockItem sandItem = new BlockItem(sand, new Item.Properties().tab(ModSetup.tabBlocks));
            sandArray[i] = sand;
            //sandArray[i].initModel(sand_name);
            sandItemBlockArray[i] = sandItem;
            BLOCKS.register(sand_name,() -> sand);
            ITEMS.register(sand_name, () -> sandItem);

            String leaf_name = "leaf_" + color;
            final LeafBlock leaf = new LeafBlock(color, Reference.BLOCK_INFO_LEAVES);
            final BlockItem leafItem = new BlockItem(leaf, new Item.Properties().tab(ModSetup.tabBlocks));
            leafArray[i] = leaf;
            //leafArray[i].initModel(leaf_name);
            leafItemBlockArray[i] = leafItem;
            BLOCKS.register(leaf_name,() -> leaf);
            ITEMS.register(leaf_name, () -> leafItem);

            String brick_name = "brick_" + color;
            final BasicBlock brick = new BasicBlock(color, Reference.BLOCK_INFO_BRICK);
            final BlockItem brickItem = new BlockItem(brick, new Item.Properties().tab(ModSetup.tabBlocks));
            brickArray[i] = brick;
            //brickArray[i].initModel(brick_name);
            brickItemBlockArray[i] = brickItem;
            BLOCKS.register(brick_name,() -> brick);
            ITEMS.register(brick_name, () -> brickItem);

            String clay_name = "clay_" + color;
            final BasicBlock clay = new BasicBlock(color, Reference.BLOCK_INFO_CLAY);
            final BlockItem clayItem = new BlockItem(clay, new Item.Properties().tab(ModSetup.tabBlocks));
            clayArray[i] = clay;
            //clayArray[i].initModel(clay_name);
            clayItemBlockArray[i] = clayItem;
            BLOCKS.register(clay_name,() -> clay);
            ITEMS.register(clay_name, () -> clayItem);

            String wool_name = "wool_" + color;
            final BasicBlock wool = new BasicBlock(color, Reference.BLOCK_INFO_WOOL);
            final BlockItem woolItem = new BlockItem(wool, new Item.Properties().tab(ModSetup.tabBlocks));
            woolArray[i] = wool;
            //woolArray[i].initModel(wool_name);
            woolItemBlockArray[i] = woolItem;
            BLOCKS.register(wool_name,() -> wool);
            ITEMS.register(wool_name, () -> woolItem);

            String cobble_name = "cobble_" + color;
            final BasicBlock cobble = new BasicBlock(color, Reference.BLOCK_INFO_COBBLE);
            final BlockItem cobbleItem = new BlockItem(cobble, new Item.Properties().tab(ModSetup.tabBlocks));
            cobbleArray[i] = cobble;
            //cobbleArray[i].initModel(cobble_name);
            cobbleItemBlockArray[i] = cobbleItem;
            BLOCKS.register(cobble_name,() -> cobble);
            ITEMS.register(cobble_name, () -> cobbleItem);

            String stonebrick_name = "stonebrick_" + color;
            final BasicBlock stonebrick = new BasicBlock(color, Reference.BLOCK_INFO_STONE_BRICK);
            final BlockItem stonebrickItem = new BlockItem(stonebrick, new Item.Properties().tab(ModSetup.tabBlocks));
            stonebrickArray[i] = stonebrick;
            //stonebrickArray[i].initModel(stonebrick_name);
            stonebrickItemBlockArray[i] = stonebrickItem;
            BLOCKS.register(stonebrick_name,() -> stonebrick);
            ITEMS.register(stonebrick_name, () -> stonebrickItem);

            String stonebrickCracked_name = "stonebrickcracked_" + color;
            final BasicBlock stonebrickCracked = new BasicBlock(color, Reference.BLOCK_INFO_STONE_BRICK_CRACKED);
            final BlockItem stonebrickCrackedItem = new BlockItem(stonebrickCracked, new Item.Properties().tab(ModSetup.tabBlocks));
            stonebrickCrackedArray[i] = stonebrickCracked;
            //stonebrickCrackedArray[i].initModel(stonebrickCracked_name);
            stonebrickCrackedItemBlockArray[i] = stonebrickCrackedItem;
            BLOCKS.register(stonebrickCracked_name,() -> stonebrickCracked);
            ITEMS.register(stonebrickCracked_name, () -> stonebrickCrackedItem);

            String stonebrickCarved_name = "stonebrickcarved_" + color;
            final BasicBlock stonebrickCarved = new BasicBlock(color, Reference.BLOCK_INFO_STONE_BRICK_CRACKED);
            final BlockItem stonebrickCarvedItem = new BlockItem(stonebrickCarved, new Item.Properties().tab(ModSetup.tabBlocks));
            stonebrickCarvedArray[i] = stonebrickCarved;
            //stonebrickCarvedArray[i].initModel(stonebrickCarved_name);
            stonebrickCarvedItemBlockArray[i] = stonebrickCrackedItem;
            BLOCKS.register(stonebrickCarved_name,() -> stonebrickCarved);
            ITEMS.register(stonebrickCarved_name, () -> stonebrickCarvedItem);
            
            String stone_name = "stone_" + color;
            final BasicBlock stone = new BasicBlock(color, Reference.BLOCK_INFO_STONE);
            final BlockItem stoneItem = new BlockItem(stone, new Item.Properties().tab(ModSetup.tabBlocks));
            stoneArray[i] = stone;
            //stoneArray[i].initModel(stone_name);
            stoneItemBlockArray[i] = stoneItem;
            BLOCKS.register(stone_name,() -> stone);
            ITEMS.register(stone_name, () -> stoneItem);

            String obsidian_name = "obsidian_" + color;
            final BasicBlock obsidian = new BasicBlock(color, Reference.BLOCK_INFO_OBSIDIAN);
            final BlockItem obsidianItem = new BlockItem(obsidian, new Item.Properties().tab(ModSetup.tabBlocks));
            obsidianArray[i] = obsidian;
            //obsidianArray[i].initModel(obsidian_name);
            obsidianItemBlockArray[i] = obsidianItem;
            BLOCKS.register(obsidian_name,() -> obsidian);
            ITEMS.register(obsidian_name, () -> obsidianItem);

            String lapis_name = "lapis_" + color;
            final BasicBlock lapis = new BasicBlock(color, Reference.BLOCK_INFO_LAPIS);
            final BlockItem lapisItem = new BlockItem(lapis, new Item.Properties().tab(ModSetup.tabBlocks));
            lapisArray[i] = lapis;
            //lapisArray[i].initModel(lapis_name);
            lapisItemBlockArray[i] = lapisItem;
            BLOCKS.register(lapis_name,() -> lapis);
            ITEMS.register(lapis_name, () -> lapisItem);

            String glowstone_name = "glowstone_" + color;
            final BasicBlock glowstone = new BasicBlock(color, Reference.BLOCK_INFO_GLOWSTONE);
            final BlockItem glowstoneItem = new BlockItem(glowstone, new Item.Properties().tab(ModSetup.tabBlocks));
            glowstoneArray[i] = glowstone;
            //glowstoneArray[i].initModel(glowstone_name);
            glowstoneItemBlockArray[i] = glowstoneItem;
            BLOCKS.register(glowstone_name,() -> glowstone);
            ITEMS.register(glowstone_name, () -> glowstoneItem);

            String coal_name = "coal_" + color;
            final BasicBlock coal = new BasicBlock(color, Reference.BLOCK_INFO_COAL);
            final BlockItem coalItem = new BlockItem(coal, new Item.Properties().tab(ModSetup.tabBlocks));
            coalArray[i] = coal;
            //coalArray[i].initModel(coal_name);
            coalItemBlockArray[i] = coalItem;
            BLOCKS.register(coal_name,() -> coal);
            ITEMS.register(coal_name, () -> coalItem);

            String soulsand_name = "soulsand_" + color;
            final BasicBlock soulsand = new BasicBlock(color, Reference.BLOCK_INFO_SOULSAND);
            final BlockItem soulsandItem = new BlockItem(soulsand, new Item.Properties().tab(ModSetup.tabBlocks));
            soulsandArray[i] = soulsand;
            //soulsandArray[i].initModel(soulsand_name);
            soulsandItemBlockArray[i] = soulsandItem;
            BLOCKS.register(soulsand_name,() -> soulsand);
            ITEMS.register(soulsand_name, () -> soulsandItem);

            String plank_name = "plank_" + color;
            final BasicBlock plank = new BasicBlock(color, Reference.BLOCK_INFO_PLANK);
            final BlockItem plankItem = new BlockItem(plank, new Item.Properties().tab(ModSetup.tabBlocks));
            plankArray[i] = plank;
            //plankArray[i].initModel(plank_name);
            plankItemBlockArray[i] = plankItem;
            BLOCKS.register(plank_name,() -> plank);
            ITEMS.register(plank_name, () -> plankItem);

            String redstone_name = "redstone_" + color;
            final PoweredBlock redstone = new PoweredBlock(color, Reference.BLOCK_INFO_REDSTONE);
            final BlockItem redstoneItem = new BlockItem(redstone, new Item.Properties().tab(ModSetup.tabBlocks));
            redstoneArray[i] = redstone;
            //redstoneArray[i].initModel(redstone_name);
            redstoneItemBlockArray[i] = redstoneItem;
            BLOCKS.register(redstone_name,() -> redstone);
            ITEMS.register(redstone_name, () -> redstoneItem);

            String quartz_name = "quartz_" + color;
            final BasicBlock quartz = new BasicBlock(color, Reference.BLOCK_INFO_QUARTZ);
            final BlockItem quartzItem = new BlockItem(quartz, new Item.Properties().tab(ModSetup.tabBlocks));
            quartzArray[i] = quartz;
            //quartzArray[i].initModel(quartz_name);
            quartzItemBlockArray[i] = quartzItem;
            BLOCKS.register(quartz_name,() -> quartz);
            ITEMS.register(quartz_name, () -> quartzItem);

            String glassFoggy_name = "glassfoggy_" + color;
            final BlockGlass glassFoggy = new BlockGlass(color, Reference.BLOCK_INFO_GLASS_FOGGY, true);
            final BlockItem glassFoggyItem = new BlockItem(glassFoggy, new Item.Properties().tab(ModSetup.tabBlocks));
            glassFoggyArray[i] = glassFoggy;
            //glassFoggyArray[i].initModel(glassFoggy_name);
            glassFoggyItemBlockArray[i] = glassFoggyItem;
            BLOCKS.register(glassFoggy_name,() -> glassFoggy);
            ITEMS.register(glassFoggy_name, () -> glassFoggyItem);

            String sapling_name = "sapling_" + color;
            final BlockSapling sapling = new BlockSapling(color, Reference.BLOCK_INFO_SAPLING);
            final BlockItem saplingItem = new BlockItem(sapling, new Item.Properties().tab(ModSetup.tabBlocks));
            saplingArray[i] = sapling;
            //saplingArray[i].initModel(sapling_name);
            saplingItemBlockArray[i] = saplingItem;
            BLOCKS.register(sapling_name,() -> sapling);
            ITEMS.register(sapling_name, () -> saplingItem);

            String tulip_name = "tulip_" + color;
            final FlowerBlock tulip = new FlowerBlock(color, Reference.BLOCK_INFO_TULIP);
            final BlockItem tulipItem = new BlockItem(tulip, new Item.Properties().tab(ModSetup.tabBlocks));
            tulipArray[i] = tulip;
            //tulipArray[i].initModel(tulip_name);
            tuLipItemBlockArray[i] = tulipItem;
            BLOCKS.register(tulip_name,() -> tulip);
            ITEMS.register(tulip_name, () -> tulipItem);

            String workbench_name = "workbench_" + color;
            final WorkbenchBlock workbench = new WorkbenchBlock(color, Reference.BLOCK_INFO_WORKBENCH);
            final BlockItem workbenchItem = new BlockItem(workbench, new Item.Properties().tab(ModSetup.tabBlocks));
            workbenchArray[i] = workbench;
            //workbenchArray[i].initModel(workbench_name);
            workbenchItemBlockArray[i] = workbenchItem;
            BLOCKS.register(workbench_name,() -> workbench);
            ITEMS.register(workbench_name, () -> workbenchItem);

            //TODO: Add Diorite/etc

            //BLOCKS.register(glass_name, () -> glass);
            //ITEMS.register(glass_name, () -> glassItem);

            //SAND[i] = new BlockFalling(name, color, Reference.BLOCK_INFO_SAND);
            //SAND_ITEM[i] = new BlockItem(SAND[i], new Item.Properties().tab(ModSetup.tabBlocks));
            //BLOCKS.register(name, () -> glass);
            //ITEMS.register(name, () -> blockItem);
        }
    }

}
