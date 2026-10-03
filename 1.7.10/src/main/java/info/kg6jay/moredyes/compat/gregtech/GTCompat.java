package info.kg6jay.moredyes.compat.gregtech;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import info.kg6jay.moredyes.block.IBlockColored;
import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.compat.NearestDye;
import info.kg6jay.moredyes.item.MDItem;

/**
 * GregTech (GTNH) machine recipes. Only loaded when GregTech is installed.
 * <ul>
 * <li>Mixer (LV): a stack of 64 vanilla blocks and one dye make 64 dyed blocks.</li>
 * <li>Mixer (LV): the vanilla dyes of every crafting table dye mix make twice the dyes the crafting table gives, so two
 * dyes make four.</li>
 * <li>Chemical Bath: a dyed block and 50 L of chlorine bleach it back to the vanilla block, the same recipe GregTech
 * uses to bleach dyed wool.</li>
 * <li>Chemical Reactor and Mixer: each dye makes GregTech's chemical dye and water-mixed dye of the vanilla color it
 * looks closest to ({@link NearestDye}), the way a vanilla dye does. Spray cans are filled with chemical dye in a fluid
 * canner, so our dyes make spray cans, which color GregTech's cables, pipes, hatches, buses and machines (and AE2's
 * cables). The infinite spray can takes no dye.</li>
 * </ul>
 */
public class GTCompat {

    private static final int STACK = 64;
    private static final int DYE_DURATION = 64 * 20;
    private static final int MIX_DURATION = 5 * 20;
    private static final int BLEACH_DURATION = 20 * 20;
    private static final int BLEACH_EUT = 2;

    public static void registerRecipes() {
        registerDyeMixes();
        registerDyeFluids();
        for (Map.Entry<Block, ItemStack> entry : MDBlock.WASHED.entrySet()) {
            Block dyed = entry.getKey();
            ItemStack vanilla = entry.getValue();
            int setIndex = setIndexOf(dyed);
            boolean dyeable = dyed != MDBlock.log[setIndex] && dyed != MDBlock.leaf[setIndex];

            for (int meta = 0; meta <= ((IBlockColored) dyed).getMaxMeta(); meta++) {
                if (dyeable) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(copy(vanilla, STACK), new ItemStack(MDItem.dye[setIndex], 1, meta))
                        .itemOutputs(new ItemStack(dyed, STACK, meta))
                        .duration(DYE_DURATION)
                        .eut(TierEU.RECIPE_LV)
                        .addTo(RecipeMaps.mixerRecipes);
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(dyed, 1, meta))
                    .itemOutputs(copy(vanilla, 1))
                    .fluidInputs(Materials.Chlorine.getGas(50L))
                    .duration(BLEACH_DURATION)
                    .eut(BLEACH_EUT)
                    .addTo(RecipeMaps.chemicalBathRecipes);
            }
        }
    }

    /** GregTech's own chemical and water-mixed dye recipes (ChemicalRecipes, MixerRecipes), with our ore names. */
    private static void registerDyeFluids() {
        NearestDye.registerOreNames();
        for (int color = 0; color < NearestDye.COLORS.length; color++) {
            String fluid = ("dye" + NearestDye.COLORS[color]).toLowerCase(Locale.ENGLISH);
            FluidStack chemical = FluidRegistry.getFluidStack("dye.chemical." + fluid, 288);
            FluidStack waterMixed = FluidRegistry.getFluidStack("dye.watermixed." + fluid, 288);
            if (chemical != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        new OreDictItemStack(NearestDye.oreName(color), 1),
                        GTOreDictUnificator.get(OrePrefixes.dust, Materials.Salt, 2))
                    .fluidInputs(Materials.SulfuricAcid.getFluid(432))
                    .fluidOutputs(chemical)
                    .duration(30 * 20)
                    .eut(48)
                    .addTo(GTRecipeConstants.UniversalChemical);
            }
            if (waterMixed != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(new OreDictItemStack(NearestDye.oreName(color), 1))
                    .circuit(1)
                    .fluidInputs(GTModHandler.getDistilledWater(288))
                    .fluidOutputs(waterMixed)
                    .duration(16)
                    .eut(4)
                    .addTo(RecipeMaps.mixerRecipes);
            }
        }
    }

    /**
     * A mixer recipe for each crafting table recipe that mixes vanilla dyes into this mod's dyes, with twice the
     * output.
     */
    private static void registerDyeMixes() {
        List<ShapelessRecipes> mixes = new ArrayList<ShapelessRecipes>();
        for (Object recipe : CraftingManager.getInstance()
            .getRecipeList()) {
            if (recipe instanceof ShapelessRecipes && isDye(((IRecipe) recipe).getRecipeOutput())
                && ((ShapelessRecipes) recipe).recipeItems.size() >= 2) {
                mixes.add((ShapelessRecipes) recipe);
            }
        }
        for (ShapelessRecipes mix : mixes) {
            ItemStack result = mix.getRecipeOutput();
            GTValues.RA.stdBuilder()
                .itemInputs(merge(mix.recipeItems))
                .itemOutputs(copy(result, result.stackSize * 2))
                .duration(MIX_DURATION)
                .eut(TierEU.RECIPE_LV)
                .addTo(RecipeMaps.mixerRecipes);
        }
    }

    private static boolean isDye(ItemStack stack) {
        return stack != null && Arrays.asList(MDItem.dye)
            .contains(stack.getItem());
    }

    /** The crafting grid's single items, with equal ones stacked together. */
    private static ItemStack[] merge(List<?> items) {
        List<ItemStack> merged = new ArrayList<ItemStack>();
        for (Object item : items) {
            ItemStack stack = (ItemStack) item;
            ItemStack same = null;
            for (ItemStack m : merged) {
                if (ItemStack.areItemStacksEqual(copy(m, 1), copy(stack, 1))) {
                    same = m;
                }
            }
            if (same != null) {
                same.stackSize += stack.stackSize;
            } else {
                merged.add(stack.copy());
            }
        }
        return merged.toArray(new ItemStack[0]);
    }

    private static ItemStack copy(ItemStack stack, int size) {
        ItemStack copy = stack.copy();
        copy.stackSize = size;
        return copy;
    }

    /** Which color set (and so which dye item) a dyed block belongs to. */
    private static int setIndexOf(Block block) {
        String colorSet = ((IBlockColored) block).getColorSet();
        for (int i = 0; i < MDBlock.colors.length; i++) {
            if (MDBlock.colors[i].equals(colorSet)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Unknown color set " + colorSet);
    }
}
