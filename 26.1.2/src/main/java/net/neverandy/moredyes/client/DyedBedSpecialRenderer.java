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
import net.minecraft.world.level.block.state.properties.BedPart;

import net.neverandy.moredyes.color.MixColors;

/**
 * Draws half of a dyed bed in the inventory and in hand, like vanilla's {@code minecraft:bed} special model but
 * tinted. Its item model names it as {@code {"type": "moredyes:dyed_bed", "color": "<hex>", "part": "head"}}.
 */
public class DyedBedSpecialRenderer implements NoDataSpecialModelRenderer {

    private final DyedBedRenderer renderer;
    private final BedPart part;
    private final int tint;

    public DyedBedSpecialRenderer(DyedBedRenderer renderer, BedPart part, int tint) {
        this.renderer = renderer;
        this.part = part;
        this.tint = tint;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords,
        boolean hasFoil, int outlineColor) {
        renderer.submitDyed(part, tint, poseStack, collector, lightCoords, overlayCoords, null, outlineColor);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        renderer.getExtents(part, output);
    }

    public record Unbaked(String color, BedPart part) implements NoDataSpecialModelRenderer.Unbaked {

        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i
            .group(Codec.STRING.fieldOf("color").forGetter(Unbaked::color),
                BedPart.CODEC.fieldOf("part").forGetter(Unbaked::part))
            .apply(i, Unbaked::new));

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public DyedBedSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            int tint = MixColors.byHex(color).map(Tints::argb).orElse(-1);
            return new DyedBedSpecialRenderer(new DyedBedRenderer(context.sprites(), context.entityModelSet()), part,
                tint);
        }
    }
}
