package net.neverandy.moredyes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Draws the dyed bed, banner and shulker box items. Their item models are vanilla's ({@code builtin/entity}), which
 * hand the drawing to this. Like vanilla's item renderer, it draws a block entity of the item's block, so the
 * Dyed*Renderer classes draw it in its color.
 */
public class DyedItemRenderer extends BlockEntityWithoutLevelRenderer
{
    private static DyedItemRenderer instance;
    private final Map<Block, BlockEntity> entities = new HashMap<>();

    private DyedItemRenderer()
    {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    public static DyedItemRenderer get()
    {
        if (instance == null)
        {
            instance = new DyedItemRenderer();
        }
        return instance;
    }

    /** Nothing to bake: the block entity renderers draw the items. */
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager)
    {
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transform, PoseStack pose, MultiBufferSource buffer, int light, int overlay)
    {
        if (!(stack.getItem() instanceof BlockItem item))
        {
            return;
        }
        BlockEntity entity = entities.computeIfAbsent(item.getBlock(), block ->
        {
            if (block instanceof BedBlock)
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
