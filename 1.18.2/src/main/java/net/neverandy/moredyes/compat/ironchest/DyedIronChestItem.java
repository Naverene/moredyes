package net.neverandy.moredyes.compat.ironchest;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;
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
                items.add(IronChestCompat.stack(tier(), color));
            }
        }
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer)
    {
        consumer.accept(new IItemRenderProperties()
        {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer()
            {
                return DyedIronChestItemRenderer.get();
            }
        });
    }
}
