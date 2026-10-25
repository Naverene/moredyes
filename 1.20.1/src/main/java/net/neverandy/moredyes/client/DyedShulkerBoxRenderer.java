package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neverandy.moredyes.reference.Reference;

/**
 * Draws every shulker box, vanilla and dyed. It replaces vanilla's renderer for the shulker box block entity, which
 * dyed boxes share: a dyed box is drawn from the grey texture {@code textures/entity/shulker/dyed.png} tinted with its
 * color. Vanilla's renderer picks the texture by dye color, so the drawing is copied from it.
 */
public class DyedShulkerBoxRenderer extends ShulkerBoxRenderer
{
    private static final Material MATERIAL = new Material(Sheets.SHULKER_SHEET, new ResourceLocation(Reference.MOD_ID, "entity/shulker/dyed"));

    private final ShulkerModel<?> model;

    public DyedShulkerBoxRenderer(BlockEntityRendererProvider.Context context)
    {
        super(context);
        this.model = new ShulkerModel<>(context.bakeLayer(ModelLayers.SHULKER));
    }

    @Override
    public void render(ShulkerBoxBlockEntity box, float partialTicks, PoseStack pose, MultiBufferSource buffer, int light, int overlay)
    {
        int tint = ColorHandlers.tintOf(box.getBlockState().getBlock());
        if (tint == -1)
        {
            super.render(box, partialTicks, pose, buffer, light, overlay);
            return;
        }
        Direction direction = Direction.UP;
        if (box.hasLevel())
        {
            BlockState state = box.getLevel().getBlockState(box.getBlockPos());
            if (state.getBlock() instanceof ShulkerBoxBlock)
            {
                direction = state.getValue(ShulkerBoxBlock.FACING);
            }
        }
        pose.pushPose();
        pose.translate(0.5F, 0.5F, 0.5F);
        pose.scale(0.9995F, 0.9995F, 0.9995F);
        pose.mulPose(direction.getRotation());
        pose.scale(1.0F, -1.0F, -1.0F);
        pose.translate(0.0F, -1.0F, 0.0F);
        ModelPart lid = model.getLid();
        float progress = box.getProgress(partialTicks);
        lid.setPos(0.0F, 24.0F - progress * 0.5F * 16.0F, 0.0F);
        lid.yRot = 270.0F * progress * (float) (Math.PI / 180.0);
        model.renderToBuffer(pose, MATERIAL.buffer(buffer, RenderType::entityCutoutNoCull), light, overlay,
                (tint >> 16 & 255) / 255.0F, (tint >> 8 & 255) / 255.0F, (tint & 255) / 255.0F, 1.0F);
        pose.popPose();
    }
}
