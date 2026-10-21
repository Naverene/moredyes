package net.neverandy.moredyes;

import java.util.List;

import net.neoforged.neoforge.common.ModConfigSpec;

import net.neverandy.moredyes.toggle.Toggles;

/**
 * The settings. The server config is saved per world in {@code serverconfig/moredyes-server.toml}; the startup config,
 * {@code config/moredyes-startup.toml}, chooses which blocks the mod adds and is read before they are registered.
 */
public final class Config {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue SHEEP_SPAWN_CHANCE = BUILDER
        .comment("Chance (0 to 1) that a sheep spawning in the world has a random More Dyes color")
        .defineInRange("sheep_spawn_chance", 0.05D, 0.0D, 1.0D);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static final ModConfigSpec.Builder STARTUP = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue STAIRS = STARTUP
        .comment("Add dyed stairs.",
            "This and the settings below choose which blocks the mod adds; turning blocks off saves memory and loading time.",
            "They are read at startup, so restart the game after changing them. A server and its players must use the same",
            "settings, and blocks that are turned off disappear from worlds that have them.")
        .define("stair_blocks", true);
    private static final ModConfigSpec.BooleanValue SLABS = STARTUP.comment("Add dyed slabs").define("slab_blocks", true);
    private static final ModConfigSpec.BooleanValue WALLS = STARTUP
        .comment("Add dyed walls (for the block types vanilla has walls for). They take a lot of memory; turn this off to save it.")
        .define("wall_blocks", true);
    private static final ModConfigSpec.BooleanValue FENCES = STARTUP.comment("Add dyed wooden fences").define("fence_blocks", true);
    private static final ModConfigSpec.BooleanValue PANES = STARTUP.comment("Add dyed glass panes").define("pane_blocks", true);
    private static final ModConfigSpec.ConfigValue<List<? extends String>> DISABLED_BLOCKS = STARTUP
        .comment("Block types to leave out, named as in their ids without the color: \"concrete_powder\" leaves out",
            "moredyes:concrete_powder_c56685 and every other color. Leaving out a block also leaves out its slabs, stairs and",
            "walls, and \"stone_slab\", \"stone_stairs\" or \"cobblestone_wall\" leave out just one shape. For example",
            "[\"chest\", \"piston\", \"sign\"]. The log lists every type if a name isn't one.")
        .defineListAllowEmpty("disabled_blocks", List.of(), () -> "", name -> name instanceof String);
    private static final ModConfigSpec.BooleanValue TREES = STARTUP
        .comment("Grow dye trees during world generation").define("generate_trees", true);
    private static final ModConfigSpec.BooleanValue FLOWERS = STARTUP
        .comment("Grow dyed tulips during world generation").define("generate_flowers", true);
    private static final ModConfigSpec.BooleanValue IRON_CHESTS = STARTUP
        .comment("Add dyed Iron Chests, when Iron Chests is installed").define("iron_chests", true);
    private static final ModConfigSpec.BooleanValue STORAGE_DRAWERS = STARTUP
        .comment("Add dyed Storage Drawers, when Storage Drawers is installed").define("storage_drawers", true);

    static final ModConfigSpec STARTUP_SPEC = STARTUP.build();

    private Config() {}

    /** Hands the startup settings to Toggles. Call once the startup config is loaded, before the blocks are registered. */
    static void applyStartup() {
        Toggles.stairs = STAIRS.get();
        Toggles.slabs = SLABS.get();
        Toggles.walls = WALLS.get();
        Toggles.fences = FENCES.get();
        Toggles.panes = PANES.get();
        Toggles.setDisabledTypes(DISABLED_BLOCKS.get());
        Toggles.ironChests = IRON_CHESTS.get();
        Toggles.storageDrawers = STORAGE_DRAWERS.get();
        // The biome modifier lists both features; leaving one out takes it off that list (see toggle/DataFilter).
        if (!TREES.get()) {
            Toggles.leftOut("dye_trees");
        }
        if (!FLOWERS.get()) {
            Toggles.leftOut("tulips");
        }
    }
}
