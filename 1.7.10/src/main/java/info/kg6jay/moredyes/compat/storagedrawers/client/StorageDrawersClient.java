package info.kg6jay.moredyes.compat.storagedrawers.client;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.block.RenderIds;
import info.kg6jay.moredyes.compat.storagedrawers.StorageDrawersCompat;

/**
 * Client setup for dyed drawers: the block and item renderers. The labels and items on the front are drawn by Storage
 * Drawers' own tile entity renderer, which also finds the dyed drawers' tile entity (a subclass of its own).
 */
@SideOnly(Side.CLIENT)
public final class StorageDrawersClient {

    private StorageDrawersClient() {}

    public static void register() {
        RenderIds.dyedDrawers = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(RenderIds.dyedDrawers, new DyedDrawersRenderer());
        DyedDrawersItemRenderer itemRenderer = new DyedDrawersItemRenderer();
        for (StorageDrawersCompat.Size size : StorageDrawersCompat.Size.values()) {
            if (size.block != null) {
                MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(size.block), itemRenderer);
            }
        }
    }
}
