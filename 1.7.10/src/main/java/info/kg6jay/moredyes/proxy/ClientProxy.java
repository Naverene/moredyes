package info.kg6jay.moredyes.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import info.kg6jay.moredyes.block.RenderIds;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDBlockColoredChest;
import info.kg6jay.moredyes.client.LayeredBlockRenderer;
import info.kg6jay.moredyes.client.TintedTextures;
import info.kg6jay.moredyes.render.TileEntityMDBlockColoredChestRenderer;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        // Must be in place before the game loads its textures at the end of mod loading.
        TintedTextures.install();
    }

    @Override
    public void registerRenderThings() {
        RenderIds.chest = RenderingRegistry.getNextAvailableRenderId();
        TileEntityMDBlockColoredChestRenderer chestRenderer = new TileEntityMDBlockColoredChestRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMDBlockColoredChest.class, chestRenderer);
        RenderingRegistry.registerBlockHandler(chestRenderer);

        RenderIds.layeredCube = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new LayeredBlockRenderer(RenderIds.layeredCube, false));
        RenderIds.layeredPlant = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new LayeredBlockRenderer(RenderIds.layeredPlant, true));
    }
}
