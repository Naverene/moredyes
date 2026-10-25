package net.neverandy.moredyes.compat.ironchest;

import javax.annotation.Nullable;

import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.common.tileentity.shulker.TileEntityIronShulkerBox;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

/**
 * An Iron Chests shulker box of one tier in any More Dyes color. It behaves like Iron Chests' own box (see
 * BlockIronShulkerBox): it opens unless something blocks the lid, and it drops itself with what it holds when broken,
 * like a vanilla shulker box. Iron Chests keeps the tier in the block's metadata and the color in the block; this block
 * has one tier, and the color is kept by the tile entity.
 */
public class BlockDyedIronShulkerBox extends Block
{
	private final IronShulkerBoxCompat.Tier tier;
	/** The side clicked to place the box, which it faces; Iron Chests does the same. */
	private EnumFacing placedFacing=EnumFacing.UP;

	public BlockDyedIronShulkerBox(IronShulkerBoxCompat.Tier tier)
	{
		super(Material.IRON);
		this.tier=tier;
		String name="dyed_"+tier.id()+"_shulker_box";
		this.setRegistryName(Reference.MOD_ID,name);
		this.setUnlocalizedName(Reference.MOD_ID+"."+name);
		// The same as Iron Chests' boxes.
		this.setHardness(3.0F);
		this.setSoundType(SoundType.METAL);
		this.setCreativeTab(MoreDyes.tabBlocks);
	}
	public IronShulkerBoxCompat.Tier getTier()
	{
		return this.tier;
	}

