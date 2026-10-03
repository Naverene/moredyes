package net.neverandy.moredyes.item.crafting;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neverandy.moredyes.reference.Reference;

/** More Dyes' own recipe types. */
public final class ModRecipes
{
    private ModRecipes() {}

    public static void register()
    {
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, new ResourceLocation(Reference.MOD_ID, KeepNbtShapelessRecipe.ID), KeepNbtShapelessRecipe.SERIALIZER);
    }
}
