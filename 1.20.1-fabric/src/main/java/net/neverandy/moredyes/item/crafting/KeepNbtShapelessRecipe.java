package net.neverandy.moredyes.item.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import org.jetbrains.annotations.Nullable;

/**
 * A shapeless recipe whose result keeps the NBT of the item that matches its first ingredient, with the result's own NBT
 * (from the recipe) merged over it. Dyeing a Storage Drawers drawer uses it, so the drawer keeps what it holds (Storage
 * Drawers keeps the contents on the item), its name and its upgrades, and only its color changes.
 *
 * <p>The JSON is that of minecraft:crafting_shapeless, and the result may have an "nbt" field (an object or an SNBT
 * string), on Fabric as well as on Forge.
 */
public class KeepNbtShapelessRecipe extends ShapelessRecipe
{
    public static final RecipeSerializer<KeepNbtShapelessRecipe> SERIALIZER = new Serializer();
    public static final String ID = "crafting_shapeless_keep_nbt";

    public KeepNbtShapelessRecipe(ShapelessRecipe base, @Nullable CompoundTag resultTag)
    {
        super(base.getId(), base.getGroup(), base.category(), result(base, resultTag), base.getIngredients());
    }

    private static ItemStack result(ShapelessRecipe base, @Nullable CompoundTag tag)
    {
        ItemStack result = base.getResultItem(RegistryAccess.EMPTY).copy();
        if (tag != null)
        {
            result.setTag(tag);
        }
        return result;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess access)
    {
        ItemStack result = super.assemble(container, access);
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

    private static final class Serializer implements RecipeSerializer<KeepNbtShapelessRecipe>
    {
        @Override
        public KeepNbtShapelessRecipe fromJson(ResourceLocation id, JsonObject json)
        {
            return new KeepNbtShapelessRecipe(RecipeSerializer.SHAPELESS_RECIPE.fromJson(id, json), resultTag(json));
        }

        /** The result's "nbt", which vanilla (and so Fabric) doesn't read. */
        @Nullable
        private static CompoundTag resultTag(JsonObject json)
        {
            JsonObject result = GsonHelper.getAsJsonObject(json, "result");
            if (!result.has("nbt"))
            {
                return null;
            }
            JsonElement nbt = result.get("nbt");
            try
            {
                return TagParser.parseTag(nbt.isJsonObject() ? nbt.toString() : GsonHelper.convertToString(nbt, "nbt"));
            }
            catch (CommandSyntaxException e)
            {
                throw new JsonSyntaxException("Invalid result nbt: " + e.getMessage());
            }
        }

        @Override
        public KeepNbtShapelessRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer)
        {
            // The result is sent with its NBT.
            return new KeepNbtShapelessRecipe(RecipeSerializer.SHAPELESS_RECIPE.fromNetwork(id, buffer), null);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, KeepNbtShapelessRecipe recipe)
        {
            RecipeSerializer.SHAPELESS_RECIPE.toNetwork(buffer, recipe);
        }
    }
}
