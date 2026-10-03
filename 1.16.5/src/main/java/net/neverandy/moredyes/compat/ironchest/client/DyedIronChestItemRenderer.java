package net.neverandy.moredyes.compat.ironchest.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.neverandy.moredyes.compat.ironchest.DyedIronChestItem;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;

/** Draws a dyed Iron Chests chest item as a small closed chest, like Iron Chests does for its own. */
public class DyedIronChestItemRenderer extends ItemStackTileEntityRenderer
{
    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transform, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay)
    {
        if (stack.getItem() instanceof DyedIronChestItem)
        {
            DyedIronChestRenderer.render(((DyedIronChestItem) stack.getItem()).tier(), IronChestCompat.colorOf(stack),
                    Direction.SOUTH, 0.0F, matrix, buffer, light, overlay);
        }
    }
}
