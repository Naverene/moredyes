package net.neverandy.moredyes.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.tileentity.SignTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.DyedSignTileEntity;

import java.util.List;

/**
 * Draws dyed signs. This is the vanilla sign renderer, except the board and post are the grey
 * textures/entity/signs/dyed multiplied by the dye color.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DyedSignRenderer extends TileEntityRenderer<DyedSignTileEntity>
{
    private static final RenderMaterial MATERIAL = new RenderMaterial(Atlases.SIGN_ATLAS, new ResourceLocation(Reference.MOD_ID, "entity/signs/dyed"));
    private static final float SCALE = 0.6666667F;

    private final SignTileEntityRenderer.SignModel model = new SignTileEntityRenderer.SignModel();

    public DyedSignRenderer(TileEntityRendererDispatcher dispatcher)
    {
        super(dispatcher);
    }

    @Override
    public void render(DyedSignTileEntity sign, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay)
    {
        BlockState state = sign.getBlockState();
        Block block = state.getBlock();
        matrix.push();
        matrix.translate(0.5D, 0.5D, 0.5D);
        if (block instanceof StandingSignBlock)
        {
            matrix.rotate(Vector3f.YP.rotationDegrees(-(state.get(StandingSignBlock.ROTATION) * 360 / 16.0F)));
            model.signStick.showModel = true;
        }
        else
        {
            matrix.rotate(Vector3f.YP.rotationDegrees(-state.get(WallSignBlock.FACING).getHorizontalAngle()));
            matrix.translate(0.0D, -0.3125D, -0.4375D);
            model.signStick.showModel = false;
        }

        int color = ColorHandlers.colorOf(block.getRegistryName());
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        matrix.push();
        matrix.scale(SCALE, -SCALE, -SCALE);
        IVertexBuilder builder = MATERIAL.getBuffer(buffer, model::getRenderType);
        model.signBoard.render(matrix, builder, light, overlay, r, g, b, 1.0F);
        model.signStick.render(matrix, builder, light, overlay, r, g, b, 1.0F);
        matrix.pop();

        FontRenderer font = renderDispatcher.getFontRenderer();
        float textScale = 0.010416667F;
        matrix.translate(0.0D, 0.33333334F, 0.046666667F);
        matrix.scale(textScale, -textScale, textScale);
        int textColor = sign.getTextColor().getTextColor();
        int darkColor = NativeImage.getCombined(0, (int) (NativeImage.getBlue(textColor) * 0.4),
                (int) (NativeImage.getGreen(textColor) * 0.4), (int) (NativeImage.getRed(textColor) * 0.4));
        for (int i = 0; i < 4; i++)
        {
            IReorderingProcessor line = sign.reorderText(i, text ->
            {
                List<IReorderingProcessor> split = font.trimStringToWidth(text, 90);
                return split.isEmpty() ? IReorderingProcessor.field_242232_a : split.get(0);
            });
            if (line != null)
            {
                float x = -font.func_243245_a(line) / 2;
                font.drawEntityText(line, x, i * 10 - 20, darkColor, false, matrix.getLast().getMatrix(), buffer, false, 0, light);
            }
        }
        matrix.pop();
    }

    /** The grey sign texture is not on the sign atlas unless we add it. */
    @SubscribeEvent
    public static void stitch(TextureStitchEvent.Pre event)
    {
        if (event.getMap().getTextureLocation().equals(Atlases.SIGN_ATLAS))
        {
            event.addSprite(MATERIAL.getTextureLocation());
        }
    }
}
