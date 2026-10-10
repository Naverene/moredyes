package net.neverandy.moredyes.item.crafting;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import net.neverandy.moredyes.registry.ModRecipes;

/**
 * A shapeless recipe whose result keeps every component of the item that matches its first ingredient, with the
 * result's own components (from the recipe) set over them, like vanilla's shulker box dyeing. Dyeing a Storage Drawers
 * drawer uses it, so the drawer keeps what it holds, its name and its upgrades, and only its color (a
 * {@code minecraft:block_state} component) changes. Its JSON is that of {@code minecraft:crafting_shapeless}.
 */
public class KeepComponentsShapelessRecipe extends ShapelessRecipe {

    public static final MapCodec<KeepComponentsShapelessRecipe> CODEC = RecipeSerializer.SHAPELESS_RECIPE.codec()
        .xmap(KeepComponentsShapelessRecipe::new, recipe -> recipe);
    public static final StreamCodec<RegistryFriendlyByteBuf, KeepComponentsShapelessRecipe> STREAM_CODEC =
        RecipeSerializer.SHAPELESS_RECIPE.streamCodec().map(KeepComponentsShapelessRecipe::new, recipe -> recipe);

    public KeepComponentsShapelessRecipe(ShapelessRecipe base) {
        // ShapelessRecipe.getResultItem only returns the result; it doesn't use the registries.
        super(base.getGroup(), base.category(), base.getResultItem(null), base.getIngredients());
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack result = super.assemble(input, registries);
        Ingredient source = getIngredients().get(0);
        for (ItemStack stack : input.items()) {
            if (!stack.isEmpty() && source.test(stack)) {
                ItemStack kept = stack.transmuteCopy(result.getItem(), result.getCount());
                kept.applyComponents(result.getComponentsPatch());
                return kept;
            }
        }
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.KEEP_COMPONENTS_SHAPELESS.get();
    }
}
