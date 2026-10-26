package net.neverandy.moredyes.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockBed;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.block.TileColor;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityDyedBed;

/** The dyed bed, in every color; the damage is the color's position in ColorStrings.ALL. It places like the vanilla bed. */
public class ItemDyedBed extends Item
{
	public ItemDyedBed()
	{
		this.setMaxStackSize(1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setRegistryName(Reference.MOD_ID,"bed");
		this.setUnlocalizedName(Reference.MOD_ID+".bed");
		this.setCreativeTab(MoreDyes.tabBlocks);
	}
	/** "ECBF99 Bed", or vanilla's name ("Red Bed") for a bed in a vanilla color. */
	@Override
	@SuppressWarnings("deprecation")
	public String getItemStackDisplayName(ItemStack stack)
	{
		int vanilla=TileEntityDyedBed.vanillaColorOf(stack);
		if(vanilla>=0)
		{
			return I18n.translateToLocal("item.bed."+EnumDyeColor.byMetadata(vanilla).getUnlocalizedName()+".name");
		}
		return ItemBlockColored.coloredName(ColorStrings.ALL[TileColor.clamp(stack.getMetadata())],this.getUnlocalizedName()+".name");
	}
	@Override
	public void getSubItems(CreativeTabs tab,NonNullList<ItemStack> items)
	{
		if(this.isInCreativeTab(tab))
		{
			for(int i=0;i<ColorStrings.ALL.length;i++)
			{
				items.add(new ItemStack(this,1,i));
			}
		}
	}
	/** The vanilla bed's placement, with the dyed bed block. */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player,World world,BlockPos pos,EnumHand hand,EnumFacing facing,float hitX,float hitY,float hitZ)
	{
		if(world.isRemote)
		{
			return EnumActionResult.SUCCESS;
		}
		if(facing!=EnumFacing.UP)
		{
			return EnumActionResult.FAIL;
		}
		boolean replaceable=world.getBlockState(pos).getBlock().isReplaceable(world,pos);
		if(!replaceable)
		{
			pos=pos.up();
		}
		EnumFacing direction=EnumFacing.getHorizontal(MathHelper.floor(player.rotationYaw*4.0f/360.0f+0.5)&3);
		BlockPos head=pos.offset(direction);
		ItemStack stack=player.getHeldItem(hand);
		if(!player.canPlayerEdit(pos,facing,stack)||!player.canPlayerEdit(head,facing,stack))
		{
			return EnumActionResult.FAIL;
		}
		IBlockState headWas=world.getBlockState(head);
		boolean footFree=replaceable||world.isAirBlock(pos);
		boolean headFree=headWas.getBlock().isReplaceable(world,head)||world.isAirBlock(head);
		if(!footFree||!headFree||!world.getBlockState(pos.down()).isTopSolid()||!world.getBlockState(head.down()).isTopSolid())
		{
			return EnumActionResult.FAIL;
		}
		IBlockState footWas=world.getBlockState(pos);
		IBlockState foot=MDBlock.bed.getDefaultState().withProperty(BlockBed.OCCUPIED,false).withProperty(BlockBed.FACING,direction)
				.withProperty(BlockBed.PART,BlockBed.EnumPartType.FOOT);
		world.setBlockState(pos,foot,10);
		world.setBlockState(head,foot.withProperty(BlockBed.PART,BlockBed.EnumPartType.HEAD),10);
		SoundType sound=MDBlock.bed.getSoundType(foot,world,pos,player);
		world.playSound(null,pos,sound.getPlaceSound(),SoundCategory.BLOCKS,(sound.getVolume()+1.0f)/2.0f,sound.getPitch()*0.8f);
		setValues(world,head,stack);
		setValues(world,pos,stack);
		world.notifyNeighborsRespectDebug(pos,footWas.getBlock(),false);
		world.notifyNeighborsRespectDebug(head,headWas.getBlock(),false);
		if(player instanceof EntityPlayerMP)
		{
			CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player,pos,stack);
		}
		stack.shrink(1);
		return EnumActionResult.SUCCESS;
	}
	/** Gives a half of the bed the item's color and wood, and sends them to the players who can see it. */
	private static void setValues(World world,BlockPos pos,ItemStack stack)
	{
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof TileEntityDyedBed)
		{
			((TileEntityDyedBed)tile).setItemValues(stack);
			IBlockState state=world.getBlockState(pos);
			world.notifyBlockUpdate(pos,state,state,3);
		}
	}
}
