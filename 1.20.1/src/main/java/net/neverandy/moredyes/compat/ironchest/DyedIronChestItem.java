package net.neverandy.moredyes.compat.ironchest;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.neverandy.moredyes.compat.ironchest.client.DyedIronChestItemRenderer;
import net.neverandy.moredyes.reference.ColorStrings;

import java.util.Locale;
import java.util.function.Consumer;

/** A dyed Iron Chests chest. Its color is in the item's BlockStateTag, which the placed block takes over. */
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

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(new IClientItemExtensions()
        {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer()
            {
                return DyedIronChestItemRenderer.get();
            }
        });
    }
}
