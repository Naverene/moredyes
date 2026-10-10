package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;

import net.neverandy.moredyes.MoreDyes;

/**
 * Draws a dyed chest item as a small tinted chest, like vanilla does for its chest item. The item models of the dyed
 * chests are vanilla's chest item model ({@code builtin/entity}), which Fabric hands to this (see MoreDyesClient).
 */
public class DyedChestItemRenderer {

    private DyedChestRenderer chest;

    public DyedChestItemRenderer() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(
            new SimpleSynchronousResourceReloadListener() {
                @Override
                public ResourceLocation getFabricId() {
                    return ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID, "chest_item");
                }

                @Override
                public void onResourceManagerReload(ResourceManager resourceManager) {
                    // The chest models are baked again from the reloaded entity models when next drawn.
                    chest = null;
                }
            });
    }

    public void render(Block block, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
        if (chest == null) {
            chest = new DyedChestRenderer(Minecraft.getInstance().getEntityModels());
        }
        chest.renderItem(block, pose, buffer, light, overlay);
    }
}
