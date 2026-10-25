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
import net.minecraft.world.item.ItemDisplayContext;

import net.neverandy.moredyes.color.MixColors;

/**
 * Draws a dyed bed in the inventory and in hand, like vanilla's {@code minecraft:bed} special model but tinted. Its
 * item model names it as {@code {"type": "moredyes:dyed_bed", "color": "<hex>"}}.
 */
public class DyedBedSpecialRenderer implements NoDataSpecialModelRenderer {

    private final DyedBedRenderer renderer;
    private final int tint;

    public DyedBedSpecialRenderer(DyedBedRenderer renderer, int tint) {
        this.renderer = renderer;
        this.tint = tint;
    }

    @Override
    public void submit(ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector collector,
        int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        renderer.submitItem(poseStack, collector, lightCoords, overlayCoords, tint, outlineColor);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        renderer.getExtents(output);
    }

    public record Unbaked(String color) implements SpecialModelRenderer.Unbaked {

        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i
            .group(Codec.STRING.fieldOf("color").forGetter(Unbaked::color))
            .apply(i, Unbaked::new));

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public DyedBedSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            int tint = MixColors.byHex(color).map(Tints::argb).orElse(-1);
            return new DyedBedSpecialRenderer(new DyedBedRenderer(context.materials(), context.entityModelSet()), tint);
        }
    }
}
