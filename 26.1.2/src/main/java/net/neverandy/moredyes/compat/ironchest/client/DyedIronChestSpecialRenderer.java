package net.neverandy.moredyes.compat.ironchest.client;

import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.progwml6.ironchest.client.IronChestsClientRegistration;
import com.progwml6.ironchest.client.model.IronChestModel;

import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;

import net.neverandy.moredyes.compat.ironchest.IronChestCompat;

/**
 * Draws a dyed Iron Chests chest in the inventory and in hand, in the color of the item's {@code minecraft:block_state}
 * component. Its item model names it as {@code {"type": "moredyes:dyed_iron_chest", "tier": "iron"}}.
 */
public class DyedIronChestSpecialRenderer implements SpecialModelRenderer<Integer> {

    private final IronChestModel model;
    private final IronChestCompat.Tier tier;

    public DyedIronChestSpecialRenderer(IronChestModel model, IronChestCompat.Tier tier) {
        this.model = model;
        this.tier = tier;
    }

    @Override
    public void submit(@Nullable Integer color, PoseStack poseStack, SubmitNodeCollector collector, int lightCoords,
        int overlayCoords, boolean hasFoil, int outlineColor) {
        DyedIronChestRenderer.submit(model, tier, color != null ? color : 0, 0.0F, poseStack, collector, lightCoords,
            overlayCoords, outlineColor, null);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        model.setupAnim(0.0F);
        model.root().getExtentsForGui(poseStack, output);
    }

    @Override
    public Integer extractArgument(ItemStack stack) {
        return IronChestCompat.colorOf(stack);
    }

    public record Unbaked(String tier) implements SpecialModelRenderer.Unbaked<Integer> {

        public static final MapCodec<Unbaked> MAP_CODEC = Codec.STRING.fieldOf("tier").xmap(Unbaked::new, Unbaked::tier);

        @Override
        public @Nullable SpecialModelRenderer<Integer> bake(SpecialModelRenderer.BakingContext context) {
            for (IronChestCompat.Tier t : IronChestCompat.Tier.values()) {
                if (t.id().equals(tier)) {
                    return new DyedIronChestSpecialRenderer(
                        new IronChestModel(context.entityModelSet().bakeLayer(IronChestsClientRegistration.IRON_CHEST)), t);
                }
            }
            return null;
        }

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }
}
