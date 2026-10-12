package net.neverandy.moredyes.compat.ironchest;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.neverandy.moredyes.reference.ColorStrings;

import java.util.Locale;

/**
 * A dyed Iron Chests chest. Its color is in the item's BlockStateTag, which the placed block takes over. Its item model
 * is vanilla's chest item model (builtin/entity), drawn by client/IronChestClient.
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
    public Component getName(ItemStack stack)
    {
        String hex = ColorStrings.ALL[IronChestCompat.colorOf(stack)].toUpperCase(Locale.ROOT);
        return Component.translatable(getDescriptionId(stack), hex);
    }
}
