package net.neverandy.moredyes.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.EnumFacing;
import net.neverandy.moredyes.block.ITileColoredBlock;
import net.neverandy.moredyes.block.TileColor;

/**
 * A dyed piston on the move, while it retracts (see BlockDyedPiston). The vanilla moving block only remembers the
 * block and its metadata, so this also carries the color, puts it back on the piston when it stops, and lets the
 * piston be drawn in its color meanwhile (TileColor.get reads it).
 */
public class TileEntityDyedPiston extends TileEntityPiston
{
	private static final String COLOR_TAG="color";

	private int color;
	private boolean colorRestored;

	public TileEntityDyedPiston()
	{
	}
	public TileEntityDyedPiston(IBlockState state,EnumFacing facing,boolean extending,boolean shouldHeadBeRendered,int color)
	{
		super(state,facing,extending,shouldHeadBeRendered);
		this.color=TileColor.clamp(color);
	}
	/** The color, by its position in ColorStrings.ALL. */
	public int getColor()
	{
		return this.color;
	}
	@Override
	public void update()
	{
		super.update();
		this.restoreColor();
	}
	@Override
	public void clearPistonTileEntity()
	{
		super.clearPistonTileEntity();
		this.restoreColor();
	}
	/** Once the moving block has put the piston back, gives it its color again. */
	private void restoreColor()
	{
		if(this.isInvalid()&&!this.colorRestored&&this.world!=null
				&&this.world.getBlockState(this.pos).getBlock() instanceof ITileColoredBlock)
		{
			this.colorRestored=true;
			TileColor.set(this.world,this.pos,this.color);
		}
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
}
