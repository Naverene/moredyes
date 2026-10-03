package net.neverandy.moredyes.compat.ironchest;

import javax.annotation.Nullable;

import cpw.mods.ironchest.IronChest;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
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
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

/**
 * An Iron Chests chest of one tier in any More Dyes color. It behaves like Iron Chests' own chest of that tier (see
 * BlockIronChest), but Iron Chests keeps the tier in the block's metadata and this block has one tier, so the
 * metadata is unused and the color is kept by the tile entity.
 */
public class BlockDyedIronChest extends Block
{
	private static final AxisAlignedBB AABB=new AxisAlignedBB(0.0625D,0.0D,0.0625D,0.9375D,0.875D,0.9375D);
	private static final EnumFacing[] ROTATION_AXES={EnumFacing.UP,EnumFacing.DOWN};

	private final IronChestCompat.Tier tier;

	public BlockDyedIronChest(IronChestCompat.Tier tier)
	{
		super(Material.IRON);
		this.tier=tier;
		String name="dyed_"+tier.id()+"_chest";
		this.setRegistryName(Reference.MOD_ID,name);
		this.setUnlocalizedName(Reference.MOD_ID+"."+name);
		// The same as Iron Chests' chests.
		this.setHardness(3.0F);
		this.setSoundType(SoundType.METAL);
		this.setCreativeTab(MoreDyes.tabBlocks);
	}
	public IronChestCompat.Tier getTier()
	{
		return this.tier;
	}

	/** The color of the chest at a position, by its position in ColorStrings.ALL. */
	public static int getColor(IBlockAccess world,BlockPos pos)
	{
		TileEntity te=world.getTileEntity(pos);
		return te instanceof TileEntityDyedIronChest?((TileEntityDyedIronChest)te).getColor():0;
	}

	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos)
	{
		return AABB;
	}
	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state)
	{
		return false;
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

	/** Opens Iron Chests' own screen for the tier, through its GUI handler. */
	@Override
	public boolean onBlockActivated(World world,BlockPos pos,IBlockState state,EntityPlayer player,EnumHand hand,EnumFacing side,float hitX,float hitY,float hitZ)
	{
		TileEntity te=world.getTileEntity(pos);
		if(!(te instanceof TileEntityDyedIronChest)||world.getBlockState(pos.up()).doesSideBlockChestOpening(world,pos.up(),EnumFacing.DOWN))
		{
			return true;
		}
		if(!world.isRemote)
		{
			player.openGui(IronChest.instance,this.tier.type.ordinal(),world,pos.getX(),pos.getY(),pos.getZ());
		}
		return true;
	}
	@Override
	public void onBlockAdded(World world,BlockPos pos,IBlockState state)
	{
		super.onBlockAdded(world,pos,state);
		world.notifyBlockUpdate(pos,state,state,3);
	}
	/** Takes the color from the item's damage value. */
	@Override
	public void onBlockPlacedBy(World world,BlockPos pos,IBlockState state,EntityLivingBase placer,ItemStack stack)
	{
		TileEntity te=world.getTileEntity(pos);
		if(te instanceof TileEntityDyedIronChest)
		{
			TileEntityDyedIronChest chest=(TileEntityDyedIronChest)te;
			chest.wasPlaced(placer,stack);
			chest.setFacing(placer.getHorizontalFacing().getOpposite());
			chest.setColor(stack.getMetadata());
			if(stack.hasDisplayName())
			{
				chest.setCustomName(stack.getDisplayName());
			}
			chest.markDirty();
			world.notifyBlockUpdate(pos,state,state,3);
		}
	}
	@Override
	public void breakBlock(World world,BlockPos pos,IBlockState state)
	{
		TileEntity te=world.getTileEntity(pos);
		if(te instanceof TileEntityDyedIronChest)
		{
			((TileEntityDyedIronChest)te).removeAdornments();
			InventoryHelper.dropInventoryItems(world,pos,(TileEntityDyedIronChest)te);
			world.updateComparatorOutputLevel(pos,this);
		}
		super.breakBlock(world,pos,state);
	}
	@Override
	@SuppressWarnings("deprecation")
	public boolean eventReceived(IBlockState state,World world,BlockPos pos,int id,int param)
	{
		super.eventReceived(state,world,pos,id,param);
		TileEntity te=world.getTileEntity(pos);
		return te!=null&&te.receiveClientEvent(id,param);
	}

	// Drops: the chest in its color. Breaking it keeps the block (and the tile entity holding the color) until the
	// drop is made, as Forge does for flower pots.
	@Override
	public boolean removedByPlayer(IBlockState state,World world,BlockPos pos,EntityPlayer player,boolean willHarvest)
	{
		return willHarvest||super.removedByPlayer(state,world,pos,player,false);
	}
	@Override
	public void harvestBlock(World world,EntityPlayer player,BlockPos pos,IBlockState state,@Nullable TileEntity te,ItemStack tool)
	{
		super.harvestBlock(world,player,pos,state,te,tool);
		world.setBlockToAir(pos);
	}
	@Override
	public void getDrops(NonNullList<ItemStack> drops,IBlockAccess world,BlockPos pos,IBlockState state,int fortune)
	{
		ItemStack drop=IronChestCompat.stack(this.tier,getColor(world,pos));
		TileEntity te=world.getTileEntity(pos);
		if(te instanceof TileEntityDyedIronChest&&((TileEntityDyedIronChest)te).hasCustomName())
		{
			drop.setStackDisplayName(((TileEntityDyedIronChest)te).getName());
		}
		drops.add(drop);
	}
	@Override
	public ItemStack getPickBlock(IBlockState state,RayTraceResult target,World world,BlockPos pos,EntityPlayer player)
	{
		return IronChestCompat.stack(this.tier,getColor(world,pos));
	}
	@Override
	public void getSubBlocks(CreativeTabs tab,NonNullList<ItemStack> items)
	{
		for(int color=0;color<ColorStrings.ALL.length;color++)
		{
			items.add(IronChestCompat.stack(this.tier,color));
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
	@Override
	public EnumFacing[] getValidRotations(World world,BlockPos pos)
	{
		return ROTATION_AXES;
	}
	@Override
	public boolean rotateBlock(World world,BlockPos pos,EnumFacing axis)
	{
		if(world.isRemote||axis.getAxis()!=EnumFacing.Axis.Y)
		{
			return false;
		}
		TileEntity te=world.getTileEntity(pos);
		if(te instanceof TileEntityDyedIronChest)
		{
			((TileEntityDyedIronChest)te).rotateAround();
		}
		return true;
	}
}
