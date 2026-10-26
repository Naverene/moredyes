package net.neverandy.moredyes.recipe;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.handler.ConfigHandler;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.tileentity.TileEntityDyedBed;

/**
 * A bed recipe that remembers the planks the bed was made from (see TileEntityDyedBed): the planks used the most in the
 * grid (unless the woodBedFrames config is off), or the wood of the bed being dyed. It wraps the vanilla bed recipes,
 * which then give a More Dyes bed in the vanilla color when the planks are not oak, and the mod's own bed recipes.
 */
public class BedRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
	private final IRecipe base;

	public BedRecipe(IRecipe base)
	{
		this.base=base;
	}
	/** The block id of a stack of planks, as the bed keeps it, or null for any other item. */
	@Nullable
	static String planks(ItemStack stack)
	{
		Block block=Block.getBlockFromItem(stack.getItem());
		if(block==Blocks.PLANKS)
		{
			return "minecraft:"+BlockPlanks.EnumType.byMetadata(stack.getMetadata()).getName()+"_planks";
		}
		for(int g=0;g<MDBlock.plank.length;g++)
		{
			if(block==MDBlock.plank[g]&&stack.getMetadata()<ColorStrings.GROUPS[g].length)
			{
				return "moredyes:planks_"+ColorStrings.GROUPS[g][stack.getMetadata()];
			}
		}
		return null;
	}
	/** The planks used the most in the grid (the first of them on a tie), or the wood of a bed in it. */
	@Nullable
	private static String wood(InventoryCrafting inv)
	{
		Map<String,Integer> counts=new LinkedHashMap<String,Integer>();
		for(int i=0;i<inv.getSizeInventory();i++)
		{
			ItemStack stack=inv.getStackInSlot(i);
			if(stack.getItem()==MDItem.bed)
			{
				return TileEntityDyedBed.woodOf(stack);
			}
			String planks=planks(stack);
			if(planks!=null)
			{
				counts.put(planks,counts.getOrDefault(planks,0)+1);
			}
		}
		String best=null;
		for(Map.Entry<String,Integer> entry:counts.entrySet())
		{
			if(best==null||entry.getValue()>counts.get(best))
			{
				best=entry.getKey();
			}
		}
		// With wooden frames turned off, new beds get the plain oak frame.
		return ConfigHandler.woodBedFrames?best:null;
	}
	@Override
	public boolean matches(InventoryCrafting inv,World world)
	{
		return this.base.matches(inv,world);
	}
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack result=this.base.getCraftingResult(inv);
		String wood=wood(inv);
		if(result.isEmpty()||wood==null||TileEntityDyedBed.OAK.equals(wood))
		{
			return result;
		}
		if(result.getItem()==Items.BED)
		{
			result=TileEntityDyedBed.vanillaColorBed(EnumDyeColor.byMetadata(result.getMetadata()));
		}
		if(result.getItem()==MDItem.bed)
		{
			TileEntityDyedBed.setWood(result,wood);
		}
		return result;
	}
	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		return this.base.getRemainingItems(inv);
	}
	@Override
	public boolean canFit(int width,int height)
	{
		return this.base.canFit(width,height);
	}
	@Override
	public ItemStack getRecipeOutput()
	{
		return this.base.getRecipeOutput();
	}
	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		return this.base.getIngredients();
	}
	@Override
	public String getGroup()
	{
		return this.base.getGroup();
	}
}
