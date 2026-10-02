package net.neverandy.moredyes.world;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.util.RandomSource;
import net.minecraft.core.BlockPos;
import net.neverandy.moredyes.ConfigHandler;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Scatters dye trees and dyed tulips through the overworld, like 1.7.10's TreeGenerator and FlowerGenerator.
 * The features are data files (data/moredyes/worldgen, written by the Forge build's data generators); the biomes they
 * grow in are picked here, the same ones as the Forge build's biome modifiers. Each can be turned off with
 * generate_trees and generate_flowers in moredyes.properties, which the "moredyes:config" placement filter checks.
 */
public final class ModWorldGen
{
    /** Places one tree of a random color and wood. */
    public static final Feature<NoneFeatureConfiguration> DYE_TREE = new Feature<>(NoneFeatureConfiguration.CODEC)
            {
                @Override
                public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
                {
                    RandomSource random = context.random();
                    String wood = DyeTrees.WOODS[random.nextInt(DyeTrees.WOODS.length)];
                    return DyeTrees.tree(wood, random.nextInt(ColorStrings.ALL.length))
                            .place(context.level(), context.chunkGenerator(), random, context.origin());
                }
            };

    public static final PlacementModifierType<ConfigFilter> CONFIG_FILTER = () -> ConfigFilter.CODEC;

    private ModWorldGen() {}

    public static void register()
    {
        Registry.register(BuiltInRegistries.FEATURE, new ResourceLocation(Reference.MOD_ID, "dye_tree"), DYE_TREE);
        Registry.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, new ResourceLocation(Reference.MOD_ID, "config"), CONFIG_FILTER);

        BiomeModifications.addFeature(overworldLand(), GenerationStep.Decoration.VEGETAL_DECORATION, placed("dye_trees"));
        BiomeModifications.addFeature(overworldLand(), GenerationStep.Decoration.VEGETAL_DECORATION, placed("tulips"));
    }

    /** Overworld land that isn't water, beach, badlands, desert, mushroom fields, underground or snowy. */
    private static Predicate<BiomeSelectionContext> overworldLand()
    {
        return BiomeSelectors.foundInOverworld().and(BiomeSelectors.tag(BiomeTags.IS_OCEAN)
                .or(BiomeSelectors.tag(BiomeTags.IS_RIVER))
                .or(BiomeSelectors.tag(BiomeTags.IS_BEACH))
                .or(BiomeSelectors.tag(BiomeTags.IS_BADLANDS))
                .or(BiomeSelectors.tag(ConventionalBiomeTags.DESERT))
                .or(BiomeSelectors.tag(ConventionalBiomeTags.MUSHROOM))
                .or(BiomeSelectors.tag(ConventionalBiomeTags.UNDERGROUND))
                .or(BiomeSelectors.includeByKey(Biomes.SNOWY_PLAINS, Biomes.ICE_SPIKES, Biomes.SNOWY_SLOPES, Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS))
                .negate());
    }

    private static ResourceKey<PlacedFeature> placed(String name)
    {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Reference.MOD_ID, name));
    }

    /** Which config option turns a feature on. */
    public enum Option implements StringRepresentable
    {
        TREES("trees", ConfigHandler::worldGenTree),
        FLOWERS("flowers", ConfigHandler::worldGenFlower);

        public static final Codec<Option> CODEC = StringRepresentable.fromEnum(Option::values);

        private final String name;
        private final Supplier<Boolean> enabled;

        Option(String name, Supplier<Boolean> enabled)
        {
            this.name = name;
            this.enabled = enabled;
        }

        @Override
        public String getSerializedName()
        {
            return name;
        }
    }

    /** Lets a placed feature through only while its config option is on. */
    public static final class ConfigFilter extends PlacementFilter
    {
        public static final Codec<ConfigFilter> CODEC = Option.CODEC.fieldOf("option").xmap(ConfigFilter::new, filter -> filter.option).codec();

        private final Option option;

        public ConfigFilter(Option option)
        {
            this.option = option;
        }

        @Override
        protected boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos)
        {
            return option.enabled.get();
        }

        @Override
        public PlacementModifierType<?> type()
        {
            return CONFIG_FILTER;
        }
    }
}
