package net.neverandy.moredyes.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.utility.BlockInfo;

/**
 * Dyed ice and packed ice. Both are slippery and only drop themselves with silk touch, like the vanilla blocks. Ice is
 * see-through, turns into water when broken over a block and melts next to bright light; packed ice does neither.
 */
public class BlockColoredIce extends BlockColored
{
	private final boolean packed;

	public BlockColoredIce(BlockInfo info,int group,boolean packed)
	{
		super(info,group);
		this.packed=packed;
		this.slipperiness=0.98f;
		// The Block constructor read isOpaqueCube before packed was set.
		this.fullBlock=packed;
		this.setLightOpacity(packed?255:3);
		this.setTickRandomly(!packed);
	}
	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}
	@Override
	protected boolean canSilkHarvest()
	{
		return true;
	}
	@Override
	public void harvestBlock(World world,EntityPlayer player,BlockPos pos,IBlockState state,@Nullable TileEntity te,ItemStack stack)
	{
		super.harvestBlock(world,player,pos,state,te,stack);
		if(!this.packed&&EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH,stack)==0&&!world.provider.doesWaterVaporize())
		{
			Material below=world.getBlockState(pos.down()).getMaterial();
			if(below.blocksMovement()||below.isLiquid())
			{
				world.setBlockState(pos,Blocks.FLOWING_WATER.getDefaultState());
			}
		}
	}
	@Override
	public void updateTick(World world,BlockPos pos,IBlockState state,Random rand)
	{
		if(!this.packed&&world.getLightFor(EnumSkyBlock.BLOCK,pos)>11-this.getLightOpacity(state,world,pos))
		{
			if(world.provider.doesWaterVaporize())
			{
				world.setBlockToAir(pos);
			}
			else
			{
				world.setBlockState(pos,Blocks.WATER.getDefaultState());
				world.neighborChanged(pos,Blocks.WATER,pos);
			}
		}
	}
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return this.packed;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return this.packed?BlockRenderLayer.SOLID:BlockRenderLayer.TRANSLUCENT;
	}
	/** Like vanilla ice, the faces between two blocks of ice are not drawn. */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state,IBlockAccess world,BlockPos pos,EnumFacing side)
	{
		if(!this.packed&&world.getBlockState(pos.offset(side)).getBlock()==this)
		{
			return false;
		}
		return super.shouldSideBeRendered(state,world,pos,side);
	}
}
