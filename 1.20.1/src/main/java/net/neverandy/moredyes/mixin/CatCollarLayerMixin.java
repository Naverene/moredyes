package net.neverandy.moredyes.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.world.entity.animal.Cat;
import net.neverandy.moredyes.client.DyedCollarLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Draws the collar of a cat with a MoreDyes collar color in that color; see WolfCollarLayerMixin. */
@Mixin(CatCollarLayer.class)
abstract class CatCollarLayerMixin
{
    @Inject(method = {
            "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Cat;FFFFFF)V",
            "m_6494_(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Cat;FFFFFF)V"},
            at = @At("HEAD"), cancellable = true, remap = false, require = 1)
    private void moredyes$dyedCollar(PoseStack pose, MultiBufferSource buffer, int light, Cat cat, float limbSwing, float limbSwingAmount,
                                     float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci)
    {
        if (DyedCollarLayers.renderCat((CatCollarLayer) (Object) this, pose, buffer, light, cat, limbSwing, limbSwingAmount, partialTicks,
                ageInTicks, netHeadYaw, headPitch))
        {
            ci.cancel();
        }
    }
}
