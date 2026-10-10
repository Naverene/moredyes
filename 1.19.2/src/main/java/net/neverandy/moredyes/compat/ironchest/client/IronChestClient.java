package net.neverandy.moredyes.compat.ironchest.client;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;

/** Client setup for dyed Iron Chests: the chest renderer and the textures it draws with. */
public final class IronChestClient
{
    private IronChestClient() {}

    public static void register(IEventBus modBus)
    {
        modBus.addListener(IronChestClient::renderers);
        modBus.addListener(IronChestClient::reloadListeners);
    }

    private static void renderers(EntityRenderersEvent.RegisterRenderers event)
    {
        for (IronChestCompat.Tier tier : IronChestCompat.Tier.values())
        {
            event.registerBlockEntityRenderer(tier.entity.get(), DyedIronChestRenderer::new);
        }
    }

    private static void reloadListeners(RegisterClientReloadListenersEvent event)
    {
        event.registerReloadListener(DyedIronChestTextures.INSTANCE);
    }
}
