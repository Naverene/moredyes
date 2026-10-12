package net.neverandy.moredyes.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySign;
import net.neverandy.moredyes.block.TileColor;

/**
 * A dyed sign: the vanilla sign's text, and its color. It stays a TileEntitySign, so the vanilla sign editor and the
 * server's checks of an edited sign work with it as they are.
 */
public class TileEntityDyedSign extends TileEntitySign
{
	private static final String COLOR_TAG="color";

	private int color;

	/** The color, by its position in ColorStrings.ALL. */
	public int getColor()
	{
		return this.color;
	}
	public void setColor(int color)
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
	/** Also what the sign sends to the players who see it. */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger(COLOR_TAG,this.color);
		return compound;
	}
}
