package info.kg6jay.moredyes;

import java.io.File;

import net.minecraftforge.common.Configuration;

/** The settings in config/moredyes.cfg. The block and item IDs are in the same file, read by MDBlocks and MDItems. */
public class Config {

    public final Configuration file;
    public final boolean worldgenFlower, worldgenTree, worldgenGranite, worldgenDiorite, worldgenAndesite;
    public final boolean preventMobSpawning;
    public final double sheepSpawnChance;

    public Config(File path) {
        this.file = new Configuration(path);
        this.file.load();
        String general = Configuration.CATEGORY_GENERAL;
        this.worldgenFlower = this.file.get(general, "WorldGen_Flower", true,
            "Set to false to disable world gen of flowers").getBoolean(true);
        this.worldgenTree = this.file.get(general, "WorldGen_Tree", true,
            "Set to false to disable world gen of dye trees").getBoolean(true);
        this.worldgenGranite = this.file.get(general, "WorldGen_Granite", true,
            "Set to false to disable world gen of granite").getBoolean(true);
        this.worldgenDiorite = this.file.get(general, "WorldGen_Diorite", true,
            "Set to false to disable world gen of diorite").getBoolean(true);
        this.worldgenAndesite = this.file.get(general, "WorldGen_Andesite", true,
            "Set to false to disable world gen of andesite").getBoolean(true);
        this.preventMobSpawning = this.file.get(general, "preventMobSpawning", true,
            "If true, mobs cannot spawn on any block from this mod. If false, the blocks follow the vanilla rules.")
            .getBoolean(true);
        double chance = this.file.get(general, "sheepSpawnChance", 0.05,
            "Chance, from 0 to 1, that a new sheep spawns in a random More Dyes color. 0 turns it off.")
            .getDouble(0.05);
        this.sheepSpawnChance = Math.max(0.0D, Math.min(1.0D, chance));
    }

    public void save() {
        this.file.save();
    }
}
