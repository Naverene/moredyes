package net.neverandy.moredyes.compat.ironchest;

import cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType;
import cpw.mods.ironchest.common.tileentity.shulker.TileEntityIronShulkerBox;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

/**
 * A dyed Iron Chests shulker box: Iron Chests' box of a tier, which holds the items, opens and closes, and keeps the
 * facing and the crystal box's top items, and the color, by its position in ColorStrings.ALL. Each tier has its own
 * class, as tile entities are saved by class.
 */
public abstract class TileEntityDyedIronShulkerBox extends TileEntityIronShulkerBox
{
	private static final String COLOR_TAG="dye";

	private final IronShulkerBoxType tier;
	private int color;

	protected TileEntityDyedIronShulkerBox(IronShulkerBoxType tier)
	{
		super(null,tier);
		this.tier=tier;
	}

	/** Iron Chests reads the tier from its own block's metadata; a dyed box's block has one tier. */
	@Override
	public IronShulkerBoxType getType()
	{
		return this.tier;
	}
	public int getDyeIndex()
	{
		return this.color;
	}
	public void setColor(int color)
	{
		this.color=IronShulkerBoxCompat.clampColor(color);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.color=IronShulkerBoxCompat.clampColor(compound.getInteger(COLOR_TAG));
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger(COLOR_TAG,this.color);
		return compound;
	}
	/** Players who see the box get its color along with what Iron Chests sends (the facing). */
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound tag=super.getUpdateTag();
		tag.setInteger(COLOR_TAG,this.color);
		return tag;
	}
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		SPacketUpdateTileEntity packet=super.getUpdatePacket();
		NBTTagCompound tag=packet==null?new NBTTagCompound():packet.getNbtCompound();
		tag.setInteger(COLOR_TAG,this.color);
		return new SPacketUpdateTileEntity(this.pos,0,tag);
	}
	@Override
	public void onDataPacket(NetworkManager net,SPacketUpdateTileEntity packet)
	{
		super.onDataPacket(net,packet);
		this.color=IronShulkerBoxCompat.clampColor(packet.getNbtCompound().getInteger(COLOR_TAG));
	}

	public static class Iron extends TileEntityDyedIronShulkerBox
	{
		public Iron()
		{
			super(IronShulkerBoxType.IRON);
		}
	}
	public static class Gold extends TileEntityDyedIronShulkerBox
	{
		public Gold()
		{
			super(IronShulkerBoxType.GOLD);
		}
	}
	public static class Diamond extends TileEntityDyedIronShulkerBox
	{
		public Diamond()
		{
			super(IronShulkerBoxType.DIAMOND);
		}
	}
	public static class Copper extends TileEntityDyedIronShulkerBox
	{
		public Copper()
		{
			super(IronShulkerBoxType.COPPER);
		}
	}
	public static class Silver extends TileEntityDyedIronShulkerBox
	{
		public Silver()
		{
			super(IronShulkerBoxType.SILVER);
		}
	}
	public static class Crystal extends TileEntityDyedIronShulkerBox
	{
		public Crystal()
		{
			super(IronShulkerBoxType.CRYSTAL);
		}
	}
	public static class Obsidian extends TileEntityDyedIronShulkerBox
	{
		public Obsidian()
		{
			super(IronShulkerBoxType.OBSIDIAN);
		}
	}
}
