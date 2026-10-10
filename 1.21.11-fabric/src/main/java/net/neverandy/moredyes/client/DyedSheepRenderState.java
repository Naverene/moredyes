package net.neverandy.moredyes.client;

import net.neverandy.moredyes.color.MixColor;

/** Added to vanilla's SheepRenderState by SheepRenderStateMixin: the More Dyes color of the sheep being drawn. */
public interface DyedSheepRenderState {

    MixColor moredyes$getColor();

    void moredyes$setColor(MixColor color);
}
