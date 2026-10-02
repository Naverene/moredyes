package net.neverandy.moredyes.compat;

import java.util.Arrays;
import java.util.Map;

import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.material.Materials;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.neverandy.moredyes.block.BlockDyedLeaves;
import net.neverandy.moredyes.block.BlockDyedLog;
import net.neverandy.moredyes.block.IColoredBlock;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.utility.LogHelper;

/**
 * GregTech machine recipes, like the 1.7.10 version has. Written against GregTech CEu, and only loaded when a mod
 * called "gregtech" is installed.
 * <ul>
 * <li>Mixer (LV): eight vanilla blocks and one dye make eight dyed blocks.</li>
 * <li>Chemical Bath: a dyed block and 50 L of chlorine bleach it back to the vanilla block, the same recipe GregTech
 * uses to bleach dyed wool.</li>
 * </ul>
 */
public class GregTechCompat
{
	public static final String GREGTECH="gregtech";

	private static final int DYE_DURATION=8*20;
	private static final int DYE_EUT=30;
	private static final int BLEACH_DURATION=20*20;
	private static final int BLEACH_EUT=2;

	public static void registerRecipes()
	{
		int recipes=0;
		for(Map.Entry<Block,ItemStack> entry:MDBlock.WASHED.entrySet())
		{
			Block dyed=entry.getKey();
			ItemStack vanilla=entry.getValue();
			IColoredBlock colored=(IColoredBlock)dyed;
			// Logs and leaves only come from dye trees; they are never dyed from the vanilla ones.
			boolean dyeable=!(dyed instanceof BlockDyedLog)&&!(dyed instanceof BlockDyedLeaves);
			for(int meta=0;meta<colored.getShadeCount();meta++)
			{
				if(dyeable)
				{
					RecipeMaps.MIXER_RECIPES.recipeBuilder()
							.inputStacks(Arrays.asList(copy(vanilla,8),MDItem.dyeStack(colored.getColorIndex(meta),1)))
							.outputs(new ItemStack(dyed,8,meta))
							.duration(DYE_DURATION).EUt(DYE_EUT)
							.buildAndRegister();
					recipes++;
				}
				RecipeMaps.CHEMICAL_BATH_RECIPES.recipeBuilder()
						.inputStacks(Arrays.asList(new ItemStack(dyed,1,meta)))
						.fluidInputs(Materials.Chlorine.getFluid(50))
						.outputs(copy(vanilla,1))
						.duration(BLEACH_DURATION).EUt(BLEACH_EUT)
						.buildAndRegister();
				recipes++;
			}
		}
		LogHelper.info("Added "+recipes+" GregTech mixer and chemical bath recipes");
	}
	private static ItemStack copy(ItemStack stack,int size)
	{
		ItemStack copy=stack.copy();
		copy.setCount(size);
		return copy;
	}
}
