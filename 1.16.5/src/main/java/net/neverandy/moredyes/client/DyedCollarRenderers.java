package net.neverandy.moredyes.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.client.renderer.entity.model.CatModel;
import net.minecraft.client.renderer.entity.model.WolfModel;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.ResourceLocation;
import net.neverandy.moredyes.entity.DyedCollars;
import net.neverandy.moredyes.reference.ColorStrings;

/** The vanilla wolf and cat renderers, with collars that can also take a MoreDyes color (see entity/DyedCollars). */
public final class DyedCollarRenderers
{
    private DyedCollarRenderers() {}

    /** The collar color to draw, or null for the vanilla color. */
    private static float[] color(int index)
    {
        if (index < 0)
        {
            return null;
        }
        int rgb = ColorHandlers.vivid(Integer.parseInt(ColorStrings.ALL[index], 16));
        return new float[] {(rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F};
    }

    public static class Wolves extends WolfRenderer
    {
        public Wolves(EntityRendererManager manager)
        {
            super(manager);
            layerRenderers.removeIf(layer -> layer instanceof WolfCollarLayer);
            addLayer(new Collar(this));
        }

        private static class Collar extends WolfCollarLayer
        {
            private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/wolf/wolf_collar.png");

            Collar(IEntityRenderer<WolfEntity, WolfModel<WolfEntity>> renderer)
            {
                super(renderer);
            }

            @Override
            public void render(MatrixStack matrix, IRenderTypeBuffer buffer, int light, WolfEntity wolf, float limbSwing, float limbSwingAmount,
                               float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
            {
                float[] rgb = color(DyedCollars.getColor(wolf));
                if (rgb == null)
                {
                    super.render(matrix, buffer, light, wolf, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
                else if (wolf.isTamed() && !wolf.isInvisible())
                {
                    renderCutoutModel(getEntityModel(), TEXTURE, matrix, buffer, light, wolf, rgb[0], rgb[1], rgb[2]);
                }
            }
        }
    }

    public static class Cats extends CatRenderer
    {
        public Cats(EntityRendererManager manager)
        {
            super(manager);
            layerRenderers.removeIf(layer -> layer instanceof CatCollarLayer);
            addLayer(new Collar(this));
        }

        private static class Collar extends CatCollarLayer
        {
            private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/cat/cat_collar.png");
            private final CatModel<CatEntity> collarModel = new CatModel<>(0.01F);

            Collar(IEntityRenderer<CatEntity, CatModel<CatEntity>> renderer)
            {
                super(renderer);
            }

            @Override
            public void render(MatrixStack matrix, IRenderTypeBuffer buffer, int light, CatEntity cat, float limbSwing, float limbSwingAmount,
                               float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
            {
                float[] rgb = color(DyedCollars.getColor(cat));
                if (rgb == null)
                {
                    super.render(matrix, buffer, light, cat, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
                else if (cat.isTamed())
                {
                    renderCopyCutoutModel(getEntityModel(), collarModel, TEXTURE, matrix, buffer, light, cat, limbSwing, limbSwingAmount,
                            ageInTicks, netHeadYaw, headPitch, partialTicks, rgb[0], rgb[1], rgb[2]);
                }
            }
        }
    }
}
