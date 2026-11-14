package net.neverandy.moredyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.entity.state.SheepRenderState;

import net.neverandy.moredyes.client.DyedSheepRenderState;
import net.neverandy.moredyes.client.Tints;
import net.neverandy.moredyes.color.MixColor;

/**
 * Keeps the More Dyes color of the sheep being drawn (set by SheepRendererMixin), and draws its wool in that color,
 * unless it is a rainbow "jeb_" sheep.
 */
@Mixin(SheepRenderState.class)
abstract class SheepRenderStateMixin implements DyedSheepRenderState {

    @Unique
    private MixColor moredyes$color;

    @Override
    public MixColor moredyes$getColor() {
        return moredyes$color;
    }

    @Override
    public void moredyes$setColor(MixColor color) {
        moredyes$color = color;
    }

    @Inject(method = "getWoolColor", at = @At("HEAD"), cancellable = true)
    private void moredyes$dyedWool(CallbackInfoReturnable<Integer> cir) {
        SheepRenderState state = (SheepRenderState) (Object) this;
        if (moredyes$color != null && !state.isJebSheep) {
            cir.setReturnValue(Tints.argb(moredyes$color));
        }
    }
}
