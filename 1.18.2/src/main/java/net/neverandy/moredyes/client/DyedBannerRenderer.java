package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Vector3f;
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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Draws every banner, vanilla and dyed. It replaces vanilla's renderer for the banner block entity, which dyed banners
 * share, and draws a dyed banner's base in its color; its patterns are drawn like vanilla's. Vanilla's renderer takes
 * the base color as a dye color, so the drawing is copied here with the color as a number.
 */
public class DyedBannerRenderer extends BannerRenderer
{
    private final ModelPart flag;
    private final ModelPart pole;
    private final ModelPart bar;

    public DyedBannerRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
        ModelPart root = context.bakeLayer(ModelLayers.BANNER);
        this.flag = root.getChild("flag");
        this.pole = root.getChild("pole");
        this.bar = root.getChild("bar");
    }

    @Override
    public void render(BannerBlockEntity banner, float partialTicks, PoseStack pose, MultiBufferSource buffer, int light, int overlay)
    {
        int tint = ColorHandlers.tintOf(banner.getBlockState().getBlock());
        if (tint == -1)
        {
            super.render(banner, partialTicks, pose, buffer, light, overlay);
            return;
        }
        boolean item = banner.getLevel() == null;
        long time;
        pose.pushPose();
        if (item)
        {
            time = 0L;
            pose.translate(0.5F, 0.5F, 0.5F);
            pole.visible = true;
        }
        else
        {
            time = banner.getLevel().getGameTime();
            BlockState state = banner.getBlockState();
            if (state.getBlock() instanceof BannerBlock)
            {
                pose.translate(0.5F, 0.5F, 0.5F);
                pose.mulPose(Vector3f.YP.rotationDegrees(-(float) (state.getValue(BannerBlock.ROTATION) * 360) / 16.0F));
                pole.visible = true;
            }
            else
            {
                pose.translate(0.5F, -0.16666667F, 0.5F);
                pose.mulPose(Vector3f.YP.rotationDegrees(-state.getValue(WallBannerBlock.FACING).toYRot()));
                pose.translate(0.0F, -0.3125F, -0.4375F);
                pole.visible = false;
            }
        }
        pose.pushPose();
        pose.scale(0.6666667F, -0.6666667F, -0.6666667F);
        VertexConsumer consumer = ModelBakery.BANNER_BASE.buffer(buffer, RenderType::entitySolid);
        pole.render(pose, consumer, light, overlay);
        bar.render(pose, consumer, light, overlay);
        BlockPos pos = banner.getBlockPos();
        float phase = ((float) Math.floorMod(pos.getX() * 7 + pos.getY() * 9 + pos.getZ() * 13 + time, 100L) + partialTicks) / 100.0F;
        flag.xRot = (-0.0125F + 0.01F * Mth.cos((float) (Math.PI * 2) * phase)) * (float) Math.PI;
        flag.y = -32.0F;
        renderFlag(pose, buffer, light, overlay, flag, tint, banner.getPatterns());
        pose.popPose();
        pose.popPose();
    }

    /**
     * Vanilla's BannerRenderer.renderPatterns for a banner, with the base layer drawn in {@code tint}. The first entry
     * of the patterns is the base in the banner's dye color (white for a dyed banner), so it is left out.
     */
    private static void renderFlag(PoseStack pose, MultiBufferSource buffer, int light, int overlay, ModelPart flag, int tint,
            List<Pair<BannerPattern, DyeColor>> patterns)
    {
        flag.render(pose, ModelBakery.BANNER_BASE.buffer(buffer, RenderType::entitySolid), light, overlay);
        renderLayer(pose, buffer, light, overlay, flag, Sheets.getBannerMaterial(BannerPattern.BASE),
                (tint >> 16 & 255) / 255.0F, (tint >> 8 & 255) / 255.0F, (tint & 255) / 255.0F);
        for (int i = 1; i < 17 && i < patterns.size(); i++)
        {
            Pair<BannerPattern, DyeColor> layer = patterns.get(i);
            float[] color = layer.getSecond().getTextureDiffuseColors();
            renderLayer(pose, buffer, light, overlay, flag, Sheets.getBannerMaterial(layer.getFirst()), color[0], color[1], color[2]);
        }
    }

    private static void renderLayer(PoseStack pose, MultiBufferSource buffer, int light, int overlay, ModelPart flag,
            Material material, float red, float green, float blue)
    {
        flag.render(pose, material.buffer(buffer, RenderType::entityNoOutline), light, overlay, red, green, blue, 1.0F);
    }
}
