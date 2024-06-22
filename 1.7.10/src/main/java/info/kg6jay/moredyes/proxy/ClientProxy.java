package info.kg6jay.moredyes.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.block.tileEntity.TileEntityColoredChest;
import info.kg6jay.moredyes.render.tileEntity.TileEntityColoredChestRender;
import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import static info.kg6jay.moredyes.block.MDBlock.colors;

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
