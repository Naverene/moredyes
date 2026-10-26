package net.neverandy.moredyes.registry;

import java.util.function.Supplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.item.crafting.BedRecipe;

/** The mod's own recipe type, {@code moredyes:crafting_bed}. */
public final class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister
        .create(Registries.RECIPE_SERIALIZER, MoreDyes.MOD_ID);

    public static final Supplier<RecipeSerializer<BedRecipe>> BED = SERIALIZERS.register("crafting_bed",
        () -> new RecipeSerializer<>(BedRecipe.CODEC, BedRecipe.STREAM_CODEC));

    private ModRecipes() {}

    public static void register(IEventBus modBus) {
        SERIALIZERS.register(modBus);
    }
}
