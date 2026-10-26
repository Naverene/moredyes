package net.neverandy.moredyes;

import net.fabricmc.loader.api.FabricLoader;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.entity.WildSheepColors;
import net.neverandy.moredyes.toggle.Toggles;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The settings in config/moredyes.properties, the same ones the Forge build keeps in its TOML files. The file is
 * read once at startup, before the blocks are registered, and written back with any missing settings filled in.
 */
public final class ConfigHandler
{
    private static boolean loaded;
    private static boolean worldGenTree = true;
    private static boolean worldGenFlower = true;
    private static double sheepSpawnChance = WildSheepColors.NATURAL_CHANCE;
    private static List<String> sheepSpawnColors = WildSheepColors.NATURAL;
    private static double rareSheepSpawnChance = WildSheepColors.RARE_CHANCE;
    private static List<String> rareSheepSpawnColors = WildSheepColors.RARE;
    private static boolean woodBedFrames = true;

    private ConfigHandler() {}

    /**
     * Reads the file, once. Storage Drawers' initializer can run before More Dyes' own (see StorageDrawersMixin), so
     * whichever needs the settings first loads them.
     */
    public static synchronized void load()
    {
        if (loaded)
        {
            return;
        }
        loaded = true;
        Path path = FabricLoader.getInstance().getConfigDir().resolve(Reference.MOD_ID + ".properties");
        Properties properties = new Properties();
        if (Files.exists(path))
        {
            try (Reader reader = Files.newBufferedReader(path))
            {
                properties.load(reader);
            }
            catch (IOException e)
            {
                MoreDyes.LOGGER.warn("Could not read {}, using the default settings", path, e);
            }
        }
        worldGenFlower = flag(properties, "generate_flowers");
        worldGenTree = flag(properties, "generate_trees");
        sheepSpawnChance = chance(properties, "sheep_spawn_chance", WildSheepColors.NATURAL_CHANCE);
        sheepSpawnColors = colors(properties, "sheep_spawn_colors", WildSheepColors.NATURAL);
        rareSheepSpawnChance = chance(properties, "rare_sheep_spawn_chance", WildSheepColors.RARE_CHANCE);
        rareSheepSpawnColors = colors(properties, "rare_sheep_spawn_colors", WildSheepColors.RARE);
        woodBedFrames = flag(properties, "wood_bed_frames");
        Toggles.stairs = flag(properties, "stair_blocks");
        Toggles.slabs = flag(properties, "slab_blocks");
        Toggles.walls = flag(properties, "wall_blocks");
        Toggles.fences = flag(properties, "fence_blocks");
        Toggles.panes = flag(properties, "pane_blocks");
        List<String> disabled = new ArrayList<>();
        for (String name : properties.getProperty("disabled_blocks", "").split(","))
        {
            if (!name.isBlank())
            {
                disabled.add(name.trim());
            }
        }
        Toggles.setDisabledTypes(disabled);
        Toggles.ironChests = flag(properties, "iron_chests");
        Toggles.storageDrawers = flag(properties, "storage_drawers");

        try (Writer writer = Files.newBufferedWriter(path))
        {
            writer.write("# More Dyes settings. Restart the game after changing them.\n\n");
            writer.write("# Allow MoreDyes flowers during world generation\n");
            writer.write("generate_flowers=" + worldGenFlower + "\n\n");
            writer.write("# Allow MoreDyes trees during world generation\n");
            writer.write("generate_trees=" + worldGenTree + "\n\n");
            writer.write("# Chance (0 to 1) that a sheep spawning in the wild gets one of the sheep_spawn_colors\n");
            writer.write("sheep_spawn_chance=" + sheepSpawnChance + "\n\n");
            writer.write("# The MoreDyes colors wild sheep can spawn with, as hex separated by commas. The default is the natural\n");
            writer.write("# browns, beiges and greys. Leave it empty for any MoreDyes color.\n");
            writer.write("sheep_spawn_colors=" + String.join(",", sheepSpawnColors) + "\n\n");
            writer.write("# Chance (0 to 1) that a sheep spawning in the wild gets one of the rare_sheep_spawn_colors instead\n");
            writer.write("rare_sheep_spawn_chance=" + rareSheepSpawnChance + "\n\n");
            writer.write("# The rare colors wild sheep can spawn with. The default is burgundy (592626). Leave it empty for any MoreDyes color.\n");
            writer.write("rare_sheep_spawn_colors=" + String.join(",", rareSheepSpawnColors) + "\n\n");
            writer.write("# Beds remember the planks they are crafted from and draw their frame in that wood.\n");
            writer.write("# Set to false to craft every bed with the plain oak frame, as in vanilla. Beds crafted before keep their wood.\n");
            writer.write("wood_bed_frames=" + woodBedFrames + "\n\n");
            writer.write("# Add dyed stairs.\n");
            writer.write("# This and the settings below choose which blocks the mod adds; turning blocks off saves memory and loading time.\n");
            writer.write("# A server and its players must use the same settings, and blocks that are turned off disappear from worlds that have them.\n");
            writer.write("stair_blocks=" + Toggles.stairs + "\n\n");
            writer.write("# Add dyed slabs\n");
            writer.write("slab_blocks=" + Toggles.slabs + "\n\n");
            writer.write("# Add dyed walls (for the block types vanilla has walls for). They take about 1 GB of memory; turn this off to save it.\n");
            writer.write("wall_blocks=" + Toggles.walls + "\n\n");
            writer.write("# Add dyed wooden fences\n");
            writer.write("fence_blocks=" + Toggles.fences + "\n\n");
            writer.write("# Add dyed glass panes\n");
            writer.write("pane_blocks=" + Toggles.panes + "\n\n");
            writer.write("# Block types to leave out, separated by commas, named as in their ids without the color: concretepowder leaves out\n");
            writer.write("# moredyes:concretepowder_334c59 and every other color. Leaving out a block also leaves out its slabs, stairs and walls,\n");
            writer.write("# and stoneslab, stonestairs or stonewall leave out just one shape. For example chest,piston,sign\n");
            writer.write("# The log lists every type if a name isn't one.\n");
            writer.write("disabled_blocks=" + String.join(",", disabled) + "\n\n");
            writer.write("# Add dyed Iron Chests, when Iron Chests is installed\n");
            writer.write("iron_chests=" + Toggles.ironChests + "\n\n");
            writer.write("# Add dyed Storage Drawers, when Storage Drawers is installed\n");
            writer.write("storage_drawers=" + Toggles.storageDrawers + "\n");
        }
        catch (IOException e)
        {
            MoreDyes.LOGGER.warn("Could not write {}", path, e);
        }
    }

