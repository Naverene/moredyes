package net.neverandy.moredyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.client.renderer.entity.state.WolfRenderState;

import net.neverandy.moredyes.client.MoreDyesClient;
import net.neverandy.moredyes.client.Tints;
import net.neverandy.moredyes.color.MixColor;

/** Draws the collar of a wolf with a More Dyes collar color (see {@code entity/DyedCollars}) in that color. */
@Mixin(WolfCollarLayer.class)
abstract class WolfCollarLayerMixin {

    @ModifyExpressionValue(
        method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/WolfRenderState;FF)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/DyeColor;getTextureDiffuseColor()I"))
    private int moredyes$dyedCollar(int color, @Local(argsOnly = true) WolfRenderState state) {
        MixColor dyed = state.getRenderData(MoreDyesClient.COLLAR_COLOR);
        return dyed == null ? color : Tints.argb(dyed);
    }
}
