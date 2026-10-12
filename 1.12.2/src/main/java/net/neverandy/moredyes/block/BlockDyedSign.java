package net.neverandy.moredyes.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityDyedSign;
import net.neverandy.moredyes.utility.BlockInfo;

/**
 * Dyed signs in every color: an oak sign whose wood takes the dye color. Like the vanilla sign there is a standing and
 * a wall block, both placed by one item (item/ItemDyedSign), and the color is kept in the sign's tile entity next to
 * its text. Both are drawn by client/DyedSignRenderer.
 */
public final class BlockDyedSign
{
	private BlockDyedSign()
	{
	}

	private static void apply(Block block,BlockInfo info,String name)
	{
		block.setHardness(info.hardness);
		block.setHarvestLevel(info.harvestTool,info.harvestLevel);
		block.setRegistryName(Reference.MOD_ID,name);
		block.setUnlocalizedName(Reference.MOD_ID+"."+info.blockName);
	}
	/** The sign item in the color of the sign at this position. */
	private static ItemStack stack(IBlockAccess world,BlockPos pos)
	{
		return new ItemStack(MDItem.sign,1,TileColor.get(world,pos));
	}
	/** Clears the sign that a player's removedByPlayer left in place for its drops; see TileColor.harvested. */
	private static boolean removedByPlayer(Block block,IBlockState state,World world,BlockPos pos,EntityPlayer player)
	{
		block.onBlockHarvested(world,pos,state,player);
		return true;
	}

	public static class Standing extends BlockStandingSign implements ITileColoredBlock
	{
		public Standing(BlockInfo info)
		{
			apply(this,info,"standing_sign");
			this.setSoundType(info.sound);
		}
		@Override
		public String getTypeName()
		{
			return "standing_sign";
		}
		@Override
		public TileEntity createNewTileEntity(World world,int meta)
		{
			return new TileEntityDyedSign();
		}
		@Override
		public void getDrops(NonNullList<ItemStack> drops,IBlockAccess world,BlockPos pos,IBlockState state,int fortune)
		{
			drops.add(stack(world,pos));
		}
		@Override
		public ItemStack getItem(World world,BlockPos pos,IBlockState state)
		{
			return stack(world,pos);
		}
		@Override
		public boolean removedByPlayer(IBlockState state,World world,BlockPos pos,EntityPlayer player,boolean willHarvest)
		{
			return willHarvest?BlockDyedSign.removedByPlayer(this,state,world,pos,player):super.removedByPlayer(state,world,pos,player,false);
		}
		@Override
		public void harvestBlock(World world,EntityPlayer player,BlockPos pos,IBlockState state,@Nullable TileEntity te,ItemStack stack)
		{
			super.harvestBlock(world,player,pos,state,te,stack);
			TileColor.harvested(world,pos);
		}
	}

	public static class Wall extends BlockWallSign implements ITileColoredBlock
	{
		public Wall(BlockInfo info)
		{
			apply(this,info,"wall_sign");
			this.setSoundType(info.sound);
		}
		@Override
		public String getTypeName()
		{
			return "wall_sign";
		}
		@Override
		public TileEntity createNewTileEntity(World world,int meta)
		{
			return new TileEntityDyedSign();
		}
		@Override
		public void getDrops(NonNullList<ItemStack> drops,IBlockAccess world,BlockPos pos,IBlockState state,int fortune)
		{
			drops.add(stack(world,pos));
		}
		@Override
		public ItemStack getItem(World world,BlockPos pos,IBlockState state)
		{
			return stack(world,pos);
		}
		@Override
		public boolean removedByPlayer(IBlockState state,World world,BlockPos pos,EntityPlayer player,boolean willHarvest)
		{
			return willHarvest?BlockDyedSign.removedByPlayer(this,state,world,pos,player):super.removedByPlayer(state,world,pos,player,false);
		}
		@Override
		public void harvestBlock(World world,EntityPlayer player,BlockPos pos,IBlockState state,@Nullable TileEntity te,ItemStack stack)
		{
			super.harvestBlock(world,player,pos,state,te,stack);
			TileColor.harvested(world,pos);
		}
	}
}
