package net.neverandy.moredyes.compat.ironchest.client;

import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;

/**
 * Client setup for dyed Iron Chests: the chest renderer, the item renderer and the textures they draw with. Only
 * loaded when Iron Chests is installed (see MoreDyesClient).
 */
public final class IronChestClient {

    private IronChestClient() {}

    public static void register(IEventBus modBus) {
        modBus.addListener(IronChestClient::renderers);
        modBus.addListener(IronChestClient::specialRenderers);
        modBus.addListener(IronChestClient::reloadListeners);
    }

    private static void renderers(EntityRenderersEvent.RegisterRenderers event) {
        for (IronChestCompat.Tier tier : IronChestCompat.Tier.values()) {
            event.registerBlockEntityRenderer(tier.entity(), DyedIronChestRenderer::new);
        }
    }

    private static void specialRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed_iron_chest"),
            DyedIronChestSpecialRenderer.Unbaked.MAP_CODEC);
    }

    private static void reloadListeners(AddClientReloadListenersEvent event) {
        event.addListener(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed_iron_chest_textures"),
            DyedIronChestTextures.INSTANCE);
    }
}
