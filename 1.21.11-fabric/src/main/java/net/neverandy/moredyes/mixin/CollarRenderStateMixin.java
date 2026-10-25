package net.neverandy.moredyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.client.renderer.entity.state.WolfRenderState;

import net.neverandy.moredyes.client.DyedCollarRenderState;
import net.neverandy.moredyes.color.MixColor;

/** Keeps the More Dyes collar color of the wolf or cat being drawn (set by WolfRendererMixin and CatRendererMixin). */
@Mixin({WolfRenderState.class, CatRenderState.class})
abstract class CollarRenderStateMixin implements DyedCollarRenderState {

    @Unique
    private MixColor moredyes$collarColor;

    @Override
    public MixColor moredyes$getCollarColor() {
        return moredyes$collarColor;
    }

    @Override
    public void moredyes$setCollarColor(MixColor color) {
        moredyes$collarColor = color;
    }
}
