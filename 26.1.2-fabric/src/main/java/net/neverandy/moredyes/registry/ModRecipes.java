package net.neverandy.moredyes.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.item.crafting.BedRecipe;

/** The mod's own recipe type, {@code moredyes:crafting_bed}. */
public final class ModRecipes {

    public static final RecipeSerializer<BedRecipe> BED = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER,
        Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "crafting_bed"),
        new RecipeSerializer<>(BedRecipe.CODEC, BedRecipe.STREAM_CODEC));

    private ModRecipes() {}

    /** Registers the recipe serializer (loading this class does it). */
    public static void register() {}
}
