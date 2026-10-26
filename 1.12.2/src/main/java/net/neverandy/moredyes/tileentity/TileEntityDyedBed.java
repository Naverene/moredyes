package net.neverandy.moredyes.tileentity;

import javax.annotation.Nullable;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityBed;
import net.neverandy.moredyes.api.MoreDyesAPI;
import net.neverandy.moredyes.block.TileColor;
import net.neverandy.moredyes.item.MDItem;

/**
 * A dyed bed: both halves keep the color, and the planks the bed was made from. It stays a TileEntityBed, whose own
 * color is set to the nearest vanilla one, so a map shows the bed in about its color.
 * <p>
 * The planks are kept as a block id in the "Wood" tag, on the item and here: "minecraft:spruce_planks" for vanilla
 * planks, "moredyes:planks_&lt;hex&gt;" for dyed ones, and no tag for oak. A bed of vanilla wool and planks other than oak
 * is a dyed bed too, in the vanilla color in its "VanillaColor" tag, so it can keep its wood.
 */
public class TileEntityDyedBed extends TileEntityBed implements IDyedTile
{
	/** The vanilla bed saves its own color as "color". */
	private static final String COLOR_TAG="dye";
	public static final String WOOD_TAG="Wood";
	public static final String VANILLA_COLOR_TAG="VanillaColor";
	public static final String OAK="minecraft:oak_planks";

	private int color;
	@Nullable
	private String wood;
	/** The vanilla color the bedding has, or -1 for the More Dyes color. */
	private int vanillaColor=-1;

	@Override
	public int getColorIndex()
	{
		return this.color;
	}
	@Override
	public void setColorIndex(int color)
	{
		this.color=TileColor.clamp(color);
		this.updateVanillaColor();
	}
	private void updateVanillaColor()
	{
		this.setColor(this.vanillaColor>=0?EnumDyeColor.byMetadata(this.vanillaColor)
				:MoreDyesAPI.nearestVanillaColor(TileColor.rgb(this.color)));
	}
	/** The planks the bed was made from, or null for oak. */
	@Nullable
	public String getWood()
	{
		return this.wood;
	}
	/** The vanilla color of the bedding, or -1 when it has the More Dyes color. */
	public int getVanillaColor()
	{
		return this.vanillaColor;
	}
	@Override
	public void setItemValues(ItemStack stack)
	{
		this.wood=woodOf(stack);
		this.vanillaColor=vanillaColorOf(stack);
		this.setColorIndex(stack.getMetadata());
	}
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.color=TileColor.clamp(compound.getInteger(COLOR_TAG));
		this.wood=compound.hasKey(WOOD_TAG,8)?compound.getString(WOOD_TAG):null;
		this.vanillaColor=compound.hasKey(VANILLA_COLOR_TAG,99)?compound.getByte(VANILLA_COLOR_TAG)&15:-1;
	}
	/** Also what the bed sends to the players who see it. */
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger(COLOR_TAG,this.color);
		if(this.wood!=null)
		{
			compound.setString(WOOD_TAG,this.wood);
		}
		if(this.vanillaColor>=0)
		{
			compound.setByte(VANILLA_COLOR_TAG,(byte)this.vanillaColor);
		}
		return compound;
	}
	@Override
	public ItemStack getItemStack()
	{
		ItemStack stack=new ItemStack(MDItem.bed,1,this.color);
		setWood(stack,this.wood);
		if(this.vanillaColor>=0)
		{
			stack.setTagInfo(VANILLA_COLOR_TAG,new NBTTagByte((byte)this.vanillaColor));
		}
		return stack;
	}

	/** The planks a bed item was made from, or null for oak. */
	@Nullable
	public static String woodOf(ItemStack stack)
	{
		NBTTagCompound tag=stack.getTagCompound();
		return tag!=null&&tag.hasKey(WOOD_TAG,8)?tag.getString(WOOD_TAG):null;
	}
	public static void setWood(ItemStack stack,@Nullable String wood)
	{
		NBTTagCompound tag=stack.getTagCompound();
		if(wood==null||OAK.equals(wood))
		{
			if(tag!=null)
			{
				tag.removeTag(WOOD_TAG);
				if(tag.hasNoTags())
				{
					stack.setTagCompound(null);
				}
			}
			return;
		}
		if(tag==null)
		{
			tag=new NBTTagCompound();
			stack.setTagCompound(tag);
		}
		tag.setString(WOOD_TAG,wood);
	}
	/** The vanilla color of a bed item's bedding, or -1 when it has the More Dyes color of its damage. */
	public static int vanillaColorOf(ItemStack stack)
	{
		NBTTagCompound tag=stack.getTagCompound();
		return tag!=null&&tag.hasKey(VANILLA_COLOR_TAG,99)?tag.getByte(VANILLA_COLOR_TAG)&15:-1;
	}
	/** A More Dyes bed item in a vanilla color, for a vanilla bed made from planks other than oak. */
	public static ItemStack vanillaColorBed(EnumDyeColor color)
	{
		ItemStack stack=new ItemStack(MDItem.bed);
		stack.setTagInfo(VANILLA_COLOR_TAG,new NBTTagByte((byte)color.getMetadata()));
		return stack;
	}
}
