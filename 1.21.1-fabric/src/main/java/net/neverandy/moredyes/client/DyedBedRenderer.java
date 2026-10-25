package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import net.neverandy.moredyes.MoreDyes;

/**
 * Draws every bed, vanilla and dyed. It replaces vanilla's renderer for the bed block entity, which dyed beds share:
 * a dyed bed is drawn from the grey bedding texture {@code textures/entity/bed/dyed.png} tinted with its color, and
 * the wooden frame ({@code dyed_frame.png}) over it as it is. Vanilla's renderer keeps its models to itself, so the
 * drawing is copied from it.
 */
public class DyedBedRenderer extends BedRenderer {

    private static final Material BEDDING = new Material(Sheets.BED_SHEET,
        ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID, "entity/bed/dyed"));
    private static final Material FRAME = new Material(Sheets.BED_SHEET,
        ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID, "entity/bed/dyed_frame"));

    private final ModelPart head;
    private final ModelPart foot;

    public DyedBedRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.head = context.bakeLayer(ModelLayers.BED_HEAD);
        this.foot = context.bakeLayer(ModelLayers.BED_FOOT);
    }

    @Override
    public void render(BedBlockEntity bed, float partialTicks, PoseStack pose, MultiBufferSource buffer, int light,
        int overlay) {
        BlockState state = bed.getBlockState();
        int tint = Tints.blockTint(state.getBlock());
        if (tint == -1) {
            super.render(bed, partialTicks, pose, buffer, light, overlay);
            return;
        }
        Level level = bed.getLevel();
        if (level != null) {
            DoubleBlockCombiner.NeighborCombineResult<? extends BedBlockEntity> combined = DoubleBlockCombiner
                .combineWithNeigbour(BlockEntityType.BED, BedBlock::getBlockType, BedBlock::getConnectedDirection,
                    ChestBlock.FACING, state, level, bed.getBlockPos(), (world, pos) -> false);
            int brightness = combined.apply(new BrightnessCombiner<>()).get(light);
            renderPiece(pose, buffer, state.getValue(BedBlock.PART) == BedPart.HEAD ? head : foot,
                state.getValue(BedBlock.FACING), tint, brightness, overlay, false);
        } else {
            renderPiece(pose, buffer, head, Direction.SOUTH, tint, light, overlay, false);
            renderPiece(pose, buffer, foot, Direction.SOUTH, tint, light, overlay, true);
        }
    }

    /** Vanilla's BedRenderer.renderPiece, drawing the tinted bedding and then the frame. */
    private static void renderPiece(PoseStack pose, MultiBufferSource buffer, ModelPart part, Direction facing,
        int tint, int light, int overlay, boolean foot) {
        pose.pushPose();
        pose.translate(0.0F, 0.5625F, foot ? -1.0F : 0.0F);
        pose.mulPose(Axis.XP.rotationDegrees(90.0F));
        pose.translate(0.5F, 0.5F, 0.5F);
        pose.mulPose(Axis.ZP.rotationDegrees(180.0F + facing.toYRot()));
        pose.translate(-0.5F, -0.5F, -0.5F);
        // Both layers are cut out, so the frame's see-through pixels don't hide the bedding (and the other way round).
        part.render(pose, BEDDING.buffer(buffer, RenderType::entityCutout), light, overlay, tint);
        part.render(pose, FRAME.buffer(buffer, RenderType::entityCutout), light, overlay);
        pose.popPose();
    }
}
