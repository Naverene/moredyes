package net.neverandy.moredyes.block;

import net.minecraft.block.Block;
import net.minecraft.block.SaplingBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.setup.ModSetup;

import java.util.ArrayList;

public class MDBlock
{
    private static final int totalColorCount=118;
    public static Block[] brick;
    public static Block[] clay;
    public static Block[] wool;
    public static Block[] cobble;
    public static Block[] stonebrick;
    public static Block[] stonebrickCracked;
    public static Block[] stonebrickCarved;
    public static Block[] stone;
    public static Block[] obsidian;
    public static Block[] lapis;
    public static Block[] glowstone;
    public static Block[] coal;
    public static Block[] soulsand;
    public static Block[] plank;
    public static Block[] redstone;
    public static Block[] quartz;
    public static Block[] glass;
    public static Block[] glassFoggy;
    public static Block[] log;
    //public static Block[] sand;
    public static Block[] leaf;
    public static Block[] sapling;
    public static Block[] tulip;
    public static Block[] workbench;

    //public static Falli sand;
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static ArrayList<RegistryObject<BlockFalling>> SAND;
    public static ArrayList<RegistryObject<Item>> SAND_ITEM;
    public static final RegistryObject<Block> GLASS = BLOCKS.register("glass", () -> new BlockGlass(ColorStrings.ALL, Reference.BLOCK_INFO_SAND, true, 0));
    public static final RegistryObject<Item> GLASS_ITEM = ITEMS.register("glass", () -> new BlockItem(GLASS.get(), new Item.Properties().tab(ModSetup.tabBlocks)));


