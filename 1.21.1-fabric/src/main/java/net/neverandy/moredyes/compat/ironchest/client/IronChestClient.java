package net.neverandy.moredyes.compat.ironchest.client;

import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.server.packs.PackType;

import net.neverandy.moredyes.compat.ironchest.DyedIronChestItem;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;

/**
 * Client setup for dyed Iron Chests: the chest renderer, the item renderer and the textures they draw with. Only
 * loaded when Iron Chests is installed (see MoreDyesClient).
 */
public final class IronChestClient {

    /** For the items. The chest model comes from code, not resource packs, so it doesn't need baking again on reload. */
    private static DyedIronChestRenderer itemRenderer;

    private IronChestClient() {}

    public static void register() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(DyedIronChestTextures.INSTANCE);
        for (IronChestCompat.Tier tier : IronChestCompat.Tier.values()) {
            BlockEntityRendererRegistry.register(tier.entity(), DyedIronChestRenderer::new);
            // The items' model is vanilla's chest item model (builtin/entity), so they are drawn as small closed chests.
            BuiltinItemRendererRegistry.INSTANCE.register(tier.item(), (stack, context, pose, buffer, light, overlay) -> {
                if (stack.getItem() instanceof DyedIronChestItem item) {
                    if (itemRenderer == null) {
                        itemRenderer = new DyedIronChestRenderer(Minecraft.getInstance().getEntityModels());
                    }
                    itemRenderer.render(item.tier(), IronChestCompat.colorOf(stack), Direction.SOUTH, 0.0F, pose, buffer,
                        light, overlay);
                }
            });
        }
    }
}
