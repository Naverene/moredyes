package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.banner.BannerFlagModel;
import net.minecraft.client.model.object.banner.BannerModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BannerRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.phys.Vec3;

/**
 * Draws every banner, vanilla and dyed. It replaces vanilla's renderer for the banner block entity, which dyed banners
 * share, and draws a dyed banner's base in its color; its patterns are drawn like vanilla's. Vanilla's renderer takes
 * the base color as a dye color, so the drawing is copied here with the color as a number.
 */
public class DyedBannerRenderer extends BannerRenderer {

    private final SpriteGetter sprites;
    private final BannerModel standingModel;
    private final BannerModel wallModel;
    private final BannerFlagModel standingFlagModel;
    private final BannerFlagModel wallFlagModel;

    public DyedBannerRenderer(BlockEntityRendererProvider.Context context) {
        this(context.entityModelSet(), context.sprites());
    }

    public DyedBannerRenderer(EntityModelSet models, SpriteGetter sprites) {
        super(models, sprites);
        this.sprites = sprites;
        this.standingModel = new BannerModel(models.bakeLayer(ModelLayers.STANDING_BANNER));
        this.wallModel = new BannerModel(models.bakeLayer(ModelLayers.WALL_BANNER));
        this.standingFlagModel = new BannerFlagModel(models.bakeLayer(ModelLayers.STANDING_BANNER_FLAG));
        this.wallFlagModel = new BannerFlagModel(models.bakeLayer(ModelLayers.WALL_BANNER_FLAG));
    }

    @Override
    public BannerRenderState createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(BannerBlockEntity banner, BannerRenderState state, float partialTicks,
        Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(banner, state, partialTicks, cameraPosition, breakProgress);
        ((State) state).tint = Tints.blockTint(banner.getBlockState().getBlock());
    }

    @Override
    public void submit(BannerRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
        CameraRenderState camera) {
        int tint = state instanceof State dyed ? dyed.tint : -1;
        if (tint == -1) {
            super.submit(state, poseStack, collector, camera);
            return;
        }
        poseStack.pushPose();
        poseStack.mulPose(state.transformation);
        submitBanner(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.attachmentType,
            state.phase, tint, state.patterns, state.breakProgress, 0);
        poseStack.popPose();
    }

    /** Vanilla's BannerRenderer.submitBanner and submitPatterns, with the base layer drawn in {@code tint}. */
    public void submitBanner(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords,
        BannerBlock.AttachmentType attachment, float phase, int tint, BannerPatternLayers patterns,
        ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, int outlineColor) {
        BannerModel model = attachment == BannerBlock.AttachmentType.WALL ? wallModel : standingModel;
        BannerFlagModel flagModel = attachment == BannerBlock.AttachmentType.WALL ? wallFlagModel : standingFlagModel;
        SpriteId base = Sheets.BANNER_BASE;
        collector.submitModel(model, Unit.INSTANCE, poseStack, lightCoords, overlayCoords, -1, base, sprites,
            outlineColor, breakProgress);
        collector.submitModel(flagModel, phase, poseStack, lightCoords, overlayCoords, -1, base, sprites,
            outlineColor, breakProgress);
        submitLayer(poseStack, collector, lightCoords, overlayCoords, flagModel, phase, Sheets.BANNER_PATTERN_BASE, tint,
            breakProgress);
        for (int i = 0; i < 16 && i < patterns.layers().size(); i++) {
            BannerPatternLayers.Layer layer = patterns.layers().get(i);
            submitLayer(poseStack, collector, lightCoords, overlayCoords, flagModel, phase,
                Sheets.getBannerSprite(layer.pattern()), layer.color().getTextureDiffuseColor(), null);
        }
    }

    private void submitLayer(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords,
        BannerFlagModel flagModel, float phase, SpriteId sprite, int color,
        ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        collector.submitModel(flagModel, phase, poseStack, sprite.renderType(RenderTypes::bannerPattern), lightCoords,
            overlayCoords, color, sprites.get(sprite), 0, breakProgress);
    }

    public static class State extends BannerRenderState {
        public int tint = -1;
    }
}
