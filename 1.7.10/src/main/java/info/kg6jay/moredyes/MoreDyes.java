package info.kg6jay.moredyes;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.compat.chisel.ChiselCompat;
import info.kg6jay.moredyes.compat.gregtech.GTCompat;
import info.kg6jay.moredyes.compat.ironchest.IronChestCompat;
import info.kg6jay.moredyes.compat.storagedrawers.StorageDrawersCompat;
import info.kg6jay.moredyes.compat.thermalexpansion.TECompat;
import info.kg6jay.moredyes.handler.CauldronWashHandler;
import info.kg6jay.moredyes.handler.ConfigHandler;
import info.kg6jay.moredyes.handler.FuelHandler;
import info.kg6jay.moredyes.handler.GuiHandler;
import info.kg6jay.moredyes.handler.SheepHandler;
import info.kg6jay.moredyes.handler.WorldGenHandler;
import info.kg6jay.moredyes.item.MDItem;
import info.kg6jay.moredyes.network.PacketHandler;
import info.kg6jay.moredyes.proxy.CommonProxy;
import info.kg6jay.moredyes.recipe.CraftManager;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.LogHelper;

@Mod(
    modid = Reference.MOD_ID,
    name = Reference.MOD_NAME,
    version = Reference.MOD_VERSION,
    dependencies = "after:ThermalExpansion;after:chisel;after:gregtech;after:IronChest;after:StorageDrawers")
public class MoreDyes {

    @Mod.Instance(Reference.MOD_ID)
    public static MoreDyes instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
    public static CommonProxy proxy;

    // Creative Tabs
    public static CreativeTabs tabDyes = new Tab("Dyes");
    public static CreativeTabs tabBlocks = new Tab("Blocks");
    public static CreativeTabs tabPlants = new Tab("Plants");
    public static CreativeTabs tabTrees = new Tab("Trees");
    public static CreativeTabs tabShapes = new Tab("Stairs, Slabs & Walls");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        proxy.preInit();
        FMLCommonHandler.instance()
            .bus()
            .register(new ConfigHandler());
        MDItem.initialize();
        MDItem.register();
        MDItem.registerOreDict();

        MDBlock.initialize();
        MDBlock.register();
        MDBlock.registerTileEntities();
        MDBlock.registerOreDictionary();

        // Optional compat. The compat classes refer to the other mod's classes, so they are only touched when it is
        // installed.
        if (Loader.isModLoaded(Reference.IRON_CHESTS)) {
            IronChestCompat.preInit();
        }
        if (Loader.isModLoaded(Reference.STORAGE_DRAWERS)) {
            StorageDrawersCompat.preInit();
        }

        // Register GUI handler
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        PacketHandler.initialize();

        WorldGenHandler.initializeWorldGen();
        if (Loader.isModLoaded("ThermalExpansion")) {
            LogHelper.info("Thermal Expansion is loaded");
            TECompat.registerThermalExpansion();
        }
        LogHelper.info("Pre Initialization Complete");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerRenderThings();
        MDBlock.registerFlammability();
        MDBlock.registerWashing();
        MinecraftForge.EVENT_BUS.register(new CauldronWashHandler());
        MinecraftForge.EVENT_BUS.register(new SheepHandler());
        GameRegistry.registerFuelHandler(new FuelHandler());
        CraftManager.addCraftingRecipes();
        CraftManager.addSmeltingRecipes();
        if (Loader.isModLoaded(Reference.IRON_CHESTS)) {
            IronChestCompat.init();
        }
        if (Loader.isModLoaded(Reference.STORAGE_DRAWERS)) {
            StorageDrawersCompat.init();
        }
        LogHelper.info("Initialization Complete");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MDBlock.detectStones();
        CraftManager.addStoneRecipes();
        if (Loader.isModLoaded("chisel")) {
            ChiselCompat.registerChisel();
        }
        if (Loader.isModLoaded("gregtech")) {
            GTCompat.registerRecipes();
        }
        ((Tab) tabDyes).setTabIconItem(MDItem.dye[0]);
        ((Tab) tabBlocks).setTabIconItem(Item.getItemFromBlock(MDBlock.wool[0]));
        ((Tab) tabPlants).setTabIconItem(Item.getItemFromBlock(MDBlock.tulip[0]));
        ((Tab) tabTrees).setTabIconItem(Item.getItemFromBlock(MDBlock.sapling[0]));
        ((Tab) tabShapes).setTabIconItem(Item.getItemFromBlock(MDBlock.stoneStairs));
        LogHelper.info("Post Initialization Complete");
    }

    /**
     * More Dyes 1.0.8 and older (the version GT: New Horizons bundles) used the mod id "MoreDyes", so worlds made with
     * it name every block and item "MoreDyes:...". The names after the mod id are the same now, so those are mapped to
     * this mod's blocks and items instead of being dropped from the world.
     */
    @Mod.EventHandler
    public void missingMappings(FMLMissingMappingsEvent event) {
        String oldPrefix = Reference.OLD_MOD_ID + ":";
        for (FMLMissingMappingsEvent.MissingMapping mapping : event.getAll()) {
            if (!mapping.name.startsWith(oldPrefix)) {
                continue;
            }
            String name = mapping.name.substring(oldPrefix.length());
            if (mapping.type == GameRegistry.Type.BLOCK) {
                Block block = GameRegistry.findBlock(Reference.MOD_ID, name);
                if (block != null) {
                    mapping.remap(block);
                }
            } else {
                Item item = GameRegistry.findItem(Reference.MOD_ID, name);
                if (item != null) {
                    mapping.remap(item);
                }
            }
        }
    }
}
