package net.neverandy.moredyes.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityColor;

/**
 * A flower pot holding a dyed flower or sapling. The vanilla pot only draws the plants it knows, so using a dyed plant
 * on an empty vanilla pot turns it into this block (see handler/FlowerPotHandler). The metadata is the plant and the
 * color is kept in a tile entity (see TileColor), so one block holds every plant in every color. Using the pot takes
 * the plant out again and leaves an empty vanilla pot. It has no item of its own.
 */
public class BlockDyedFlowerPot extends Block implements ITileColoredBlock
{
	/** The plants a pot can hold, each a dyed block with a block per color group (see MDBlock.pottable). */
	public enum Plant implements IStringSerializable
	{
		TULIP,CORNFLOWER,ALLIUM,AZURE_BLUET,BLUE_ORCHID,DANDELION,LILY_OF_THE_VALLEY,OXEYE_DAISY,POPPY,SAPLING;

		@Override
		public String getName()
		{
			return this.name().toLowerCase();
		}
		/** The plant a dyed block is, or null if it is not one that fits in a pot. */
		@Nullable
		public static Plant of(Block block)
		{
			for(Plant plant:values())
			{
				for(Block colored:MDBlock.pottable(plant))
				{
					if(colored==block)
					{
						return plant;
					}
				}
			}
			return null;
		}
	}

	public static final PropertyEnum<Plant> PLANT=PropertyEnum.create("plant",Plant.class);
	private static final AxisAlignedBB FLOWER_POT_AABB=new AxisAlignedBB(0.3125,0.0,0.3125,0.6875,0.375,0.6875);

	public BlockDyedFlowerPot()
	{
		super(Material.CIRCUITS);
		this.setHardness(0.0f);
		this.setSoundType(SoundType.STONE);
		this.setRegistryName(Reference.MOD_ID,"flower_pot");
		this.setUnlocalizedName(Reference.MOD_ID+".flower_pot");
		this.setDefaultState(this.blockState.getBaseState().withProperty(PLANT,Plant.TULIP));
	}
	@Override
	public String getTypeName()
	{
		return "flower_pot";
	}
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this,PLANT);
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		Plant[] plants=Plant.values();
		return this.getDefaultState().withProperty(PLANT,plants[meta<plants.length?meta:0]);
	}
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(PLANT).ordinal();
	}
	/** The plant in the pot at this position, in its color. */
	private static ItemStack plantStack(IBlockAccess world,BlockPos pos,IBlockState state)
	{
		int color=TileColor.get(world,pos);
		Block plant=MDBlock.pottable(state.getValue(PLANT))[ColorStrings.groupOf(color)];
		return new ItemStack(plant,1,ColorStrings.shadeOf(color));
	}
	/** Puts a dyed plant in the vanilla flower pot at this position, if it is one that fits. */
	public static boolean pot(World world,BlockPos pos,Block plantBlock,int meta)
	{
		Plant plant=Plant.of(plantBlock);
		if(plant==null)
		{
			return false;
		}
		world.setBlockState(pos,MDBlock.flowerPot.getDefaultState().withProperty(PLANT,plant),3);
		TileColor.set(world,pos,((IColoredBlock)plantBlock).getColorIndex(meta));
		return true;
	}
	/** Takes the plant out, like the vanilla pot does. */
	@Override
	public boolean onBlockActivated(World world,BlockPos pos,IBlockState state,EntityPlayer player,EnumHand hand,EnumFacing facing,float hitX,float hitY,float hitZ)
	{
		if(!world.isRemote)
		{
			ItemStack plant=plantStack(world,pos,state);
			ItemStack held=player.getHeldItem(hand);
			if(held.isEmpty())
			{
				player.setHeldItem(hand,plant);
			}
			else if(!player.addItemStackToInventory(plant))
			{
				player.dropItem(plant,false);
			}
			world.setBlockState(pos,Blocks.FLOWER_POT.getDefaultState(),3);
		}
		return true;
	}
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess source,BlockPos pos)
	{
		return FLOWER_POT_AABB;
	}
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world,IBlockState state,BlockPos pos,EnumFacing face)
	{
		return BlockFaceShape.UNDEFINED;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	/** Falls off like the vanilla pot when the block under it goes. */
	@Override
	public void neighborChanged(IBlockState state,World world,BlockPos pos,Block block,BlockPos fromPos)
	{
		IBlockState below=world.getBlockState(pos.down());
		if(!below.isTopSolid()&&below.getBlockFaceShape(world,pos.down(),EnumFacing.UP)!=BlockFaceShape.SOLID)
		{
			this.dropBlockAsItem(world,pos,state,0);
			world.setBlockToAir(pos);
		}
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
	/** The pot and the plant. */
	@Override
	public void getDrops(NonNullList<ItemStack> drops,IBlockAccess world,BlockPos pos,IBlockState state,int fortune)
	{
		drops.add(new ItemStack(Items.FLOWER_POT));
		drops.add(plantStack(world,pos,state));
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
	/** Picking the pot gives the plant, as for the vanilla pot. */
	@Override
	public ItemStack getItem(World world,BlockPos pos,IBlockState state)
	{
		return plantStack(world,pos,state);
	}
}
