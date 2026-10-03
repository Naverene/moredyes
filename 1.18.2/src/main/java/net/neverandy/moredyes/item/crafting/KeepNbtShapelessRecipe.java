package net.neverandy.moredyes.item.crafting;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * A shapeless recipe whose result keeps the NBT of the item that matches its first ingredient, with the result's own NBT
 * (from the recipe) merged over it. Dyeing a Storage Drawers drawer uses it, so the drawer keeps what it holds (Storage
 * Drawers keeps the contents on the item), its name and its upgrades, and only its color changes.
 *
 * <p>The JSON is that of minecraft:crafting_shapeless; Forge reads the result's "nbt" field.
 */
public class KeepNbtShapelessRecipe extends ShapelessRecipe
{
    public static final RecipeSerializer<KeepNbtShapelessRecipe> SERIALIZER = new Serializer();
    public static final String ID = "crafting_shapeless_keep_nbt";

    public KeepNbtShapelessRecipe(ShapelessRecipe base)
    {
        super(base.getId(), base.getGroup(), base.getResultItem(), base.getIngredients());
    }

    @Override
    public ItemStack assemble(CraftingContainer container)
    {
        ItemStack result = super.assemble(container);
        Ingredient source = getIngredients().get(0);
        for (int i = 0; i < container.getContainerSize(); i++)
        {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty() && source.test(stack))
            {
                if (stack.getTag() != null)
                {
                    CompoundTag tag = stack.getTag().copy();
                    if (result.getTag() != null)
                    {
                        tag.merge(result.getTag());
                    }
                    result.setTag(tag);
                }
                break;
            }
        }
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return SERIALIZER;
    }

    private static final class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<KeepNbtShapelessRecipe>
    {
        @Override
        public KeepNbtShapelessRecipe fromJson(ResourceLocation id, JsonObject json)
        {
            return new KeepNbtShapelessRecipe(RecipeSerializer.SHAPELESS_RECIPE.fromJson(id, json));
        }

        @Override
        public KeepNbtShapelessRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer)
        {
            return new KeepNbtShapelessRecipe(RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(id, buffer));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, KeepNbtShapelessRecipe recipe)
        {
            RecipeSerializer.SHAPELESS_RECIPE.toNetwork(buffer, recipe);
        }
    }
}
