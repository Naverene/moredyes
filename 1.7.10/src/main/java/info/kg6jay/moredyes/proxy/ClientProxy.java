package info.kg6jay.moredyes.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDBlockColoredChest;
import info.kg6jay.moredyes.render.TileEntityMDBlockColoredChestRenderer;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public void registerRenderThings() {
        ClientRegistry.bindTileEntitySpecialRenderer(
            TileEntityMDBlockColoredChest.class,
            new TileEntityMDBlockColoredChestRenderer());
    }

    @Override
    public void registerTileEntities() {

    }
}
