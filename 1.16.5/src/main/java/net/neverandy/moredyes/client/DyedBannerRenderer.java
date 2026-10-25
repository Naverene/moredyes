package net.neverandy.moredyes.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBannerBlock;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.BannerTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

import java.util.List;

/**
 * Draws every banner, vanilla and dyed. It replaces vanilla's renderer for the banner tile entity, which dyed banners
 * share, and draws a dyed banner's base in its color; its patterns are drawn like vanilla's. Vanilla's renderer takes
 * the base color as a dye color, so the drawing is copied here with the color as a number.
 */
public class DyedBannerRenderer extends BannerTileEntityRenderer
{
    private final ModelRenderer flag = BannerTileEntityRenderer.getModelRender();
    private final ModelRenderer pole = new ModelRenderer(64, 64, 44, 0);
    private final ModelRenderer bar;

    public DyedBannerRenderer(TileEntityRendererDispatcher dispatcher)
    {
        super(dispatcher);
        pole.addBox(-1.0F, -30.0F, -1.0F, 2.0F, 42.0F, 2.0F, 0.0F);
        bar = new ModelRenderer(64, 64, 0, 42);
        bar.addBox(-10.0F, -32.0F, -1.0F, 20.0F, 2.0F, 2.0F, 0.0F);
    }

    @Override
    public void render(BannerTileEntity banner, float partialTicks, MatrixStack pose, IRenderTypeBuffer buffer, int light, int overlay)
    {
        int tint = banner.getWorld() == null ? -1 : ColorHandlers.tintOf(banner.getBlockState().getBlock());
        if (tint == -1)
        {
            super.render(banner, partialTicks, pose, buffer, light, overlay);
            return;
        }
        BlockState state = banner.getBlockState();
        pose.push();
        if (state.getBlock() instanceof BannerBlock)
        {
            pose.translate(0.5, 0.5, 0.5);
            pose.rotate(Vector3f.YP.rotationDegrees(-state.get(BannerBlock.ROTATION) * 360 / 16.0F));
            pole.showModel = true;
        }
        else
        {
            pose.translate(0.5, -0.16666667F, 0.5);
            pose.rotate(Vector3f.YP.rotationDegrees(-state.get(WallBannerBlock.HORIZONTAL_FACING).getHorizontalAngle()));
            pose.translate(0.0, -0.3125, -0.4375);
            pole.showModel = false;
        }
        renderBanner(banner, banner.getWorld().getGameTime(), partialTicks, tint, pose, buffer, light, overlay);
        pose.pop();
    }

    /** Draws a dyed banner item, whose tile entity has no world to tell which banner it is. */
    public void renderItem(BannerTileEntity banner, int tint, MatrixStack pose, IRenderTypeBuffer buffer, int light, int overlay)
    {
        pose.push();
        pose.translate(0.5, 0.5, 0.5);
        pole.showModel = true;
        renderBanner(banner, 0L, 0.0F, tint, pose, buffer, light, overlay);
        pose.pop();
    }

    private void renderBanner(BannerTileEntity banner, long time, float partialTicks, int tint, MatrixStack pose,
            IRenderTypeBuffer buffer, int light, int overlay)
    {
        List<Pair<BannerPattern, DyeColor>> patterns = banner.getPatternList();
        pose.push();
        pose.scale(0.6666667F, -0.6666667F, -0.6666667F);
        IVertexBuilder base = ModelBakery.LOCATION_BANNER_BASE.getBuffer(buffer, RenderType::getEntitySolid);
        pole.render(pose, base, light, overlay);
        bar.render(pose, base, light, overlay);
        BlockPos pos = banner.getPos();
        float phase = ((float) Math.floorMod(pos.getX() * 7 + pos.getY() * 9 + pos.getZ() * 13 + time, 100L) + partialTicks) / 100.0F;
        flag.rotateAngleX = (-0.0125F + 0.01F * MathHelper.cos((float) (Math.PI * 2) * phase)) * (float) Math.PI;
        flag.rotationPointY = -32.0F;
        flag.render(pose, ModelBakery.LOCATION_BANNER_BASE.getBuffer(buffer, RenderType::getEntitySolid), light, overlay);
        // The first pattern is the base in the banner's dye color (white for a dyed banner), so it is drawn tinted instead.
        renderLayer(pose, buffer, light, overlay, BannerPattern.BASE,
                (tint >> 16 & 255) / 255.0F, (tint >> 8 & 255) / 255.0F, (tint & 255) / 255.0F);
        for (int i = 1; i < 17 && patterns != null && i < patterns.size(); i++)
        {
            Pair<BannerPattern, DyeColor> layer = patterns.get(i);
            float[] color = layer.getSecond().getColorComponentValues();
            renderLayer(pose, buffer, light, overlay, layer.getFirst(), color[0], color[1], color[2]);
        }
        pose.pop();
    }

    private void renderLayer(MatrixStack pose, IRenderTypeBuffer buffer, int light, int overlay, BannerPattern pattern,
            float red, float green, float blue)
    {
        RenderMaterial material = new RenderMaterial(Atlases.BANNER_ATLAS, pattern.getTextureLocation(true));
        flag.render(pose, material.getBuffer(buffer, RenderType::getEntityNoOutline), light, overlay, red, green, blue, 1.0F);
    }
}
