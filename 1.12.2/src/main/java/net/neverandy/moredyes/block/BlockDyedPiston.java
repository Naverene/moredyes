package net.neverandy.moredyes.block;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.BlockPistonStructureHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityColor;
import net.neverandy.moredyes.tileentity.TileEntityDyedPiston;

/**
 * Dyed piston or sticky piston in every color, as in 1.7.10. The metadata holds the direction and whether it is
 * extended, so the color is kept in a tile entity (see TileColor). Only its cobblestone takes the dye: the model draws
 * the wood, iron and cracks over it untinted.
 * <p>
 * It works like the vanilla piston. The parts of vanilla that place the piston head and move the piston are copied
 * here, because they are private and name the vanilla blocks: this piston places BlockDyedPistonHead as its head,
 * and retracts in a TileEntityDyedPiston so it keeps its color. Other pistons cannot push it, since blocks with a
 * tile entity cannot be moved.
 */
public class BlockDyedPiston extends BlockPistonBase implements ITileColoredBlock
{
	private final boolean sticky;
	private final String typeName;

	public BlockDyedPiston(boolean sticky,String name)
	{
		super(sticky);
		this.sticky=sticky;
		this.typeName=name;
		this.setRegistryName(Reference.MOD_ID,name);
		this.setUnlocalizedName(Reference.MOD_ID+"."+name);
		this.setCreativeTab(MoreDyes.tabShapes);
	}
	public boolean isSticky()
	{
		return this.sticky;
	}
	@Override
	public String getTypeName()
	{
		return this.typeName;
	}

	@Override
	public void onBlockPlacedBy(World world,BlockPos pos,IBlockState state,EntityLivingBase placer,ItemStack stack)
	{
		world.setBlockState(pos,state.withProperty(FACING,EnumFacing.getDirectionFromEntityLiving(pos,placer)),2);
		if(!world.isRemote)
		{
			this.checkForMove(world,pos,world.getBlockState(pos));
		}
	}
	@Override
	public void neighborChanged(IBlockState state,World world,BlockPos pos,Block block,BlockPos fromPos)
	{
		if(!world.isRemote)
		{
			this.checkForMove(world,pos,state);
		}
	}
	@Override
	public void onBlockAdded(World world,BlockPos pos,IBlockState state)
	{
		if(!world.isRemote&&!(world.getTileEntity(pos) instanceof TileEntityPiston))
		{
			this.checkForMove(world,pos,state);
		}
	}

	private void checkForMove(World world,BlockPos pos,IBlockState state)
	{
		EnumFacing facing=state.getValue(FACING);
		boolean powered=this.shouldBeExtended(world,pos,facing);
		if(powered&&!state.getValue(EXTENDED))
		{
			if(new BlockPistonStructureHelper(world,pos,facing,true).canMove())
			{
				world.addBlockEvent(pos,this,0,facing.getIndex());
			}
		}
		else if(!powered&&state.getValue(EXTENDED))
		{
			world.addBlockEvent(pos,this,1,facing.getIndex());
		}
	}
	private boolean shouldBeExtended(World world,BlockPos pos,EnumFacing facing)
	{
		for(EnumFacing side:EnumFacing.values())
		{
			if(side!=facing&&world.isSidePowered(pos.offset(side),side))
			{
				return true;
			}
		}
		if(world.isSidePowered(pos,EnumFacing.DOWN))
		{
			return true;
		}
		BlockPos above=pos.up();
		for(EnumFacing side:EnumFacing.values())
		{
			if(side!=EnumFacing.DOWN&&world.isSidePowered(above.offset(side),side))
			{
				return true;
			}
		}
		return false;
	}

