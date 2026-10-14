package net.neverandy.moredyes.paint.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps every block model so painted blocks are drawn in their paint color. The chunk renderer asks each model for
 * its model data per block; for a painted block this adds the color, and the quads then come back with their vertices
 * colored, their texture swapped for a grey copy ({@link GreySprites}) and their usual tint (grass, More Dyes colors)
 * dropped, so the block shows the paint color itself.
 */
public class PaintedModel extends BakedModelWrapper<BakedModel>
{
    private static final ModelProperty<Integer> PAINT = new ModelProperty<>();

    /** In the block vertex format each vertex is 8 ints: position (3), color, texture u and v, light, normal. */
    private static final int STRIDE = 8;
    private static final int COLOR = 3;
    private static final int U = 4;
    private static final int V = 5;

    public PaintedModel(BakedModel original)
    {
        super(original);
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData)
    {
        ModelData data = super.getModelData(level, pos, state, modelData);
        int color = ClientPaint.colorAt(pos, state);
        return color < 0 ? data : data.derive().with(PAINT, color).build();
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType)
    {
        List<BakedQuad> quads = super.getQuads(state, side, rand, data, renderType);
        Integer color = data.get(PAINT);
        if (color == null || quads.isEmpty())
        {
            return quads;
        }
        List<BakedQuad> painted = new ArrayList<>(quads.size());
        for (BakedQuad quad : quads)
        {
            painted.add(paint(quad, color));
        }
        return painted;
    }

    private static BakedQuad paint(BakedQuad quad, int rgb)
    {
        int[] vertices = quad.getVertices().clone();
        TextureAtlasSprite sprite = quad.getSprite();
        TextureAtlasSprite grey = GreySprites.find(sprite);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        for (int i = 0; i + STRIDE <= vertices.length; i += STRIDE)
        {
            // The color is stored as bytes R, G, B, A, so the int reads 0xAABBGGRR.
            int old = vertices[i + COLOR];
            int nr = (old & 0xFF) * r / 255;
            int ng = ((old >> 8) & 0xFF) * g / 255;
            int nb = ((old >> 16) & 0xFF) * b / 255;
            vertices[i + COLOR] = (old & 0xFF000000) | (nb << 16) | (ng << 8) | nr;
            if (grey != null)
            {
                float u = Float.intBitsToFloat(vertices[i + U]);
                float v = Float.intBitsToFloat(vertices[i + V]);
                u = grey.getU0() + (u - sprite.getU0()) / (sprite.getU1() - sprite.getU0()) * (grey.getU1() - grey.getU0());
                v = grey.getV0() + (v - sprite.getV0()) / (sprite.getV1() - sprite.getV0()) * (grey.getV1() - grey.getV0());
                vertices[i + U] = Float.floatToRawIntBits(u);
                vertices[i + V] = Float.floatToRawIntBits(v);
            }
        }
        return new BakedQuad(vertices, -1, quad.getDirection(), grey != null ? grey : sprite, quad.isShade(), quad.hasAmbientOcclusion());
    }
}
