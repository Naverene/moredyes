package net.neverandy.moredyes.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.tileentity.BedTileEntity;
import net.minecraft.tileentity.ShulkerBoxTileEntity;

/**
 * Draws the dyed bed, banner and shulker box items. Their item models are vanilla's ({@code builtin/entity}), which
 * hand the drawing to this. An item's tile entity has no world, so it can't tell the renderers which block it is;
 * this hands them the color instead.
 */
public class DyedItemRenderer extends ItemStackTileEntityRenderer
{
    private final BedTileEntity bed = new BedTileEntity();
    private final BannerTileEntity banner = new BannerTileEntity();
    private final ShulkerBoxTileEntity box = new ShulkerBoxTileEntity();

    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transform, MatrixStack pose, IRenderTypeBuffer buffer,
            int light, int overlay)
    {
        if (!(stack.getItem() instanceof BlockItem))
        {
            return;
        }
        Block block = ((BlockItem) stack.getItem()).getBlock();
        int tint = ColorHandlers.tintOf(block);
        TileEntityRendererDispatcher dispatcher = TileEntityRendererDispatcher.instance;
        if (block instanceof BedBlock)
        {
            TileEntityRenderer<?> renderer = dispatcher.getRenderer(bed);
            if (renderer instanceof DyedBedRenderer)
            {
                ((DyedBedRenderer) renderer).renderItem(stack, pose, buffer, light, overlay);
            }
        }
        else if (block instanceof BannerBlock)
        {
            banner.loadFromItemStack(stack, DyeColor.WHITE);
            TileEntityRenderer<?> renderer = dispatcher.getRenderer(banner);
            if (renderer instanceof DyedBannerRenderer)
            {
                ((DyedBannerRenderer) renderer).renderItem(banner, tint, pose, buffer, light, overlay);
            }
        }
        else if (block instanceof ShulkerBoxBlock)
        {
            TileEntityRenderer<?> renderer = dispatcher.getRenderer(box);
            if (renderer instanceof DyedShulkerBoxRenderer)
            {
                ((DyedShulkerBoxRenderer) renderer).renderItem(tint, pose, buffer, light, overlay);
            }
        }
    }
}
