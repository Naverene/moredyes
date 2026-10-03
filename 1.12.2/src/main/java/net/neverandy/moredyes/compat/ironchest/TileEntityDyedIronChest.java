package net.neverandy.moredyes.compat.ironchest;

import cpw.mods.ironchest.common.blocks.chest.IronChestType;
import cpw.mods.ironchest.common.tileentity.chest.TileEntityIronChest;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

/**
 * The inventory of a dyed Iron Chests chest: Iron Chests' chest of its tier, which also keeps the color. One subclass
 * per tier, because the game makes tile entities from their class when a world loads.
 * <p>
 * Iron Chests takes a chest's tier from its block, and sends the lid and facing updates to the clients as block
 * events of its own block, which the game drops for any other block. So the tier is given here, and those events
 * are sent again for the dyed chest block.
 */
public abstract class TileEntityDyedIronChest extends TileEntityIronChest
{
	private static final String COLOR_TAG="MoreDyesColor";

	private final IronChestCompat.Tier tier;
	private int color;

	protected TileEntityDyedIronChest(IronChestCompat.Tier tier)
	{
		super(tier.type);
		this.tier=tier;
	}
	public IronChestCompat.Tier getTier()
	{
		return this.tier;
	}
	@Override
	public IronChestType getType()
	{
		return this.tier.type;
	}
	/** The color, by its position in ColorStrings.ALL. */
	public int getColor()
	{
		return this.color;
	}
	/** Sets the color; a chest in a world must then be marked dirty and sent to the clients. */
	public void setColor(int color)
	{
		this.color=IronChestCompat.clampColor(color);
	}

	/**
	 * Iron Chests sorts the items a crystal chest shows and sends them to the clients, which needs a world. Its own
	 * chests have no tier until they are in a world, so it never sorts before; this chest always has one.
	 */
	@Override
	protected void sortTopStacks()
	{
		if(this.world!=null)
		{
			super.sortTopStacks();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.color=IronChestCompat.clampColor(compound.getInteger(COLOR_TAG));
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger(COLOR_TAG,this.color);
		return compound;
	}
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound compound=super.getUpdateTag();
		compound.setInteger(COLOR_TAG,this.color);
		return compound;
	}
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		SPacketUpdateTileEntity packet=super.getUpdatePacket();
		packet.getNbtCompound().setInteger(COLOR_TAG,this.color);
		return packet;
	}
	@Override
	public void onDataPacket(NetworkManager net,SPacketUpdateTileEntity packet)
	{
		super.onDataPacket(net,packet);
		if(packet.getNbtCompound().hasKey(COLOR_TAG))
		{
			this.color=IronChestCompat.clampColor(packet.getNbtCompound().getInteger(COLOR_TAG));
		}
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
		super.openInventory(player);
		this.sendPlayerCount(player);
	}
	@Override
	public void closeInventory(EntityPlayer player)
	{
		super.closeInventory(player);
		this.sendPlayerCount(player);
	}
	/** Tells the clients how many players have the chest open, so they open and close its lid. */
	private void sendPlayerCount(EntityPlayer player)
	{
		if(this.world!=null&&!player.isSpectator())
		{
			Block block=this.getBlockType();
			this.world.addBlockEvent(this.pos,block,1,this.numPlayersUsing);
			this.world.notifyNeighborsOfStateChange(this.pos,block,false);
			this.world.notifyNeighborsOfStateChange(this.pos.down(),block,false);
		}
	}
	@Override
	public void rotateAround()
	{
		this.setFacing(this.getFacing().rotateY());
		this.world.addBlockEvent(this.pos,this.getBlockType(),2,this.getFacing().ordinal());
		this.markDirty();
	}

	public static class Iron extends TileEntityDyedIronChest
	{
		public Iron()
		{
			super(IronChestCompat.Tier.IRON);
		}
	}
	public static class Gold extends TileEntityDyedIronChest
	{
		public Gold()
		{
			super(IronChestCompat.Tier.GOLD);
		}
	}
	public static class Diamond extends TileEntityDyedIronChest
	{
		public Diamond()
		{
			super(IronChestCompat.Tier.DIAMOND);
		}
	}
	public static class Copper extends TileEntityDyedIronChest
	{
		public Copper()
		{
			super(IronChestCompat.Tier.COPPER);
		}
	}
	public static class Silver extends TileEntityDyedIronChest
	{
		public Silver()
		{
			super(IronChestCompat.Tier.SILVER);
		}
	}
	public static class Crystal extends TileEntityDyedIronChest
	{
		public Crystal()
		{
			super(IronChestCompat.Tier.CRYSTAL);
		}
	}
	public static class Obsidian extends TileEntityDyedIronChest
	{
		public Obsidian()
		{
			super(IronChestCompat.Tier.OBSIDIAN);
		}
	}
}