    public static void initialize()
    {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        String[] all = ColorStrings.ALL;
        for(int i = 0; i < all.length; i++)
        {
            String color = all[i];
            String name = "sand_" + color;
            SAND.add(BLOCKS.register(name, () -> new BlockFalling(name, color, Reference.BLOCK_INFO_SAND)));
            final Block block = SAND.get(i).get();
            SAND_ITEM.add(ITEMS.register(name, () -> new BlockItem(block, new Item.Properties().tab(ModSetup.tabBlocks))));
        }


        //sand = new BlockFalling(ColorStrings.SET_0, Reference.BLOCK_INFO_SAND, 0);
        /*
        sand = new Block[8];
        sand[0] = new Sand0(ColorStrings.SET_0, Reference.BLOCK_INFO_SAND,0);
        sand[1] = new Sand1(ColorStrings.SET_1,Reference.BLOCK_INFO_SAND,1);
        sand[2] = new Sand2(ColorStrings.SET_2,Reference.BLOCK_INFO_SAND,2);
        sand[3] = new Sand3(ColorStrings.SET_3,Reference.BLOCK_INFO_SAND,3);
        sand[4] = new Sand4(ColorStrings.SET_4,Reference.BLOCK_INFO_SAND,4);
        sand[5] = new Sand5(ColorStrings.SET_5,Reference.BLOCK_INFO_SAND,5);
        sand[6] = new Sand6(ColorStrings.SET_6,Reference.BLOCK_INFO_SAND,6);
        sand[7] = new Sand7(ColorStrings.SET_7,Reference.BLOCK_INFO_SAND,7);

        redstone = new Block[8];
        redstone[0] = new Redstone0(ColorStrings.SET_0,Reference.BLOCK_INFO_REDSTONE,0);
        redstone[1] = new Redstone1(ColorStrings.SET_1,Reference.BLOCK_INFO_REDSTONE,1);
        redstone[2] = new Redstone2(ColorStrings.SET_2,Reference.BLOCK_INFO_REDSTONE,2);
        redstone[3] = new Redstone3(ColorStrings.SET_3,Reference.BLOCK_INFO_REDSTONE,3);
        redstone[4] = new Redstone4(ColorStrings.SET_4,Reference.BLOCK_INFO_REDSTONE,4);
        redstone[5] = new Redstone5(ColorStrings.SET_5,Reference.BLOCK_INFO_REDSTONE,5);
        redstone[6] = new Redstone6(ColorStrings.SET_6,Reference.BLOCK_INFO_REDSTONE,6);
        redstone[7] = new Redstone7(ColorStrings.SET_7,Reference.BLOCK_INFO_REDSTONE,7);

        cobble = new Block[8];
        cobble[0] = new Basic0(ColorStrings.SET_0,Reference.BLOCK_INFO_COBBLE,0);
        cobble[1] = new Basic1(ColorStrings.SET_1,Reference.BLOCK_INFO_COBBLE,1);
        cobble[2] = new Basic2(ColorStrings.SET_2,Reference.BLOCK_INFO_COBBLE,2);
        cobble[3] = new Basic3(ColorStrings.SET_3,Reference.BLOCK_INFO_COBBLE,3);
        cobble[4] = new Basic4(ColorStrings.SET_4,Reference.BLOCK_INFO_COBBLE,4);
        cobble[5] = new Basic5(ColorStrings.SET_5,Reference.BLOCK_INFO_COBBLE,5);
        cobble[6] = new Basic6(ColorStrings.SET_6,Reference.BLOCK_INFO_COBBLE,6);
        cobble[7] = new Basic7(ColorStrings.SET_7,Reference.BLOCK_INFO_COBBLE,7);

        coal = new Block[8];
        coal[0] = new Basic0(ColorStrings.SET_0,Reference.BLOCK_INFO_COAL,0);
        coal[1] = new Basic1(ColorStrings.SET_1,Reference.BLOCK_INFO_COAL,1);
        coal[2] = new Basic2(ColorStrings.SET_2,Reference.BLOCK_INFO_COAL,2);
        coal[3] = new Basic3(ColorStrings.SET_3,Reference.BLOCK_INFO_COAL,3);
        coal[4] = new Basic4(ColorStrings.SET_4,Reference.BLOCK_INFO_COAL,4);
        coal[5] = new Basic5(ColorStrings.SET_5,Reference.BLOCK_INFO_COAL,5);
        coal[6] = new Basic6(ColorStrings.SET_6,Reference.BLOCK_INFO_COAL,6);
        coal[7] = new Basic7(ColorStrings.SET_7,Reference.BLOCK_INFO_COAL,7);

        brick = new Block[8];
        brick[0] = new Basic0(ColorStrings.SET_0,Reference.BLOCK_INFO_BRICK,0);
        brick[1] = new Basic1(ColorStrings.SET_1,Reference.BLOCK_INFO_BRICK,1);
        brick[2] = new Basic2(ColorStrings.SET_2,Reference.BLOCK_INFO_BRICK,2);
        brick[3] = new Basic3(ColorStrings.SET_3,Reference.BLOCK_INFO_BRICK,3);
        brick[4] = new Basic4(ColorStrings.SET_4,Reference.BLOCK_INFO_BRICK,4);
        brick[5] = new Basic5(ColorStrings.SET_5,Reference.BLOCK_INFO_BRICK,5);
        brick[6] = new Basic6(ColorStrings.SET_6,Reference.BLOCK_INFO_BRICK,6);
        brick[7] = new Basic7(ColorStrings.SET_7,Reference.BLOCK_INFO_BRICK,7);

        clay = new Block[8];
        clay[0] = new Basic0(ColorStrings.SET_0,Reference.BLOCK_INFO_CLAY,0);
        clay[1] = new Basic1(ColorStrings.SET_1,Reference.BLOCK_INFO_CLAY,1);
        clay[2] = new Basic2(ColorStrings.SET_2,Reference.BLOCK_INFO_CLAY,2);
        clay[3] = new Basic3(ColorStrings.SET_3,Reference.BLOCK_INFO_CLAY,3);
        clay[4] = new Basic4(ColorStrings.SET_4,Reference.BLOCK_INFO_CLAY,4);
        clay[5] = new Basic5(ColorStrings.SET_5,Reference.BLOCK_INFO_CLAY,5);
        clay[6] = new Basic6(ColorStrings.SET_6,Reference.BLOCK_INFO_CLAY,6);
        clay[7] = new Basic7(ColorStrings.SET_7,Reference.BLOCK_INFO_CLAY,7);

        wool = new Block[8];
        wool[0] = new Basic0(ColorStrings.SET_0,Reference.BLOCK_INFO_WOOL,0);
        wool[1] = new Basic1(ColorStrings.SET_1,Reference.BLOCK_INFO_WOOL,1);
        wool[2] = new Basic2(ColorStrings.SET_2,Reference.BLOCK_INFO_WOOL,2);
        wool[3] = new Basic3(ColorStrings.SET_3,Reference.BLOCK_INFO_WOOL,3);
        wool[4] = new Basic4(ColorStrings.SET_4,Reference.BLOCK_INFO_WOOL,4);
        wool[5] = new Basic5(ColorStrings.SET_5,Reference.BLOCK_INFO_WOOL,5);
        wool[6] = new Basic6(ColorStrings.SET_6,Reference.BLOCK_INFO_WOOL,6);
        wool[7] = new Basic7(ColorStrings.SET_7,Reference.BLOCK_INFO_WOOL,7);

        lapis = new Block[8];
        lapis[0] = new Basic0(ColorStrings.SET_0,Reference.BLOCK_INFO_LAPIS,0);
        lapis[1] = new Basic1(ColorStrings.SET_1,Reference.BLOCK_INFO_LAPIS,1);
        lapis[2] = new Basic2(ColorStrings.SET_2,Reference.BLOCK_INFO_LAPIS,2);
        lapis[3] = new Basic3(ColorStrings.SET_3,Reference.BLOCK_INFO_LAPIS,3);
        lapis[4] = new Basic4(ColorStrings.SET_4,Reference.BLOCK_INFO_LAPIS,4);
        lapis[5] = new Basic5(ColorStrings.SET_5,Reference.BLOCK_INFO_LAPIS,5);
        lapis[6] = new Basic6(ColorStrings.SET_6,Reference.BLOCK_INFO_LAPIS,6);
        lapis[7] = new Basic7(ColorStrings.SET_7,Reference.BLOCK_INFO_LAPIS,7);

        obsidian = new Block[8];
        obsidian[0] = new Basic0(ColorStrings.SET_0,Reference.BLOCK_INFO_OBSIDIAN,0);
        obsidian[1] = new Basic1(ColorStrings.SET_1,Reference.BLOCK_INFO_OBSIDIAN,1);
        obsidian[2] = new Basic2(ColorStrings.SET_2,Reference.BLOCK_INFO_OBSIDIAN,2);
        obsidian[3] = new Basic3(ColorStrings.SET_3,Reference.BLOCK_INFO_OBSIDIAN,3);
        obsidian[4] = new Basic4(ColorStrings.SET_4,Reference.BLOCK_INFO_OBSIDIAN,4);
        obsidian[5] = new Basic5(ColorStrings.SET_5,Reference.BLOCK_INFO_OBSIDIAN,5);
        obsidian[6] = new Basic6(ColorStrings.SET_6,Reference.BLOCK_INFO_OBSIDIAN,6);
        obsidian[7] = new Basic7(ColorStrings.SET_7,Reference.BLOCK_INFO_OBSIDIAN,7);

        plank = new Block[8];
        plank[0] = new Basic0(ColorStrings.SET_0,Reference.BLOCK_INFO_PLANK,0);
        plank[1] = new Basic1(ColorStrings.SET_1,Reference.BLOCK_INFO_PLANK,1);
        plank[2] = new Basic2(ColorStrings.SET_2,Reference.BLOCK_INFO_PLANK,2);
        plank[3] = new Basic3(ColorStrings.SET_3,Reference.BLOCK_INFO_PLANK,3);
        plank[4] = new Basic4(ColorStrings.SET_4,Reference.BLOCK_INFO_PLANK,4);
        plank[5] = new Basic5(ColorStrings.SET_5,Reference.BLOCK_INFO_PLANK,5);
        plank[6] = new Basic6(ColorStrings.SET_6,Reference.BLOCK_INFO_PLANK,6);
        plank[7] = new Basic7(ColorStrings.SET_7,Reference.BLOCK_INFO_PLANK,7);

        glowstone = new Block[8];

        glowstone[0] = new Basic0(ColorStrings.SET_0,Reference.BLOCK_INFO_GLOWSTONE,0).setLightLevel(1.0F);
        glowstone[1] = new Basic1(ColorStrings.SET_1,Reference.BLOCK_INFO_GLOWSTONE,1).setLightLevel(1.0F);
        glowstone[2] = new Basic2(ColorStrings.SET_2,Reference.BLOCK_INFO_GLOWSTONE,2).setLightLevel(1.0F);
        glowstone[3] = new Basic3(ColorStrings.SET_3,Reference.BLOCK_INFO_GLOWSTONE,3).setLightLevel(1.0F);
        glowstone[4] = new Basic4(ColorStrings.SET_4,Reference.BLOCK_INFO_GLOWSTONE,4).setLightLevel(1.0F);
        glowstone[5] = new Basic5(ColorStrings.SET_5,Reference.BLOCK_INFO_GLOWSTONE,5).setLightLevel(1.0F);
        glowstone[6] = new Basic6(ColorStrings.SET_6,Reference.BLOCK_INFO_GLOWSTONE,6).setLightLevel(1.0F);
        glowstone[7] = new Basic7(ColorStrings.SET_7,Reference.BLOCK_INFO_GLOWSTONE,7).setLightLevel(1.0F);

        soulsand = new Block[8];
        soulsand[0] = new Basic0(ColorStrings.SET_0,Reference.BLOCK_INFO_SOULSAND,0);
        soulsand[1] = new Basic1(ColorStrings.SET_1,Reference.BLOCK_INFO_SOULSAND,1);
        soulsand[2] = new Basic2(ColorStrings.SET_2,Reference.BLOCK_INFO_SOULSAND,2);
        soulsand[3] = new Basic3(ColorStrings.SET_3,Reference.BLOCK_INFO_SOULSAND,3);
        soulsand[4] = new Basic4(ColorStrings.SET_4,Reference.BLOCK_INFO_SOULSAND,4);
        soulsand[5] = new Basic5(ColorStrings.SET_5,Reference.BLOCK_INFO_SOULSAND,5);
        soulsand[6] = new Basic6(ColorStrings.SET_6,Reference.BLOCK_INFO_SOULSAND,6);
        soulsand[7] = new Basic7(ColorStrings.SET_7,Reference.BLOCK_INFO_SOULSAND,7);

        quartz = new Block[8];
        quartz[0] = new Basic0(ColorStrings.SET_0,Reference.BLOCK_INFO_QUARTZ,0);
        quartz[1] = new Basic1(ColorStrings.SET_1,Reference.BLOCK_INFO_QUARTZ,1);
        quartz[2] = new Basic2(ColorStrings.SET_2,Reference.BLOCK_INFO_QUARTZ,2);
        quartz[3] = new Basic3(ColorStrings.SET_3,Reference.BLOCK_INFO_QUARTZ,3);
        quartz[4] = new Basic4(ColorStrings.SET_4,Reference.BLOCK_INFO_QUARTZ,4);
        quartz[5] = new Basic5(ColorStrings.SET_5,Reference.BLOCK_INFO_QUARTZ,5);
        quartz[6] = new Basic6(ColorStrings.SET_6,Reference.BLOCK_INFO_QUARTZ,6);
        quartz[7] = new Basic7(ColorStrings.SET_7,Reference.BLOCK_INFO_QUARTZ,7);

        stone = new Block[8];
        stone[0] = new Basic0(ColorStrings.SET_0,Reference.BLOCK_INFO_STONE,0);
        stone[1] = new Basic1(ColorStrings.SET_1,Reference.BLOCK_INFO_STONE,1);
        stone[2] = new Basic2(ColorStrings.SET_2,Reference.BLOCK_INFO_STONE,2);
        stone[3] = new Basic3(ColorStrings.SET_3,Reference.BLOCK_INFO_STONE,3);
        stone[4] = new Basic4(ColorStrings.SET_4,Reference.BLOCK_INFO_STONE,4);
        stone[5] = new Basic5(ColorStrings.SET_5,Reference.BLOCK_INFO_STONE,5);
        stone[6] = new Basic6(ColorStrings.SET_6,Reference.BLOCK_INFO_STONE,6);
        stone[7] = new Basic7(ColorStrings.SET_7,Reference.BLOCK_INFO_STONE,7);

        stonebrick = new Block[8];
        stonebrick[0] = new Basic0(ColorStrings.SET_0,Reference.BLOCK_INFO_STONE_BRICK,0);
        stonebrick[1] = new Basic1(ColorStrings.SET_1,Reference.BLOCK_INFO_STONE_BRICK,1);
        stonebrick[2] = new Basic2(ColorStrings.SET_2,Reference.BLOCK_INFO_STONE_BRICK,2);
        stonebrick[3] = new Basic3(ColorStrings.SET_3,Reference.BLOCK_INFO_STONE_BRICK,3);
        stonebrick[4] = new Basic4(ColorStrings.SET_4,Reference.BLOCK_INFO_STONE_BRICK,4);
        stonebrick[5] = new Basic5(ColorStrings.SET_5,Reference.BLOCK_INFO_STONE_BRICK,5);
        stonebrick[6] = new Basic6(ColorStrings.SET_6,Reference.BLOCK_INFO_STONE_BRICK,6);
        stonebrick[7] = new Basic7(ColorStrings.SET_7,Reference.BLOCK_INFO_STONE_BRICK,7);

        stonebrickCracked = new Block[8];
        stonebrickCracked[0] = new Basic0(ColorStrings.SET_0,Reference.BLOCK_INFO_STONE_BRICK_CRACKED,0);
        stonebrickCracked[1] = new Basic1(ColorStrings.SET_1,Reference.BLOCK_INFO_STONE_BRICK_CRACKED,1);
        stonebrickCracked[2] = new Basic2(ColorStrings.SET_2,Reference.BLOCK_INFO_STONE_BRICK_CRACKED,2);
        stonebrickCracked[3] = new Basic3(ColorStrings.SET_3,Reference.BLOCK_INFO_STONE_BRICK_CRACKED,3);
        stonebrickCracked[4] = new Basic4(ColorStrings.SET_4,Reference.BLOCK_INFO_STONE_BRICK_CRACKED,4);
        stonebrickCracked[5] = new Basic5(ColorStrings.SET_5,Reference.BLOCK_INFO_STONE_BRICK_CRACKED,5);
        stonebrickCracked[6] = new Basic6(ColorStrings.SET_6,Reference.BLOCK_INFO_STONE_BRICK_CRACKED,6);
        stonebrickCracked[7] = new Basic7(ColorStrings.SET_7,Reference.BLOCK_INFO_STONE_BRICK_CRACKED,7);

        stonebrickCarved = new Block[8];
        stonebrickCarved[0] = new Basic0(ColorStrings.SET_0,Reference.BLOCK_INFO_STONE_BRICK_CARVED,0);
        stonebrickCarved[1] = new Basic1(ColorStrings.SET_1,Reference.BLOCK_INFO_STONE_BRICK_CARVED,1);
        stonebrickCarved[2] = new Basic2(ColorStrings.SET_2,Reference.BLOCK_INFO_STONE_BRICK_CARVED,2);
        stonebrickCarved[3] = new Basic3(ColorStrings.SET_3,Reference.BLOCK_INFO_STONE_BRICK_CARVED,3);
        stonebrickCarved[4] = new Basic4(ColorStrings.SET_4,Reference.BLOCK_INFO_STONE_BRICK_CARVED,4);
        stonebrickCarved[5] = new Basic5(ColorStrings.SET_5,Reference.BLOCK_INFO_STONE_BRICK_CARVED,5);
        stonebrickCarved[6] = new Basic6(ColorStrings.SET_6,Reference.BLOCK_INFO_STONE_BRICK_CARVED,6);
        stonebrickCarved[7] = new Basic7(ColorStrings.SET_7,Reference.BLOCK_INFO_STONE_BRICK_CARVED,7);

        glass = new Block[8];
        glass[0] = new Glass0(ColorStrings.SET_0,Reference.BLOCK_INFO_GLASS,true,0);
        glass[1] = new Glass1(ColorStrings.SET_1,Reference.BLOCK_INFO_GLASS,true,1);
        glass[2] = new Glass2(ColorStrings.SET_2,Reference.BLOCK_INFO_GLASS,true,2);
        glass[3] = new Glass3(ColorStrings.SET_3,Reference.BLOCK_INFO_GLASS,true,3);
        glass[4] = new Glass4(ColorStrings.SET_4,Reference.BLOCK_INFO_GLASS,true,4);
        glass[5] = new Glass5(ColorStrings.SET_5,Reference.BLOCK_INFO_GLASS,true,5);
        glass[6] = new Glass6(ColorStrings.SET_6,Reference.BLOCK_INFO_GLASS,true,6);
        glass[7] = new Glass7(ColorStrings.SET_7,Reference.BLOCK_INFO_GLASS,true,7);

        glassFoggy = new Block[8];
        glassFoggy[0] = new Glass0(ColorStrings.SET_0,Reference.BLOCK_INFO_GLASS_FOGGY,false,0);
        glassFoggy[1] = new Glass1(ColorStrings.SET_1,Reference.BLOCK_INFO_GLASS_FOGGY,false,1);
        glassFoggy[2] = new Glass2(ColorStrings.SET_2,Reference.BLOCK_INFO_GLASS_FOGGY,false,2);
        glassFoggy[3] = new Glass3(ColorStrings.SET_3,Reference.BLOCK_INFO_GLASS_FOGGY,false,3);
        glassFoggy[4] = new Glass4(ColorStrings.SET_4,Reference.BLOCK_INFO_GLASS_FOGGY,false,4);
        glassFoggy[5] = new Glass5(ColorStrings.SET_5,Reference.BLOCK_INFO_GLASS_FOGGY,false,5);
        glassFoggy[6] = new Glass6(ColorStrings.SET_6,Reference.BLOCK_INFO_GLASS_FOGGY,false,6);
        glassFoggy[7] = new Glass7(ColorStrings.SET_7,Reference.BLOCK_INFO_GLASS_FOGGY,false,7);

        workbench = new Block[8];
        workbench[0] = new Workbench0(ColorStrings.SET_0,Reference.BLOCK_INFO_WORKBENCH,0);
        workbench[1] = new Workbench1(ColorStrings.SET_1,Reference.BLOCK_INFO_WORKBENCH,1);
        workbench[2] = new Workbench2(ColorStrings.SET_2,Reference.BLOCK_INFO_WORKBENCH,2);
        workbench[3] = new Workbench3(ColorStrings.SET_3,Reference.BLOCK_INFO_WORKBENCH,3);
        workbench[4] = new Workbench4(ColorStrings.SET_4,Reference.BLOCK_INFO_WORKBENCH,4);
        workbench[5] = new Workbench5(ColorStrings.SET_5,Reference.BLOCK_INFO_WORKBENCH,5);
        workbench[6] = new Workbench6(ColorStrings.SET_6,Reference.BLOCK_INFO_WORKBENCH,6);
        workbench[7] = new Workbench7(ColorStrings.SET_7,Reference.BLOCK_INFO_WORKBENCH,7);

        log = new Block[totalColorCount];
        leaf = new Block[totalColorCount];
        sapling = new Block[totalColorCount];
        tulip = new Block[totalColorCount];

         */
        /*
        for(int i=0;i<totalColorCount;i++)
        {
            //log[i] = new LogBlock(ColorStrings.ALL[i],Reference.BLOCK_INFO_LOG);
            leaf[i]=new LeafBlock(ColorStrings.ALL[i],Reference.BLOCK_INFO_LEAVES,i);
            sapling[i]=new BlockSapling(ColorStrings.ALL[i],Reference.BLOCK_INFO_SAPLING,i);
            tulip[i]=new FlowerBlock(ColorStrings.ALL[i],Reference.BLOCK_INFO_TULIP);
        }

         */

    }
}
