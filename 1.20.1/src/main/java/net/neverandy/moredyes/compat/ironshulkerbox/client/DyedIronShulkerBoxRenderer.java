package net.neverandy.moredyes.compat.ironshulkerbox.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.progwml6.ironshulkerbox.common.block.AbstractIronShulkerBoxBlock;
import com.progwml6.ironshulkerbox.common.block.entity.ICrystalShulkerBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neverandy.moredyes.client.ColorHandlers;
import net.neverandy.moredyes.compat.ironshulkerbox.DyedIronShulkerBoxBlock;
import net.neverandy.moredyes.compat.ironshulkerbox.DyedIronShulkerBoxBlockEntity;
import net.neverandy.moredyes.compat.ironshulkerbox.IronShulkerBoxCompat;
import net.neverandy.moredyes.reference.ColorStrings;

/**
 * Draws a dyed Iron Shulker Boxes box: vanilla's shulker model, once with the grey shell texture multiplied by the dye
 * color and once with the metal trim in its own colors (see DyedIronShulkerBoxTextures). A crystal box also shows the
 * items it holds, as Iron Shulker Boxes draws them.
 */
public class DyedIronShulkerBoxRenderer implements BlockEntityRenderer<DyedIronShulkerBoxBlockEntity>
{
    /** Where a crystal box shows its top items, as in Iron Shulker Boxes. */
    private static final float[][] ITEM_POSITIONS = {{0.3F, 0.45F, 0.3F}, {0.7F, 0.45F, 0.3F}, {0.3F, 0.45F, 0.7F},
            {0.7F, 0.45F, 0.7F}, {0.3F, 0.1F, 0.3F}, {0.7F, 0.1F, 0.3F}, {0.3F, 0.1F, 0.7F}, {0.7F, 0.1F, 0.7F}};
    private static final float ITEM_SCALE = 3.0F / 16.0F;

    private final ShulkerModel<?> model;

    public DyedIronShulkerBoxRenderer(BlockEntityRendererProvider.Context context)
    {
        this(context.getModelSet());
    }

    public DyedIronShulkerBoxRenderer(EntityModelSet models)
    {
        model = new ShulkerModel<>(models.bakeLayer(ModelLayers.SHULKER));
    }

    @Override
    public void render(DyedIronShulkerBoxBlockEntity box, float partialTicks, PoseStack pose, MultiBufferSource buffer, int light, int overlay)
    {
        BlockState state = box.getBlockState();
        if (!(state.getBlock() instanceof DyedIronShulkerBoxBlock))
        {
            return;
        }
        render(box.tier(), state.getValue(DyedIronShulkerBoxBlock.COLOR), state.getValue(AbstractIronShulkerBoxBlock.FACING),
                box.getProgress(partialTicks), pose, buffer, light, overlay);
        if (box instanceof ICrystalShulkerBox crystal && box.getLevel() != null
                && Vec3.atCenterOf(box.getBlockPos()).closerThan(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition(), 128.0))
        {
            float rotation = (float) (360.0 * (System.currentTimeMillis() & 16383L) / 16383.0) - partialTicks;
            for (int i = 0; i < ITEM_POSITIONS.length && i < crystal.getTopItems().size(); i++)
            {
                renderItem(pose, buffer, crystal.getTopItems().get(i), ITEM_POSITIONS[i], rotation, light);
            }
        }
    }

    public void render(IronShulkerBoxCompat.Tier tier, int color, Direction facing, float progress, PoseStack pose, MultiBufferSource buffer, int light, int overlay)
    {
        int rgb = ColorHandlers.vivid(Integer.parseInt(ColorStrings.ALL[color], 16));
        float r = (rgb >> 16 & 255) / 255.0F;
        float g = (rgb >> 8 & 255) / 255.0F;
        float b = (rgb & 255) / 255.0F;

        pose.pushPose();
        pose.translate(0.5F, 0.5F, 0.5F);
        pose.scale(0.9995F, 0.9995F, 0.9995F);
        pose.mulPose(facing.getRotation());
        pose.scale(1.0F, -1.0F, -1.0F);
        pose.translate(0.0F, -1.0F, 0.0F);
        ModelPart lid = model.getLid();
        lid.setPos(0.0F, 24.0F - progress * 0.5F * 16.0F, 0.0F);
        lid.yRot = 270.0F * progress * (float) (Math.PI / 180.0);
        model.renderToBuffer(pose, buffer.getBuffer(RenderType.entityCutoutNoCull(DyedIronShulkerBoxTextures.shell(tier))),
                light, overlay, r, g, b, 1.0F);
        model.renderToBuffer(pose, buffer.getBuffer(RenderType.entityCutoutNoCull(DyedIronShulkerBoxTextures.trim(tier))),
                light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        pose.popPose();
    }

    private static void renderItem(PoseStack pose, MultiBufferSource buffer, ItemStack item, float[] center, float rotation, int light)
    {
        if (item.isEmpty())
        {
            return;
        }
        pose.pushPose();
        pose.translate(center[0], center[1], center[2]);
        pose.mulPose(Axis.YP.rotationDegrees(rotation));
        pose.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, pose, buffer, null, 0);
        pose.popPose();
    }
}
