package net.neverandy.moredyes.item;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.block.BlockDyedSlab;
import net.neverandy.moredyes.block.TileColor;

/**
 * The item of a dyed slab. Like the vanilla slab item, it turns a slab into a double slab when used on its free
 * half, but only when both slabs have the same color.
 */
public class ItemBlockDyedSlab extends ItemBlockTileColored
{
	public ItemBlockDyedSlab(Block block)
	{
		super(block);
	}
	@Override
	public EnumActionResult onItemUse(EntityPlayer player,World world,BlockPos pos,EnumHand hand,EnumFacing facing,float hitX,float hitY,float hitZ)
	{
		ItemStack stack=player.getHeldItem(hand);
		if(!stack.isEmpty()&&player.canPlayerEdit(pos.offset(facing),facing,stack))
		{
			// The slab that was clicked, on the half facing the player
			IBlockState state=world.getBlockState(pos);
			if(this.canJoin(world,pos,state,stack))
			{
				BlockDyedSlab.Type type=state.getValue(BlockDyedSlab.TYPE);
				if(facing==EnumFacing.UP&&type==BlockDyedSlab.Type.BOTTOM||facing==EnumFacing.DOWN&&type==BlockDyedSlab.Type.TOP)
				{
					return this.join(player,world,pos,state,stack);
				}
			}
			// Or the slab in the block the new one would go into
			BlockPos next=pos.offset(facing);
			IBlockState nextState=world.getBlockState(next);
			if(this.canJoin(world,next,nextState,stack))
			{
				return this.join(player,world,next,nextState,stack);
			}
		}
		return super.onItemUse(player,world,pos,hand,facing,hitX,hitY,hitZ);
	}
	/** The client only asks the server to use the item where this says it can go. */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean canPlaceBlockOnSide(World world,BlockPos pos,EnumFacing side,EntityPlayer player,ItemStack stack)
	{
		IBlockState state=world.getBlockState(pos);
		if(this.canJoin(world,pos,state,stack))
		{
			BlockDyedSlab.Type type=state.getValue(BlockDyedSlab.TYPE);
			if(side==EnumFacing.UP&&type==BlockDyedSlab.Type.BOTTOM||side==EnumFacing.DOWN&&type==BlockDyedSlab.Type.TOP)
			{
				return true;
			}
		}
		BlockPos next=pos.offset(side);
		return this.canJoin(world,next,world.getBlockState(next),stack)||super.canPlaceBlockOnSide(world,pos,side,player,stack);
	}
	private boolean canJoin(World world,BlockPos pos,IBlockState state,ItemStack stack)
	{
		return state.getBlock()==this.block&&state.getValue(BlockDyedSlab.TYPE)!=BlockDyedSlab.Type.DOUBLE
				&&TileColor.get(world,pos)==TileColor.clamp(stack.getMetadata());
	}
	private EnumActionResult join(EntityPlayer player,World world,BlockPos pos,IBlockState state,ItemStack stack)
	{
		IBlockState joined=state.withProperty(BlockDyedSlab.TYPE,BlockDyedSlab.Type.DOUBLE);
		if(world.checkNoEntityCollision(joined.getCollisionBoundingBox(world,pos).offset(pos))&&world.setBlockState(pos,joined,11))
		{
			SoundType sound=this.block.getSoundType(joined,world,pos,player);
			world.playSound(player,pos,sound.getPlaceSound(),SoundCategory.BLOCKS,(sound.getVolume()+1.0f)/2.0f,sound.getPitch()*0.8f);
			stack.shrink(1);
		}
		return EnumActionResult.SUCCESS;
	}
}
