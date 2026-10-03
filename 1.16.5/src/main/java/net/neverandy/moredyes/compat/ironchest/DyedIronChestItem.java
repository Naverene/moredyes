package net.neverandy.moredyes.compat.ironchest;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.neverandy.moredyes.reference.ColorStrings;

import java.util.Locale;

/**
 * A dyed Iron Chests chest. Its color is in the item's BlockStateTag, which the placed block takes over. The client
 * draws it with client/DyedIronChestItemRenderer.
 */
public class DyedIronChestItem extends BlockItem
{
    public DyedIronChestItem(DyedIronChestBlock block, Properties properties)
    {
        super(block, properties);
    }

    public IronChestCompat.Tier tier()
    {
        return ((DyedIronChestBlock) getBlock()).tier();
    }

    /** "263366 Iron Chest", like the names of the other dyed blocks. */
    @Override
    public ITextComponent getDisplayName(ItemStack stack)
    {
        String hex = ColorStrings.ALL[IronChestCompat.colorOf(stack)].toUpperCase(Locale.ROOT);
        return new TranslationTextComponent(getTranslationKey(stack), hex);
    }

    /** Every color, in the More Dyes blocks tab. */
    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        if (isInGroup(group))
        {
            for (int color = 0; color < ColorStrings.ALL.length; color++)
            {
                items.add(IronChestCompat.stack(tier(), color));
            }
        }
    }
}
