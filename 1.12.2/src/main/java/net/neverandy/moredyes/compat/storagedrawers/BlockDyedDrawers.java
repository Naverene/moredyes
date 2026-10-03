package net.neverandy.moredyes.compat.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.api.storage.EnumBasicDrawer;
import com.jaquadro.minecraft.storagedrawers.block.BlockStandardDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

/**
 * Storage Drawers' standard drawers in a More Dyes color. The metadata is the size, as for Storage Drawers' own, and
 * the tile entity's material is the color's hex code (see StorageDrawersCompat).
 */
public class BlockDyedDrawers extends BlockStandardDrawers
{
	public BlockDyedDrawers()
	{
		super(Reference.MOD_ID+":dyed_drawers",Reference.MOD_ID+".dyed_drawers");
		this.setCreativeTab(MoreDyes.tabBlocks);
	}

	/** Storage Drawers' drop (which may carry the contents) in this drawer's color. */
	@Override
	protected ItemStack getMainDrop(IBlockAccess world,BlockPos pos,IBlockState state)
	{
		ItemStack drop=super.getMainDrop(world,pos,state);
		TileEntityDrawers tile=this.getTileEntity(world,pos);
		if(tile!=null)
		{
			NBTTagCompound tag=drop.hasTagCompound()?drop.getTagCompound():new NBTTagCompound();
			tag.setString(StorageDrawersCompat.MATERIAL,ColorStrings.ALL[StorageDrawersCompat.colorOf(tile)]);
			drop.setTagCompound(tag);
		}
		return drop;
	}
	@Override
	public void getSubBlocks(CreativeTabs tab,NonNullList<ItemStack> items)
	{
		for(EnumBasicDrawer size:EnumBasicDrawer.values())
		{
			for(int color=0;color<ColorStrings.ALL.length;color++)
			{
				items.add(StorageDrawersCompat.stack(size,color));
			}
		}
	}
}
