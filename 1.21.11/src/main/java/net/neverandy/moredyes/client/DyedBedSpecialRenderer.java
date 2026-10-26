package net.neverandy.moredyes.client;

import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import net.neverandy.moredyes.block.DyedBedBlockEntity;
import net.neverandy.moredyes.color.MixColors;

/**
 * Draws a More Dyes bed in the inventory and in hand, like vanilla's {@code minecraft:bed} special model, with its
 * frame in the planks the bed was made from (the item's {@code moredyes:wood}). Its item model names it as
 * {@code {"type": "moredyes:dyed_bed", "color": "<hex>"}} for a dyed bed, or with a vanilla color's name ("red") for a
 * More Dyes bed in that color.
 */
public class DyedBedSpecialRenderer implements SpecialModelRenderer<Identifier> {

    private final DyedBedRenderer renderer;
    private final @Nullable DyeColor vanilla;
    private final int tint;

    public DyedBedSpecialRenderer(DyedBedRenderer renderer, @Nullable DyeColor vanilla, int tint) {
        this.renderer = renderer;
        this.vanilla = vanilla;
        this.tint = tint;
    }

    @Override
    public @Nullable Identifier extractArgument(ItemStack stack) {
        return DyedBedBlockEntity.woodOf(stack);
    }

    @Override
    public void submit(@Nullable Identifier wood, ItemDisplayContext displayContext, PoseStack poseStack,
        SubmitNodeCollector collector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        DyedBedRenderer.Look look = vanilla != null ? DyedBedRenderer.Look.vanilla(vanilla, wood)
            : DyedBedRenderer.Look.dyed(tint, wood);
        renderer.submitItem(poseStack, collector, lightCoords, overlayCoords, look, outlineColor);
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
            DyeColor vanilla = DyeColor.byName(color, null);
            int tint = MixColors.byHex(color).map(Tints::argb).orElse(-1);
            return new DyedBedSpecialRenderer(new DyedBedRenderer(context.materials(), context.entityModelSet()),
                vanilla, tint);
        }
    }
}
