package net.neverandy.moredyes.compat.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.item.ItemDrawers;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.neverandy.moredyes.reference.ColorStrings;

import java.util.Locale;

/** A dyed drawer. Its color is in the item's BlockStateTag, which the placed block takes over. */
public class DyedDrawersItem extends ItemDrawers
{
    public DyedDrawersItem(DyedDrawersBlock block, Properties properties)
    {
        super(block, properties);
    }

    /** "263366 Drawers 1x1", like the names of the other dyed blocks. */
    @Override
    public ITextComponent getDisplayName(ItemStack stack)
    {
        String hex = ColorStrings.ALL[StorageDrawersCompat.colorOf(stack)].toUpperCase(Locale.ROOT);
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
                items.add(StorageDrawersCompat.stack(this, color));
            }
        }
    }
}
