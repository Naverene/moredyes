package net.neverandy.moredyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.world.entity.animal.sheep.Sheep;

import net.neverandy.moredyes.client.DyedSheepRenderState;
import net.neverandy.moredyes.entity.DyedSheep;

/** Hands a sheep's More Dyes color to its render state, as the NeoForge build's render state modifier does. */
@Mixin(SheepRenderer.class)
abstract class SheepRendererMixin {

    @Inject(
        method = "extractRenderState(Lnet/minecraft/world/entity/animal/sheep/Sheep;Lnet/minecraft/client/renderer/entity/state/SheepRenderState;F)V",
        at = @At("TAIL"))
    private void moredyes$color(Sheep sheep, SheepRenderState state, float partialTicks, CallbackInfo ci) {
        ((DyedSheepRenderState) state).moredyes$setColor(DyedSheep.getColor(sheep).orElse(null));
    }
}
