package net.neverandy.moredyes.compat.ironchest;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.item.ItemBlockColored;
import net.neverandy.moredyes.reference.ColorStrings;

/**
 * A dyed Iron Chests shulker box. Its damage value is the color, by its position in ColorStrings.ALL, and its
 * BlockEntityTag holds the contents, as on Iron Chests' own boxes.
 */
public class ItemDyedIronShulkerBox extends ItemBlock
{
	public ItemDyedIronShulkerBox(BlockDyedIronShulkerBox block)
	{
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}
	public IronShulkerBoxCompat.Tier getTier()
	{
		return ((BlockDyedIronShulkerBox)this.block).getTier();
	}
	/** The block has no metadata of its own; the color goes to the tile entity (BlockDyedIronShulkerBox.onBlockPlacedBy). */
	@Override
	public int getMetadata(int damage)
	{
		return 0;
	}
	/** "ECBF99 Iron Shulker Box", like the other dyed blocks. */
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return ItemBlockColored.coloredName(ColorStrings.ALL[IronShulkerBoxCompat.clampColor(stack.getMetadata())],this.block.getUnlocalizedName()+".name");
	}
	/** The first five stacks inside, as on a vanilla shulker box. */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack,@Nullable World world,List<String> tooltip,ITooltipFlag flag)
	{
		super.addInformation(stack,world,tooltip,flag);
		NBTTagCompound tag=stack.getSubCompound("BlockEntityTag");
		if(tag==null||!tag.hasKey("Items",9))
		{
			return;
		}
		NonNullList<ItemStack> items=NonNullList.withSize(this.getTier().type.size,ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(tag,items);
		int shown=0,total=0;
		for(ItemStack item:items)
		{
			if(!item.isEmpty())
			{
				total++;
				if(shown<5)
				{
					shown++;
					tooltip.add(String.format("%s x%d",item.getDisplayName(),item.getCount()));
				}
			}
		}
		if(total>shown)
		{
			tooltip.add(TextFormatting.ITALIC+I18n.format("container.shulkerBox.more",total-shown));
		}
	}
}