	/** The color of the box at a position, by its position in ColorStrings.ALL. */
	public static int getColor(IBlockAccess world,BlockPos pos)
	{
		TileEntity te=world.getTileEntity(pos);
		return te instanceof TileEntityDyedIronShulkerBox?((TileEntityDyedIronShulkerBox)te).getDyeIndex():0;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	@Override
	@SuppressWarnings("deprecation")
	public boolean causesSuffocation(IBlockState state)
	{
		return true;
	}
	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("deprecation")
	public boolean hasCustomBreakingProgress(IBlockState state)
	{
		return true;
	}
	@Override
	@SuppressWarnings("deprecation")
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	@Override
	@SuppressWarnings("deprecation")
	public BlockFaceShape getBlockFaceShape(IBlockAccess world,IBlockState state,BlockPos pos,EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos)
	{
		TileEntity te=source.getTileEntity(pos);
		return te instanceof TileEntityIronShulkerBox?((TileEntityIronShulkerBox)te).getBoundingBox():FULL_BLOCK_AABB;
	}
	@Override
	@SuppressWarnings("deprecation")
	public EnumPushReaction getMobilityFlag(IBlockState state)
	{
		return EnumPushReaction.DESTROY;
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	@Override
	public TileEntity createTileEntity(World world,IBlockState state)
	{
		try
		{
			return this.tier.tileClass.newInstance();
		}
		catch(ReflectiveOperationException e)
		{
			throw new IllegalStateException(e);
		}
	}

	/** Opens Iron Chests' own screen for the tier, through its GUI handler, unless something blocks the lid. */
	@Override
	public boolean onBlockActivated(World world,BlockPos pos,IBlockState state,EntityPlayer player,EnumHand hand,EnumFacing side,float hitX,float hitY,float hitZ)
	{
		if(world.isRemote||player.isSpectator())
		{
			return true;
		}
		TileEntity te=world.getTileEntity(pos);
		if(!(te instanceof TileEntityDyedIronShulkerBox))
		{
			return false;
		}
		TileEntityDyedIronShulkerBox box=(TileEntityDyedIronShulkerBox)te;
		EnumFacing facing=box.getFacing();
		boolean open=true;
		if(box.getAnimationStatus()==TileEntityIronShulkerBox.AnimationStatus.CLOSED)
		{
			AxisAlignedBB lid=FULL_BLOCK_AABB.expand(0.5F*facing.getFrontOffsetX(),0.5F*facing.getFrontOffsetY(),0.5F*facing.getFrontOffsetZ())
					.contract(facing.getFrontOffsetX(),facing.getFrontOffsetY(),facing.getFrontOffsetZ());
			open=!world.collidesWithAnyBlock(lid.offset(pos.offset(facing)));
		}
		if(open)
		{
			player.openGui(IronChest.instance,this.tier.type.ordinal(),world,pos.getX(),pos.getY(),pos.getZ());
		}
		return true;
	}
	@Override
	public IBlockState getStateForPlacement(World world,BlockPos pos,EnumFacing facing,float hitX,float hitY,float hitZ,int meta,EntityLivingBase placer,EnumHand hand)
	{
		this.placedFacing=facing;
		return this.getDefaultState();
	}
	@Override
	public void onBlockAdded(World world,BlockPos pos,IBlockState state)
	{
		super.onBlockAdded(world,pos,state);
		world.notifyBlockUpdate(pos,state,state,3);
	}
	/** Takes the color from the item's damage value; the contents come from its BlockEntityTag, as on any block. */
	@Override
	public void onBlockPlacedBy(World world,BlockPos pos,IBlockState state,EntityLivingBase placer,ItemStack stack)
	{
		TileEntity te=world.getTileEntity(pos);
		if(te instanceof TileEntityDyedIronShulkerBox)
		{
			TileEntityDyedIronShulkerBox box=(TileEntityDyedIronShulkerBox)te;
			box.setFacing(this.placedFacing);
			box.setColor(stack.getMetadata());
			if(stack.hasDisplayName())
			{
				box.setCustomName(stack.getDisplayName());
			}
			box.markDirty();
			world.notifyBlockUpdate(pos,state,state,3);
		}
	}
	@Override
	@SuppressWarnings("deprecation")
	public boolean eventReceived(IBlockState state,World world,BlockPos pos,int id,int param)
	{
		super.eventReceived(state,world,pos,id,param);
		TileEntity te=world.getTileEntity(pos);
		return te!=null&&te.receiveClientEvent(id,param);
	}

	/** The box in its color, holding what it holds, with its name. */
	public static ItemStack stack(IronShulkerBoxCompat.Tier tier,TileEntityDyedIronShulkerBox box)
	{
		ItemStack stack=IronShulkerBoxCompat.stack(tier,box.getDyeIndex());
		stack.setTagInfo("BlockEntityTag",box.saveToNbt(new NBTTagCompound()));
		if(box.hasCustomName())
		{
			stack.setStackDisplayName(box.getName());
		}
		return stack;
	}

	// Drops: like a vanilla shulker box, the box drops itself with its contents when it is broken (breakBlock), even in
	// creative mode if it holds something or has a name. It drops nothing else.
	@Override
	public void onBlockHarvested(World world,BlockPos pos,IBlockState state,EntityPlayer player)
	{
		TileEntity te=world.getTileEntity(pos);
		if(te instanceof TileEntityDyedIronShulkerBox)
		{
			((TileEntityDyedIronShulkerBox)te).setDestroyedByCreativePlayer(player.capabilities.isCreativeMode);
			((TileEntityDyedIronShulkerBox)te).fillWithLoot(player);
		}
	}
	@Override
	public void breakBlock(World world,BlockPos pos,IBlockState state)
	{
		TileEntity te=world.getTileEntity(pos);
		if(te instanceof TileEntityDyedIronShulkerBox)
		{
			TileEntityDyedIronShulkerBox box=(TileEntityDyedIronShulkerBox)te;
			if(!box.isCleared()&&!box.beenUpgraded()&&(!box.isDestroyedByCreativePlayer()||!box.isEmpty()||box.hasCustomName()))
			{
				spawnAsEntity(world,pos,stack(this.tier,box));
			}
			box.clear();
			world.updateComparatorOutputLevel(pos,this);
		}
		super.breakBlock(world,pos,state);
	}
	@Override
	public void getDrops(NonNullList<ItemStack> drops,IBlockAccess world,BlockPos pos,IBlockState state,int fortune)
	{
	}
	@Override
	public ItemStack getPickBlock(IBlockState state,RayTraceResult target,World world,BlockPos pos,EntityPlayer player)
	{
		TileEntity te=world.getTileEntity(pos);
		return te instanceof TileEntityDyedIronShulkerBox?stack(this.tier,(TileEntityDyedIronShulkerBox)te):IronShulkerBoxCompat.stack(this.tier,0);
	}
	@Override
	public void getSubBlocks(CreativeTabs tab,NonNullList<ItemStack> items)
	{
		for(int color=0;color<ColorStrings.ALL.length;color++)
		{
			items.add(IronShulkerBoxCompat.stack(this.tier,color));
		}
	}

	@Override
	public float getExplosionResistance(World world,BlockPos pos,@Nullable Entity exploder,Explosion explosion)
	{
		return this.tier.type.isExplosionResistant()?10000.0F:super.getExplosionResistance(world,pos,exploder,explosion);
	}
	@Override
	@SuppressWarnings("deprecation")
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}
	@Override
	@SuppressWarnings("deprecation")
	public int getComparatorInputOverride(IBlockState state,World world,BlockPos pos)
	{
		return Container.calcRedstone(world.getTileEntity(pos));
	}
}
