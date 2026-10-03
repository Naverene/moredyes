package net.neverandy.moredyes.compat.ironchest;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.neverandy.moredyes.item.ItemBlockColored;
import net.neverandy.moredyes.reference.ColorStrings;

/** A dyed Iron Chests chest. Its damage value is the color, by its position in ColorStrings.ALL. */
public class ItemDyedIronChest extends ItemBlock
{
	public ItemDyedIronChest(BlockDyedIronChest block)
	{
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}
	public IronChestCompat.Tier getTier()
	{
		return ((BlockDyedIronChest)this.block).getTier();
	}
	/** The block has no metadata of its own; the color goes to the tile entity (BlockDyedIronChest.onBlockPlacedBy). */
	@Override
	public int getMetadata(int damage)
	{
		return 0;
	}
	/** "ECBF99 Iron Chest", like the other dyed blocks. */
	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		return ItemBlockColored.coloredName(ColorStrings.ALL[IronChestCompat.clampColor(stack.getMetadata())],this.block.getUnlocalizedName()+".name");
	}
}
