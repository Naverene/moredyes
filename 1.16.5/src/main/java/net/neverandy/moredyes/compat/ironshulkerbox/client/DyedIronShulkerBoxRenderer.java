package net.neverandy.moredyes.compat.ironshulkerbox.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.progwml6.ironshulkerbox.common.block.GenericIronShulkerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.ShulkerModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.neverandy.moredyes.client.ColorHandlers;
import net.neverandy.moredyes.compat.ironshulkerbox.DyedIronShulkerBoxBlock;
import net.neverandy.moredyes.compat.ironshulkerbox.DyedIronShulkerBoxTileEntity;
import net.neverandy.moredyes.compat.ironshulkerbox.IronShulkerBoxCompat;
import net.neverandy.moredyes.reference.ColorStrings;

/**
 * Draws a dyed Iron Shulker Boxes box: vanilla's shulker model, as Iron Shulker Boxes draws its own, once with the grey
 * shell texture multiplied by the dye color and once with the metal trim in its own colors (see
 * DyedIronShulkerBoxTextures).
 */
public class DyedIronShulkerBoxRenderer extends TileEntityRenderer<DyedIronShulkerBoxTileEntity>
{
    private static final ShulkerModel<?> MODEL = new ShulkerModel<>();

    public DyedIronShulkerBoxRenderer(TileEntityRendererDispatcher dispatcher)
    {
        super(dispatcher);
    }

    @Override
    public void render(DyedIronShulkerBoxTileEntity box, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay)
    {
        BlockState state = box.getBlockState();
        if (!(state.getBlock() instanceof DyedIronShulkerBoxBlock))
        {
            return;
        }
        render(box.tier(), state.get(DyedIronShulkerBoxBlock.COLOR), state.get(GenericIronShulkerBlock.FACING),
                box.getProgress(partialTicks), matrix, buffer, light, overlay);
    }

    public static void render(IronShulkerBoxCompat.Tier tier, int color, Direction facing, float progress, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay)
    {
        int rgb = ColorHandlers.vivid(Integer.parseInt(ColorStrings.ALL[color], 16));
        float r = (rgb >> 16 & 255) / 255.0F;
        float g = (rgb >> 8 & 255) / 255.0F;
        float b = (rgb & 255) / 255.0F;
        // One buffer at a time: asking for the second ends the first.
        draw(buffer.getBuffer(RenderType.getEntityCutoutNoCull(DyedIronShulkerBoxTextures.shell(tier))), facing, progress, matrix, light, overlay, r, g, b);
        draw(buffer.getBuffer(RenderType.getEntityCutoutNoCull(DyedIronShulkerBoxTextures.trim(tier))), facing, progress, matrix, light, overlay, 1.0F, 1.0F, 1.0F);
    }

    /** The box as Iron Shulker Boxes' IronShulkerBoxTileEntityRenderer draws it, in one texture and color. */
    private static void draw(IVertexBuilder builder, Direction facing, float progress, MatrixStack matrix, int light, int overlay, float r, float g, float b)
    {
        matrix.push();
        matrix.translate(0.5D, 0.5D, 0.5D);
        matrix.scale(0.9995F, 0.9995F, 0.9995F);
        matrix.rotate(facing.getRotation());
        matrix.scale(1.0F, -1.0F, -1.0F);
        matrix.translate(0.0D, -1.0D, 0.0D);
        MODEL.getBase().render(matrix, builder, light, overlay, r, g, b, 1.0F);
        matrix.translate(0.0D, -progress * 0.5F, 0.0D);
        matrix.rotate(Vector3f.YP.rotationDegrees(270.0F * progress));
        MODEL.getLid().render(matrix, builder, light, overlay, r, g, b, 1.0F);
        matrix.pop();
    }
}
