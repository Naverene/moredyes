package net.neverandy.moredyes.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;
import net.neverandy.moredyes.client.DyedItemRenderer;

import java.util.function.Consumer;

/**
 * The items of dyed beds, banners and shulker boxes. Their models are vanilla's, which leave the drawing to code, so
 * the client draws them with client/DyedItemRenderer.
 */
public final class DyedEntityItems
{
    private DyedEntityItems() {}

    private static void renderer(Consumer<IItemRenderProperties> consumer)
    {
        consumer.accept(new IItemRenderProperties()
        {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer()
            {
                return DyedItemRenderer.get();
            }
        });
    }

    public static class Bed extends BedItem
    {
        public Bed(Block block, Properties properties)
        {
            super(block, properties.stacksTo(1));
        }

        @Override
        public void initializeClient(Consumer<IItemRenderProperties> consumer)
        {
            renderer(consumer);
        }
    }

    public static class Banner extends BannerItem
    {
        public Banner(Block banner, Block wallBanner, Properties properties)
        {
            super(banner, wallBanner, properties.stacksTo(16));
        }

        @Override
        public void initializeClient(Consumer<IItemRenderProperties> consumer)
        {
            renderer(consumer);
        }
    }

    public static class ShulkerBox extends BlockItem
    {
        public ShulkerBox(Block block, Properties properties)
        {
            super(block, properties.stacksTo(1));
        }

        @Override
        public void initializeClient(Consumer<IItemRenderProperties> consumer)
        {
            renderer(consumer);
        }
    }
}
