package net.neverandy.moredyes.compat.ironchest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neverandy.moredyes.client.ColorHandlers;
import net.neverandy.moredyes.compat.ironchest.DyedIronChestBlock;
import net.neverandy.moredyes.compat.ironchest.DyedIronChestBlockEntity;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;
import net.neverandy.moredyes.reference.ColorStrings;

/**
 * Draws a dyed Iron Chests chest: the vanilla chest model, which Iron Chests draws its own with, once with the grey panel
 * texture multiplied by the dye color and once with the frame, latch and inside in their own colors (see
 * DyedIronChestTextures).
 */
public class DyedIronChestRenderer implements BlockEntityRenderer<DyedIronChestBlockEntity>
{
    private final ModelPart lid;
    private final ModelPart bottom;
    private final ModelPart lock;

    public DyedIronChestRenderer(BlockEntityRendererProvider.Context context)
    {
        this(context.getModelSet());
    }

    public DyedIronChestRenderer(EntityModelSet models)
    {
        ModelPart root = models.bakeLayer(ModelLayers.CHEST);
        bottom = root.getChild("bottom");
        lid = root.getChild("lid");
        lock = root.getChild("lock");
    }

    @Override
    public void render(DyedIronChestBlockEntity chest, float partialTicks, PoseStack pose, MultiBufferSource buffer, int light, int overlay)
    {
        BlockState state = chest.getBlockState();
        if (chest.getLevel() == null || !(state.getBlock() instanceof DyedIronChestBlock block))
        {
            return;
        }
        // Always a single chest, so it is lit and opened on its own.
        render(block.tier(), state.getValue(DyedIronChestBlock.COLOR), state.getValue(ChestBlock.FACING), chest.getOpenNess(partialTicks),
                pose, buffer, light, overlay);
    }

    public void render(IronChestCompat.Tier tier, int color, Direction facing, float openness, PoseStack pose, MultiBufferSource buffer, int light, int overlay)
    {
        int rgb = ColorHandlers.vivid(Integer.parseInt(ColorStrings.ALL[color], 16));
        float r = (rgb >> 16 & 255) / 255.0F;
        float g = (rgb >> 8 & 255) / 255.0F;
        float b = (rgb & 255) / 255.0F;

        pose.pushPose();
        pose.translate(0.5F, 0.5F, 0.5F);
        pose.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        pose.translate(-0.5F, -0.5F, -0.5F);

        openness = 1.0F - openness;
        openness = 1.0F - openness * openness * openness;
        lid.xRot = -(openness * ((float) Math.PI / 2F));
        lock.xRot = lid.xRot;

        VertexConsumer panels = buffer.getBuffer(RenderType.entityCutout(DyedIronChestTextures.panels(tier)));
        lid.render(pose, panels, light, overlay, r, g, b, 1.0F);
        bottom.render(pose, panels, light, overlay, r, g, b, 1.0F);
        VertexConsumer trim = buffer.getBuffer(RenderType.entityCutout(DyedIronChestTextures.trim(tier)));
        lid.render(pose, trim, light, overlay);
        lock.render(pose, trim, light, overlay);
        bottom.render(pose, trim, light, overlay);
        pose.popPose();
    }
}
