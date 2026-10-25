package net.neverandy.moredyes.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.LlamaModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.util.ResourceLocation;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.reference.Reference;

/**
 * Draws a dyed carpet on a llama's back, like vanilla's LlamaDecorLayer does for its carpets: the grey carpet
 * ({@code textures/entity/llama/decor/dyed.png}) in the carpet's color, and its red and green stripes over it. The
 * server tells the client which carpet a llama wears (see entity/LlamaCarpets).
 */
public class DyedLlamaDecorLayer extends LayerRenderer<LlamaEntity, LlamaModel<LlamaEntity>>
{
    private static final String KEY = Reference.MOD_ID + ":carpet";
    private static final ResourceLocation CARPET = new ResourceLocation(Reference.MOD_ID, "textures/entity/llama/decor/dyed.png");
    private static final ResourceLocation STRIPES = new ResourceLocation(Reference.MOD_ID, "textures/entity/llama/decor/dyed_stripes.png");

    private final LlamaModel<LlamaEntity> model = new LlamaModel<>(0.5F);

    public DyedLlamaDecorLayer(IEntityRenderer<LlamaEntity, LlamaModel<LlamaEntity>> parent)
    {
        super(parent);
    }

    /** Remembers on the client which dyed carpet a llama wears (-1 for none). */
    public static void setCarpet(LlamaEntity llama, int color)
    {
        llama.getPersistentData().putInt(KEY, color);
    }

    @Override
    public void render(MatrixStack pose, IRenderTypeBuffer buffer, int light, LlamaEntity llama, float limbSwing, float limbSwingAmount,
            float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        if (!llama.getPersistentData().contains(KEY))
        {
            return;
        }
        int color = llama.getPersistentData().getInt(KEY);
        if (color < 0 || color >= MDBlock.carpetArray.length || MDBlock.carpetArray[color] == null)
        {
            return;
        }
        int tint = ColorHandlers.tintOf(MDBlock.carpetArray[color]);
        getEntityModel().copyModelAttributesTo(model);
        model.setRotationAngles(llama, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        model.render(pose, buffer.getBuffer(RenderType.getEntityCutoutNoCull(CARPET)), light, OverlayTexture.NO_OVERLAY,
                (tint >> 16 & 255) / 255.0F, (tint >> 8 & 255) / 255.0F, (tint & 255) / 255.0F, 1.0F);
        model.render(pose, buffer.getBuffer(RenderType.getEntityCutoutNoCull(STRIPES)), light, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);
    }
}
