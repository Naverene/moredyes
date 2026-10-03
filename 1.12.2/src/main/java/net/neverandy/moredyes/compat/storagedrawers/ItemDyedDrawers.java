package net.neverandy.moredyes.compat.storagedrawers;

import java.util.List;

import javax.annotation.Nullable;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.api.storage.EnumBasicDrawer;
import com.jaquadro.minecraft.storagedrawers.item.ItemDrawers;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.item.ItemBlockColored;
import net.neverandy.moredyes.reference.ColorStrings;

/**
 * A dyed drawer. Its damage value is the size and its NBT material is the color, as Storage Drawers' own drawer items
 * keep the size and the wood; placing it hands the material to the tile entity (ItemDrawers.placeBlockAt).
 */
public class ItemDyedDrawers extends ItemDrawers
{
	public ItemDyedDrawers(BlockDyedDrawers block)
	{
		super(block);
		this.setHasSubtypes(true);
	}
	/** Named per size as Storage Drawers names its own: tile.moredyes.dyed_drawers.fulldrawers2 and so on. */
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return this.block.getUnlocalizedName()+"."+EnumBasicDrawer.byMetadata(stack.getMetadata()).getUnlocalizedName();
	}
	/** "ECBF99 Drawers 1x2", like the other dyed blocks. */
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return ItemBlockColored.coloredName(ColorStrings.ALL[StorageDrawersCompat.colorOf(stack)],this.getUnlocalizedName(stack)+".name");
	}
	/** Storage Drawers' tooltip, but for its line naming the wood, which would show the color's hex code again. */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack,@Nullable World world,List<String> tooltip,ITooltipFlag flag)
	{
		String size=EnumBasicDrawer.byMetadata(stack.getMetadata()).getUnlocalizedName();
		tooltip.add(I18n.format("storagedrawers.drawers.description",StorageDrawers.config.getBlockBaseStorage(size)));
		if(stack.hasTagCompound()&&stack.getTagCompound().hasKey("tile"))
		{
			tooltip.add(TextFormatting.YELLOW+I18n.format("storagedrawers.drawers.sealed"));
		}
	}
}
