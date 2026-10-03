package net.neverandy.moredyes.compat;

import java.util.Arrays;
import java.util.Map;

import gregtech.api.recipes.RecipeMaps;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.neverandy.moredyes.block.BlockDyedLeaves;
import net.neverandy.moredyes.block.BlockDyedLog;
import net.neverandy.moredyes.block.IColoredBlock;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.recipe.CraftManager;
import net.neverandy.moredyes.utility.LogHelper;

/**
 * GregTech machine recipes, like the 1.7.10 version has. Written against GregTech CEu, and only loaded when a mod
 * called "gregtech" is installed.
 * <ul>
 * <li>Mixer (LV): a stack of 64 vanilla blocks and one dye make 64 dyed blocks.</li>
 * <li>Mixer (LV): the vanilla dyes of every crafting table dye mix make twice the dyes the crafting table gives, so two
 * dyes make four.</li>
 * <li>Chemical Bath: a dyed block and 50 L of chlorine bleach it back to the vanilla block, the same recipe GregTech
 * uses to bleach dyed wool.</li>
 * <li>Chemical Reactor: each dye makes GregTech's chemical dye of the vanilla color it looks closest to
 * ({@link NearestDye}), the way a vanilla dye does. Spray cans are filled with chemical dye in a canner, so our dyes
 * make spray cans, which color GregTech's cables, pipes and machines (and AE2's cables).</li>
 * </ul>
 */
public class GregTechCompat
{
	public static final String GREGTECH="gregtech";

	private static final int DYE_DURATION=64*20;
	private static final int DYE_EUT=30;
	private static final int STACK=64;
	private static final int MIX_DURATION=5*20;
	private static final int BLEACH_DURATION=20*20;
	private static final int BLEACH_EUT=2;

	public static void registerRecipes()
	{
		int recipes=0;
		for(CraftManager.DyeMix mix:CraftManager.DYE_MIXES)
		{
			ItemStack result=mix.result.copy();
			result.setCount(result.getCount()*2);
			RecipeMaps.MIXER_RECIPES.recipeBuilder()
					.input(mix.first,mix.count)
					.input(mix.second,mix.count)
					.outputs(result)
					.duration(MIX_DURATION).EUt(DYE_EUT)
					.buildAndRegister();
			recipes++;
		}
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
							.inputStacks(Arrays.asList(copy(vanilla,STACK),MDItem.dyeStack(colored.getColorIndex(meta),1)))
							.outputs(new ItemStack(dyed,STACK,meta))
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
		NearestDye.registerOreNames();
		for(int color=0;color<Materials.CHEMICAL_DYES.length;color++)
		{
			// GregTech's own recipe for a vanilla dye (ReactorRecipes), with our ore name.
			RecipeMaps.CHEMICAL_RECIPES.recipeBuilder()
					.input(NearestDye.oreName(color),1)
					.input(OrePrefix.dust,Materials.Salt,2)
					.fluidInputs(Materials.SulfuricAcid.getFluid(250))
					.fluidOutputs(Materials.CHEMICAL_DYES[color].getFluid(288))
					.duration(600).EUt(24)
					.buildAndRegister();
			recipes++;
		}
		LogHelper.info("Added "+recipes+" GregTech mixer, chemical bath and chemical reactor recipes");
	}
	private static ItemStack copy(ItemStack stack,int size)
	{
		ItemStack copy=stack.copy();
		copy.setCount(size);
		return copy;
	}
}
