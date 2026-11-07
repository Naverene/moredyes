package net.neverandy.moredyes.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.utility.BlockInfo;

import java.util.Random;

/**
 * A dyed redstone lamp, like the colored lamps of RedPower and Project Red. A normal lamp lights up while it is powered,
 * like vanilla's; an inverted one is lit until it is powered. Like vanilla's lamp, each is two blocks, one lit and one
 * dark, that swap when the power changes, keeping the shade. Only the one placed without power has an item in the
 * creative tab (the dark normal lamp and the lit inverted lamp), and both drop it. A lamp turns on at once and off four
 * ticks later, so a short pulse doesn't make it flicker.
 */
public class BlockColoredLamp extends BlockColored
{
	private final boolean inverted;
	private final boolean lit;
	/** The same lamp in the other state; set by {@link #pair}. */
	private BlockColoredLamp other;

	public BlockColoredLamp(BlockInfo info,int group,boolean inverted,boolean lit)
	{
		super(info,group);
		this.inverted=inverted;
		this.lit=lit;
		this.setSoundType(SoundType.GLASS);
		if(lit)
		{
			this.setLightLevel(1.0f);
		}
	}
	/** Links the dark and lit block of one lamp. The one without power is the item; the other shares its name. */
	public static void pair(BlockColoredLamp dark,BlockColoredLamp lit)
	{
		dark.other=lit;
		lit.other=dark;
		BlockColoredLamp hidden=dark.inverted?dark:lit;
		hidden.setCreativeTab(null);
		hidden.setUnlocalizedName(hidden.other.getUnlocalizedName().substring("tile.".length()));
	}
	public boolean isInverted()
	{
		return this.inverted;
	}
	/** The block of this lamp that is placed without power, which is the one that has an item. */
	private BlockColoredLamp itemBlock()
	{
		return this.lit==this.inverted?this:this.other;
	}
	private boolean shouldBeLit(World world,BlockPos pos)
	{
		return world.isBlockPowered(pos)!=this.inverted;
	}
	private void swap(World world,BlockPos pos,IBlockState state)
	{
		world.setBlockState(pos,this.other.getDefaultState().withProperty(SHADE,state.getValue(SHADE)),2);
	}
	@Override
	public void onBlockAdded(World world,BlockPos pos,IBlockState state)
	{
		if(!world.isRemote&&this.lit!=this.shouldBeLit(world,pos))
		{
			this.swap(world,pos,state);
		}
	}
	@Override
	public void neighborChanged(IBlockState state,World world,BlockPos pos,net.minecraft.block.Block block,BlockPos fromPos)
	{
		if(world.isRemote||this.lit==this.shouldBeLit(world,pos))
		{
			return;
		}
		if(this.lit)
		{
			world.scheduleUpdate(pos,this,4);
		}
		else
		{
			this.swap(world,pos,state);
		}
	}
	@Override
	public void updateTick(World world,BlockPos pos,IBlockState state,Random rand)
	{
		if(!world.isRemote&&this.lit&&!this.shouldBeLit(world,pos))
		{
			this.swap(world,pos,state);
		}
	}
	@Override
	public Item getItemDropped(IBlockState state,Random rand,int fortune)
	{
		return Item.getItemFromBlock(this.itemBlock());
	}
	@Override
	public ItemStack getItem(World world,BlockPos pos,IBlockState state)
	{
		return new ItemStack(this.itemBlock(),1,this.getMetaFromState(state));
	}

	/** A lit lamp has a see-through halo around it (models/block/tinted_cube_all_glow), so it is drawn in the translucent layer. */
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return this.lit?BlockRenderLayer.TRANSLUCENT:super.getBlockLayer();
	}

	/** A lit lamp draws at full brightness (its model is not shaded either), so it looks like the light comes from it. */
	@Override
	@SideOnly(Side.CLIENT)
	public int getPackedLightmapCoords(IBlockState state,IBlockAccess source,BlockPos pos)
	{
		return this.lit?15<<20|15<<4:super.getPackedLightmapCoords(state,source,pos);
	}
}
