package net.neverandy.moredyes.item;

import net.minecraft.block.Block;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BedItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

/**
 * The items of dyed beds, banners and shulker boxes. Their models are vanilla's, which leave the drawing to code, so
 * the client draws them with client/DyedItemRenderer.
 */
public final class DyedEntityItems
{
    private DyedEntityItems() {}

    private static Item.Properties drawn(Item.Properties properties, int stackSize)
    {
        return properties.maxStackSize(stackSize).setISTER(() -> net.neverandy.moredyes.client.DyedItemRenderer::new);
    }

    public static Item bed(Block bed, Item.Properties properties)
    {
        return new BedItem(bed, drawn(properties, 1));
    }

    public static Item banner(Block banner, Block wallBanner, Item.Properties properties)
    {
        return new BannerItem(banner, wallBanner, drawn(properties, 16));
    }

    public static Item shulkerBox(Block box, Item.Properties properties)
    {
        return new BlockItem(box, drawn(properties, 1));
    }
}
