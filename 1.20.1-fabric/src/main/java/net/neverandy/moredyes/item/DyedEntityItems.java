package net.neverandy.moredyes.item;

import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

/**
 * The items of dyed beds, banners and shulker boxes. Their models are vanilla's, which leave the drawing to code, so
 * the client draws them with client/DyedItemRenderer (registered in client/ClientSetup).
 */
public final class DyedEntityItems
{
    private DyedEntityItems() {}

    public static class Bed extends BedItem
    {
        public Bed(Block block, Properties properties)
        {
            super(block, properties.stacksTo(1));
        }
    }

    public static class Banner extends BannerItem
    {
        public Banner(Block banner, Block wallBanner, Properties properties)
        {
            super(banner, wallBanner, properties.stacksTo(16));
        }
    }

    public static class ShulkerBox extends BlockItem
    {
        public ShulkerBox(Block block, Properties properties)
        {
            super(block, properties.stacksTo(1));
        }
    }
}
