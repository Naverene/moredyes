package net.neverandy.moredyes.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.neverandy.moredyes.block.TileColor;

/**
 * The color of a block that holds every color in one block (stairs, slabs, walls, the iron trapdoor and pistons),
 * whose metadata is taken by its shape. See TileColor.
 */
public class TileEntityColor extends TileEntity implements IDyedTile
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
	public int getColorIndex()
	{
		return this.color;
	}
	@Override
	public void setColorIndex(int color)
	{
		this.setColor(color);
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
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return this.writeToNBT(new NBTTagCompound());
	}
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(this.pos,0,this.getUpdateTag());
	}
	@Override
	public void onDataPacket(NetworkManager net,SPacketUpdateTileEntity packet)
	{
		this.readFromNBT(packet.getNbtCompound());
		this.world.markBlockRangeForRenderUpdate(this.pos,this.pos);
	}
	/** The color stays while the block changes its shape (a stair turning, a slab doubling, a trapdoor opening). */
	@Override
	public boolean shouldRefresh(World world,BlockPos pos,IBlockState oldState,IBlockState newState)
	{
		return oldState.getBlock()!=newState.getBlock();
	}
}
