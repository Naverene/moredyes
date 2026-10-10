package net.neverandy.moredyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.world.entity.animal.Sheep;

import net.neverandy.moredyes.client.Tints;
import net.neverandy.moredyes.entity.DyedSheep;

/**
 * Draws the wool of a sheep with a More Dyes color in that color. Vanilla takes the wool color from the sheep's
 * vanilla color with {@code Sheep.getColor(DyeColor)}; the rainbow "jeb_" sheep takes two colors the same way, so the
 * color is only replaced at the last of the three calls, the one for an ordinary sheep.
 */
@Mixin(SheepFurLayer.class)
abstract class SheepFurLayerMixin {

    @ModifyExpressionValue(
        method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Sheep;FFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/Sheep;getColor(Lnet/minecraft/world/item/DyeColor;)I",
            ordinal = 2))
    private int moredyes$dyedWool(int color, @Local(argsOnly = true) Sheep sheep) {
        return DyedSheep.getColor(sheep).map(Tints::argb).orElse(color);
    }
}
