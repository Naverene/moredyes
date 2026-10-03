package net.neverandy.moredyes.compat.ironchest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.progwml6.ironchest.client.IronChestsClientRegistration;
import com.progwml6.ironchest.client.model.IronChestModel;
import com.progwml6.ironchest.client.renderer.IronChestRenderer;
import com.progwml6.ironchest.common.block.regular.AbstractIronChestBlock;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.neverandy.moredyes.client.Tints;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.compat.ironchest.DyedIronChestBlock;
import net.neverandy.moredyes.compat.ironchest.DyedIronChestBlockEntity;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;

/**
 * Draws a dyed Iron Chests chest: Iron Chests' own chest model, once with the grey wood texture multiplied by the dye
 * color and once with the metal, latch and inside in their own colors (see DyedIronChestTextures).
 */
public class DyedIronChestRenderer implements BlockEntityRenderer<DyedIronChestBlockEntity, DyedIronChestRenderer.State> {

    private final IronChestModel model;

    public DyedIronChestRenderer(BlockEntityRendererProvider.Context context) {
        this(context.entityModelSet());
    }

    public DyedIronChestRenderer(EntityModelSet models) {
        this.model = new IronChestModel(models.bakeLayer(IronChestsClientRegistration.IRON_CHEST));
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
        state.facing = blockState.getValue(AbstractIronChestBlock.FACING);
        state.color = blockState.hasProperty(DyedIronChestBlock.COLOR) ? blockState.getValue(DyedIronChestBlock.COLOR) : 0;
        state.open = chest.getOpenNess(partialTicks);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(IronChestRenderer.modelTransformation(state.facing));
        float open = 1.0F - state.open;
        open = 1.0F - open * open * open;
        submit(model, state.tier, state.color, open, poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY,
            0, state.breakProgress);
        poseStack.popPose();
    }

    /** Draws the chest model in a color: the tinted wood, then the trim over it. */
    static void submit(IronChestModel model, IronChestCompat.Tier tier, int color, float open, PoseStack poseStack,
        SubmitNodeCollector collector, int light, int overlay, int outline,
        ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        int tint = Tints.argb(MixColors.ALL.get(color));
        collector.submitModel(model, open, poseStack, RenderTypes.entityCutout(DyedIronChestTextures.wood(tier)), light,
            overlay, tint, null, outline, breakProgress);
        collector.submitModel(model, open, poseStack, RenderTypes.entityCutout(DyedIronChestTextures.trim(tier)), light,
            overlay, -1, null, outline, null);
    }

    @Override
    public AABB getRenderBoundingBox(DyedIronChestBlockEntity chest) {
        return AABB.encapsulatingFullBlocks(chest.getBlockPos().offset(-1, 0, -1), chest.getBlockPos().offset(1, 1, 1));
    }

    public static class State extends BlockEntityRenderState {
        IronChestCompat.Tier tier = IronChestCompat.Tier.IRON;
        Direction facing = Direction.SOUTH;
        int color;
        float open;
    }
}
