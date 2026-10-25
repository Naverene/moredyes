package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BedRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.phys.Vec3;

import net.neverandy.moredyes.MoreDyes;

/**
 * Draws every bed, vanilla and dyed. It replaces vanilla's renderer for the bed block entity, which dyed beds share,
 * and draws a dyed bed from two textures on the bed atlas: {@code textures/entity/bed/dyed.png}, the grey bedding
 * tinted with the bed's color, and {@code dyed_frame.png}, the wooden frame in its own colors. Each is clear where
 * the other is drawn, and both are drawn cut out, so neither covers the other whichever is drawn first.
 */
public class DyedBedRenderer extends BedRenderer {

    public static final Material BEDDING = Sheets.BED_MAPPER.apply(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed"));
    public static final Material FRAME = Sheets.BED_MAPPER.apply(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed_frame"));

    private final MaterialSet materials;
    private final Model.Simple headModel;
    private final Model.Simple footModel;

    public DyedBedRenderer(BlockEntityRendererProvider.Context context) {
        this(context.materials(), context.entityModelSet());
    }

    public DyedBedRenderer(MaterialSet materials, EntityModelSet models) {
        super(materials, models);
        this.materials = materials;
        this.headModel = new Model.Simple(models.bakeLayer(ModelLayers.BED_HEAD), RenderTypes::entitySolid);
        this.footModel = new Model.Simple(models.bakeLayer(ModelLayers.BED_FOOT), RenderTypes::entitySolid);
    }

    @Override
    public BedRenderState createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(BedBlockEntity bed, BedRenderState state, float partialTicks, Vec3 cameraPosition,
        ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(bed, state, partialTicks, cameraPosition, breakProgress);
        ((State) state).tint = Tints.blockTint(bed.getBlockState().getBlock());
    }

    @Override
    public void submit(BedRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
        CameraRenderState camera) {
        int tint = state instanceof State dyed ? dyed.tint : -1;
        if (tint == -1) {
            super.submit(state, poseStack, collector, camera);
            return;
        }
        submitPiece(poseStack, collector, state.isHead ? headModel : footModel, state.facing, tint, state.lightCoords,
            OverlayTexture.NO_OVERLAY, false, state.breakProgress, 0);
    }

    /** Draws a dyed bed as an item, like vanilla's submitSpecial. */
    public void submitItem(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords,
        int tint, int outlineColor) {
        submitPiece(poseStack, collector, headModel, Direction.SOUTH, tint, lightCoords, overlayCoords, false, null,
            outlineColor);
        submitPiece(poseStack, collector, footModel, Direction.SOUTH, tint, lightCoords, overlayCoords, true, null,
            outlineColor);
    }

    private void submitPiece(PoseStack poseStack, SubmitNodeCollector collector, Model.Simple model,
        Direction facing, int tint, int lightCoords, int overlayCoords, boolean foot,
        ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, int outlineColor) {
        poseStack.pushPose();
        // Vanilla's BedRenderer.preparePose
        poseStack.translate(0.0F, 0.5625F, foot ? -1.0F : 0.0F);
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F + facing.toYRot()));
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        collector.submitModel(model, Unit.INSTANCE, poseStack, BEDDING.renderType(RenderTypes::entityCutout),
            lightCoords, overlayCoords, tint, materials.get(BEDDING), outlineColor, breakProgress);
        collector.submitModel(model, Unit.INSTANCE, poseStack, FRAME.renderType(RenderTypes::entityCutout),
            lightCoords, overlayCoords, -1, materials.get(FRAME), 0, null);
        poseStack.popPose();
    }

    public static class State extends BedRenderState {
        public int tint = -1;
    }
}
