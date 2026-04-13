package info.kg6jay.moredyes.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDBlockColoredChest;
import info.kg6jay.moredyes.render.TileEntityMDBlockColoredChestRenderer;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @SideOnly(Side.CLIENT)
    @Override
    public void registerRenderThings() {
        // Do not bind a TileEntitySpecialRenderer for the colored chest.
        // The vanilla chest renderer handles chest models correctly. Our custom
        // TESR previously bound a single flat-color texture and produced the
        // magenta/weird-looking chest in-world. If we need custom coloring,
        // implement a proper renderer that re-uses the vanilla chest texture or
        // tints it appropriately.
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMDBlockColoredChest.class, new TileEntityMDBlockColoredChestRenderer());


    }

    @Override
    public void registerTileEntities() {

    }
}
