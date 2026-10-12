package net.neverandy.moredyes.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.block.TileColor;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

/**
 * The dyed sign, in every color; the damage is the color's position in ColorStrings.ALL. It places a standing or a
 * wall sign like the vanilla sign and opens the sign editor.
 */
public class ItemDyedSign extends Item
{
	public ItemDyedSign()
	{
		this.setMaxStackSize(16);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setRegistryName(Reference.MOD_ID,"sign");
		this.setUnlocalizedName(Reference.MOD_ID+".sign");
		this.setCreativeTab(MoreDyes.tabBlocks);
	}
	/** "ECBF99 Sign". */
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
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
	/** The vanilla sign's placement, with the dyed sign blocks. */
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
		if(!player.canPlayerEdit(pos,facing,stack)||!MDBlock.standingSign.canPlaceBlockAt(world,pos))
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
			world.setBlockState(pos,MDBlock.standingSign.getDefaultState().withProperty(BlockStandingSign.ROTATION,rotation),11);
		}
		else
		{
			world.setBlockState(pos,MDBlock.wallSign.getDefaultState().withProperty(BlockWallSign.FACING,facing),11);
		}
		TileColor.set(world,pos,stack.getMetadata());
		TileEntity tile=world.getTileEntity(pos);
		if(tile instanceof TileEntitySign&&!ItemBlock.setTileEntityNBT(world,player,pos,stack))
		{
			player.openEditSign((TileEntitySign)tile);
		}
		if(player instanceof EntityPlayerMP)
		{
			CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player,pos,stack);
		}
		stack.shrink(1);
		return EnumActionResult.SUCCESS;
	}
}
