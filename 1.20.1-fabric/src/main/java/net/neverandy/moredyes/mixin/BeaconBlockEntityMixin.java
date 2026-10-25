package net.neverandy.moredyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;

import net.neverandy.moredyes.block.DyedBeaconBeamBlock;

/**
 * Colors a beacon beam in the exact color of the More Dyes glass it passes through. Vanilla takes the color from
 * {@code BeaconBeamBlock.getColor()}, which can only be one of the 16 vanilla colors.
 */
@Mixin(BeaconBlockEntity.class)
abstract class BeaconBlockEntityMixin
{
    @ModifyExpressionValue(
        method = "tick",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/DyeColor;getTextureDiffuseColors()[F"))
    private static float[] moredyes$dyedGlassColor(float[] color, @Local Block block)
    {
        return block instanceof DyedBeaconBeamBlock dyed ? dyed.getBeaconColor() : color;
    }
}
