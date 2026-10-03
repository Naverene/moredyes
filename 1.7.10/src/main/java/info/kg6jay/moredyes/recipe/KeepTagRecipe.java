package info.kg6jay.moredyes.recipe;

import java.util.Arrays;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;

/**
 * A shapeless recipe whose result keeps the NBT tag of its first ingredient, such as the contents of a taped Storage
 * Drawers drawer or a chest's custom name, when that ingredient is dyed.
 */
public class KeepTagRecipe extends ShapelessRecipes {

    private final Item keepFrom;

    public KeepTagRecipe(ItemStack result, ItemStack... ingredients) {
        super(result, Arrays.asList(ingredients));
        this.keepFrom = ingredients[0].getItem();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        ItemStack result = this.getRecipeOutput()
            .copy();
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() == this.keepFrom && stack.hasTagCompound()) {
                result.setTagCompound(
                    (NBTTagCompound) stack.getTagCompound()
                        .copy());
                break;
            }
        }
        return result;
    }
}
