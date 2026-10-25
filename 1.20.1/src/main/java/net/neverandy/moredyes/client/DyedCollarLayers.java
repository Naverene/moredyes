package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.neverandy.moredyes.entity.DyedCollars;
import net.neverandy.moredyes.reference.ColorStrings;

import java.lang.reflect.Field;

/**
 * Draws the collar of a wolf or cat with a MoreDyes collar color (see entity/DyedCollars) in that color.
 * mixin/WolfCollarLayerMixin and mixin/CatCollarLayerMixin call it from the vanilla collar layers, so it uses whatever
 * models those layers have, also when OptiFine's custom entity models (resource packs) replace them.
 *
 * <p>It extends RenderLayer only to reach its protected drawing helpers; it is never created.
 */
public abstract class DyedCollarLayers extends RenderLayer<Wolf, WolfModel<Wolf>>
{
    private static final ResourceLocation WOLF_COLLAR = new ResourceLocation("textures/entity/wolf/wolf_collar.png");
    private static final ResourceLocation CAT_COLLAR = new ResourceLocation("textures/entity/cat/cat_collar.png");
    /** CatCollarLayer.catModel, found by its SRG name so the released game finds it too. */
    private static final Field CAT_MODEL = ObfuscationReflectionHelper.findField(CatCollarLayer.class, "f_116650_");

    private DyedCollarLayers()
    {
        super(null);
    }

    /** The collar color to draw, or null for the vanilla color. */
    private static float[] color(TamableAnimal pet)
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
    public static boolean renderWolf(WolfCollarLayer layer, PoseStack pose, MultiBufferSource buffer, int light, Wolf wolf)
    {
        float[] rgb = color(wolf);
        if (rgb == null)
        {
            return false;
        }
        if (wolf.isTame() && !wolf.isInvisible())
        {
            renderColoredCutoutModel(layer.getParentModel(), WOLF_COLLAR, pose, buffer, light, wolf, rgb[0], rgb[1], rgb[2]);
        }
        return true;
    }

    /** Draws the cat's MoreDyes collar, and returns false if it has none so the layer draws its vanilla collar. */
    @SuppressWarnings("unchecked")
    public static boolean renderCat(CatCollarLayer layer, PoseStack pose, MultiBufferSource buffer, int light, Cat cat, float limbSwing,
                                    float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        float[] rgb = color(cat);
        if (rgb == null)
        {
            return false;
        }
        if (!cat.isTame())
        {
            return true;
        }
        CatModel<Cat> model;
        try
        {
            model = (CatModel<Cat>) CAT_MODEL.get(layer);
        }
        catch (IllegalAccessException e)
        {
            return false;
        }
        coloredCutoutModelCopyLayerRender(layer.getParentModel(), model, CAT_COLLAR, pose, buffer, light, cat, limbSwing, limbSwingAmount,
                ageInTicks, netHeadYaw, headPitch, partialTicks, rgb[0], rgb[1], rgb[2]);
        return true;
    }
}
