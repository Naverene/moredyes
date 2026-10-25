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
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;

import net.neverandy.moredyes.color.MixColors;

/**
 * Draws a dyed shulker box in the inventory and in hand, like vanilla's {@code minecraft:shulker_box} special model but
 * tinted. Its item model names it as {@code {"type": "moredyes:dyed_shulker_box", "color": "<hex>"}}.
 */
public class DyedShulkerBoxSpecialRenderer implements NoDataSpecialModelRenderer {

    private final DyedShulkerBoxRenderer renderer;
    private final int tint;

    public DyedShulkerBoxSpecialRenderer(DyedShulkerBoxRenderer renderer, int tint) {
        this.renderer = renderer;
        this.tint = tint;
    }

    @Override
    public void submit(ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector collector,
        int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        renderer.submitDyed(poseStack, collector, lightCoords, overlayCoords, Direction.UP, 0.0F, null, tint,
            outlineColor);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        renderer.extents(Direction.UP, 0.0F, output);
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
        public DyedShulkerBoxSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            int tint = MixColors.byHex(color).map(Tints::argb).orElse(-1);
            return new DyedShulkerBoxSpecialRenderer(
                new DyedShulkerBoxRenderer(context.entityModelSet(), context.materials()), tint);
        }
    }
}
