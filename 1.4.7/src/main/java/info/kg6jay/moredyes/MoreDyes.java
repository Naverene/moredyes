package info.kg6jay.moredyes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import info.kg6jay.moredyes.block.MDBlocks;
import info.kg6jay.moredyes.entity.EntityFallingDyed;
import info.kg6jay.moredyes.handler.BonemealHandler;
import info.kg6jay.moredyes.handler.CauldronWashHandler;
import info.kg6jay.moredyes.handler.FuelHandler;
import info.kg6jay.moredyes.handler.GuiHandler;
import info.kg6jay.moredyes.handler.SheepHandler;
import info.kg6jay.moredyes.item.MDItems;
import info.kg6jay.moredyes.network.PacketHandler;
import info.kg6jay.moredyes.proxy.CommonProxy;
import info.kg6jay.moredyes.recipe.Recipes;
import info.kg6jay.moredyes.world.PlantGenerator;
import info.kg6jay.moredyes.world.StoneGenerator;

// The version comes from mcmod.info, which the build fills in from gradle.properties.
@Mod(modid = MoreDyes.MOD_ID, name = "More Dyes", useMetadata = true)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { PacketHandler.CHANNEL },
    packetHandler = PacketHandler.class)
public class MoreDyes {

    public static final String MOD_ID = "moredyes";

    @Instance(MOD_ID)
    public static MoreDyes instance;

    @SidedProxy(clientSide = "info.kg6jay.moredyes.proxy.ClientProxy",
        serverSide = "info.kg6jay.moredyes.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static Config config;

    public static final Tab tabDyes = new Tab("Dyes");
    public static final Tab tabBlocks = new Tab("Blocks");
    public static final Tab tabPlants = new Tab("Plants");
    public static final Tab tabTrees = new Tab("Trees");
    public static final Tab tabShapes = new Tab("Stairs, Slabs & Walls");

    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        config = new Config(event.getSuggestedConfigurationFile());
        proxy.registerRenderers();

        MDBlocks.init(config.file);
        MDItems.init(config.file);
        config.save();
        MDBlocks.register();
        LanguageRegistry.instance().addStringLocalization("tile.moredyes.granitePlain.name", "Granite");
        LanguageRegistry.instance().addStringLocalization("tile.moredyes.dioritePlain.name", "Diorite");
        LanguageRegistry.instance().addStringLocalization("tile.moredyes.andesitePlain.name", "Andesite");

        // Same ore names as the vanilla blocks, so recipes that take any planks or logs take dyed ones too.
        OreDictionary.registerOre("plankWood", new ItemStack(MDBlocks.plank, 1, -1));
        OreDictionary.registerOre("logWood", new ItemStack(MDBlocks.log, 1, -1));
        OreDictionary.registerOre("treeSapling", new ItemStack(MDBlocks.sapling, 1, -1));
        OreDictionary.registerOre("treeLeaves", new ItemStack(MDBlocks.leaves, 1, -1));

        EntityRegistry.registerModEntity(EntityFallingDyed.class, "FallingDyed", 0, this, 160, 20, true);
        NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
        GameRegistry.registerWorldGenerator(new PlantGenerator());
        GameRegistry.registerWorldGenerator(new StoneGenerator());
    }

    @Init
    public void init(FMLInitializationEvent event) {
        Recipes.addCrafting();
        Recipes.addSmelting();
        GameRegistry.registerFuelHandler(new FuelHandler());
        MinecraftForge.EVENT_BUS.register(new SheepHandler());
        MinecraftForge.EVENT_BUS.register(new CauldronWashHandler());
        MinecraftForge.EVENT_BUS.register(new BonemealHandler());
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event) {
        tabDyes.setIcon(new ItemStack(MDItems.dye, 1, 0));
        tabBlocks.setIcon(new ItemStack(MDBlocks.wool, 1, 0));
        tabPlants.setIcon(new ItemStack(MDBlocks.tulip, 1, 0));
        tabTrees.setIcon(new ItemStack(MDBlocks.sapling, 1, 0));
        tabShapes.setIcon(new ItemStack(MDBlocks.SHAPES.get(0).block, 1, 0));
    }
}
