package net.neverandy.moredyes.compat.ironshulkerbox.client;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.neverandy.moredyes.compat.ironshulkerbox.IronShulkerBoxCompat;

/** Client setup for dyed Iron Shulker Boxes: the box renderer and the textures it draws with. */
public final class IronShulkerBoxClient
{
    private IronShulkerBoxClient() {}

    public static void register(IEventBus modBus)
    {
        modBus.addListener(IronShulkerBoxClient::renderers);
        modBus.addListener(IronShulkerBoxClient::reloadListeners);
    }

    private static void renderers(EntityRenderersEvent.RegisterRenderers event)
    {
        for (IronShulkerBoxCompat.Tier tier : IronShulkerBoxCompat.Tier.values())
        {
            event.registerBlockEntityRenderer(tier.entity.get(), DyedIronShulkerBoxRenderer::new);
        }
    }

    private static void reloadListeners(RegisterClientReloadListenersEvent event)
    {
        event.registerReloadListener(DyedIronShulkerBoxTextures.INSTANCE);
    }
}
