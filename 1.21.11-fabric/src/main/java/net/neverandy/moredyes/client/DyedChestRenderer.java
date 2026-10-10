package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;

import org.jspecify.annotations.Nullable;

import net.minecraft.client.model.object.chest.ChestModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.math.Axis;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.Vec3;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.DyedChestBlockEntity;

/**
 * Draws dyed chests: the vanilla chest model with a grey texture ({@code textures/entity/chest/normal*.png} in this
 * mod), tinted with the chest's color. It follows the vanilla ChestRenderer, which has no way to pass a tint.
 */
public class DyedChestRenderer implements BlockEntityRenderer<DyedChestBlockEntity, DyedChestRenderer.State> {

    /** The grey chest textures, on the vanilla chest atlas. */
    public static final Material SINGLE = Sheets.CHEST_MAPPER.apply(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "normal"));
    public static final Material LEFT = Sheets.CHEST_MAPPER.apply(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "normal_left"));
    public static final Material RIGHT = Sheets.CHEST_MAPPER.apply(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "normal_right"));

    private final MaterialSet materials;
    private final ChestModel single;
    private final ChestModel left;
    private final ChestModel right;

    public DyedChestRenderer(BlockEntityRendererProvider.Context context) {
        this.materials = context.materials();
        this.single = new ChestModel(context.bakeLayer(ModelLayers.CHEST));
        this.left = new ChestModel(context.bakeLayer(ModelLayers.DOUBLE_CHEST_LEFT));
        this.right = new ChestModel(context.bakeLayer(ModelLayers.DOUBLE_CHEST_RIGHT));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(DyedChestBlockEntity chest, State state, float partialTicks, Vec3 cameraPosition,
        ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(chest, state, partialTicks, cameraPosition, breakProgress);
        BlockState blockState = chest.getBlockState();
        state.type = blockState.hasProperty(ChestBlock.TYPE) ? blockState.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
        state.angle = blockState.getValue(ChestBlock.FACING).toYRot();
        state.tint = Tints.blockTint(blockState.getBlock());
        DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combined;
        if (chest.getLevel() != null && blockState.getBlock() instanceof ChestBlock block) {
            combined = block.combine(blockState, chest.getLevel(), chest.getBlockPos(), true);
        } else {
            combined = DoubleBlockCombiner.Combiner::acceptNone;
        }
        state.open = combined.apply(ChestBlock.opennessCombiner(chest)).get(partialTicks);
        if (state.type != ChestType.SINGLE) {
            state.lightCoords = combined.apply(new BrightnessCombiner<>()).applyAsInt(state.lightCoords);
        }
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.angle));
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        float open = 1.0F - state.open;
        open = 1.0F - open * open * open;
        Material material = material(state.type);
        ChestModel model = switch (state.type) {
            case LEFT -> left;
            case RIGHT -> right;
            default -> single;
        };
        collector.submitModel(model, open, poseStack, material.renderType(RenderTypes::entityCutout), state.lightCoords,
            OverlayTexture.NO_OVERLAY, state.tint, materials.get(material), 0, state.breakProgress);
        poseStack.popPose();
    }


    /** The grey texture for a chest of this type. */
    public static Material material(ChestType type) {
        return switch (type) {
            case LEFT -> LEFT;
            case RIGHT -> RIGHT;
            default -> SINGLE;
        };
    }

    public static class State extends ChestRenderState {
        public int tint = -1;
    }
}
