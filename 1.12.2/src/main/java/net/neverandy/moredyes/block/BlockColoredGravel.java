package net.neverandy.moredyes.block;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.neverandy.moredyes.utility.BlockInfo;

/** Dyed gravel falls like sand and sometimes drops a flint instead of itself, like vanilla gravel. */
public class BlockColoredGravel extends BlockColoredSand
{
	public BlockColoredGravel(BlockInfo info,int group)
	{
		super(info,group);
	}
	/** The vanilla chance: one in ten, more with fortune, and always flint with fortune above III. */
	@Override
	public void getDrops(NonNullList<ItemStack> drops,IBlockAccess world,BlockPos pos,IBlockState state,int fortune)
	{
		Random rand=world instanceof World?((World)world).rand:RANDOM;
		if(fortune>3||rand.nextInt(10-fortune*3)==0)
		{
			drops.add(new ItemStack(Items.FLINT));
		}
		else
		{
			drops.add(new ItemStack(this,1,this.damageDropped(state)));
		}
	}
	/** Only what grows in caves, unlike sand. */
	@Override
	public boolean canSustainPlant(IBlockState state,IBlockAccess world,BlockPos pos,EnumFacing direction,IPlantable plantable)
	{
		return plantable.getPlantType(world,pos.offset(direction))==EnumPlantType.Cave&&state.isSideSolid(world,pos,EnumFacing.UP);
	}
}
