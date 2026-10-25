package net.neverandy.moredyes.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.client.renderer.entity.model.CatModel;
import net.minecraft.client.renderer.entity.model.WolfModel;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.neverandy.moredyes.entity.DyedCollars;
import net.neverandy.moredyes.reference.ColorStrings;

import java.lang.reflect.Field;

/**
 * Draws the collar of a wolf or cat with a MoreDyes collar color (see entity/DyedCollars) in that color.
 * mixin/WolfCollarLayerMixin and mixin/CatCollarLayerMixin call it from the vanilla collar layers, so it uses whatever
 * models those layers have, also when OptiFine's custom entity models (resource packs) replace them.
 *
 * <p>It extends LayerRenderer only to reach its protected drawing helpers; it is never created.
 */
public abstract class DyedCollarLayers extends LayerRenderer<WolfEntity, WolfModel<WolfEntity>>
{
    private static final ResourceLocation WOLF_COLLAR = new ResourceLocation("textures/entity/wolf/wolf_collar.png");
    private static final ResourceLocation CAT_COLLAR = new ResourceLocation("textures/entity/cat/cat_collar.png");
    /** CatCollarLayer's collar model, found by its SRG name so the released game finds it too. */
    private static final Field CAT_MODEL = ObfuscationReflectionHelper.findField(CatCollarLayer.class, "field_215340_b");

    private DyedCollarLayers()
    {
        super(null);
    }

    /** The collar color to draw, or null for the vanilla color. */
    private static float[] color(TameableEntity pet)
    {
        int index = DyedCollars.getColor(pet);
        if (index < 0)
        {
            return null;
        }
        int rgb = ColorHandlers.vivid(Integer.parseInt(ColorStrings.ALL[index], 16));
        return new float[] {(rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F};
    }

    /** Draws the wolf's MoreDyes collar, and returns false if it has none so the layer draws its vanilla collar. */
    public static boolean renderWolf(WolfCollarLayer layer, MatrixStack matrix, IRenderTypeBuffer buffer, int light, WolfEntity wolf)
    {
        float[] rgb = color(wolf);
        if (rgb == null)
        {
            return false;
        }
        if (wolf.isTamed() && !wolf.isInvisible())
        {
            renderCutoutModel(layer.getEntityModel(), WOLF_COLLAR, matrix, buffer, light, wolf, rgb[0], rgb[1], rgb[2]);
        }
        return true;
    }

    /** Draws the cat's MoreDyes collar, and returns false if it has none so the layer draws its vanilla collar. */
    @SuppressWarnings("unchecked")
    public static boolean renderCat(CatCollarLayer layer, MatrixStack matrix, IRenderTypeBuffer buffer, int light, CatEntity cat,
                                    float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
                                    float headPitch)
    {
        float[] rgb = color(cat);
        if (rgb == null)
        {
            return false;
        }
        if (!cat.isTamed())
        {
            return true;
        }
        CatModel<CatEntity> model;
        try
        {
            model = (CatModel<CatEntity>) CAT_MODEL.get(layer);
        }
        catch (IllegalAccessException e)
        {
            return false;
        }
        renderCopyCutoutModel(layer.getEntityModel(), model, CAT_COLLAR, matrix, buffer, light, cat, limbSwing, limbSwingAmount,
                ageInTicks, netHeadYaw, headPitch, partialTicks, rgb[0], rgb[1], rgb[2]);
        return true;
    }
}
