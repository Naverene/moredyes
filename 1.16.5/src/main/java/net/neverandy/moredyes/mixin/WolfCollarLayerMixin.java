package net.neverandy.moredyes.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.entity.passive.WolfEntity;
import net.neverandy.moredyes.client.DyedCollarLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Draws the collar of a wolf with a MoreDyes collar color in that color (see client/DyedCollarLayers), in the vanilla
 * collar layer itself so the color survives renderers that other mods or OptiFine put in place of the vanilla one.
 *
 * <p>The method is named twice: by its MCP name for the dev game, and by its SRG name for the released game, since
 * this mod's build makes no mixin refmap.
 */
@Mixin(WolfCollarLayer.class)
abstract class WolfCollarLayerMixin
{
    @Inject(method = {
            "render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/entity/passive/WolfEntity;FFFFFF)V",
            "func_225628_a_(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/entity/passive/WolfEntity;FFFFFF)V"},
            at = @At("HEAD"), cancellable = true, remap = false, require = 1)
    private void moredyes$dyedCollar(MatrixStack matrix, IRenderTypeBuffer buffer, int light, WolfEntity wolf, float limbSwing,
                                     float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch,
                                     CallbackInfo ci)
    {
        if (DyedCollarLayers.renderWolf((WolfCollarLayer) (Object) this, matrix, buffer, light, wolf))
        {
            ci.cancel();
        }
    }
}
