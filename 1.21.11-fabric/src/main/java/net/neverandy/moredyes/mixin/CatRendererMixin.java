package net.neverandy.moredyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.world.entity.animal.feline.Cat;

import net.neverandy.moredyes.client.DyedCollarRenderState;
import net.neverandy.moredyes.entity.DyedCollars;

/** Copies a cat's More Dyes collar color into its render state, for CatCollarLayerMixin. */
@Mixin(CatRenderer.class)
abstract class CatRendererMixin {

    @Inject(
        method = "extractRenderState(Lnet/minecraft/world/entity/animal/feline/Cat;Lnet/minecraft/client/renderer/entity/state/CatRenderState;F)V",
        at = @At("TAIL"))
    private void moredyes$collarColor(Cat cat, CatRenderState state, float partialTicks, CallbackInfo ci) {
        ((DyedCollarRenderState) state).moredyes$setCollarColor(DyedCollars.getColor(cat).orElse(null));
    }
}
