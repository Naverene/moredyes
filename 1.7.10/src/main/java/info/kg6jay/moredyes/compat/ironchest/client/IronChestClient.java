package info.kg6jay.moredyes.compat.ironchest.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.block.RenderIds;
import info.kg6jay.moredyes.compat.ironchest.DyedIronChestTile;

/** Client setup for dyed Iron Chests: one renderer draws them in the world and in the inventory. */
@SideOnly(Side.CLIENT)
public final class IronChestClient {

    private IronChestClient() {}

    public static void register() {
        RenderIds.dyedIronChest = RenderingRegistry.getNextAvailableRenderId();
        DyedIronChestRenderer renderer = new DyedIronChestRenderer();
        // The tier subclasses find this renderer through their shared superclass.
        ClientRegistry.bindTileEntitySpecialRenderer(DyedIronChestTile.class, renderer);
        RenderingRegistry.registerBlockHandler(renderer);
    }
}
