package net.neverandy.moredyes.compat.ironshulkerbox;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.neverandy.moredyes.reference.ColorStrings;

import java.util.Locale;

/**
 * A dyed Iron Shulker Boxes box. Its color is in the item's BlockStateTag, which the placed block takes over, and its
 * contents are in the BlockEntityTag, as on a vanilla shulker box. The client draws it with
 * client/DyedIronShulkerBoxItemRenderer.
 */
public class DyedIronShulkerBoxItem extends BlockItem
{
    public DyedIronShulkerBoxItem(DyedIronShulkerBoxBlock block, Properties properties)
    {
        super(block, properties);
    }

    public IronShulkerBoxCompat.Tier tier()
    {
        return ((DyedIronShulkerBoxBlock) getBlock()).tier();
    }

    /** "263366 Iron Shulker Box", like the names of the other dyed blocks. */
    @Override
    public ITextComponent getDisplayName(ItemStack stack)
    {
        String hex = ColorStrings.ALL[IronShulkerBoxCompat.colorOf(stack)].toUpperCase(Locale.ROOT);
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
                items.add(IronShulkerBoxCompat.stack(tier(), color));
            }
        }
    }
}
