package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * Draws a dyed chest item as a small tinted chest, like vanilla does for its chest item. The item models of the dyed
 * chests are vanilla's chest item model ({@code builtin/entity}), which hands the drawing to this.
 */
public class DyedChestItemRenderer extends BlockEntityWithoutLevelRenderer {

    private DyedChestRenderer chest;

    public DyedChestItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        // The chest models are baked again from the reloaded entity models when next drawn.
        chest = null;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack pose, MultiBufferSource buffer,
        int light, int overlay) {
        if (stack.getItem() instanceof BlockItem item) {
            if (chest == null) {
                chest = new DyedChestRenderer(Minecraft.getInstance().getEntityModels());
            }
            chest.renderItem(item.getBlock(), pose, buffer, light, overlay);
        }
    }
}
