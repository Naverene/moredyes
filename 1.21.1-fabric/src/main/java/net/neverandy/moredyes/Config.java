package net.neverandy.moredyes;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import net.fabricmc.loader.api.FabricLoader;

/**
 * The settings in {@code config/moredyes.properties}, the same ones the NeoForge build keeps in its server config. The
 * file is read once at startup and written back with any missing settings filled in.
 */
public final class Config {

    private static double sheepSpawnChance = 0.05D;

    private Config() {}

    public static void load() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(MoreDyes.MOD_ID + ".properties");
        Properties properties = new Properties();
        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                properties.load(reader);
            } catch (IOException e) {
                MoreDyes.LOGGER.warn("Could not read {}, using the default settings", path, e);
            }
        }
        try {
            sheepSpawnChance = Math.max(0.0D, Math.min(1.0D,
                Double.parseDouble(properties.getProperty("sheep_spawn_chance", "0.05"))));
        } catch (NumberFormatException e) {
            sheepSpawnChance = 0.05D;
        }
        try (Writer writer = Files.newBufferedWriter(path)) {
            writer.write("# More Dyes settings. Restart the game after changing them.\n\n");
            writer.write("# Chance (0 to 1) that a sheep spawning in the world has a random More Dyes color\n");
            writer.write("sheep_spawn_chance=" + sheepSpawnChance + "\n");
        } catch (IOException e) {
            MoreDyes.LOGGER.warn("Could not write {}", path, e);
        }
    }

    /** Chance (0 to 1) that a sheep spawning in the world has a random More Dyes color. */
    public static double sheepSpawnChance() {
        return sheepSpawnChance;
    }
}
