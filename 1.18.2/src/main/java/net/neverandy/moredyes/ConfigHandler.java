package net.neverandy.moredyes;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import net.minecraftforge.data.loading.DatagenModLoader;
import net.neverandy.moredyes.toggle.Toggles;

import java.io.File;
import java.util.List;


@Mod.EventBusSubscriber
public class ConfigHandler
{
    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.BooleanValue worldGenTree;
    public static ForgeConfigSpec.BooleanValue worldGenFlower;
    public static ForgeConfigSpec.DoubleValue sheepSpawnChance;
    public static ForgeConfigSpec.BooleanValue wallBlocks;
    private static ForgeConfigSpec.BooleanValue stairBlocks;
    private static ForgeConfigSpec.BooleanValue slabBlocks;
    private static ForgeConfigSpec.BooleanValue fenceBlocks;
    private static ForgeConfigSpec.BooleanValue paneBlocks;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> disabledBlocks;
    private static ForgeConfigSpec.BooleanValue ironChests;
    private static ForgeConfigSpec.BooleanValue storageDrawers;

    static
    {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        worldGen(SERVER_BUILDER, CLIENT_BUILDER);
        // Read when blocks are registered, so they are in the file loaded at startup (moredyes-client.toml) on both sides.
        // The settings for which blocks to add stay at the top of the file, where wall_blocks always was.
        stairBlocks = CLIENT_BUILDER.comment("Add dyed stairs.",
                "This and the settings below choose which blocks the mod adds; turning blocks off saves memory and loading time.",
                "They are read at startup, so restart the game after changing them. A server and its players must use the same",
                "settings, and blocks that are turned off disappear from worlds that have them.").define("stair_blocks", true);
        slabBlocks = CLIENT_BUILDER.comment("Add dyed slabs").define("slab_blocks", true);
        wallBlocks = CLIENT_BUILDER.comment("Add dyed walls (for the block types vanilla has walls for). They take about 1 GB of memory; turn this off to save it.")
                .define("wall_blocks", true);
        fenceBlocks = CLIENT_BUILDER.comment("Add dyed wooden fences").define("fence_blocks", true);
        paneBlocks = CLIENT_BUILDER.comment("Add dyed glass panes").define("pane_blocks", true);
        disabledBlocks = CLIENT_BUILDER.comment("Block types to leave out, named as in their ids without the color: \"concretepowder\" leaves out",
                "moredyes:concretepowder_334c59 and every other color. Leaving out a block also leaves out its slabs, stairs and walls,",
                "and \"stoneslab\", \"stonestairs\" or \"stonewall\" leave out just one shape. For example [\"chest\", \"piston\", \"sign\"].",
                "The log lists every type if a name isn't one.")
                .defineListAllowEmpty(List.of("disabled_blocks"), List::of, name -> name instanceof String);
        CLIENT_BUILDER.comment("Blocks for other mods, added only when that mod is installed").push("compat");
        ironChests = CLIENT_BUILDER.comment("Add dyed Iron Chests").define("iron_chests", true);
        storageDrawers = CLIENT_BUILDER.comment("Add dyed Storage Drawers").define("storage_drawers", true);
        CLIENT_BUILDER.pop();

        SERVER_CONFIG = SERVER_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent)
    {

    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading configEvent)
    {

    }

    /** Copies the block settings to Toggles, once moredyes-client.toml is loaded. Data generation always makes everything. */
    public static void applyToggles()
    {
        if (DatagenModLoader.isRunningDataGen())
        {
            return;
        }
        Toggles.stairs = stairBlocks.get();
        Toggles.slabs = slabBlocks.get();
        Toggles.walls = wallBlocks.get();
        Toggles.fences = fenceBlocks.get();
        Toggles.panes = paneBlocks.get();
        Toggles.setDisabledTypes(disabledBlocks.get());
        Toggles.ironChests = ironChests.get();
        Toggles.storageDrawers = storageDrawers.get();
    }

    public static void loadConfigFile(ForgeConfigSpec config, String path){
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path))
                .sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }

    private static void worldGen(ForgeConfigSpec.Builder server, ForgeConfigSpec.Builder client){
        worldGenFlower = client.comment("Allow MoreDyes flowers during world generation").define("generate_flowers", true);
        worldGenTree = client.comment("Allow MoreDyes trees during world generation").define("generate_trees", true);
        sheepSpawnChance = server.comment("Chance (0 to 1) that a sheep spawning in the world has a random MoreDyes color")
                .defineInRange("sheep_spawn_chance", 0.05D, 0.0D, 1.0D);
    }
}
