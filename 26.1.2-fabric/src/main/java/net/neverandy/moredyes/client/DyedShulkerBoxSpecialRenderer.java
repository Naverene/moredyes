package net.neverandy.moredyes.client;

import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import org.joml.Vector3fc;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;

import net.neverandy.moredyes.color.MixColors;

/**
 * Draws a dyed shulker box in the inventory and in hand, like vanilla's {@code minecraft:shulker_box} special model but
 * tinted. Its item model names it as {@code {"type": "moredyes:dyed_shulker_box", "color": "<hex>"}}.
 */
public class DyedShulkerBoxSpecialRenderer implements NoDataSpecialModelRenderer {

    private final DyedShulkerBoxRenderer renderer;
    private final float openness;
    private final int tint;

    public DyedShulkerBoxSpecialRenderer(DyedShulkerBoxRenderer renderer, float openness, int tint) {
        this.renderer = renderer;
        this.openness = openness;
        this.tint = tint;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords,
        boolean hasFoil, int outlineColor) {
        renderer.submitDyed(poseStack, collector, lightCoords, overlayCoords, openness, null, tint, outlineColor);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        renderer.extents(openness, output);
    }

    public record Unbaked(String color, float openness) implements NoDataSpecialModelRenderer.Unbaked {

        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i
            .group(Codec.STRING.fieldOf("color").forGetter(Unbaked::color),
                Codec.FLOAT.optionalFieldOf("openness", 0.0F).forGetter(Unbaked::openness))
            .apply(i, Unbaked::new));

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public DyedShulkerBoxSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            int tint = MixColors.byHex(color).map(Tints::argb).orElse(-1);
            return new DyedShulkerBoxSpecialRenderer(
                new DyedShulkerBoxRenderer(context.entityModelSet(), context.sprites()), openness, tint);
        }
    }
}
