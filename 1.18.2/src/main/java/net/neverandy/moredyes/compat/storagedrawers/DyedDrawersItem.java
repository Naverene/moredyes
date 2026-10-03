package net.neverandy.moredyes.compat.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.item.ItemDrawers;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
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
    public Component getName(ItemStack stack)
    {
        String hex = ColorStrings.ALL[StorageDrawersCompat.colorOf(stack)].toUpperCase(Locale.ROOT);
        return new TranslatableComponent(getDescriptionId(stack), hex);
    }

    /** Lists every color in the creative tab. */
    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items)
    {
        if (allowdedIn(tab))
        {
            for (int color = 0; color < ColorStrings.ALL.length; color++)
            {
                items.add(StorageDrawersCompat.withColor(new ItemStack(this), color));
            }
        }
    }
}
