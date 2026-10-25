package net.neverandy.moredyes.tileentity;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.neverandy.moredyes.block.TileColor;

/** A dyed shulker box: the vanilla box (purple underneath), and its color. */
public class TileEntityDyedShulkerBox extends TileEntityShulkerBox implements IDyedTile
{
	private static final String COLOR_TAG="dye";

	private int color;

	public TileEntityDyedShulkerBox()
	{
		super(EnumDyeColor.PURPLE);
	}
	@Override
	public int getColorIndex()
	{
		return this.color;
	}
	@Override
	public void setColorIndex(int color)
	{
		this.color=TileColor.clamp(color);
		this.markDirty();
	}
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.color=TileColor.clamp(compound.getInteger(COLOR_TAG));
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger(COLOR_TAG,this.color);
		return compound;
	}
	/** The vanilla box sends the players who see it no data; this one sends its color. */
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound tag=super.getUpdateTag();
		tag.setInteger(COLOR_TAG,this.color);
		return tag;
	}
}
