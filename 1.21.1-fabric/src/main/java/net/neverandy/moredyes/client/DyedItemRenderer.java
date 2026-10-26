package net.neverandy.moredyes.client;

import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
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
import net.neverandy.moredyes.block.DyedBedBlockEntity;

/**
 * Draws the dyed bed, banner and shulker box items. Their item models are vanilla's ({@code builtin/entity}), which
 * Fabric hands to this (see MoreDyesClient). Like vanilla's item renderer, it draws a block entity of the item's block, so the
 * Dyed*Renderer classes draw it in its color.
 */
public class DyedItemRenderer {

    private final Map<Block, BlockEntity> entities = new HashMap<>();

    public void render(ItemStack stack, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
        if (!(stack.getItem() instanceof BlockItem item)) {
            return;
        }
        Block block = item.getBlock();
        BlockEntity entity = entities.computeIfAbsent(block, b -> {
            if (b instanceof DyedBedBlock) {
                return new DyedBedBlockEntity(BlockPos.ZERO, b.defaultBlockState());
            } else if (b instanceof BedBlock) {
                return new BedBlockEntity(BlockPos.ZERO, b.defaultBlockState());
            } else if (b instanceof BannerBlock) {
                return new BannerBlockEntity(BlockPos.ZERO, b.defaultBlockState());
            } else if (b instanceof ShulkerBoxBlock) {
                return new ShulkerBoxBlockEntity(BlockPos.ZERO, b.defaultBlockState());
            }
            return null;
        });
        if (entity instanceof DyedBedBlockEntity bed) {
            bed.setWood(DyedBedBlockEntity.woodOf(stack));
        }
        if (entity instanceof BannerBlockEntity banner) {
            banner.fromItem(stack, DyeColor.WHITE);
        }
        if (entity != null) {
            Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(entity, pose, buffer, light, overlay);
        }
    }
}
