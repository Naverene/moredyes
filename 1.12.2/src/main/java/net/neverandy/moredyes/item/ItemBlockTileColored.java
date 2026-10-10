package net.neverandy.moredyes.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.neverandy.moredyes.block.TileColor;
import net.neverandy.moredyes.reference.ColorStrings;

/**
 * The item of a block that keeps its color in a tile entity (see TileColor). The item damage is the color; the
 * placed block gets it before anything else, so a piston already has its color when it first moves.
 */
public class ItemBlockTileColored extends ItemBlock
{
	public ItemBlockTileColored(Block block)
	{
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}
	/** The color is not part of the placed block's metadata. */
	@Override
	public int getMetadata(int damage)
	{
		return 0;
	}
	/** "ECBF99 Granite Stairs". */
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return ItemBlockColored.coloredName(ColorStrings.ALL[TileColor.clamp(stack.getMetadata())],this.block.getUnlocalizedName()+".name");
	}
	@Override
	public boolean placeBlockAt(ItemStack stack,EntityPlayer player,World world,BlockPos pos,EnumFacing side,float hitX,float hitY,float hitZ,IBlockState newState)
	{
		if(!world.setBlockState(pos,newState,11))
		{
			return false;
		}
		IBlockState state=world.getBlockState(pos);
		if(state.getBlock()==this.block)
		{
			TileColor.set(world,pos,stack.getMetadata());
			setTileEntityNBT(world,player,pos,stack);
			this.block.onBlockPlacedBy(world,pos,state,player,stack);
			if(player instanceof EntityPlayerMP)
			{
				CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player,pos,stack);
			}
		}
		return true;
	}
}
