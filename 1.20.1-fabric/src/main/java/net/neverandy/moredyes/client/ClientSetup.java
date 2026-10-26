package net.neverandy.moredyes.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.entity.LlamaRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neverandy.moredyes.block.DyedShapes;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.compat.ironchest.client.IronChestClient;
import net.neverandy.moredyes.compat.storagedrawers.client.StorageDrawersClient;
import net.neverandy.moredyes.tileentity.ModTileEntities;

/** Client-only setup: which blocks are see-through, the colors, and the renderers for chests, signs, pistons, beds, banners, shulker boxes, sheep and llamas. */
public final class ClientSetup implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        layers();
        ColorHandlers.register();
        ClientPackets.register();
        renderers();
        if (FabricLoader.getInstance().isModLoaded("storagedrawers"))
        {
            StorageDrawersClient.register();
        }
        if (FabricLoader.getInstance().isModLoaded("ironchest"))
        {
            IronChestClient.register();
        }
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
                layer(RenderType.cutout(), MDBlock.chainArray[i]);
                // The books on a dyed bookshelf are an untinted layer drawn over the tinted wood.
                layer(RenderType.cutout(), MDBlock.bookshelfArray[i]);
                layer(RenderType.cutout(), MDBlock.carpetArray[i]);
                for (MDBlock.PottedPlants potted : MDBlock.POTTED)
                {
                    layer(RenderType.cutout(), potted.pots[i]);
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
        for (Block block : blocks)
        {
            // Blocks the config turned off are null.
            if (block != null)
            {
                BlockRenderLayerMap.INSTANCE.putBlock(block, type);
            }
        }
    }

    private static void renderers()
    {
        BlockEntityRenderers.register(ModTileEntities.CHEST, ChestRenderer::new);
        BlockEntityRenderers.register(ModTileEntities.SIGN, DyedSignRenderer::new);
        // Replaces the vanilla renderer for blocks being moved by pistons, so dyed pistons move their own heads.
        BlockEntityRenderers.register(BlockEntityType.PISTON, DyedPistonRenderer::new);
        for (Block chest : MDBlock.chestArray)
        {
            if (chest == null)
            {
                continue;
            }
            BuiltinItemRendererRegistry.INSTANCE.register(chest, ChestItemRenderer.get());
        }
        // Dyed beds, banners and shulker boxes share vanilla's block entities, so these replace vanilla's renderers.
        BlockEntityRenderers.register(BlockEntityType.BED, DyedBedRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.BANNER, DyedBannerRenderer::new);
        BlockEntityRenderers.register(BlockEntityType.SHULKER_BOX, DyedShulkerBoxRenderer::new);
        for (Block[] blocks : new Block[][] {MDBlock.bedArray, MDBlock.bannerArray, MDBlock.shulkerBoxArray})
        {
            for (Block block : blocks)
            {
                if (block != null)
                {
                    BuiltinItemRendererRegistry.INSTANCE.register(block, DyedItemRenderer.get());
                }
            }
        }
        // Llamas wear dyed carpets.
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((type, renderer, helper, context) ->
        {
            if (renderer instanceof LlamaRenderer llama)
            {
                helper.register(new DyedLlamaDecorLayer(llama, context.getModelSet()));
            }
        });
    }
}
