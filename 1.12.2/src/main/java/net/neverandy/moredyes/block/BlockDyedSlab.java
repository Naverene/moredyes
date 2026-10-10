package net.neverandy.moredyes.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityColor;
import net.neverandy.moredyes.utility.BlockInfo;

/**
 * Dyed slabs in every color, made from a dyed block. The color is kept in a tile entity (see TileColor), so one
 * block holds the bottom, top and double slab, which only join when both halves have the same color (see
 * ItemBlockDyedSlab).
 */
public class BlockDyedSlab extends Block implements ITileColoredBlock
{
	public enum Type implements IStringSerializable
	{
		BOTTOM,TOP,DOUBLE;

		@Override
		public String getName()
		{
			return this.name().toLowerCase();
		}
	}

	public static final PropertyEnum<Type> TYPE=PropertyEnum.create("type",Type.class);
	private static final AxisAlignedBB BOTTOM_AABB=new AxisAlignedBB(0.0,0.0,0.0,1.0,0.5,1.0);
	private static final AxisAlignedBB TOP_AABB=new AxisAlignedBB(0.0,0.5,0.0,1.0,1.0,1.0);

	private final String typeName;

	public BlockDyedSlab(BlockInfo info,String name)
	{
		super(info.blockMaterial);
		this.typeName=name;
		this.setHardness(info.hardness);
		if(info.resistance>=0.0f)
		{
			this.setResistance(info.resistance);
		}
		if(info.harvestTool!=null)
		{
			this.setHarvestLevel(info.harvestTool,info.harvestLevel);
		}
		this.setSoundType(info.sound);
		this.setRegistryName(Reference.MOD_ID,name);
		this.setUnlocalizedName(Reference.MOD_ID+"."+name);
		this.setCreativeTab(MoreDyes.tabShapes);
		this.setLightOpacity(255);
		this.useNeighborBrightness=true;
		this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE,Type.BOTTOM));
	}
	@Override
	public String getTypeName()
	{
		return this.typeName;
	}
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this,TYPE);
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		Type[] types=Type.values();
		return this.getDefaultState().withProperty(TYPE,types[meta<types.length?meta:0]);
	}
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(TYPE).ordinal();
	}
	/** The top half when placed against the top of a block or the upper half of its side, like vanilla slabs. */
	@Override
	public IBlockState getStateForPlacement(World world,BlockPos pos,EnumFacing facing,float hitX,float hitY,float hitZ,int meta,EntityLivingBase placer)
	{
		boolean bottom=facing!=EnumFacing.DOWN&&(facing==EnumFacing.UP||hitY<=0.5f);
		return this.getDefaultState().withProperty(TYPE,bottom?Type.BOTTOM:Type.TOP);
	}
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos)
	{
		switch(state.getValue(TYPE))
		{
		case BOTTOM:
			return BOTTOM_AABB;
		case TOP:
			return TOP_AABB;
		default:
			return FULL_BLOCK_AABB;
		}
	}
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return state.getValue(TYPE)==Type.DOUBLE;
	}
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return state.getValue(TYPE)==Type.DOUBLE;
	}
	@Override
	public boolean isTopSolid(IBlockState state)
	{
		return state.getValue(TYPE)!=Type.BOTTOM;
	}
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world,IBlockState state,BlockPos pos,EnumFacing face)
	{
		Type type=state.getValue(TYPE);
		if(type==Type.DOUBLE||face==EnumFacing.UP&&type==Type.TOP||face==EnumFacing.DOWN&&type==Type.BOTTOM)
		{
			return BlockFaceShape.SOLID;
		}
		return BlockFaceShape.UNDEFINED;
	}
	@Override
	public boolean doesSideBlockRendering(IBlockState state,IBlockAccess world,BlockPos pos,EnumFacing face)
	{
		Type type=state.getValue(TYPE);
		return type==Type.DOUBLE||face==EnumFacing.UP&&type==Type.TOP||face==EnumFacing.DOWN&&type==Type.BOTTOM;
	}
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	@Override
	public TileEntity createTileEntity(World world,IBlockState state)
	{
		return new TileEntityColor();
	}
	/** A double slab drops two slabs. */
	@Override
	public void getDrops(NonNullList<ItemStack> drops,IBlockAccess world,BlockPos pos,IBlockState state,int fortune)
	{
		drops.add(TileColor.stack(this,world,pos,state.getValue(TYPE)==Type.DOUBLE?2:1));
	}
	@Override
	public boolean removedByPlayer(IBlockState state,World world,BlockPos pos,EntityPlayer player,boolean willHarvest)
	{
		if(willHarvest)
		{
			this.onBlockHarvested(world,pos,state,player);
			return true;
		}
		return super.removedByPlayer(state,world,pos,player,false);
	}
	@Override
	public void harvestBlock(World world,EntityPlayer player,BlockPos pos,IBlockState state,@Nullable TileEntity te,ItemStack stack)
	{
		super.harvestBlock(world,player,pos,state,te,stack);
		TileColor.harvested(world,pos);
	}
	@Override
	public ItemStack getItem(World world,BlockPos pos,IBlockState state)
	{
		return TileColor.stack(this,world,pos,1);
	}
	@Override
	public void getSubBlocks(CreativeTabs tab,NonNullList<ItemStack> items)
	{
		TileColor.getSubBlocks(this,items);
	}
}
