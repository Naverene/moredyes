package net.neverandy.moredyes.compat.ironchest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;

/** Client setup for dyed Iron Chests: the chest renderer and the textures it draws with. */
public final class IronChestClient
{
    private IronChestClient() {}

    public static void register(IEventBus modBus)
    {
        modBus.addListener(IronChestClient::setup);
        // The textures are split again whenever resource packs change. Minecraft's resource manager already exists
        // while mods are constructed, and its first load comes after. Data generation has no Minecraft.
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft != null)
        {
            ((IReloadableResourceManager) minecraft.getResourceManager()).addReloadListener(DyedIronChestTextures.INSTANCE);
        }
    }

    private static void setup(FMLClientSetupEvent event)
    {
        for (IronChestCompat.Tier tier : IronChestCompat.Tier.values())
        {
            ClientRegistry.bindTileEntityRenderer(tier.tileEntity.get(), DyedIronChestRenderer::new);
        }
    }
}
