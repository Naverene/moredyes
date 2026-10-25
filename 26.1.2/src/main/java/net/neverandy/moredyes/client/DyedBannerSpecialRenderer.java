package net.neverandy.moredyes.client;

import java.util.Objects;
import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import net.neverandy.moredyes.color.MixColors;

/**
 * Draws a dyed banner in the inventory and in hand, like vanilla's {@code minecraft:banner} special model but with the
 * base in a More Dyes color. Its item model names it as {@code {"type": "moredyes:dyed_banner", "color": "<hex>"}}.
 */
public class DyedBannerSpecialRenderer implements SpecialModelRenderer<BannerPatternLayers> {

    private final DyedBannerRenderer renderer;
    private final BannerBlock.AttachmentType attachment;
    private final int tint;

    public DyedBannerSpecialRenderer(DyedBannerRenderer renderer, BannerBlock.AttachmentType attachment, int tint) {
        this.renderer = renderer;
        this.attachment = attachment;
        this.tint = tint;
    }

    @Override
    public @Nullable BannerPatternLayers extractArgument(ItemStack stack) {
        return stack.get(DataComponents.BANNER_PATTERNS);
    }

    @Override
    public void submit(@Nullable BannerPatternLayers patterns, PoseStack poseStack, SubmitNodeCollector collector,
        int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        renderer.submitBanner(poseStack, collector, lightCoords, overlayCoords, attachment, 0.0F, tint,
            Objects.requireNonNullElse(patterns, BannerPatternLayers.EMPTY), null, outlineColor);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        renderer.getExtents(output);
    }

    public record Unbaked(String color, BannerBlock.AttachmentType attachment)
        implements SpecialModelRenderer.Unbaked<BannerPatternLayers> {

        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i
            .group(Codec.STRING.fieldOf("color").forGetter(Unbaked::color),
                BannerBlock.AttachmentType.CODEC.optionalFieldOf("attachment", BannerBlock.AttachmentType.GROUND)
                    .forGetter(Unbaked::attachment))
            .apply(i, Unbaked::new));

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public DyedBannerSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
            int tint = MixColors.byHex(color).map(Tints::argb).orElse(-1);
            return new DyedBannerSpecialRenderer(new DyedBannerRenderer(context.entityModelSet(), context.sprites()),
                attachment, tint);
        }
    }
}
