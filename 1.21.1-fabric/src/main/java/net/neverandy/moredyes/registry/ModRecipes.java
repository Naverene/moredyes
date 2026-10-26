package net.neverandy.moredyes.registry;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.item.crafting.BedRecipe;
import net.neverandy.moredyes.item.crafting.KeepComponentsShapelessRecipe;

/** The mod's own recipe types, {@code moredyes:crafting_shapeless_keep_components} and {@code moredyes:crafting_bed}. */
public final class ModRecipes {

    public static final RecipeSerializer<KeepComponentsShapelessRecipe> KEEP_COMPONENTS_SHAPELESS = Registry.register(
        BuiltInRegistries.RECIPE_SERIALIZER,
        ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID, "crafting_shapeless_keep_components"),
        new RecipeSerializer<>() {
            @Override
            public MapCodec<KeepComponentsShapelessRecipe> codec() {
                return KeepComponentsShapelessRecipe.CODEC;
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, KeepComponentsShapelessRecipe> streamCodec() {
                return KeepComponentsShapelessRecipe.STREAM_CODEC;
            }
        });

    public static final RecipeSerializer<BedRecipe> BED = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER,
        ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID, "crafting_bed"), new RecipeSerializer<>() {
            @Override
            public MapCodec<BedRecipe> codec() {
                return BedRecipe.CODEC;
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, BedRecipe> streamCodec() {
                return BedRecipe.STREAM_CODEC;
            }
        });

    private ModRecipes() {}

    /** Registers the recipe serializers (loading this class does it). */
    public static void register() {}
}
