package net.neverandy.moredyes.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityBanner;
import net.neverandy.moredyes.block.TileColor;
import net.neverandy.moredyes.item.MDItem;

/**
 * A dyed banner: the vanilla banner's patterns and name, on a base of its color. client/DyedBannerRenderer draws the
 * base in the color instead of the vanilla base color.
 */
public class TileEntityDyedBanner extends TileEntityBanner implements IDyedTile
{
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
		this.markDirty();
	}
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.color=TileColor.clamp(compound.getInteger(COLOR_TAG));
	}
	/** Also what the banner sends to the players who see it. */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger(COLOR_TAG,this.color);
		return compound;
	}
	/** The dyed banner item with this banner's patterns and name. */
	@Override
	public ItemStack getItem()
	{
		ItemStack vanilla=super.getItem();
		ItemStack stack=new ItemStack(MDItem.banner,1,this.color);
		stack.setTagCompound(vanilla.getTagCompound());
		return stack;
	}
}
