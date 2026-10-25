package net.neverandy.moredyes.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import net.neverandy.moredyes.client.ColorHandlers;
import net.neverandy.moredyes.entity.DyedCollars;
import net.neverandy.moredyes.reference.ColorStrings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Draws the collar of a wolf with a MoreDyes collar color (see entity/DyedCollars) in that color, in the vanilla collar
 * layer itself so the color survives renderers that other mods put in place of the vanilla one.
 */
@Mixin(WolfCollarLayer.class)
abstract class WolfCollarLayerMixin extends RenderLayer<Wolf, WolfModel<Wolf>>
{
    @Unique
    private static final ResourceLocation MOREDYES$TEXTURE = new ResourceLocation("textures/entity/wolf/wolf_collar.png");

    private WolfCollarLayerMixin(RenderLayerParent<Wolf, WolfModel<Wolf>> parent)
    {
        super(parent);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Wolf;FFFFFF)V",
            at = @At("HEAD"), cancellable = true)
    private void moredyes$dyedCollar(PoseStack pose, MultiBufferSource buffer, int light, Wolf wolf, float limbSwing, float limbSwingAmount,
                                     float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci)
    {
        int color = DyedCollars.getColor(wolf);
        if (color < 0)
        {
            return;
        }
        if (wolf.isTame() && !wolf.isInvisible())
        {
            int rgb = ColorHandlers.vivid(Integer.parseInt(ColorStrings.ALL[color], 16));
            renderColoredCutoutModel(getParentModel(), MOREDYES$TEXTURE, pose, buffer, light, wolf,
                    (rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F);
        }
        ci.cancel();
    }
}
