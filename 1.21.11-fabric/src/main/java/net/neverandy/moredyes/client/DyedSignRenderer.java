package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.blockentity.state.SignRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.Vec3;

import net.neverandy.moredyes.MoreDyes;

/**
 * Draws every sign, vanilla and dyed. It replaces vanilla's renderer for the sign block entity, which dyed signs share,
 * and draws a dyed sign with the grey texture {@code textures/entity/signs/dyed.png} tinted with its color.
 */
public class DyedSignRenderer extends SignRenderer {

    /** The grey sign texture, on the vanilla sign atlas. */
    public static final Material MATERIAL = Sheets.SIGN_MAPPER.apply(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed"));

    private final MaterialSet materials;
    /** The tint of the sign being submitted: vanilla's submit does not pass its render state on to submitSign. */
    private int tint = -1;

    public DyedSignRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.materials = context.materials();
    }

    @Override
    public SignRenderState createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(SignBlockEntity sign, SignRenderState state, float partialTicks,
        Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(sign, state, partialTicks, cameraPosition, breakProgress);
        ((State) state).tint = Tints.blockTint(sign.getBlockState().getBlock());
    }

    @Override
    public void submit(SignRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
        CameraRenderState camera) {
        tint = state instanceof State dyed ? dyed.tint : -1;
        try {
            super.submit(state, poseStack, collector, camera);
        } finally {
            tint = -1;
        }
    }

    @Override
    protected void submitSign(PoseStack poseStack, int lightCoords, WoodType type, Model.Simple model,
        ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, SubmitNodeCollector collector) {
        if (tint == -1) {
            super.submitSign(poseStack, lightCoords, type, model, breakProgress, collector);
            return;
        }
        poseStack.pushPose();
        float scale = getSignModelRenderScale();
        poseStack.scale(scale, -scale, -scale);
        collector.submitModel(model, Unit.INSTANCE, poseStack, MATERIAL.renderType(model::renderType), lightCoords,
            OverlayTexture.NO_OVERLAY, tint, materials.get(MATERIAL), 0, breakProgress);
        poseStack.popPose();
    }

    public static class State extends SignRenderState {
        public int tint = -1;
    }
}
