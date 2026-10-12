package net.neverandy.moredyes.compat.ironchest.client;

import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;

/**
 * Client setup for dyed Iron Chests: the chest renderer, the item renderer and the textures they draw with. Only
 * loaded when Iron Chests is installed (see MoreDyesClient).
 */
public final class IronChestClient {

    private IronChestClient() {}

    public static void register() {
        for (IronChestCompat.Tier tier : IronChestCompat.Tier.values()) {
            BlockEntityRendererRegistry.register(tier.entity(), DyedIronChestRenderer::new);
        }
        SpecialModelRenderers.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed_iron_chest"),
            DyedIronChestSpecialRenderer.Unbaked.MAP_CODEC);
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(
            Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed_iron_chest_textures"), DyedIronChestTextures.INSTANCE);
    }
}
