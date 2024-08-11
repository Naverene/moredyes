package info.kg6jay.moredyes.proxy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    public void registerRenderThings()
    {
        /*
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityColoredChest.class, new TileEntityColoredChestRender());
        for(int i=0;i<colors.length;i++)
        {
            MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(MDBlock.chest[i]), new TileEntityColoredChestRender());
        }
         */
    }
}
