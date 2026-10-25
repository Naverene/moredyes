package net.neverandy.moredyes.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBanner;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityDyedBanner;

/**
 * Dyed banners in every color: the vanilla banner, with a base of the dye color under its patterns. Like the vanilla
 * banner there is a standing and a wall block, both placed by one item (item/ItemDyedBanner); the color is kept in
 * the banner's tile entity next to its patterns, and both are drawn by client/DyedBannerRenderer. The vanilla blocks
 * already drop what the tile entity's getItem gives.
 */
public final class BlockDyedBanner
{
	private BlockDyedBanner()
	{
	}

	private static void apply(Block block,String name)
	{
		block.setHardness(1.0f);
		block.setRegistryName(Reference.MOD_ID,name);
		block.setUnlocalizedName(Reference.MOD_ID+".banner");
	}

	public static class Standing extends BlockBanner.BlockBannerStanding
	{
		public Standing()
		{
			apply(this,"standing_banner");
			this.setSoundType(SoundType.WOOD);
		}
		@Override
		public TileEntity createNewTileEntity(World world,int meta)
		{
			return new TileEntityDyedBanner();
		}
		@Override
		public Item getItemDropped(IBlockState state,Random rand,int fortune)
		{
			return MDItem.banner;
		}
	}

	public static class Wall extends BlockBanner.BlockBannerHanging
	{
		public Wall()
		{
			apply(this,"wall_banner");
			this.setSoundType(SoundType.WOOD);
		}
		@Override
		public TileEntity createNewTileEntity(World world,int meta)
		{
			return new TileEntityDyedBanner();
		}
		@Override
		public Item getItemDropped(IBlockState state,Random rand,int fortune)
		{
			return MDItem.banner;
		}
	}
}
