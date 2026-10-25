package net.neverandy.moredyes.compat.ironshulkerbox;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.neverandy.moredyes.compat.ironshulkerbox.client.DyedIronShulkerBoxItemRenderer;
import net.neverandy.moredyes.reference.ColorStrings;

import java.util.Locale;
import java.util.function.Consumer;

/**
 * A dyed Iron Shulker Boxes box. Its color is in the item's BlockStateTag, which the placed block takes over, and its
 * contents are in the BlockEntityTag, as on a vanilla shulker box.
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
    public Component getName(ItemStack stack)
    {
        String hex = ColorStrings.ALL[IronShulkerBoxCompat.colorOf(stack)].toUpperCase(Locale.ROOT);
        return Component.translatable(getDescriptionId(stack), hex);
    }

    /** Shulker boxes can't go into other shulker boxes or bundles. */
    @Override
    public boolean canFitInsideContainerItems()
    {
        return false;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(new IClientItemExtensions()
        {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer()
            {
                return DyedIronShulkerBoxItemRenderer.get();
            }
        });
    }
}
