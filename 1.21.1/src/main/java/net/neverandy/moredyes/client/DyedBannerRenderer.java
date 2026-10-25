package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;

/**
 * Draws every banner, vanilla and dyed. It replaces vanilla's renderer for the banner block entity, which dyed banners
 * share, and draws a dyed banner's base in its color; its patterns are drawn like vanilla's. Vanilla's renderer takes
 * the base color as a dye color, so the drawing is copied here with the color as a number.
 */
public class DyedBannerRenderer extends BannerRenderer {

    private final ModelPart flag;
    private final ModelPart pole;
    private final ModelPart bar;

    public DyedBannerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        ModelPart root = context.bakeLayer(ModelLayers.BANNER);
        this.flag = root.getChild("flag");
        this.pole = root.getChild("pole");
        this.bar = root.getChild("bar");
    }

    @Override
    public void render(BannerBlockEntity banner, float partialTicks, PoseStack pose, MultiBufferSource buffer,
        int light, int overlay) {
        int tint = Tints.blockTint(banner.getBlockState().getBlock());
        if (tint == -1) {
            super.render(banner, partialTicks, pose, buffer, light, overlay);
            return;
        }
        boolean item = banner.getLevel() == null;
        long time;
        pose.pushPose();
        if (item) {
            time = 0L;
            pose.translate(0.5F, 0.5F, 0.5F);
            pole.visible = true;
        } else {
            time = banner.getLevel().getGameTime();
            BlockState state = banner.getBlockState();
            if (state.getBlock() instanceof BannerBlock) {
                pose.translate(0.5F, 0.5F, 0.5F);
                pose.mulPose(Axis.YP.rotationDegrees(-RotationSegment.convertToDegrees(state.getValue(BannerBlock.ROTATION))));
                pole.visible = true;
            } else {
                pose.translate(0.5F, -0.16666667F, 0.5F);
                pose.mulPose(Axis.YP.rotationDegrees(-state.getValue(WallBannerBlock.FACING).toYRot()));
                pose.translate(0.0F, -0.3125F, -0.4375F);
                pole.visible = false;
            }
        }
        pose.pushPose();
        pose.scale(0.6666667F, -0.6666667F, -0.6666667F);
        var consumer = ModelBakery.BANNER_BASE.buffer(buffer, RenderType::entitySolid);
        pole.render(pose, consumer, light, overlay);
        bar.render(pose, consumer, light, overlay);
        BlockPos pos = banner.getBlockPos();
        float phase = ((float) Math.floorMod((long) (pos.getX() * 7 + pos.getY() * 9 + pos.getZ() * 13) + time, 100L)
            + partialTicks) / 100.0F;
        flag.xRot = (-0.0125F + 0.01F * Mth.cos((float) (Math.PI * 2) * phase)) * (float) Math.PI;
        flag.y = -32.0F;
        renderFlag(pose, buffer, light, overlay, flag, tint, banner.getPatterns());
        pose.popPose();
        pose.popPose();
    }

    /** Vanilla's BannerRenderer.renderPatterns for a banner, with the base layer drawn in {@code tint}. */
    private static void renderFlag(PoseStack pose, MultiBufferSource buffer, int light, int overlay, ModelPart flag,
        int tint, BannerPatternLayers patterns) {
        flag.render(pose, ModelBakery.BANNER_BASE.buffer(buffer, RenderType::entitySolid), light, overlay);
        renderLayer(pose, buffer, light, overlay, flag, Sheets.BANNER_BASE, tint);
        for (int i = 0; i < 16 && i < patterns.layers().size(); i++) {
            BannerPatternLayers.Layer layer = patterns.layers().get(i);
            renderLayer(pose, buffer, light, overlay, flag, Sheets.getBannerMaterial(layer.pattern()),
                layer.color().getTextureDiffuseColor());
        }
    }

    private static void renderLayer(PoseStack pose, MultiBufferSource buffer, int light, int overlay, ModelPart flag,
        Material material, int color) {
        flag.render(pose, material.buffer(buffer, RenderType::entityNoOutline), light, overlay, color);
    }
}
