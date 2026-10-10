package net.neverandy.moredyes.block;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityColor;
import net.neverandy.moredyes.utility.BlockInfo;

/**
 * Dyed walls in every color, made from a dyed block. The color is kept in a tile entity (see TileColor). They work
 * like the vanilla cobblestone wall, without its mossy variant, and join vanilla walls and fence gates.
 */
public class BlockDyedWall extends Block implements ITileColoredBlock
{
	// The vanilla wall's boxes: a post, and an arm towards each joined side (index: one bit per horizontal side).
	private static final AxisAlignedBB[] AABB_BY_INDEX=new AxisAlignedBB[]{new AxisAlignedBB(0.25,0.0,0.25,0.75,1.0,0.75),
			new AxisAlignedBB(0.25,0.0,0.25,0.75,1.0,1.0),new AxisAlignedBB(0.0,0.0,0.25,0.75,1.0,0.75),
			new AxisAlignedBB(0.0,0.0,0.25,0.75,1.0,1.0),new AxisAlignedBB(0.25,0.0,0.0,0.75,1.0,0.75),
			new AxisAlignedBB(0.3125,0.0,0.0,0.6875,0.875,1.0),new AxisAlignedBB(0.0,0.0,0.0,0.75,1.0,0.75),
			new AxisAlignedBB(0.0,0.0,0.0,0.75,1.0,1.0),new AxisAlignedBB(0.25,0.0,0.25,1.0,1.0,0.75),
			new AxisAlignedBB(0.25,0.0,0.25,1.0,1.0,1.0),new AxisAlignedBB(0.0,0.0,0.3125,1.0,0.875,0.6875),
			new AxisAlignedBB(0.0,0.0,0.25,1.0,1.0,1.0),new AxisAlignedBB(0.25,0.0,0.0,1.0,1.0,0.75),
			new AxisAlignedBB(0.25,0.0,0.0,1.0,1.0,1.0),new AxisAlignedBB(0.0,0.0,0.0,1.0,1.0,0.75),
			new AxisAlignedBB(0.0,0.0,0.0,1.0,1.0,1.0)};
	private static final AxisAlignedBB[] CLIP_AABB_BY_INDEX=new AxisAlignedBB[AABB_BY_INDEX.length];

	static
	{
		for(int i=0;i<AABB_BY_INDEX.length;i++)
		{
			CLIP_AABB_BY_INDEX[i]=AABB_BY_INDEX[i].setMaxY(1.5);
		}
	}

	private final String typeName;

	public BlockDyedWall(BlockInfo info,String name)
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
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockWall.UP,false).withProperty(BlockWall.NORTH,false)
				.withProperty(BlockWall.EAST,false).withProperty(BlockWall.SOUTH,false).withProperty(BlockWall.WEST,false));
	}
	@Override
	public String getTypeName()
	{
		return this.typeName;
	}
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this,BlockWall.UP,BlockWall.NORTH,BlockWall.EAST,BlockWall.WEST,BlockWall.SOUTH);
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState();
	}
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	@Override
	public IBlockState getActualState(IBlockState state,IBlockAccess world,BlockPos pos)
	{
		boolean north=this.canWallConnectTo(world,pos,EnumFacing.NORTH);
		boolean east=this.canWallConnectTo(world,pos,EnumFacing.EAST);
		boolean south=this.canWallConnectTo(world,pos,EnumFacing.SOUTH);
		boolean west=this.canWallConnectTo(world,pos,EnumFacing.WEST);
		// A straight run has no post, unless something is on top.
		boolean straight=north&&!east&&south&&!west||!north&&east&&!south&&west;
		return state.withProperty(BlockWall.UP,!straight||!world.isAirBlock(pos.up())).withProperty(BlockWall.NORTH,north)
				.withProperty(BlockWall.EAST,east).withProperty(BlockWall.SOUTH,south).withProperty(BlockWall.WEST,west);
	}
	private static int getAABBIndex(IBlockState state)
	{
		int i=0;
		if(state.getValue(BlockWall.NORTH))
		{
			i|=1<<EnumFacing.NORTH.getHorizontalIndex();
		}
		if(state.getValue(BlockWall.EAST))
		{
			i|=1<<EnumFacing.EAST.getHorizontalIndex();
		}
		if(state.getValue(BlockWall.SOUTH))
		{
			i|=1<<EnumFacing.SOUTH.getHorizontalIndex();
		}
		if(state.getValue(BlockWall.WEST))
		{
			i|=1<<EnumFacing.WEST.getHorizontalIndex();
		}
		return i;
	}
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos)
	{
		return AABB_BY_INDEX[getAABBIndex(this.getActualState(state,source,pos))];
	}
	@Override
	public void addCollisionBoxToList(IBlockState state,World world,BlockPos pos,AxisAlignedBB entityBox,List<AxisAlignedBB> collidingBoxes,@Nullable Entity entity,boolean isActualState)
	{
		if(!isActualState)
		{
			state=this.getActualState(state,world,pos);
		}
		addCollisionBoxToList(pos,entityBox,collidingBoxes,CLIP_AABB_BY_INDEX[getAABBIndex(state)]);
	}
	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state,IBlockAccess world,BlockPos pos)
	{
		return CLIP_AABB_BY_INDEX[getAABBIndex(this.getActualState(state,world,pos))];
	}
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	@Override
	public boolean isPassable(IBlockAccess world,BlockPos pos)
	{
		return false;
	}
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state,IBlockAccess world,BlockPos pos,EnumFacing side)
	{
		return side!=EnumFacing.DOWN||super.shouldSideBeRendered(state,world,pos,side);
	}
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world,IBlockState state,BlockPos pos,EnumFacing face)
	{
		return face!=EnumFacing.UP&&face!=EnumFacing.DOWN?BlockFaceShape.MIDDLE_POLE_THICK:BlockFaceShape.CENTER_BIG;
	}
	/** Joins walls (vanilla ones too), fence gates and solid sides, like the vanilla wall. */
	private boolean canConnectTo(IBlockAccess world,BlockPos pos,EnumFacing face)
	{
		IBlockState state=world.getBlockState(pos);
		Block block=state.getBlock();
		BlockFaceShape shape=state.getBlockFaceShape(world,pos,face);
		boolean wallOrGate=shape==BlockFaceShape.MIDDLE_POLE_THICK||shape==BlockFaceShape.MIDDLE_POLE&&block instanceof BlockFenceGate;
		boolean excepted=isExceptBlockForAttachWithPiston(block)||block==Blocks.BARRIER||block==Blocks.MELON_BLOCK
				||block==Blocks.PUMPKIN||block==Blocks.LIT_PUMPKIN;
		return !excepted&&shape==BlockFaceShape.SOLID||wallOrGate;
	}
	@Override
	public boolean canBeConnectedTo(IBlockAccess world,BlockPos pos,EnumFacing facing)
	{
		return this.canConnectTo(world,pos.offset(facing),facing.getOpposite());
	}
	private boolean canWallConnectTo(IBlockAccess world,BlockPos pos,EnumFacing facing)
	{
		BlockPos other=pos.offset(facing);
		Block block=world.getBlockState(other).getBlock();
		return block.canBeConnectedTo(world,other,facing.getOpposite())||this.canConnectTo(world,other,facing.getOpposite());
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
	@Override
	public void getDrops(NonNullList<ItemStack> drops,IBlockAccess world,BlockPos pos,IBlockState state,int fortune)
	{
		drops.add(TileColor.stack(this,world,pos,1));
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
