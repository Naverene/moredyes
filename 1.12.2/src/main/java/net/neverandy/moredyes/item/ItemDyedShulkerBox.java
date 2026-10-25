package net.neverandy.moredyes.item;

import net.minecraft.block.Block;

/** The dyed shulker box item: one to a stack, like the vanilla box, and its color is the damage. */
public class ItemDyedShulkerBox extends ItemBlockTileColored
{
	public ItemDyedShulkerBox(Block block)
	{
		super(block);
		this.setMaxStackSize(1);
	}
}
