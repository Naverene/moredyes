package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;

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
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.Vec3;

import net.neverandy.moredyes.MoreDyes;

/**
 * Draws every bed, vanilla and dyed. It replaces vanilla's renderer for the bed block entity, which dyed beds share,
 * and draws a dyed bed from two textures on the bed atlas: {@code textures/entity/bed/dyed.png}, the grey bedding
 * tinted with the bed's color, and {@code dyed_frame.png}, the wooden frame in its own colors. Each is clear where
 * the other is drawn, and both are drawn cut out, so neither covers the other whichever is drawn first.
 */
public class DyedBedRenderer extends BedRenderer {

    public static final SpriteId BEDDING = Sheets.BED_MAPPER.apply(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed"));
    public static final SpriteId FRAME = Sheets.BED_MAPPER.apply(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed_frame"));

    private final SpriteGetter sprites;
    private final Model.Simple headModel;
    private final Model.Simple footModel;

    public DyedBedRenderer(BlockEntityRendererProvider.Context context) {
        this(context.sprites(), context.entityModelSet());
    }

    public DyedBedRenderer(SpriteGetter sprites, EntityModelSet models) {
        super(sprites, models);
        this.sprites = sprites;
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
        poseStack.pushPose();
        poseStack.mulPose(modelTransform(state.facing));
        submitDyed(state.part, tint, poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY,
            state.breakProgress, 0);
        poseStack.popPose();
    }

    /** Vanilla's submitPiece, with the dyed textures and a tint. */
    public void submitDyed(BedPart part, int tint, PoseStack poseStack, SubmitNodeCollector collector,
        int lightCoords, int overlayCoords, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress,
        int outlineColor) {
        Model.Simple model = part == BedPart.HEAD ? headModel : footModel;
        collector.submitModel(model, Unit.INSTANCE, poseStack, BEDDING.renderType(RenderTypes::entityCutout),
            lightCoords, overlayCoords, tint, sprites.get(BEDDING), outlineColor, breakProgress);
        collector.submitModel(model, Unit.INSTANCE, poseStack, FRAME.renderType(RenderTypes::entityCutout),
            lightCoords, overlayCoords, -1, sprites.get(FRAME), 0, null);
    }

    public static class State extends BedRenderState {
        public int tint = -1;
    }
}
