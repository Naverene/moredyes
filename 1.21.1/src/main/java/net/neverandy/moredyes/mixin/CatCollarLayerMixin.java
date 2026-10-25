package net.neverandy.moredyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.world.entity.animal.Cat;

import net.neverandy.moredyes.client.Tints;
import net.neverandy.moredyes.entity.DyedCollars;

/** Draws the collar of a cat with a More Dyes collar color in that color (see {@link DyedCollars}). */
@Mixin(CatCollarLayer.class)
abstract class CatCollarLayerMixin {

    @ModifyExpressionValue(
        method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Cat;FFFFFF)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/DyeColor;getTextureDiffuseColor()I"))
    private int moredyes$dyedCollar(int color, @Local(argsOnly = true) Cat cat) {
        return DyedCollars.getColor(cat).map(Tints::argb).orElse(color);
    }
}
