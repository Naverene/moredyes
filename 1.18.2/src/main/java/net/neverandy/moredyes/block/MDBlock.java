package net.neverandy.moredyes.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.neverandy.moredyes.ConfigHandler;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.BlockInfo;

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
    public static BlockLog[] logArray = new BlockLog[totalColorCount];
    public static BlockItem[] logItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] sandstoneArray = new BasicBlock[totalColorCount];
    public static BlockItem[] sandstoneItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] sandstoneCarvedArray = new BasicBlock[totalColorCount];
    public static BlockItem[] sandstoneCarvedItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] sandstoneSmoothArray = new BasicBlock[totalColorCount];
    public static BlockItem[] sandstoneSmoothItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] andesiteArray = new BasicBlock[totalColorCount];
    public static BlockItem[] andesiteItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] dioriteArray = new BasicBlock[totalColorCount];
    public static BlockItem[] dioriteItemBlockArray = new BlockItem[totalColorCount];
    public static BasicBlock[] concretePowderArray = new BasicBlock[totalColorCount];
    public static BlockItem[] concretePowderItemBlockArray = new BlockItem[totalColorCount];
    public static BlockLadder[] ladderBlockArray = new BlockLadder[totalColorCount];
    public static BlockItem[] ladderBlockItemBlockArray = new BlockItem[totalColorCount];

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);


    public static void initRegistries()
    {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        ConfigHandler.loadConfigFile(ConfigHandler.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get()
                .resolve(Reference.MOD_ID + "-client.toml").toString());
        ConfigHandler.loadConfigFile(ConfigHandler.SERVER_CONFIG, FMLPaths.CONFIGDIR.get()
                .resolve(Reference.MOD_ID + "-server.toml").toString());
    }
    public static void initialize()
    {
        //Init the registries that are added to in the rest of the calls.
        initRegistries();

        registerGlass();
        registerSand();
        registerLeaf();
        registerBrick();
        registerClay();
        registerWool();
        registerCobble();
        registerStonebrick();
        registerStonebrickCracked();
        registerStonebrickCarved();
        registerStone();
        registerObsidian();
        registerLapis();
        registerGlowstone();
        registerCoal();
        registerSoulsand();
        registerPlank();
        registerGlassFoggy();
        registerQuartz();
        registerSapling();
        registerWorkbench();
        registerTulip();
        registerRedstone();
        registerLog();
        registerSandstone();
        registerSandstoneCarved();
        registerSandstonesmooth();
        registerAndesite();
        registerDiorite();
        registerConcretePowder();
        //registerLadder();
    }
    private static void registerDiorite()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String diorite_name = "diorite_" + color;
            final BasicBlock diorite = new BasicBlock(color, Reference.BLOCK_INFO_DIORITE);
            final BlockItem dioriteItem = new BlockItem(diorite, new Item.Properties().group(MoreDyes.tabBlocks));
            dioriteArray[i] = diorite;
            dioriteItemBlockArray[i] = dioriteItem;
            BLOCKS.register(diorite_name, () -> diorite);
            ITEMS.register(diorite_name, () -> dioriteItem);

        }
    }
    private static void registerLadder()
    {
        BlockInfo info = Reference.BLOCK_INFO_LOG;
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String ladder_name = "ladder_" + color;
            final BlockLadder ladder = new BlockLadder(AbstractBlock.Properties.create(info.blockMaterial)
                    .hardnessAndResistance(info.hardness,info.resistance)
                    .harvestLevel(info.harvestLevel).sound(info.sound));
            final BlockItem ladderItem = new BlockItem(ladder, new Item.Properties().group(MoreDyes.tabBlocks));
            ladderBlockArray[i] = ladder;
            ladderBlockItemBlockArray[i] = ladderItem;
            BLOCKS.register(ladder_name, () -> ladder);
            ITEMS.register(ladder_name, () -> ladderItem);

        }
    }
    private static void registerConcretePowder()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String concretePowder_name = "concretepowder_" + color;
            final BasicBlock concretePowder = new BasicBlock(color, Reference.BLOCK_INFO_CONCRETE_POWDER);
            final BlockItem concretePowderItem = new BlockItem(concretePowder, new Item.Properties().group(MoreDyes.tabBlocks));
            concretePowderArray[i] = concretePowder;
            concretePowderItemBlockArray[i] = concretePowderItem;
            BLOCKS.register(concretePowder_name, () -> concretePowder);
            ITEMS.register(concretePowder_name, () -> concretePowderItem);

        }
    }
    private static void registerAndesite()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String andesite_name = "andesite_" + color;
            final BasicBlock andesite = new BasicBlock(color, Reference.BLOCK_INFO_ANDESITE);
            final BlockItem andesiteItem = new BlockItem(andesite, new Item.Properties().group(MoreDyes.tabBlocks));
            andesiteArray[i] = andesite;
            andesiteItemBlockArray[i] = andesiteItem;
            BLOCKS.register(andesite_name, () -> andesite);
            ITEMS.register(andesite_name, () -> andesiteItem);

        }
    }
    private static void registerSandstone()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String sandstone_name = "sandstone_" + color;
            final BasicBlock sandstone = new BasicBlock(color, Reference.BLOCK_INFO_SANDSTONE);
            final BlockItem sandstoneItem = new BlockItem(sandstone, new Item.Properties().group(MoreDyes.tabBlocks));
            sandstoneArray[i] = sandstone;
            sandstoneItemBlockArray[i] = sandstoneItem;
            BLOCKS.register(sandstone_name, () -> sandstone);
            ITEMS.register(sandstone_name, () -> sandstoneItem);

        }
    }
    private static void registerSandstoneCarved()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String sandstone_name = "sandstonecarved_" + color;
            final BasicBlock sandstoneCarved = new BasicBlock(color, Reference.BLOCK_INFO_SANDSTONE);
            final BlockItem sandstoneCarvedItem = new BlockItem(sandstoneCarved, new Item.Properties().group(MoreDyes.tabBlocks));
            sandstoneCarvedArray[i] = sandstoneCarved;
            sandstoneCarvedItemBlockArray[i] = sandstoneCarvedItem;
            BLOCKS.register(sandstone_name, () -> sandstoneCarved);
            ITEMS.register(sandstone_name, () -> sandstoneCarvedItem);
        }
    }
    private static void registerSandstonesmooth()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String sandstone_name = "sandstonesmooth_" + color;
            final BasicBlock sandstoneSmooth = new BasicBlock(color, Reference.BLOCK_INFO_SANDSTONE);
            final BlockItem sandstoneSmoothItem = new BlockItem(sandstoneSmooth, new Item.Properties().group(MoreDyes.tabBlocks));
            sandstoneSmoothArray[i] = sandstoneSmooth;
            sandstoneSmoothItemBlockArray[i] = sandstoneSmoothItem;
            BLOCKS.register(sandstone_name, () -> sandstoneSmooth);
            ITEMS.register(sandstone_name, () -> sandstoneSmoothItem);
        }
    }
    private static void registerGlass()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String glass_name = "glass_" + color;
            final BlockGlass glass = new BlockGlass(color, Reference.BLOCK_INFO_GLASS, true);
            final BlockItem glassItem = new BlockItem(glass, new Item.Properties().group(MoreDyes.tabBlocks));
            RenderTypeLookup.setRenderLayer(glass, RenderType.getCutout());
            glassArray[i] = glass;
            glassItemBlockArray[i] = glassItem;
            BLOCKS.register(glass_name, () -> glass);
            ITEMS.register(glass_name, () -> glassItem);

        }
    }

    private static void registerSand()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];

            String sand_name = "sand_" + color;
            final BlockFalling sand = new BlockFalling(color, Reference.BLOCK_INFO_SAND);
            final BlockItem sandItem = new BlockItem(sand, new Item.Properties().group(MoreDyes.tabBlocks));
            sandArray[i] = sand;
            sandItemBlockArray[i] = sandItem;
            BLOCKS.register(sand_name, () -> sand);
            ITEMS.register(sand_name, () -> sandItem);
        }
    }

    private static void registerLeaf()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];

            String leaf_name = "leaf_" + color;
            final LeafBlock leaf = new LeafBlock(color, Reference.BLOCK_INFO_LEAVES);
            final BlockItem leafItem = new BlockItem(leaf, new Item.Properties().group(MoreDyes.tabTrees));
            leafArray[i] = leaf;
            leafItemBlockArray[i] = leafItem;
            BLOCKS.register(leaf_name, () -> leaf);
            ITEMS.register(leaf_name, () -> leafItem);
        }
    }

    private static void registerBrick()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];

            String brick_name = "brick_" + color;
            final BasicBlock brick = new BasicBlock(color, Reference.BLOCK_INFO_BRICK);
            final BlockItem brickItem = new BlockItem(brick, new Item.Properties().group(MoreDyes.tabBlocks));
            brickArray[i] = brick;
            brickItemBlockArray[i] = brickItem;
            BLOCKS.register(brick_name, () -> brick);
            ITEMS.register(brick_name, () -> brickItem);
        }
    }

    private static void registerClay()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String clay_name = "clay_" + color;
            final BasicBlock clay = new BasicBlock(color, Reference.BLOCK_INFO_CLAY);
            final BlockItem clayItem = new BlockItem(clay, new Item.Properties().group(MoreDyes.tabBlocks));
            clayArray[i] = clay;
            clayItemBlockArray[i] = clayItem;
            BLOCKS.register(clay_name, () -> clay);
            ITEMS.register(clay_name, () -> clayItem);
        }
    }

    private static void registerWool()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String wool_name = "wool_" + color;
            final BasicBlock wool = new BasicBlock(color, Reference.BLOCK_INFO_WOOL);
            final BlockItem woolItem = new BlockItem(wool, new Item.Properties().group(MoreDyes.tabBlocks));
            woolArray[i] = wool;
            woolItemBlockArray[i] = woolItem;
            BLOCKS.register(wool_name, () -> wool);
            ITEMS.register(wool_name, () -> woolItem);
        }
    }

    private static void registerCobble()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String cobble_name = "cobble_" + color;
            final BasicBlock cobble = new BasicBlock(color, Reference.BLOCK_INFO_COBBLE);
            final BlockItem cobbleItem = new BlockItem(cobble, new Item.Properties().group(MoreDyes.tabBlocks));
            cobbleArray[i] = cobble;
            cobbleItemBlockArray[i] = cobbleItem;
            BLOCKS.register(cobble_name, () -> cobble);
            ITEMS.register(cobble_name, () -> cobbleItem);
        }
    }

    private static void registerStonebrick()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String stonebrick_name = "stonebrick_" + color;
            final BasicBlock stonebrick = new BasicBlock(color, Reference.BLOCK_INFO_STONE_BRICK);
            final BlockItem stonebrickItem = new BlockItem(stonebrick, new Item.Properties().group(MoreDyes.tabBlocks));
            stonebrickArray[i] = stonebrick;
            stonebrickItemBlockArray[i] = stonebrickItem;
            BLOCKS.register(stonebrick_name, () -> stonebrick);
            ITEMS.register(stonebrick_name, () -> stonebrickItem);
        }
    }

    private static void registerStonebrickCracked()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String stonebrickCracked_name = "stonebrickcracked_" + color;
            final BasicBlock stonebrickCracked = new BasicBlock(color, Reference.BLOCK_INFO_STONE_BRICK_CRACKED);
            final BlockItem stonebrickCrackedItem = new BlockItem(stonebrickCracked, new Item.Properties().group(MoreDyes.tabBlocks));
            stonebrickCrackedArray[i] = stonebrickCracked;
            stonebrickCrackedItemBlockArray[i] = stonebrickCrackedItem;
            BLOCKS.register(stonebrickCracked_name, () -> stonebrickCracked);
            ITEMS.register(stonebrickCracked_name, () -> stonebrickCrackedItem);
        }
    }

    private static void registerStonebrickCarved()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String stonebrickCarved_name = "stonebrickcarved_" + color;
            final BasicBlock stonebrickCarved = new BasicBlock(color, Reference.BLOCK_INFO_STONE_BRICK_CARVED);
            final BlockItem stonebrickCarvedItem = new BlockItem(stonebrickCarved, new Item.Properties().group(MoreDyes.tabBlocks));
            stonebrickCarvedArray[i] = stonebrickCarved;
            stonebrickCarvedItemBlockArray[i] = stonebrickCarvedItem;
            BLOCKS.register(stonebrickCarved_name, () -> stonebrickCarved);
            ITEMS.register(stonebrickCarved_name, () -> stonebrickCarvedItem);
        }
    }

    private static void registerStone()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String stone_name = "stone_" + color;
            final BasicBlock stone = new BasicBlock(color, Reference.BLOCK_INFO_STONE);
            final BlockItem stoneItem = new BlockItem(stone, new Item.Properties().group(MoreDyes.tabBlocks));
            stoneArray[i] = stone;
            stoneItemBlockArray[i] = stoneItem;
            BLOCKS.register(stone_name, () -> stone);
            ITEMS.register(stone_name, () -> stoneItem);
        }
    }

    private static void registerObsidian()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String obsidian_name = "obsidian_" + color;
            final BasicBlock obsidian = new BasicBlock(color, Reference.BLOCK_INFO_OBSIDIAN);
            final BlockItem obsidianItem = new BlockItem(obsidian, new Item.Properties().group(MoreDyes.tabBlocks));
            obsidianArray[i] = obsidian;
            obsidianItemBlockArray[i] = obsidianItem;
            BLOCKS.register(obsidian_name, () -> obsidian);
            ITEMS.register(obsidian_name, () -> obsidianItem);
        }
    }

    private static void registerLapis()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String lapis_name = "lapis_" + color;
            final BasicBlock lapis = new BasicBlock(color, Reference.BLOCK_INFO_LAPIS);
            final BlockItem lapisItem = new BlockItem(lapis, new Item.Properties().group(MoreDyes.tabBlocks));
            lapisArray[i] = lapis;
            lapisItemBlockArray[i] = lapisItem;
            BLOCKS.register(lapis_name, () -> lapis);
            ITEMS.register(lapis_name, () -> lapisItem);
        }
    }

    private static void registerGlowstone()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String glowstone_name = "glowstone_" + color;
            final BasicBlock glowstone = new BasicBlock(color, Reference.BLOCK_INFO_GLOWSTONE);
            final BlockItem glowstoneItem = new BlockItem(glowstone, new Item.Properties().group(MoreDyes.tabBlocks));
            glowstoneArray[i] = glowstone;
            glowstoneItemBlockArray[i] = glowstoneItem;
            BLOCKS.register(glowstone_name, () -> glowstone);
            ITEMS.register(glowstone_name, () -> glowstoneItem);
        }
    }

    private static void registerCoal()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String coal_name = "coal_" + color;
            final BasicBlock coal = new BasicBlock(color, Reference.BLOCK_INFO_COAL);
            final BlockItem coalItem = new BlockItem(coal, new Item.Properties().group(MoreDyes.tabBlocks));
            coalArray[i] = coal;
            coalItemBlockArray[i] = coalItem;
            BLOCKS.register(coal_name, () -> coal);
            ITEMS.register(coal_name, () -> coalItem);
        }
    }

    private static void registerSoulsand()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String soulsand_name = "soulsand_" + color;
            final BasicBlock soulsand = new BasicBlock(color, Reference.BLOCK_INFO_SOULSAND);
            final BlockItem soulsandItem = new BlockItem(soulsand, new Item.Properties().group(MoreDyes.tabBlocks));
            soulsandArray[i] = soulsand;
            soulsandItemBlockArray[i] = soulsandItem;
            BLOCKS.register(soulsand_name, () -> soulsand);
            ITEMS.register(soulsand_name, () -> soulsandItem);
        }
    }

    private static void registerPlank()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String plank_name = "plank_" + color;
            final BasicBlock plank = new BasicBlock(color, Reference.BLOCK_INFO_PLANK);
            final BlockItem plankItem = new BlockItem(plank, new Item.Properties().group(MoreDyes.tabBlocks));
            plankArray[i] = plank;
            plankItemBlockArray[i] = plankItem;
            BLOCKS.register(plank_name, () -> plank);
            ITEMS.register(plank_name, () -> plankItem);
        }

    }

    private static void registerRedstone()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String redstone_name = "redstone_" + color;
            final PoweredBlock redstone = new PoweredBlock(color, Reference.BLOCK_INFO_REDSTONE);
            final BlockItem redstoneItem = new BlockItem(redstone, new Item.Properties().group(MoreDyes.tabBlocks));
            redstoneArray[i] = redstone;
            redstoneItemBlockArray[i] = redstoneItem;
            BLOCKS.register(redstone_name, () -> redstone);
            ITEMS.register(redstone_name, () -> redstoneItem);
        }

    }

    private static void registerQuartz()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String quartz_name = "quartz_" + color;
            final BasicBlock quartz = new BasicBlock(color, Reference.BLOCK_INFO_QUARTZ);
            final BlockItem quartzItem = new BlockItem(quartz, new Item.Properties().group(MoreDyes.tabBlocks));
            quartzArray[i] = quartz;
            quartzItemBlockArray[i] = quartzItem;
            BLOCKS.register(quartz_name, () -> quartz);
            ITEMS.register(quartz_name, () -> quartzItem);
        }

    }

    private static void registerGlassFoggy() //Doesn't work for some reason?
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String glassFoggy_name = "glassfoggy_" + color;
            final BlockGlass glassFoggy = new BlockGlass(color, Reference.BLOCK_INFO_GLASS_FOGGY, true);
            final BlockItem glassFoggyItem = new BlockItem(glassFoggy, new Item.Properties().group(MoreDyes.tabBlocks));
            glassFoggyArray[i] = glassFoggy;
            glassFoggyItemBlockArray[i] = glassFoggyItem;
            BLOCKS.register(glassFoggy_name, () -> glassFoggy);
            ITEMS.register(glassFoggy_name, () -> glassFoggyItem);
        }
    }

    private static void registerSapling()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String sapling_name = "sapling_" + color;
            final BlockSapling sapling = new BlockSapling(color, Reference.BLOCK_INFO_SAPLING);
            final BlockItem saplingItem = new BlockItem(sapling, new Item.Properties().group(MoreDyes.tabTrees));
            saplingArray[i] = sapling;
            saplingItemBlockArray[i] = saplingItem;
            BLOCKS.register(sapling_name, () -> sapling);
            ITEMS.register(sapling_name, () -> saplingItem);
        }

    }

    private static void registerTulip()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String tulip_name = "tulip_" + color;
            final FlowerBlock tulip = new FlowerBlock(color, Reference.BLOCK_INFO_TULIP);
            final BlockItem tulipItem = new BlockItem(tulip, new Item.Properties().group(MoreDyes.tabPlants));
            tulipArray[i] = tulip;
            tuLipItemBlockArray[i] = tulipItem;
            BLOCKS.register(tulip_name, () -> tulip);
            ITEMS.register(tulip_name, () -> tulipItem);
        }
    }

    private static void registerWorkbench()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String workbench_name = "workbench_" + color;
            final WorkbenchBlock workbench = new WorkbenchBlock(color, Reference.BLOCK_INFO_WORKBENCH);
            final BlockItem workbenchItem = new BlockItem(workbench, new Item.Properties().group(MoreDyes.tabBlocks));
            workbenchArray[i] = workbench;
            workbenchItemBlockArray[i] = workbenchItem;
            BLOCKS.register(workbench_name, () -> workbench);
            ITEMS.register(workbench_name, () -> workbenchItem);
        }
    }
    private static void registerLog ()
    {
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            String color = ColorStrings.ALL[i];
            String workbench_name = "log_" + color;
            final BlockLog log = new BlockLog(color, Reference.BLOCK_INFO_LOG);
            final BlockItem logItem = new BlockItem(log, new Item.Properties().group(MoreDyes.tabTrees));
            logArray[i] = log;
            logItemBlockArray[i] = logItem;
            BLOCKS.register(workbench_name, () -> log);
            ITEMS.register(workbench_name, () -> logItem);
        }

    }
}