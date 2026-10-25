package net.neverandy.moredyes.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockBanner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.block.TileColor;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

/**
 * The dyed banner, in every color; the damage is the color's position in ColorStrings.ALL, and the patterns are kept
 * like the vanilla banner's. It places a standing or a wall banner like the vanilla banner.
 */
public class ItemDyedBanner extends Item
{
	public ItemDyedBanner()
	{
		this.setMaxStackSize(16);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setRegistryName(Reference.MOD_ID,"banner");
		this.setUnlocalizedName(Reference.MOD_ID+".banner");
		this.setCreativeTab(MoreDyes.tabBlocks);
	}
	/** "ECBF99 Banner". */
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return ItemBlockColored.coloredName(ColorStrings.ALL[TileColor.clamp(stack.getMetadata())],this.getUnlocalizedName()+".name");
	}
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack,@Nullable World world,List<String> tooltip,ITooltipFlag flag)
	{
		ItemBanner.appendHoverTextFromTileEntityTag(stack,tooltip);
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
	/** The vanilla banner's placement, with the dyed banner blocks. */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player,World world,BlockPos pos,EnumHand hand,EnumFacing facing,float hitX,float hitY,float hitZ)
	{
		IBlockState clicked=world.getBlockState(pos);
		boolean replaceable=clicked.getBlock().isReplaceable(world,pos);
		if(facing==EnumFacing.DOWN||!clicked.getMaterial().isSolid()&&!replaceable||replaceable&&facing!=EnumFacing.UP)
		{
			return EnumActionResult.FAIL;
		}
		pos=pos.offset(facing);
		ItemStack stack=player.getHeldItem(hand);
		if(!player.canPlayerEdit(pos,facing,stack)||!MDBlock.standingBanner.canPlaceBlockAt(world,pos))
		{
			return EnumActionResult.FAIL;
		}
		if(world.isRemote)
		{
			return EnumActionResult.SUCCESS;
		}
		pos=replaceable?pos.down():pos;
		if(facing==EnumFacing.UP)
		{
			int rotation=MathHelper.floor((double)((player.rotationYaw+180.0f)*16.0f/360.0f)+0.5)&15;
			world.setBlockState(pos,MDBlock.standingBanner.getDefaultState().withProperty(BlockBanner.ROTATION,rotation),3);
		}
		else
		{
			world.setBlockState(pos,MDBlock.wallBanner.getDefaultState().withProperty(BlockBanner.FACING,facing),3);
		}
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof TileEntityBanner)
		{
			((TileEntityBanner)tile).setItemValues(stack,false);
		}
		TileColor.set(world,pos,stack.getMetadata());
		if(player instanceof EntityPlayerMP)
		{
			CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player,pos,stack);
		}
		stack.shrink(1);
		return EnumActionResult.SUCCESS;
	}
}
