package net.neverandy.moredyes.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.tileentity.TileEntityColor;
import net.neverandy.moredyes.tileentity.TileEntityDyedPiston;
import net.neverandy.moredyes.tileentity.TileEntityDyedSign;
import net.neverandy.moredyes.utility.ColorUtil;

/**
 * Shared code for the blocks that keep their color in a TileEntityColor (stairs, slabs, walls, fences, tall flowers,
 * flower pots, the iron trapdoor and pistons; signs keep theirs in TileEntityDyedSign), as in 1.7.10. Their metadata
 * holds their shape, so there is one such block for all colors, and the item damage is the color's position in
 * ColorStrings.ALL.
 * <p>
 * These blocks stay in the world until harvestBlock when a player breaks them (see {@link #harvested}), so the
 * tile entity, and with it the color, is still there when the drops are worked out. Like any block with a tile
 * entity, pistons cannot push them.
 */
public final class TileColor
{
	private TileColor()
	{
	}

	/** Keeps a color inside ColorStrings.ALL, falling back to the first color. */
	public static int clamp(int color)
	{
		return color<0||color>=ColorStrings.ALL.length?0:color;
	}
	/** The color of the block at this position, also while it is a moving dyed piston, or 0 if it has none. */
	public static int get(IBlockAccess world,BlockPos pos)
	{
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof TileEntityColor)
		{
			return ((TileEntityColor)tile).getColor();
		}
		if(tile instanceof TileEntityDyedPiston)
		{
			return ((TileEntityDyedPiston)tile).getColor();
		}
		if(tile instanceof TileEntityDyedSign)
		{
			return ((TileEntityDyedSign)tile).getColor();
		}
		return 0;
	}
	/** Sets the color of the block at this position and sends it to the players who can see it. */
	public static void set(World world,BlockPos pos,int color)
	{
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof TileEntityColor)
		{
			((TileEntityColor)tile).setColor(color);
		}
		else if(tile instanceof TileEntityDyedSign)
		{
			((TileEntityDyedSign)tile).setColor(color);
		}
		else
		{
			return;
		}
		IBlockState state=world.getBlockState(pos);
		world.notifyBlockUpdate(pos,state,state,3);
	}
	/** The color as 0xRRGGBB. */
	public static int rgb(int color)
	{
		return ColorUtil.fromHex(ColorStrings.ALL[clamp(color)]);
	}
	/** The item of this block in the color of the block at this position. */
	public static ItemStack stack(Block block,IBlockAccess world,BlockPos pos,int count)
	{
		return new ItemStack(block,count,get(world,pos));
	}
	/** One item per color, for the creative tabs. */
	public static void getSubBlocks(Block block,NonNullList<ItemStack> items)
	{
		for(int i=0;i<ColorStrings.ALL.length;i++)
		{
			items.add(new ItemStack(block,1,i));
		}
	}
	/**
	 * Use at the end of Block.harvestBlock: removes the block that removedByPlayer left in place. The blocks'
	 * removedByPlayer leaves a block that will drop in place, so its color can still be read for the drops.
	 */
	public static void harvested(World world,BlockPos pos)
	{
		world.setBlockToAir(pos);
	}
}
