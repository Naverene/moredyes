package net.neverandy.moredyes.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;
import net.minecraft.world.entity.animal.Wolf;
import net.neverandy.moredyes.client.DyedCollarLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Draws the collar of a wolf with a MoreDyes collar color in that color (see client/DyedCollarLayers), in the vanilla
 * collar layer itself so the color survives renderers that other mods or OptiFine put in place of the vanilla one.
 *
 * <p>The method is named twice: by its Mojang name for the dev game, and by its SRG name for the released game, since
 * this mod's build makes no mixin refmap.
 */
@Mixin(WolfCollarLayer.class)
abstract class WolfCollarLayerMixin
{
    @Inject(method = {
            "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Wolf;FFFFFF)V",
            "m_6494_(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Wolf;FFFFFF)V"},
            at = @At("HEAD"), cancellable = true, remap = false, require = 1)
    private void moredyes$dyedCollar(PoseStack pose, MultiBufferSource buffer, int light, Wolf wolf, float limbSwing, float limbSwingAmount,
                                     float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci)
    {
        if (DyedCollarLayers.renderWolf((WolfCollarLayer) (Object) this, pose, buffer, light, wolf))
        {
            ci.cancel();
        }
    }
}
