package net.neverandy.moredyes.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.ShulkerModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.ShulkerBoxTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.ShulkerBoxTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.neverandy.moredyes.reference.Reference;

/**
 * Draws every shulker box, vanilla and dyed. It replaces vanilla's renderer for the shulker box tile entity, which
 * dyed boxes share: a dyed box is drawn from the grey texture {@code textures/entity/shulker/dyed.png} tinted with its
 * color. Vanilla's renderer picks the texture by dye color, so the drawing is copied from it.
 */
public class DyedShulkerBoxRenderer extends ShulkerBoxTileEntityRenderer
{
    static final RenderMaterial MATERIAL = new RenderMaterial(Atlases.SHULKER_BOX_ATLAS, new ResourceLocation(Reference.MOD_ID, "entity/shulker/dyed"));

    private final ShulkerModel<?> model = new ShulkerModel<>();

    public DyedShulkerBoxRenderer(TileEntityRendererDispatcher dispatcher)
    {
        super(new ShulkerModel<>(), dispatcher);
    }

    @Override
    public void render(ShulkerBoxTileEntity box, float partialTicks, MatrixStack pose, IRenderTypeBuffer buffer, int light, int overlay)
    {
        if (!box.hasWorld())
        {
            super.render(box, partialTicks, pose, buffer, light, overlay);
            return;
        }
        BlockState state = box.getWorld().getBlockState(box.getPos());
        int tint = ColorHandlers.tintOf(state.getBlock());
        if (tint == -1)
        {
            super.render(box, partialTicks, pose, buffer, light, overlay);
            return;
        }
        Direction direction = state.getBlock() instanceof ShulkerBoxBlock ? state.get(ShulkerBoxBlock.FACING) : Direction.UP;
        renderTinted(direction, box.getProgress(partialTicks), tint, pose, buffer, light, overlay);
    }

    /** Draws a dyed shulker box item, which has no world to tell which box it is. */
    public void renderItem(int tint, MatrixStack pose, IRenderTypeBuffer buffer, int light, int overlay)
    {
        renderTinted(Direction.UP, 0.0F, tint, pose, buffer, light, overlay);
    }

    private void renderTinted(Direction direction, float progress, int tint, MatrixStack pose, IRenderTypeBuffer buffer, int light, int overlay)
    {
        float red = (tint >> 16 & 255) / 255.0F;
        float green = (tint >> 8 & 255) / 255.0F;
        float blue = (tint & 255) / 255.0F;
        pose.push();
        pose.translate(0.5, 0.5, 0.5);
        pose.scale(0.9995F, 0.9995F, 0.9995F);
        pose.rotate(direction.getRotation());
        pose.scale(1.0F, -1.0F, -1.0F);
        pose.translate(0.0, -1.0, 0.0);
        IVertexBuilder builder = MATERIAL.getBuffer(buffer, RenderType::getEntityCutoutNoCull);
        model.getBase().render(pose, builder, light, overlay, red, green, blue, 1.0F);
        pose.translate(0.0, -progress * 0.5F, 0.0);
        pose.rotate(Vector3f.YP.rotationDegrees(270.0F * progress));
        model.getLid().render(pose, builder, light, overlay, red, green, blue, 1.0F);
        pose.pop();
    }
}
