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
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.entity.LlamaCarpets;
import net.neverandy.moredyes.reference.Reference;

/**
 * Draws a dyed carpet on a llama's back, like vanilla's LlamaDecorLayer does for its carpets: the grey carpet
 * ({@code textures/entity/llama/decor/dyed.png}) in the carpet's color, and its red and green stripes over it. The
 * server tells the client which carpet a llama wears (see entity/LlamaCarpets).
 */
public class DyedLlamaDecorLayer extends RenderLayer<Llama, LlamaModel<Llama>>
{
    private static final ResourceLocation CARPET = new ResourceLocation(Reference.MOD_ID, "textures/entity/llama/decor/dyed.png");
    private static final ResourceLocation STRIPES = new ResourceLocation(Reference.MOD_ID, "textures/entity/llama/decor/dyed_stripes.png");

    private final LlamaModel<Llama> model;

    public DyedLlamaDecorLayer(RenderLayerParent<Llama, LlamaModel<Llama>> parent, EntityModelSet models)
    {
        super(parent);
        this.model = new LlamaModel<>(models.bakeLayer(ModelLayers.LLAMA_DECOR));
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int light, Llama llama, float limbSwing, float limbSwingAmount,
            float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        int color = ((LlamaCarpets.Carpet) llama).moredyes$getCarpet();
        if (color < 0 || color >= MDBlock.COLORS || MDBlock.carpetArray[color] == null)
        {
            return;
        }
        int tint = ColorHandlers.tintOf(MDBlock.carpetArray[color]);
        getParentModel().copyPropertiesTo(model);
        model.setupAnim(llama, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        model.renderToBuffer(pose, buffer.getBuffer(RenderType.entityCutoutNoCull(CARPET)), light, OverlayTexture.NO_OVERLAY,
                (tint >> 16 & 255) / 255.0F, (tint >> 8 & 255) / 255.0F, (tint & 255) / 255.0F, 1.0F);
        model.renderToBuffer(pose, buffer.getBuffer(RenderType.entityCutoutNoCull(STRIPES)), light, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);
    }
}
