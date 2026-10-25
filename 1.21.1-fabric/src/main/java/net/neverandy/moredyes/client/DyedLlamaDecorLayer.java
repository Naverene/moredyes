package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.LlamaModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarpetBlock;

import net.neverandy.moredyes.MoreDyes;

/**
 * Draws a dyed carpet on a llama's back, like vanilla's LlamaDecorLayer does for its carpets: the grey carpet
 * ({@code textures/entity/llama/decor/dyed.png}) in the carpet's color, and its red and green stripes over it.
 */
public class DyedLlamaDecorLayer extends RenderLayer<Llama, LlamaModel<Llama>> {

    private static final ResourceLocation CARPET = ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID,
        "textures/entity/llama/decor/dyed.png");
    private static final ResourceLocation STRIPES = ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID,
        "textures/entity/llama/decor/dyed_stripes.png");

    private final LlamaModel<Llama> model;

    public DyedLlamaDecorLayer(RenderLayerParent<Llama, LlamaModel<Llama>> parent, EntityModelSet models) {
        super(parent);
        this.model = new LlamaModel<>(models.bakeLayer(ModelLayers.LLAMA_DECOR));
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int light, Llama llama, float limbSwing,
        float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack carpet = llama.getBodyArmorItem();
        int tint = Tints.itemTint(carpet.getItem());
        // Vanilla's layer draws vanilla carpets; a dyed one is a carpet with a dye color.
        if (tint == -1 || !(Block.byItem(carpet.getItem()) instanceof CarpetBlock)) {
            return;
        }
        getParentModel().copyPropertiesTo(model);
        model.setupAnim(llama, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        model.renderToBuffer(pose, buffer.getBuffer(RenderType.entityCutoutNoCull(CARPET)), light,
            OverlayTexture.NO_OVERLAY, tint);
        model.renderToBuffer(pose, buffer.getBuffer(RenderType.entityCutoutNoCull(STRIPES)), light,
            OverlayTexture.NO_OVERLAY);
    }
}
