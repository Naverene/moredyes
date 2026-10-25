package net.neverandy.moredyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.world.entity.animal.wolf.Wolf;

import net.neverandy.moredyes.client.DyedCollarRenderState;
import net.neverandy.moredyes.entity.DyedCollars;

/** Copies a wolf's More Dyes collar color into its render state, for WolfCollarLayerMixin. */
@Mixin(WolfRenderer.class)
abstract class WolfRendererMixin {

    @Inject(
        method = "extractRenderState(Lnet/minecraft/world/entity/animal/wolf/Wolf;Lnet/minecraft/client/renderer/entity/state/WolfRenderState;F)V",
        at = @At("TAIL"))
    private void moredyes$collarColor(Wolf wolf, WolfRenderState state, float partialTicks, CallbackInfo ci) {
        ((DyedCollarRenderState) state).moredyes$setCollarColor(DyedCollars.getColor(wolf).orElse(null));
    }
}