    /** Whether crafted beds remember their planks and draw their frame in that wood. */
    public static boolean woodBedFrames()
    {
        return woodBedFrames;
    }

    /** A true/false setting, on unless the file says otherwise. */
    private static boolean flag(Properties properties, String key)
    {
        return Boolean.parseBoolean(properties.getProperty(key, "true").trim());
    }

    public static boolean worldGenTree()
    {
        return worldGenTree;
    }

    public static boolean worldGenFlower()
    {
        return worldGenFlower;
    }

    /** A chance from 0 to 1, or the default if the file has none or it isn't a number. */
    private static double chance(Properties properties, String key, double fallback)
    {
        try
        {
            return Math.max(0.0D, Math.min(1.0D, Double.parseDouble(properties.getProperty(key, String.valueOf(fallback)).trim())));
        }
        catch (NumberFormatException e)
        {
            return fallback;
        }
    }

    /** A list of colors separated by commas, or the default if the file has none. */
    private static List<String> colors(Properties properties, String key, List<String> fallback)
    {
        String value = properties.getProperty(key);
        if (value == null)
        {
            return fallback;
        }
        List<String> colors = new ArrayList<>();
        for (String color : value.split(","))
        {
            if (!color.isBlank())
            {
                colors.add(WildSheepColors.normalize(color));
            }
        }
        return colors;
    }

    public static double sheepSpawnChance()
    {
        return sheepSpawnChance;
    }

    public static List<String> sheepSpawnColors()
    {
        return sheepSpawnColors;
    }

    public static double rareSheepSpawnChance()
    {
        return rareSheepSpawnChance;
    }

    public static List<String> rareSheepSpawnColors()
    {
        return rareSheepSpawnColors;
    }
}
