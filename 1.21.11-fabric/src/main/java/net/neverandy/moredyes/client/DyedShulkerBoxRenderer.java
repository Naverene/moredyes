package net.neverandy.moredyes.client;

import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;

import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
import net.minecraft.client.renderer.blockentity.state.ShulkerBoxRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.phys.Vec3;

import net.neverandy.moredyes.MoreDyes;

/**
 * Draws every shulker box, vanilla and dyed. It replaces vanilla's renderer for the shulker box block entity, which
 * dyed boxes share, and draws a dyed box with the grey texture {@code textures/entity/shulker/dyed.png} tinted with
 * its color. It follows the vanilla renderer, which has no way to pass a tint.
 */
public class DyedShulkerBoxRenderer extends ShulkerBoxRenderer {

    public static final Material MATERIAL = Sheets.SHULKER_MAPPER.apply(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed"));

    private final MaterialSet materials;
    private final BoxModel model;

    public DyedShulkerBoxRenderer(BlockEntityRendererProvider.Context context) {
        this(context.entityModelSet(), context.materials());
    }

    public DyedShulkerBoxRenderer(EntityModelSet models, MaterialSet materials) {
        super(models, materials);
        this.materials = materials;
        this.model = new BoxModel(models.bakeLayer(ModelLayers.SHULKER_BOX));
    }

    @Override
    public ShulkerBoxRenderState createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(ShulkerBoxBlockEntity box, ShulkerBoxRenderState state, float partialTicks,
        Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(box, state, partialTicks, cameraPosition, breakProgress);
        ((State) state).tint = Tints.blockTint(box.getBlockState().getBlock());
    }

    @Override
    public void submit(ShulkerBoxRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
        CameraRenderState camera) {
        int tint = state instanceof State dyed ? dyed.tint : -1;
        if (tint == -1) {
            super.submit(state, poseStack, collector, camera);
            return;
        }
        submitDyed(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.direction, state.progress,
            state.breakProgress, tint, 0);
    }

    /** Vanilla's submit, with the dyed texture and a tint. */
    public void submitDyed(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords,
        Direction direction, float progress, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, int tint,
        int outlineColor) {
        poseStack.pushPose();
        prepare(poseStack, direction, progress);
        collector.submitModel(model, progress, poseStack, MATERIAL.renderType(model::renderType), lightCoords,
            overlayCoords, tint, materials.get(MATERIAL), outlineColor, breakProgress);
        poseStack.popPose();
    }

    public void extents(Direction direction, float progress, Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        prepare(poseStack, direction, progress);
        model.root().getExtentsForGui(poseStack, output);
    }

    private void prepare(PoseStack poseStack, Direction direction, float progress) {
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.scale(0.9995F, 0.9995F, 0.9995F);
        poseStack.mulPose(direction.getRotation());
        poseStack.scale(1.0F, -1.0F, -1.0F);
        poseStack.translate(0.0F, -1.0F, 0.0F);
        model.setupAnim(progress);
    }

    public static class State extends ShulkerBoxRenderState {
        public int tint = -1;
    }

    /** Vanilla's ShulkerBoxRenderer.ShulkerBoxModel, which is not public. */
    private static class BoxModel extends Model<Float> {
        private final ModelPart lid;

        BoxModel(ModelPart root) {
            super(root, RenderTypes::entityCutoutNoCull);
            this.lid = root.getChild("lid");
        }

        @Override
        public void setupAnim(Float progress) {
            super.setupAnim(progress);
            lid.setPos(0.0F, 24.0F - progress * 0.5F * 16.0F, 0.0F);
            lid.yRot = 270.0F * progress * (float) (Math.PI / 180.0);
        }
    }
}
