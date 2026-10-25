package net.neverandy.moredyes.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.world.entity.animal.Sheep;
import net.neverandy.moredyes.client.DyedSheepWool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Draws the wool of a sheep with a MoreDyes color in that color (see client/DyedSheepWool), in the vanilla wool layer
 * itself so the color survives renderers that other mods or OptiFine put in place of the vanilla sheep renderer.
 *
 * <p>The method is named twice: by its Mojang name for the dev game, and by its SRG name for the released game, since
 * this mod's build makes no mixin refmap.
 */
@Mixin(SheepFurLayer.class)
abstract class SheepFurLayerMixin
{
    @Inject(method = {
            "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Sheep;FFFFFF)V",
            "m_6494_(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Sheep;FFFFFF)V"},
            at = @At("HEAD"), cancellable = true, remap = false, require = 1)
    private void moredyes$dyedWool(PoseStack pose, MultiBufferSource buffer, int light, Sheep sheep, float limbSwing, float limbSwingAmount,
                                   float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci)
    {
        if (DyedSheepWool.render((SheepFurLayer) (Object) this, pose, buffer, light, sheep, limbSwing, limbSwingAmount, partialTicks,
                ageInTicks, netHeadYaw, headPitch))
        {
            ci.cancel();
        }
    }
}
