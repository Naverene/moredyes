package net.neverandy.moredyes.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.client.renderer.entity.model.SheepModel;
import net.minecraft.client.renderer.entity.model.SheepWoolModel;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.neverandy.moredyes.entity.DyedSheep;
import net.neverandy.moredyes.reference.ColorStrings;

import java.lang.reflect.Field;

/**
 * Draws the wool of a sheep with a MoreDyes color (see entity/DyedSheep) in that color. mixin/SheepWoolLayerMixin calls
 * it from the vanilla wool layer, so it uses whatever wool model that layer has: OptiFine's custom entity models (used
 * by resource packs such as Sheeps Reimagined) build their own sheep renderer and wool layer, and a replaced renderer
 * would never be drawn.
 *
 * <p>It extends LayerRenderer only to reach its protected drawing helper; it is never created.
 */
public abstract class DyedSheepWool extends LayerRenderer<SheepEntity, SheepModel<SheepEntity>>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    /** SheepWoolLayer.sheepModel, found by its SRG name so the released game finds it too. */
    private static final Field MODEL = ObfuscationReflectionHelper.findField(SheepWoolLayer.class, "field_177164_c");

    private DyedSheepWool()
    {
        super(null);
    }

    /** Draws the sheep's MoreDyes wool, and returns false if it has none so the layer draws its vanilla wool. */
    @SuppressWarnings("unchecked")
    public static boolean render(SheepWoolLayer layer, MatrixStack matrix, IRenderTypeBuffer buffer, int light, SheepEntity sheep,
                                 float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        int color = DyedSheep.getColor(sheep);
        boolean jeb = sheep.hasCustomName() && "jeb_".equals(sheep.getName().getUnformattedComponentText());
        if (color < 0 || jeb)
        {
            return false;
        }
        if (sheep.getSheared() || sheep.isInvisible())
        {
            return true;
        }
        SheepWoolModel<SheepEntity> model;
        try
        {
            model = (SheepWoolModel<SheepEntity>) MODEL.get(layer);
        }
        catch (IllegalAccessException e)
        {
            return false;
        }
        int rgb = ColorHandlers.vivid(Integer.parseInt(ColorStrings.ALL[color], 16));
        renderCopyCutoutModel(layer.getEntityModel(), model, TEXTURE, matrix, buffer, light, sheep, limbSwing, limbSwingAmount,
                ageInTicks, netHeadYaw, headPitch, partialTicks,
                (rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F);
        return true;
    }
}
