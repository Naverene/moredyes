package net.neverandy.moredyes.client;

import net.neverandy.moredyes.color.MixColor;

/**
 * Added to vanilla's WolfRenderState and CatRenderState by CollarRenderStateMixin: the More Dyes collar color of the
 * pet being drawn.
 */
public interface DyedCollarRenderState {

    MixColor moredyes$getCollarColor();

    void moredyes$setCollarColor(MixColor color);
}
