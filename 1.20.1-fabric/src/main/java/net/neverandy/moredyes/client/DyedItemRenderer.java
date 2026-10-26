package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.neverandy.moredyes.block.DyedBedBlock;
import net.neverandy.moredyes.tileentity.DyedBedBlockEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Draws the dyed bed, banner and shulker box items. Their item models are vanilla's ({@code builtin/entity}), which
 * hand the drawing to this (registered in ClientSetup). Like vanilla's item renderer, it draws a block entity of the item's block, so the
 * Dyed*Renderer classes draw it in its color.
 */
public class DyedItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer
{
    private static DyedItemRenderer instance;
    private final Map<Block, BlockEntity> entities = new HashMap<>();

    private DyedItemRenderer() {}

    public static DyedItemRenderer get()
    {
        if (instance == null)
        {
            instance = new DyedItemRenderer();
        }
        return instance;
    }

    @Override
    public void render(ItemStack stack, ItemDisplayContext context, PoseStack pose, MultiBufferSource buffer, int light, int overlay)
    {
        if (!(stack.getItem() instanceof BlockItem item))
        {
            return;
        }
        BlockEntity entity = entities.computeIfAbsent(item.getBlock(), block ->
        {
            if (block instanceof DyedBedBlock)
            {
                return new DyedBedBlockEntity(BlockPos.ZERO, block.defaultBlockState());
            }
            else if (block instanceof BedBlock)
            {
                return new BedBlockEntity(BlockPos.ZERO, block.defaultBlockState());
            }
            else if (block instanceof BannerBlock)
            {
                return new BannerBlockEntity(BlockPos.ZERO, block.defaultBlockState());
            }
            else if (block instanceof ShulkerBoxBlock)
            {
                return new ShulkerBoxBlockEntity(BlockPos.ZERO, block.defaultBlockState());
            }
            return null;
        });
        if (entity instanceof DyedBedBlockEntity bed)
        {
            bed.setWood(DyedBedBlockEntity.woodOf(stack));
        }
        if (entity instanceof BannerBlockEntity banner)
        {
            banner.fromItem(stack, DyeColor.WHITE);
        }
        if (entity != null)
        {
            Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(entity, pose, buffer, light, overlay);
        }
    }
}
