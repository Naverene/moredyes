package net.neverandy.moredyes.block;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.neverandy.moredyes.utility.BlockInfo;

/** A dyed snow block drops four snowballs, or itself with silk touch, like the vanilla one. */
public class BlockColoredSnow extends BlockColored
{
	public BlockColoredSnow(BlockInfo info,int group)
	{
		super(info,group);
	}
	@Override
	public Item getItemDropped(IBlockState state,Random rand,int fortune)
	{
		return Items.SNOWBALL;
	}
	@Override
	public int quantityDropped(Random random)
	{
		return 4;
	}
	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}
}
