package net.neverandy.moredyes.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.entity.passive.CatEntity;
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
            "render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/entity/passive/CatEntity;FFFFFF)V",
            "func_225628_a_(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/entity/passive/CatEntity;FFFFFF)V"},
            at = @At("HEAD"), cancellable = true, remap = false, require = 1)
    private void moredyes$dyedCollar(MatrixStack matrix, IRenderTypeBuffer buffer, int light, CatEntity cat, float limbSwing,
                                     float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch,
                                     CallbackInfo ci)
    {
        if (DyedCollarLayers.renderCat((CatCollarLayer) (Object) this, matrix, buffer, light, cat, limbSwing, limbSwingAmount, partialTicks,
                ageInTicks, netHeadYaw, headPitch))
        {
            ci.cancel();
        }
    }
}
