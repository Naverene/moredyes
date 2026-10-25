package net.neverandy.moredyes.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * A shapeless recipe whose result keeps the NBT of the ingredient that has some: a banner keeps its patterns and a
 * shulker box its contents when it is dyed or washed.
 */
public class KeepNbtShapelessRecipe extends ShapelessOreRecipe
{
	public KeepNbtShapelessRecipe(ResourceLocation group,ItemStack result,Object... recipe)
	{
		super(group,result,recipe);
	}
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack result=super.getCraftingResult(inv);
		for(int i=0;i<inv.getSizeInventory();i++)
		{
			ItemStack stack=inv.getStackInSlot(i);
			if(!stack.isEmpty()&&stack.hasTagCompound())
			{
				result.setTagCompound(stack.getTagCompound().copy());
				break;
			}
		}
		return result;
	}
}
