package net.neverandy.moredyes.item.crafting;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
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
    public static final IRecipeSerializer<KeepNbtShapelessRecipe> SERIALIZER = new Serializer();
    public static final String ID = "crafting_shapeless_keep_nbt";

    public KeepNbtShapelessRecipe(ShapelessRecipe base)
    {
        super(base.getId(), base.getGroup(), base.getRecipeOutput(), base.getIngredients());
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory container)
    {
        ItemStack result = super.getCraftingResult(container);
        Ingredient source = getIngredients().get(0);
        for (int i = 0; i < container.getSizeInventory(); i++)
        {
            ItemStack stack = container.getStackInSlot(i);
            if (!stack.isEmpty() && source.test(stack))
            {
                if (stack.getTag() != null)
                {
                    CompoundNBT tag = stack.getTag().copy();
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
    public IRecipeSerializer<?> getSerializer()
    {
        return SERIALIZER;
    }

    private static final class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<KeepNbtShapelessRecipe>
    {
        @Override
        public KeepNbtShapelessRecipe read(ResourceLocation id, JsonObject json)
        {
            return new KeepNbtShapelessRecipe(IRecipeSerializer.CRAFTING_SHAPELESS.read(id, json));
        }

        @Override
        public KeepNbtShapelessRecipe read(ResourceLocation id, PacketBuffer buffer)
        {
            return new KeepNbtShapelessRecipe(IRecipeSerializer.CRAFTING_SHAPELESS.read(id, buffer));
        }

        @Override
        public void write(PacketBuffer buffer, KeepNbtShapelessRecipe recipe)
        {
            IRecipeSerializer.CRAFTING_SHAPELESS.write(buffer, recipe);
        }
    }
}
