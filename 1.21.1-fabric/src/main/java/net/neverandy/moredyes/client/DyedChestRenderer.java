package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.DyedChestBlockEntity;

/**
 * Draws dyed chests. This is the vanilla chest renderer, except the textures are the grey copies in
 * {@code textures/entity/chest} and the lid and base are multiplied by the dye color; the latch keeps its own color.
 * The vanilla chest atlas picks up every texture in {@code textures/entity/chest}, so the grey ones need no registering.
 */
public class DyedChestRenderer implements BlockEntityRenderer<DyedChestBlockEntity> {

    private static final Material SINGLE = material("normal");
    private static final Material LEFT = material("normal_left");
    private static final Material RIGHT = material("normal_right");

    private final Parts single;
    private final Parts left;
    private final Parts right;

    public DyedChestRenderer(BlockEntityRendererProvider.Context context) {
        this(context.getModelSet());
    }

    public DyedChestRenderer(EntityModelSet models) {
        single = new Parts(models.bakeLayer(ModelLayers.CHEST));
        left = new Parts(models.bakeLayer(ModelLayers.DOUBLE_CHEST_LEFT));
        right = new Parts(models.bakeLayer(ModelLayers.DOUBLE_CHEST_RIGHT));
    }

    @Override
    public void render(DyedChestBlockEntity chest, float partialTicks, PoseStack pose, MultiBufferSource buffer,
        int light, int overlay) {
        Level level = chest.getLevel();
        BlockState state = chest.getBlockState();
        if (level == null || !(state.getBlock() instanceof ChestBlock block)) {
            return;
        }
        DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combined = block.combine(state, level,
            chest.getBlockPos(), true);
        float lid = combined.<Float2FloatFunction>apply(ChestBlock.opennessCombiner(chest)).get(partialTicks);
        int combinedLight = combined.<Int2IntFunction>apply(new BrightnessCombiner<>()).applyAsInt(light);
        render(block, state.getValue(ChestBlock.TYPE), state.getValue(ChestBlock.FACING), lid, pose, buffer,
            combinedLight, overlay);
    }

    /** Draws a closed single chest facing south, for the item (see DyedChestItemRenderer). */
    public void renderItem(Block block, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
        render(block, ChestType.SINGLE, Direction.SOUTH, 0.0F, pose, buffer, light, overlay);
    }

    private void render(Block block, ChestType type, Direction facing, float lid, PoseStack pose,
        MultiBufferSource buffer, int light, int overlay) {
        int tint = Tints.blockTint(block);
        pose.pushPose();
        pose.translate(0.5F, 0.5F, 0.5F);
        pose.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        pose.translate(-0.5F, -0.5F, -0.5F);

        lid = 1.0F - lid;
        lid = 1.0F - lid * lid * lid;
        Material material = type == ChestType.LEFT ? LEFT : type == ChestType.RIGHT ? RIGHT : SINGLE;
        VertexConsumer consumer = material.buffer(buffer, RenderType::entityCutout);
        Parts parts = type == ChestType.LEFT ? left : type == ChestType.RIGHT ? right : single;
        parts.lid.xRot = -(lid * ((float) Math.PI / 2F));
        parts.lock.xRot = parts.lid.xRot;
        parts.lid.render(pose, consumer, light, overlay, tint);
        parts.lock.render(pose, consumer, light, overlay);
        parts.bottom.render(pose, consumer, light, overlay, tint);
        pose.popPose();
    }

    private static Material material(String name) {
        return new Material(Sheets.CHEST_SHEET, ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID, "entity/chest/" + name));
    }

    /** The boxes of one chest shape, from the vanilla chest models. */
    private static final class Parts {
        final ModelPart lid;
        final ModelPart lock;
        final ModelPart bottom;

        Parts(ModelPart root) {
            lid = root.getChild("lid");
            lock = root.getChild("lock");
            bottom = root.getChild("bottom");
        }
    }
}
