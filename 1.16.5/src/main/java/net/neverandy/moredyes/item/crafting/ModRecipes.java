package net.neverandy.moredyes.item.crafting;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.neverandy.moredyes.reference.Reference;

/** More Dyes' own recipe types. */
public final class ModRecipes
{
    private static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);

    private ModRecipes() {}

    public static void register(IEventBus modBus)
    {
        SERIALIZERS.register(KeepNbtShapelessRecipe.ID, () -> KeepNbtShapelessRecipe.SERIALIZER);
        SERIALIZERS.register(modBus);
    }
}
