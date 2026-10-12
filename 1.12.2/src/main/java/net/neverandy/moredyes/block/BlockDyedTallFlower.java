package net.neverandy.moredyes.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityColor;
import net.neverandy.moredyes.utility.BlockInfo;

/**
 * A dyed two-block-tall flower (lilac, peony, rose bush) in every color. A shade and the half it is would not both fit
 * in the metadata, so the color is kept in a tile entity in each half (see TileColor). Only the lower half drops the
 * flower: breaking either half breaks the other.
 */
public class BlockDyedTallFlower extends BlockBush implements ITileColoredBlock
{
	private final String typeName;

	public BlockDyedTallFlower(BlockInfo info)
	{
		super(info.blockMaterial);
		this.typeName=info.blockName;
		this.setHardness(info.hardness);
		this.setSoundType(info.sound);
		this.setRegistryName(Reference.MOD_ID,info.blockName);
		this.setUnlocalizedName(Reference.MOD_ID+"."+info.blockName);
		this.setCreativeTab(info.tab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDoublePlant.HALF,BlockDoublePlant.EnumBlockHalf.LOWER));
	}
	@Override
	public String getTypeName()
	{
		return this.typeName;
	}
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this,BlockDoublePlant.HALF);
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(BlockDoublePlant.HALF,meta==1?BlockDoublePlant.EnumBlockHalf.UPPER:BlockDoublePlant.EnumBlockHalf.LOWER);
	}
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return isUpper(state)?1:0;
	}
	private static boolean isUpper(IBlockState state)
	{
		return state.getValue(BlockDoublePlant.HALF)==BlockDoublePlant.EnumBlockHalf.UPPER;
	}
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos)
	{
		return FULL_BLOCK_AABB.offset(state.getOffset(source,pos));
	}
	/** Drawn slightly off the middle of the block, like vanilla flowers; both halves have the same offset. */
	@Override
	public Block.EnumOffsetType getOffsetType()
	{
		return Block.EnumOffsetType.XZ;
	}
	/** Needs room for the upper half. */
	@Override
	public boolean canPlaceBlockAt(World world,BlockPos pos)
	{
		return super.canPlaceBlockAt(world,pos)&&world.isAirBlock(pos.up());
	}
	/** The lower half needs soil and the upper half; the upper half needs the lower one. */
	@Override
	public boolean canBlockStay(World world,BlockPos pos,IBlockState state)
	{
		if(state.getBlock()!=this)
		{
			return super.canBlockStay(world,pos,state);
		}
		if(isUpper(state))
		{
			IBlockState below=world.getBlockState(pos.down());
			return below.getBlock()==this&&!isUpper(below);
		}
		IBlockState above=world.getBlockState(pos.up());
		return above.getBlock()==this&&isUpper(above)&&super.canBlockStay(world,pos,state);
	}
	/** Runs after the item gave the lower half its color, so the upper half gets the same one. */
	@Override
	public void onBlockPlacedBy(World world,BlockPos pos,IBlockState state,EntityLivingBase placer,ItemStack stack)
	{
		BlockPos up=pos.up();
		world.setBlockState(up,this.getDefaultState().withProperty(BlockDoublePlant.HALF,BlockDoublePlant.EnumBlockHalf.UPPER),2);
		TileColor.set(world,up,TileColor.get(world,pos));
	}
	/** In creative mode, breaking the upper half removes the lower one without dropping it. */
	@Override
	public void onBlockHarvested(World world,BlockPos pos,IBlockState state,EntityPlayer player)
	{
		if(isUpper(state)&&player.capabilities.isCreativeMode&&world.getBlockState(pos.down()).getBlock()==this)
		{
			world.setBlockToAir(pos.down());
		}
		super.onBlockHarvested(world,pos,state,player);
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
		if(!isUpper(state))
		{
			drops.add(TileColor.stack(this,world,pos,1));
		}
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
