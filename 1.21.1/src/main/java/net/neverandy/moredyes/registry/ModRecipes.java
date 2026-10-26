package net.neverandy.moredyes.registry;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.item.crafting.BedRecipe;
import net.neverandy.moredyes.item.crafting.KeepComponentsShapelessRecipe;

/** The mod's own recipe types, {@code moredyes:crafting_shapeless_keep_components} and {@code moredyes:crafting_bed}. */
public final class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister
        .create(Registries.RECIPE_SERIALIZER, MoreDyes.MOD_ID);

    public static final Supplier<RecipeSerializer<KeepComponentsShapelessRecipe>> KEEP_COMPONENTS_SHAPELESS = SERIALIZERS
        .register("crafting_shapeless_keep_components", () -> new RecipeSerializer<>() {
            @Override
            public MapCodec<KeepComponentsShapelessRecipe> codec() {
                return KeepComponentsShapelessRecipe.CODEC;
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, KeepComponentsShapelessRecipe> streamCodec() {
                return KeepComponentsShapelessRecipe.STREAM_CODEC;
            }
        });

    public static final Supplier<RecipeSerializer<BedRecipe>> BED = SERIALIZERS.register("crafting_bed",
        () -> new RecipeSerializer<>() {
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

    public static void register(IEventBus modBus) {
        SERIALIZERS.register(modBus);
    }
}
