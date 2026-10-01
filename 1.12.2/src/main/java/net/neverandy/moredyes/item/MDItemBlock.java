package net.neverandy.moredyes.item;

import net.neverandy.moredyes.block.IColoredBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class MDItemBlock extends ItemBlock
{
	public MDItemBlock(Block block)
	{
		super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + "." + ((IColoredBlock)this.block).getColorName(stack);
    }
    /**
     * Sets the unlocalized name of this item to the string passed as the parameter, prefixed by "item."
     */
    public ItemBlock setUnlocalizedName(String unlocalizedName)
    {
        super.setUnlocalizedName(unlocalizedName);
        return this;
    }
}
