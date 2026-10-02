package net.neverandy.moredyes.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neverandy.moredyes.block.DyedShapes;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.tileentity.ModTileEntities;

/** Client-only setup: which blocks are see-through, the colors, and the renderers for chests, pistons and sheep. */
public final class ClientSetup implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        layers();
        ColorHandlers.register();
        ClientPackets.register();
        renderers();
    }

    private static void layers()
    {
        {
            for (int i = 0; i < MDBlock.COLORS; i++)
            {
                layer(RenderType.cutout(), MDBlock.glassArray[i], MDBlock.tulipArray[i],
                        MDBlock.oakSaplingArray[i], MDBlock.birchSaplingArray[i], MDBlock.acaciaSaplingArray[i],
                        MDBlock.darkOakSaplingArray[i], MDBlock.jungleSaplingArray[i], MDBlock.spruceSaplingArray[i],
                        // The wood and iron on a dyed piston are an untinted layer drawn over the tinted cobblestone, with see-through gaps.
                        MDBlock.pistonArray[i], MDBlock.stickyPistonArray[i]);
                layer(RenderType.cutoutMipped(), MDBlock.glassPaneArray[i],
                        MDBlock.oakLeafArray[i], MDBlock.birchLeafArray[i], MDBlock.acaciaLeafArray[i],
                        MDBlock.darkOakLeafArray[i], MDBlock.jungleLeafArray[i], MDBlock.spruceLeafArray[i]);
                // Foggy glass is half see-through, so it needs the translucent layer.
                layer(RenderType.translucent(), MDBlock.glassFoggyArray[i], MDBlock.glassFoggyPaneArray[i], MDBlock.iceArray[i]);
                for (Block[] flowers : MDBlock.smallFlowerArrays)
                {
                    layer(RenderType.cutout(), flowers[i]);
                }
                for (Block[] flowers : MDBlock.tallFlowerArrays)
                {
                    layer(RenderType.cutout(), flowers[i]);
                }
            }
            for (DyedShapes shapes : DyedShapes.ALL)
            {
                if (shapes.layer == DyedShapes.Layer.SOLID)
                {
                    continue;
                }
                RenderType layer = shapes.layer == DyedShapes.Layer.CUTOUT ? RenderType.cutout() : RenderType.translucent();
                layer(layer, shapes.slabs);
                layer(layer, shapes.stairs);
                layer(layer, shapes.walls);
            }
        }
    }

    private static void layer(RenderType type, Block... blocks)
    {
        BlockRenderLayerMap.INSTANCE.putBlocks(type, blocks);
    }

    private static void renderers()
    {
        BlockEntityRenderers.register(ModTileEntities.CHEST, ChestRenderer::new);
        // Replaces the vanilla renderer for blocks being moved by pistons, so dyed pistons move their own heads.
        BlockEntityRenderers.register(BlockEntityType.PISTON, DyedPistonRenderer::new);
        EntityRendererRegistry.register(EntityType.SHEEP, DyedSheepRenderer::new);
        for (Block chest : MDBlock.chestArray)
        {
            BuiltinItemRendererRegistry.INSTANCE.register(chest, ChestItemRenderer.get());
        }
    }
}
