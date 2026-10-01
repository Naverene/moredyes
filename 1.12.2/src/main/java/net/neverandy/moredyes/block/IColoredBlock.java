package net.neverandy.moredyes.block;

import net.minecraft.item.ItemStack;

public interface IColoredBlock
{
	public String[] getColors();
	public int getColorCount();
	public String getColorName(ItemStack stack);
	
}
