package net.neverandy.moredyes.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityBed;
import net.neverandy.moredyes.api.MoreDyesAPI;
import net.neverandy.moredyes.block.TileColor;
import net.neverandy.moredyes.item.MDItem;

/**
 * A dyed bed: both halves keep the color. It stays a TileEntityBed, whose own color is set to the nearest vanilla one,
 * so a map shows the bed in about its color.
 */
public class TileEntityDyedBed extends TileEntityBed implements IDyedTile
{
	/** The vanilla bed saves its own color as "color". */
	private static final String COLOR_TAG="dye";

	private int color;

	@Override
	public int getColorIndex()
	{
		return this.color;
	}
	@Override
	public void setColorIndex(int color)
	{
		this.color=TileColor.clamp(color);
		this.setColor(MoreDyesAPI.nearestVanillaColor(TileColor.rgb(this.color)));
	}
	@Override
	public void setItemValues(ItemStack stack)
	{
		this.setColorIndex(stack.getMetadata());
	}
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.color=TileColor.clamp(compound.getInteger(COLOR_TAG));
	}
	/** Also what the bed sends to the players who see it. */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger(COLOR_TAG,this.color);
		return compound;
	}
	@Override
	public ItemStack getItemStack()
	{
		return new ItemStack(MDItem.bed,1,this.color);
	}
}
