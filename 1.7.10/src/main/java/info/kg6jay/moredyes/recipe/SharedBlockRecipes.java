package info.kg6jay.moredyes.recipe;

import java.util.List;
import java.util.Map;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import info.kg6jay.moredyes.item.SharedBlocks;

/**
 * The recipes are written with the dyed blocks' own items; once they are all registered, convert() switches every
 * recipe (ours and other mods') over to the shared dyed block item (see SharedBlocks). Stacks are changed in place,
 * since the recipe classes keep them in final fields.
 */
public final class SharedBlockRecipes {

    private SharedBlockRecipes() {}

    public static void convert() {
        for (Object recipe : CraftingManager.getInstance()
            .getRecipeList()) {
            if (recipe instanceof ShapedRecipes shaped) {
                convert(shaped.getRecipeOutput());
                for (ItemStack stack : shaped.recipeItems) {
                    convert(stack);
                }
            } else if (recipe instanceof ShapelessRecipes shapeless) {
                convert(shapeless.getRecipeOutput());
                for (Object stack : shapeless.recipeItems) {
                    convertObject(stack);
                }
            } else if (recipe instanceof ShapedOreRecipe shaped) {
                convert(shaped.getRecipeOutput());
                for (Object input : shaped.getInput()) {
                    convertObject(input);
                }
            } else if (recipe instanceof ShapelessOreRecipe shapeless) {
                convert(shapeless.getRecipeOutput());
                for (Object input : shapeless.getInput()) {
                    convertObject(input);
                }
            } else if (recipe instanceof IRecipe other) {
                convert(other.getRecipeOutput());
            }
        }
        for (Object entry : FurnaceRecipes.smelting()
            .getSmeltingList()
            .entrySet()) {
            Map.Entry<?, ?> e = (Map.Entry<?, ?>) entry;
            convertObject(e.getKey());
            convertObject(e.getValue());
        }
        GameRegistry.addRecipe(new OldItemRecipe());
    }

    private static void convertObject(Object o) {
        if (o instanceof ItemStack stack) {
            convert(stack);
        } else if (o instanceof List<?>list) {
            for (Object element : list) {
                if (element instanceof ItemStack stack) {
                    convert(stack);
                }
            }
        }
    }

    /** Makes an old item's stack a shared one, in place. */
    public static void convert(ItemStack stack) {
        ItemStack shared = SharedBlocks.toShared(stack);
        if (shared != null) {
            stack.func_150996_a(shared.getItem());
            stack.setItemDamage(shared.getItemDamage());
            stack.setTagCompound(shared.getTagCompound());
        }
    }

    /** One old item alone in the crafting grid makes the same block as the shared item, for old items in storage. */
    public static class OldItemRecipe implements IRecipe {

        private static ItemStack only(InventoryCrafting grid) {
            ItemStack found = null;
            for (int i = 0; i < grid.getSizeInventory(); i++) {
                ItemStack stack = grid.getStackInSlot(i);
                if (stack != null) {
                    if (found != null || !SharedBlocks.isOld(stack.getItem())) {
                        return null;
                    }
                    found = stack;
                }
            }
            return found;
        }

        @Override
        public boolean matches(InventoryCrafting grid, World world) {
            return this.getCraftingResult(grid) != null;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting grid) {
            ItemStack old = only(grid);
            if (old == null) {
                return null;
            }
            ItemStack shared = SharedBlocks.toShared(old);
            if (shared != null) {
                shared.stackSize = 1;
            }
            return shared;
        }

        @Override
        public int getRecipeSize() {
            return 1;
        }

        @Override
        public ItemStack getRecipeOutput() {
            return null;
        }
    }
}
