package info.kg6jay.moredyes;

import java.io.File;

import net.minecraftforge.common.Configuration;

import info.kg6jay.moredyes.handler.WildSheepColors;

/** The settings in config/moredyes.cfg. The block and item IDs are in the same file, read by MDBlocks and MDItems. */
public class Config {

    public final Configuration file;
    public final boolean worldgenFlower, worldgenTree, worldgenGranite, worldgenDiorite, worldgenAndesite;
    public final boolean preventMobSpawning;
    public final double sheepSpawnChance, rareSheepSpawnChance;
    public final String[] sheepSpawnColors, rareSheepSpawnColors;

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
        double chance = this.file.get(general, "sheepSpawnChance", WildSheepColors.NATURAL_CHANCE,
            "Chance, from 0 to 1, that a new sheep spawns in one of the sheepSpawnColors. 0 turns it off.")
            .getDouble(WildSheepColors.NATURAL_CHANCE);
        this.sheepSpawnChance = Math.max(0.0D, Math.min(1.0D, chance));
        this.sheepSpawnColors = this.file.get(general, "sheepSpawnColors", WildSheepColors.NATURAL,
            "The More Dyes colors (hex, like 7f5933) a new sheep can spawn in. The default is the natural browns, beiges and greys. Leave it empty for any More Dyes color.")
            .valueList;
        double rareChance = this.file.get(general, "rareSheepSpawnChance", WildSheepColors.RARE_CHANCE,
            "Chance, from 0 to 1, that a new sheep spawns in one of the rareSheepSpawnColors instead. 0 turns it off.")
            .getDouble(WildSheepColors.RARE_CHANCE);
        this.rareSheepSpawnChance = Math.max(0.0D, Math.min(1.0D, rareChance));
        this.rareSheepSpawnColors = this.file.get(general, "rareSheepSpawnColors", WildSheepColors.RARE,
            "The rare colors a new sheep can spawn in. The default is burgundy (592626). Leave it empty for any More Dyes color.")
            .valueList;
    }

    public void save() {
        this.file.save();
    }
}
