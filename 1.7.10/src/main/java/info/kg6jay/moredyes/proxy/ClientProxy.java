package info.kg6jay.moredyes.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import info.kg6jay.moredyes.block.MDBlockColoredChest;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDBlockColoredChest;
import info.kg6jay.moredyes.render.TileEntityMDBlockColoredChestRenderer;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenderThings() {
        TileEntityMDBlockColoredChestRenderer chestRenderer = new TileEntityMDBlockColoredChestRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMDBlockColoredChest.class, chestRenderer);
        MDBlockColoredChest.renderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(chestRenderer);
    }
}
