package net.neverandy.moredyes.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SheepFurModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.world.entity.animal.Sheep;
import net.neverandy.moredyes.client.DyedSheepWool;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Draws the wool of a sheep with a MoreDyes color in that color (see client/DyedSheepWool), in the vanilla wool layer
 * itself so the color survives renderers or wool models that other mods put in place of the vanilla ones.
 */
@Mixin(SheepFurLayer.class)
abstract class SheepFurLayerMixin extends RenderLayer<Sheep, SheepModel<Sheep>>
{
    @Shadow
    @Final
    private SheepFurModel<Sheep> model;

    private SheepFurLayerMixin(RenderLayerParent<Sheep, SheepModel<Sheep>> parent)
    {
        super(parent);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/Sheep;FFFFFF)V",
            at = @At("HEAD"), cancellable = true)
    private void moredyes$dyedWool(PoseStack pose, MultiBufferSource buffer, int light, Sheep sheep, float limbSwing, float limbSwingAmount,
                                   float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci)
    {
        if (DyedSheepWool.render(getParentModel(), model, pose, buffer, light, sheep, limbSwing, limbSwingAmount, partialTicks,
                ageInTicks, netHeadYaw, headPitch))
        {
            ci.cancel();
        }
    }
}
