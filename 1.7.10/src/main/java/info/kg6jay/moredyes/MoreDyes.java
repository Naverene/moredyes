package info.kg6jay.moredyes;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.compat.chisel.ChiselCompat;
import info.kg6jay.moredyes.compat.thermalexpansion.TECompat;
import info.kg6jay.moredyes.handler.ConfigHandler;
import info.kg6jay.moredyes.handler.GuiHandler;
import info.kg6jay.moredyes.handler.WorldGenHandler;
import info.kg6jay.moredyes.item.MDItem;
import info.kg6jay.moredyes.proxy.CommonProxy;
import info.kg6jay.moredyes.recipe.CraftManager;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.LogHelper;

@Mod(
    modid = Reference.MOD_ID,
    name = Reference.MOD_NAME,
    version = Reference.MOD_VERSION,
    dependencies = "after:ThermalExpansion")
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

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.init(event.getSuggestedConfigurationFile());
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

        // Register GUI handler
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        WorldGenHandler.initializeWorldGen();
        if (Loader.isModLoaded("ThermalExpansion")) {
            LogHelper.info("Thermal Expansion is loaded");
            TECompat.registerThermalExpansion();
        }
        LogHelper.info("Pre Initialization Complete");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (Loader.isModLoaded("chisel")) {
            ChiselCompat.registerChisel();
        }
        proxy.registerRenderThings();
        CraftManager.addCraftingRecipes();
        CraftManager.addSmeltingRecipes();
        LogHelper.info("Initialization Complete");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ((Tab) tabDyes).setTabIconItem(MDItem.dye[0]);
        ((Tab) tabBlocks).setTabIconItem(Item.getItemFromBlock(MDBlock.wool[0]));
        ((Tab) tabPlants).setTabIconItem(Item.getItemFromBlock(MDBlock.tulip[0]));
        ((Tab) tabTrees).setTabIconItem(Item.getItemFromBlock(MDBlock.sapling[0]));
        LogHelper.info("Post Initialization Complete");
    }
}
