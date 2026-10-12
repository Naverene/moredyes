package net.neverandy.moredyes.compat.ironchest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import net.neverandy.moredyes.client.Tints;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.compat.ironchest.DyedIronChestBlock;
import net.neverandy.moredyes.compat.ironchest.DyedIronChestBlockEntity;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;

/**
 * Draws a dyed Iron Chests chest: the vanilla chest model, which Iron Chests draws its own with, once with the grey
 * panel texture multiplied by the dye color and once with the frame, latch and inside in their own colors (see
 * DyedIronChestTextures).
 */
public class DyedIronChestRenderer implements BlockEntityRenderer<DyedIronChestBlockEntity, DyedIronChestRenderer.State> {

    private final ChestModel model;

    public DyedIronChestRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(DyedIronChestBlockEntity chest, State state, float partialTicks, Vec3 cameraPosition,
        ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(chest, state, partialTicks, cameraPosition, breakProgress);
        BlockState blockState = chest.getBlockState();
        state.tier = chest.tier();
        state.facing = blockState.getValue(ChestBlock.FACING);
        state.color = blockState.hasProperty(DyedIronChestBlock.COLOR) ? blockState.getValue(DyedIronChestBlock.COLOR) : 0;
        // Always a single chest, so it is opened on its own.
        state.open = chest.getOpenNess(partialTicks);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.facing.toYRot()));
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        float open = 1.0F - state.open;
        open = 1.0F - open * open * open;
        submit(model, state.tier, state.color, open, poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY,
            0, state.breakProgress);
        poseStack.popPose();
    }

    /** Draws the chest model in a color: the tinted panels, then the frame, latch and inside over them. */
    static void submit(ChestModel model, IronChestCompat.Tier tier, int color, float open, PoseStack poseStack,
        SubmitNodeCollector collector, int light, int overlay, int outline,
        ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        int tint = Tints.argb(MixColors.ALL.get(color));
        collector.submitModel(model, open, poseStack, RenderTypes.entityCutout(DyedIronChestTextures.panels(tier)), light,
            overlay, tint, null, outline, breakProgress);
        collector.submitModel(model, open, poseStack, RenderTypes.entityCutout(DyedIronChestTextures.trim(tier)), light,
            overlay, -1, null, outline, null);
    }

    public static class State extends BlockEntityRenderState {
        IronChestCompat.Tier tier = IronChestCompat.Tier.IRON;
        Direction facing = Direction.SOUTH;
        int color;
        float open;
    }
}
