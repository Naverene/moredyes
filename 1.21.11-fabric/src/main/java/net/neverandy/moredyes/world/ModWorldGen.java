package net.neverandy.moredyes.world;

import java.util.function.Predicate;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import net.neverandy.moredyes.MoreDyes;

/**
 * Adds the dye trees and patches of dyed tulips to the overworld. The features are data files
 * ({@code data/moredyes/worldgen}, shared with the NeoForge build); the biomes they grow in are the ones the NeoForge
 * build's biome modifier ({@code data/moredyes/neoforge/biome_modifier/dye_trees_and_tulips.json}) picks.
 */
public final class ModWorldGen {

    private ModWorldGen() {}

    public static void register() {
        BiomeModifications.addFeature(overworldLand(), GenerationStep.Decoration.VEGETAL_DECORATION, placed("dye_trees"));
        BiomeModifications.addFeature(overworldLand(), GenerationStep.Decoration.VEGETAL_DECORATION, placed("tulips"));
    }

    /** Overworld biomes that aren't water, beach, desert, badlands, icy, mushroom fields or caves. */
    private static Predicate<BiomeSelectionContext> overworldLand() {
        return BiomeSelectors.tag(tag("is_overworld")).and(BiomeSelectors.tag(tag("is_aquatic"))
            .or(BiomeSelectors.tag(tag("is_beach")))
            .or(BiomeSelectors.tag(tag("is_desert")))
            .or(BiomeSelectors.tag(tag("is_badlands")))
            .or(BiomeSelectors.tag(tag("is_icy")))
            .or(BiomeSelectors.tag(tag("is_mushroom")))
            .or(BiomeSelectors.tag(tag("is_cave")))
            .negate());
    }

    private static TagKey<net.minecraft.world.level.biome.Biome> tag(String name) {
        return TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", name));
    }

    private static ResourceKey<PlacedFeature> placed(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, name));
    }
}
