package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.neverandy.moredyes.entity.DyedSheep;
import net.neverandy.moredyes.reference.ColorStrings;

import java.lang.reflect.Field;

/**
 * Draws the wool of a sheep with a MoreDyes color (see entity/DyedSheep) in that color. mixin/SheepFurLayerMixin calls
 * it from the vanilla wool layer, so it uses whatever wool model that layer has: OptiFine's custom entity models (used
 * by resource packs such as Sheeps Reimagined) build their own sheep renderer and wool layer, and a replaced renderer
 * would never be drawn.
 *
 * <p>It extends RenderLayer only to reach its protected drawing helper; it is never created.
 */
public abstract class DyedSheepWool extends RenderLayer<Sheep, SheepModel<Sheep>>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
    /** SheepFurLayer.model, found by its SRG name so the released game finds it too. */
    private static final Field MODEL = ObfuscationReflectionHelper.findField(SheepFurLayer.class, "f_117405_");

    private DyedSheepWool()
    {
        super(null);
    }

    /** Draws the sheep's MoreDyes wool, and returns false if it has none so the layer draws its vanilla wool. */
    @SuppressWarnings("unchecked")
    public static boolean render(SheepFurLayer layer, PoseStack pose, MultiBufferSource buffer, int light, Sheep sheep, float limbSwing,
                                 float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        int color = DyedSheep.getColor(sheep);
        boolean jeb = sheep.hasCustomName() && "jeb_".equals(sheep.getName().getContents());
        // Invisible sheep keep the vanilla layer, which only draws a glowing outline.
        if (color < 0 || jeb || sheep.isInvisible())
        {
            return false;
        }
        if (sheep.isSheared())
        {
            return true;
        }
        SheepFurModel<Sheep> model;
        try
        {
            model = (SheepFurModel<Sheep>) MODEL.get(layer);
        }
        catch (IllegalAccessException e)
        {
            return false;
        }
        int rgb = ColorHandlers.vivid(Integer.parseInt(ColorStrings.ALL[color], 16));
        coloredCutoutModelCopyLayerRender(layer.getParentModel(), model, TEXTURE, pose, buffer, light, sheep, limbSwing, limbSwingAmount,
                ageInTicks, netHeadYaw, headPitch, partialTicks,
                (rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F);
        return true;
    }
}
