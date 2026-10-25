package net.neverandy.moredyes.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.entity.passive.SheepEntity;
import net.neverandy.moredyes.client.DyedSheepWool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Draws the wool of a sheep with a MoreDyes color in that color (see client/DyedSheepWool), in the vanilla wool layer
 * itself so the color survives renderers that other mods or OptiFine put in place of the vanilla sheep renderer.
 *
 * <p>The method is named twice: by its MCP name for the dev game, and by its SRG name for the released game, since
 * this mod's build makes no mixin refmap.
 */
@Mixin(SheepWoolLayer.class)
abstract class SheepWoolLayerMixin
{
    @Inject(method = {
            "render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/entity/passive/SheepEntity;FFFFFF)V",
            "func_225628_a_(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/entity/passive/SheepEntity;FFFFFF)V"},
            at = @At("HEAD"), cancellable = true, remap = false, require = 1)
    private void moredyes$dyedWool(MatrixStack matrix, IRenderTypeBuffer buffer, int light, SheepEntity sheep, float limbSwing,
                                   float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch,
                                   CallbackInfo ci)
    {
        if (DyedSheepWool.render((SheepWoolLayer) (Object) this, matrix, buffer, light, sheep, limbSwing, limbSwingAmount, partialTicks,
                ageInTicks, netHeadYaw, headPitch))
        {
            ci.cancel();
        }
    }
}
