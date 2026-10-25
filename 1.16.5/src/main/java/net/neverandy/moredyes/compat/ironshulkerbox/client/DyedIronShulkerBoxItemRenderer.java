package net.neverandy.moredyes.compat.ironshulkerbox.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.neverandy.moredyes.compat.ironshulkerbox.DyedIronShulkerBoxItem;
import net.neverandy.moredyes.compat.ironshulkerbox.IronShulkerBoxCompat;

/** Draws a dyed Iron Shulker Boxes item as a small closed box, like Iron Shulker Boxes does for its own. */
public class DyedIronShulkerBoxItemRenderer extends ItemStackTileEntityRenderer
{
    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transform, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay)
    {
        if (stack.getItem() instanceof DyedIronShulkerBoxItem)
        {
            DyedIronShulkerBoxRenderer.render(((DyedIronShulkerBoxItem) stack.getItem()).tier(), IronShulkerBoxCompat.colorOf(stack),
                    Direction.UP, 0.0F, matrix, buffer, light, overlay);
        }
    }
}