	/** Event 0 extends the piston, 1 retracts it; the parameter is its facing. As vanilla, with the dyed head. */
	@Override
	public boolean eventReceived(IBlockState state,World world,BlockPos pos,int id,int param)
	{
		EnumFacing facing=state.getValue(FACING);
		if(!world.isRemote)
		{
			boolean powered=this.shouldBeExtended(world,pos,facing);
			if(powered&&id==1)
			{
				world.setBlockState(pos,state.withProperty(EXTENDED,true),2);
				return false;
			}
			if(!powered&&id==0)
			{
				return false;
			}
		}
		if(id==0)
		{
			if(!this.doMove(world,pos,facing,true))
			{
				return false;
			}
			world.setBlockState(pos,state.withProperty(EXTENDED,true),3);
			world.playSound(null,pos,SoundEvents.BLOCK_PISTON_EXTEND,SoundCategory.BLOCKS,0.5f,world.rand.nextFloat()*0.25f+0.6f);
		}
		else if(id==1)
		{
			TileEntity head=world.getTileEntity(pos.offset(facing));
			if(head instanceof TileEntityPiston)
			{
				((TileEntityPiston)head).clearPistonTileEntity();
			}
			// The piston itself becomes a moving block while its head slides in; that keeps its color.
			int color=TileColor.get(world,pos);
			world.setBlockState(pos,Blocks.PISTON_EXTENSION.getDefaultState().withProperty(BlockPistonMoving.FACING,facing)
					.withProperty(BlockPistonMoving.TYPE,this.headType()),3);
			world.setTileEntity(pos,new TileEntityDyedPiston(this.getStateFromMeta(param),facing,false,true,color));
			if(this.sticky)
			{
				BlockPos pulled=pos.add(facing.getFrontOffsetX()*2,facing.getFrontOffsetY()*2,facing.getFrontOffsetZ()*2);
				IBlockState pulledState=world.getBlockState(pulled);
				Block pulledBlock=pulledState.getBlock();
				boolean stopped=false;
				if(pulledBlock==Blocks.PISTON_EXTENSION)
				{
					TileEntity tile=world.getTileEntity(pulled);
					if(tile instanceof TileEntityPiston)
					{
						TileEntityPiston moving=(TileEntityPiston)tile;
						if(moving.getFacing()==facing&&moving.isExtending())
						{
							moving.clearPistonTileEntity();
							stopped=true;
						}
					}
				}
				if(!stopped&&!pulledBlock.isAir(pulledState,world,pulled)
						&&canPush(pulledState,world,pulled,facing.getOpposite(),false,facing)
						&&(pulledState.getMobilityFlag()==EnumPushReaction.NORMAL||pulledBlock==Blocks.PISTON||pulledBlock==Blocks.STICKY_PISTON))
				{
					this.doMove(world,pos,facing,false);
				}
			}
			else
			{
				world.setBlockToAir(pos.offset(facing));
			}
			world.playSound(null,pos,SoundEvents.BLOCK_PISTON_CONTRACT,SoundCategory.BLOCKS,0.5f,world.rand.nextFloat()*0.15f+0.6f);
		}
		return true;
	}
	private BlockPistonExtension.EnumPistonType headType()
	{
		return this.sticky?BlockPistonExtension.EnumPistonType.STICKY:BlockPistonExtension.EnumPistonType.DEFAULT;
	}
	/** Moves the blocks in front, as vanilla; when extending, the dyed head slides out after them. */
	private boolean doMove(World world,BlockPos pos,EnumFacing direction,boolean extending)
	{
		if(!extending)
		{
			world.setBlockToAir(pos.offset(direction));
		}
		BlockPistonStructureHelper helper=new BlockPistonStructureHelper(world,pos,direction,extending);
		if(!helper.canMove())
		{
			return false;
		}
		List<BlockPos> toMove=helper.getBlocksToMove();
		List<IBlockState> moving=new ArrayList<IBlockState>();
		for(BlockPos moved:toMove)
		{
			moving.add(world.getBlockState(moved).getActualState(world,moved));
		}
		List<BlockPos> toDestroy=helper.getBlocksToDestroy();
		int k=toMove.size()+toDestroy.size();
		IBlockState[] changed=new IBlockState[k];
		EnumFacing towards=extending?direction:direction.getOpposite();
		for(int j=toDestroy.size()-1;j>=0;--j)
		{
			BlockPos destroyed=toDestroy.get(j);
			IBlockState destroyedState=world.getBlockState(destroyed);
			float chance=destroyedState.getBlock() instanceof BlockSnow?-1.0f:1.0f;
			destroyedState.getBlock().dropBlockAsItemWithChance(world,destroyed,destroyedState,chance,0);
			world.setBlockState(destroyed,Blocks.AIR.getDefaultState(),4);
			changed[--k]=destroyedState;
		}
		for(int l=toMove.size()-1;l>=0;--l)
		{
			BlockPos from=toMove.get(l);
			IBlockState fromState=world.getBlockState(from);
			world.setBlockState(from,Blocks.AIR.getDefaultState(),2);
			BlockPos to=from.offset(towards);
			world.setBlockState(to,Blocks.PISTON_EXTENSION.getDefaultState().withProperty(FACING,direction),4);
			world.setTileEntity(to,BlockPistonMoving.createTilePiston(moving.get(l),direction,extending,false));
			changed[--k]=fromState;
		}
		BlockPos headPos=pos.offset(direction);
		if(extending)
		{
			IBlockState head=MDBlock.pistonHead.getDefaultState().withProperty(BlockPistonExtension.FACING,direction)
					.withProperty(BlockPistonExtension.TYPE,this.headType());
			world.setBlockState(headPos,Blocks.PISTON_EXTENSION.getDefaultState().withProperty(BlockPistonMoving.FACING,direction)
					.withProperty(BlockPistonMoving.TYPE,this.headType()),4);
			world.setTileEntity(headPos,BlockPistonMoving.createTilePiston(head,direction,true,true));
		}
		for(int i=toDestroy.size()-1;i>=0;--i)
		{
			world.notifyNeighborsOfStateChange(toDestroy.get(i),changed[k++].getBlock(),false);
		}
		for(int j=toMove.size()-1;j>=0;--j)
		{
			world.notifyNeighborsOfStateChange(toMove.get(j),changed[k++].getBlock(),false);
		}
		if(extending)
		{
			world.notifyNeighborsOfStateChange(headPos,MDBlock.pistonHead,false);
		}
		return true;
	}

	/** The wood, iron and cracks are drawn over the dyed cobblestone with see-through gaps. */
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
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
