package net.neverandy.moredyes.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CatModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cat;
import net.neverandy.moredyes.client.ColorHandlers;
import net.neverandy.moredyes.entity.DyedCollars;
import net.neverandy.moredyes.reference.ColorStrings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Draws the collar of a cat with a MoreDyes collar color in that color; see WolfCollarLayerMixin. */
@Mixin(CatCollarLayer.class)
abstract class CatCollarLayerMixin extends RenderLayer<Cat, CatModel<Cat>>
{
    @Unique
    private static final ResourceLocation MOREDYES$TEXTURE = new ResourceLocation("textures/entity/cat/cat_collar.png");

    @Shadow
    @Final
    private CatModel<Cat> catModel;

    private CatCollarLayerMixin(RenderLayerParent<Cat, CatModel<Cat>> parent)
    {
        super(parent);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Cat;FFFFFF)V",
            at = @At("HEAD"), cancellable = true)
    private void moredyes$dyedCollar(PoseStack pose, MultiBufferSource buffer, int light, Cat cat, float limbSwing, float limbSwingAmount,
                                     float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci)
    {
        int color = DyedCollars.getColor(cat);
        if (color < 0)
        {
            return;
        }
        if (cat.isTame())
        {
            int rgb = ColorHandlers.vivid(Integer.parseInt(ColorStrings.ALL[color], 16));
            coloredCutoutModelCopyLayerRender(getParentModel(), catModel, MOREDYES$TEXTURE, pose, buffer, light, cat, limbSwing,
                    limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks,
                    (rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F);
        }
        ci.cancel();
    }
}
