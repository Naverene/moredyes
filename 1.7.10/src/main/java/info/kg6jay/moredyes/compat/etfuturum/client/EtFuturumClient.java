package info.kg6jay.moredyes.compat.etfuturum.client;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.compat.etfuturum.DyedShulkerBoxTile;
import info.kg6jay.moredyes.compat.etfuturum.EtFuturumCompat;

/** Client setup for the dyed shulker boxes: one renderer draws them in the world, and through it in the inventory. */
@SideOnly(Side.CLIENT)
public final class EtFuturumClient {

    private EtFuturumClient() {}

    public static void register() {
        if (!EtFuturumCompat.enabled()) {
            return;
        }
        ClientRegistry.bindTileEntitySpecialRenderer(DyedShulkerBoxTile.class, new DyedShulkerBoxRenderer());
        MinecraftForgeClient
            .registerItemRenderer(Item.getItemFromBlock(EtFuturumCompat.block), new DyedShulkerBoxItemRenderer());
    }
}
