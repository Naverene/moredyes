package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
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
        public Wolves(EntityRendererProvider.Context context)
        {
            super(context);
            layers.removeIf(layer -> layer instanceof WolfCollarLayer);
            addLayer(new Collar(this));
        }

        private static class Collar extends WolfCollarLayer
        {
            private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/wolf/wolf_collar.png");

            Collar(RenderLayerParent<Wolf, WolfModel<Wolf>> renderer)
            {
                super(renderer);
            }

            @Override
            public void render(PoseStack pose, MultiBufferSource buffer, int light, Wolf wolf, float limbSwing, float limbSwingAmount,
                               float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
            {
                float[] rgb = color(DyedCollars.getColor(wolf));
                if (rgb == null)
                {
                    super.render(pose, buffer, light, wolf, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
                else if (wolf.isTame() && !wolf.isInvisible())
                {
                    renderColoredCutoutModel(getParentModel(), TEXTURE, pose, buffer, light, wolf, rgb[0], rgb[1], rgb[2]);
                }
            }
        }
    }

    public static class Cats extends CatRenderer
    {
        public Cats(EntityRendererProvider.Context context)
        {
            super(context);
            layers.removeIf(layer -> layer instanceof CatCollarLayer);
            addLayer(new Collar(this, context.getModelSet()));
        }

        private static class Collar extends CatCollarLayer
        {
            private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/cat/cat_collar.png");
            private final CatModel<Cat> collarModel;

            Collar(RenderLayerParent<Cat, CatModel<Cat>> renderer, EntityModelSet models)
            {
                super(renderer, models);
                collarModel = new CatModel<>(models.bakeLayer(ModelLayers.CAT_COLLAR));
            }

            @Override
            public void render(PoseStack pose, MultiBufferSource buffer, int light, Cat cat, float limbSwing, float limbSwingAmount,
                               float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
            {
                float[] rgb = color(DyedCollars.getColor(cat));
                if (rgb == null)
                {
                    super.render(pose, buffer, light, cat, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }
                else if (cat.isTame())
                {
                    coloredCutoutModelCopyLayerRender(getParentModel(), collarModel, TEXTURE, pose, buffer, light, cat, limbSwing,
                            limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, rgb[0], rgb[1], rgb[2]);
                }
            }
        }
    }
}
