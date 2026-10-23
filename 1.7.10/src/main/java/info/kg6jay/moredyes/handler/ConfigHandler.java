package info.kg6jay.moredyes.handler;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import info.kg6jay.moredyes.reference.Reference;

public class ConfigHandler {

    public static Configuration config;
    public static boolean chisel_plank = true;
    public static boolean chisel_support = true;
    public static boolean worldgen_flower = true;
    public static boolean worldgen_tree = true;
    public static boolean worldgen_granite = true;
    public static boolean worldgen_diorite = true;
    public static boolean worldgen_andesite = true;
    public static boolean preventMobSpawning = true;
    public static double sheepSpawnChance = WildSheepColors.NATURAL_CHANCE;
    public static String[] sheepSpawnColors = WildSheepColors.NATURAL;
    public static double rareSheepSpawnChance = WildSheepColors.RARE_CHANCE;
    public static String[] rareSheepSpawnColors = WildSheepColors.RARE;
    public static boolean iron_chests = true;
    public static boolean storage_drawers = true;
    public static boolean gregtech_recipes = true;

    public static void init(File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfiguration();
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(Reference.MOD_ID)) {
            loadConfiguration();
        }
    }

    /** Whether the dyed Iron Chests are added: Iron Chests is installed and the config keeps them. */
    public static boolean ironChests() {
        return iron_chests && Loader.isModLoaded(Reference.IRON_CHESTS);
    }

    /** Whether the dyed Storage Drawers are added: Storage Drawers is installed and the config keeps them. */
    public static boolean storageDrawers() {
        return storage_drawers && Loader.isModLoaded(Reference.STORAGE_DRAWERS);
    }

    private static void loadConfiguration() {
        chisel_support = config.getBoolean(
            "ChiselSupport_Overall",
            Configuration.CATEGORY_GENERAL,
            true,
            "Set to false to disable Chisel support entirely");
        chisel_plank = config.getBoolean(
            "ChiselSupport_Plank",
            Configuration.CATEGORY_GENERAL,
            true,
            "Set to false to disable Chisel support for dyed planks");
        worldgen_flower = config.getBoolean(
            "WorldGen_Flower",
            Configuration.CATEGORY_GENERAL,
            true,
            "Set to false to disable world gen of flowers");
        worldgen_tree = config.getBoolean(
            "WorldGen_Tree",
            Configuration.CATEGORY_GENERAL,
            true,
            "Set to false to disable world gen of dye trees");
        worldgen_granite = config.getBoolean(
            "WorldGen_Granite",
            Configuration.CATEGORY_GENERAL,
            true,
            "Set to false to disable world gen of granite. It is also skipped when another mod provides granite.");
        worldgen_andesite = config.getBoolean(
            "WorldGen_Andesite",
            Configuration.CATEGORY_GENERAL,
            true,
            "Set to false to disable world gen of andesite. It is also skipped when another mod provides andesite.");
        worldgen_diorite = config.getBoolean(
            "WorldGen_Diorite",
            Configuration.CATEGORY_GENERAL,
            true,
            "Set to false to disable world gen of diorite. It is also skipped when another mod provides diorite.");
        preventMobSpawning = config.getBoolean(
            "preventMobSpawning",
            Configuration.CATEGORY_GENERAL,
            true,
            "If true, mobs cannot spawn on any block from this mod. If false, the blocks follow the vanilla rules.");
        sheepSpawnChance = config
            .get(
                Configuration.CATEGORY_GENERAL,
                "sheepSpawnChance",
                WildSheepColors.NATURAL_CHANCE,
                "Chance, from 0 to 1, that a new sheep spawns in one of the sheepSpawnColors. 0 turns it off.",
                0.0,
                1.0)
            .getDouble();
        sheepSpawnColors = config.getStringList(
            "sheepSpawnColors",
            Configuration.CATEGORY_GENERAL,
            WildSheepColors.NATURAL,
            "The More Dyes shades (hex, like 7f5933) a new sheep can spawn in. The default is the natural browns, beiges and greys. Leave it empty for any More Dyes shade.");
        rareSheepSpawnChance = config
            .get(
                Configuration.CATEGORY_GENERAL,
                "rareSheepSpawnChance",
                WildSheepColors.RARE_CHANCE,
                "Chance, from 0 to 1, that a new sheep spawns in one of the rareSheepSpawnColors instead. 0 turns it off.",
                0.0,
                1.0)
            .getDouble();
        rareSheepSpawnColors = config.getStringList(
            "rareSheepSpawnColors",
            Configuration.CATEGORY_GENERAL,
            WildSheepColors.RARE,
            "The rare shades a new sheep can spawn in. The default is burgundy (592626). Leave it empty for any More Dyes shade.");
        iron_chests = config.getBoolean(
            "Compat_IronChests",
            Configuration.CATEGORY_GENERAL,
            true,
            "Set to false to leave out the dyed Iron Chests. Only matters when Iron Chests is installed; restart the game after changing it.");
        storage_drawers = config.getBoolean(
            "Compat_StorageDrawers",
            Configuration.CATEGORY_GENERAL,
            true,
            "Set to false to leave out the dyed Storage Drawers. Only matters when Storage Drawers is installed; restart the game after changing it.");
        gregtech_recipes = config.getBoolean(
            "Compat_GregTechRecipes",
            Configuration.CATEGORY_GENERAL,
            true,
            "Set to false to leave out the GregTech mixer and chemical bath recipes. Only matters when GregTech is installed; restart the game after changing it.");
        // Replaced by preventMobSpawning.
        config.getCategory(Configuration.CATEGORY_GENERAL)
            .remove("mobSpawnOnBlock");
        config.getCategory(Configuration.CATEGORY_GENERAL)
            .remove("overrideDefaultMobSpawning");
        if (config.hasChanged()) {
            config.save();
        }
    }
}
