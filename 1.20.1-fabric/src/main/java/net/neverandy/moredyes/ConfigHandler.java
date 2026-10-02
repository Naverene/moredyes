package net.neverandy.moredyes;

import net.fabricmc.loader.api.FabricLoader;
import net.neverandy.moredyes.reference.Reference;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * The settings in config/moredyes.properties, the same ones the Forge build keeps in its TOML files. The file is
 * read once at startup, before the blocks are registered, and written back with any missing settings filled in.
 */
public final class ConfigHandler
{
    private static boolean worldGenTree = true;
    private static boolean worldGenFlower = true;
    private static double sheepSpawnChance = 0.05D;
    private static boolean wallBlocks = true;

    private ConfigHandler() {}

    public static void load()
    {
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
        worldGenFlower = Boolean.parseBoolean(properties.getProperty("generate_flowers", "true"));
        worldGenTree = Boolean.parseBoolean(properties.getProperty("generate_trees", "true"));
        try
        {
            sheepSpawnChance = Math.max(0.0D, Math.min(1.0D, Double.parseDouble(properties.getProperty("sheep_spawn_chance", "0.05"))));
        }
        catch (NumberFormatException e)
        {
            sheepSpawnChance = 0.05D;
        }
        wallBlocks = Boolean.parseBoolean(properties.getProperty("wall_blocks", "true"));

        try (Writer writer = Files.newBufferedWriter(path))
        {
            writer.write("# More Dyes settings. Restart the game after changing them.\n\n");
            writer.write("# Allow MoreDyes flowers during world generation\n");
            writer.write("generate_flowers=" + worldGenFlower + "\n\n");
            writer.write("# Allow MoreDyes trees during world generation\n");
            writer.write("generate_trees=" + worldGenTree + "\n\n");
            writer.write("# Chance (0 to 1) that a sheep spawning in the world has a random MoreDyes color\n");
            writer.write("sheep_spawn_chance=" + sheepSpawnChance + "\n\n");
            writer.write("# Add dyed walls (for the block types vanilla has walls for). They take about 1 GB of memory; turn this off to save it.\n");
            writer.write("# A server and its players must use the same setting, and worlds built with walls lose them when it is off.\n");
            writer.write("wall_blocks=" + wallBlocks + "\n");
        }
        catch (IOException e)
        {
            MoreDyes.LOGGER.warn("Could not write {}", path, e);
        }
    }

    public static boolean worldGenTree()
    {
        return worldGenTree;
    }

    public static boolean worldGenFlower()
    {
        return worldGenFlower;
    }

    public static double sheepSpawnChance()
    {
        return sheepSpawnChance;
    }

    /** Whether dyed walls are registered. */
    public static boolean wallBlocks()
    {
        return wallBlocks;
    }
}
