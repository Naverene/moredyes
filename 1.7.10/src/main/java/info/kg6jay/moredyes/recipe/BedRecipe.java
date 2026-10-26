package info.kg6jay.moredyes.recipe;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import info.kg6jay.moredyes.block.BedWood;
import info.kg6jay.moredyes.handler.ConfigHandler;
import info.kg6jay.moredyes.item.MDItem;

/**
 * A bed recipe that remembers the planks the bed was made from (see BedWood): the planks used the most in the grid
 * (unless the WoodBedFrames config is off), or the wood of the bed being dyed. It wraps the vanilla bed recipe, which
 * then gives a dyed bed with the vanilla bedding when the planks are not oak, and the mod's own bed recipes.
 */
public class BedRecipe implements IRecipe {

    private final IRecipe base;

    public BedRecipe(IRecipe base) {
        this.base = base;
    }

    /** Wraps every recipe that makes a vanilla bed. Call after the vanilla and Forge recipes are in. */
    @SuppressWarnings("unchecked")
    public static void wrapVanilla() {
        List<IRecipe> recipes = CraftingManager.getInstance()
            .getRecipeList();
        for (int i = 0; i < recipes.size(); i++) {
            IRecipe recipe = recipes.get(i);
            ItemStack output = recipe.getRecipeOutput();
            if (!(recipe instanceof BedRecipe) && output != null && output.getItem() == Items.bed) {
                recipes.set(i, new BedRecipe(recipe));
            }
        }
    }

    /** The planks used the most in the grid (the first of them on a tie), or the wood of a dyed bed in it. */
    private static String wood(InventoryCrafting inv) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack == null) {
                continue;
            }
            if (stack.getItem() == MDItem.bed) {
                return BedWood.woodOf(stack);
            }
            String planks = BedWood.of(stack);
            if (planks != null) {
                counts.merge(planks, 1, Integer::sum);
            }
        }
        String best = null;
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            if (best == null || entry.getValue() > counts.get(best)) {
                best = entry.getKey();
            }
        }
        // With wooden frames turned off, new beds get the plain oak frame.
        return ConfigHandler.wood_bed_frames ? best : null;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        return this.base.matches(inv, world);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack result = this.base.getCraftingResult(inv);
        if (result == null) {
            return result;
        }
        String wood = wood(inv);
        if (wood == null || BedWood.OAK.equals(wood)) {
            return result;
        }
        if (result.getItem() == Items.bed) {
            result = BedWood.vanillaBed();
        }
        if (result.getItem() == MDItem.bed) {
            BedWood.setWood(result, wood);
        }
        return result;
    }

    @Override
    public int getRecipeSize() {
        return this.base.getRecipeSize();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.base.getRecipeOutput();
    }
}
