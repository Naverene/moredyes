package net.neverandy.moredyes.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LlamaRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;
import net.neverandy.moredyes.compat.ironchest.client.IronChestClient;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.neverandy.moredyes.compat.storagedrawers.client.StorageDrawersClient;
import net.neverandy.moredyes.registry.ModBlockEntities;
import net.neverandy.moredyes.registry.ModBlocks;

/**
 * Client setup: the colors of the dyed blocks and items, which blocks are see-through, and the renderers for dyed
 * chests, signs and pistons. Dyed sheep are drawn by {@code mixin/SheepFurLayerMixin}, dyed
 * collars by {@code mixin/WolfCollarLayerMixin} and {@code mixin/CatCollarLayerMixin}.
 */
public class MoreDyesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        renderLayers();
        blockColors();
        itemColors();
        renderers();
        // Optional compat, only touched when the other mod is installed.
        if (FabricLoader.getInstance().isModLoaded(StorageDrawersCompat.MOD_ID)) {
            StorageDrawersClient.register();
        }
        if (FabricLoader.getInstance().isModLoaded(IronChestCompat.MOD_ID)) {
            IronChestClient.register();
        }
    }

    private static void renderLayers() {
        for (Kind kind : Kind.values()) {
            RenderType layer = layer(kind);
            if (layer != null) {
                BlockRenderLayerMap.INSTANCE.putBlocks(layer, ModBlocks.all(kind).toArray(Block[]::new));
            }
        }
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.potted().toArray(Block[]::new));
    }

    /**
     * The layer a kind of block is drawn in, as vanilla draws its vanilla block, or null for solid blocks. Pistons and
     * bookshelves are cut out too: their wood and iron (or books) are an untinted layer with see-through gaps over the
     * tinted cobblestone (or wood).
     */
    private static RenderType layer(Kind kind) {
        if (kind.base() != null) {
            return layer(kind.base());
        }
        String id = kind.id();
        if (kind.tab() == Kind.Tab.PLANTS || id.endsWith("_sapling") || id.equals("glass") || id.contains("piston")
            || id.equals("chain") || id.equals("bookshelf")) {
            return RenderType.cutout();
        }
        if (id.endsWith("_leaves") || id.equals("glass_pane")) {
            return RenderType.cutoutMipped();
        }
        // Foggy glass is half see-through, so it needs the translucent layer, like ice.
        if (id.startsWith("foggy_glass") || id.equals("ice")) {
            return RenderType.translucent();
        }
        return null;
    }

    private static void blockColors() {
        for (Block block : ModBlocks.entries()) {
            int tint = Tints.blockTint(block);
            if (tint != -1) {
                ColorProviderRegistry.BLOCK.register((state, level, pos, tintIndex) -> tintIndex == 0 ? tint : -1, block);
            }
        }
    }

    /** A flat item model gives each layer its own tint index; only the first is the dyed one. */
    private static void itemColors() {
        for (Item item : BuiltInRegistries.ITEM) {
            if (!MoreDyes.MOD_ID.equals(BuiltInRegistries.ITEM.getKey(item).getNamespace())) {
                continue;
            }
            int tint = Tints.itemTint(item);
            if (tint != -1) {
                ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? tint : -1, item);
            }
        }
    }

    private static void renderers() {
        BlockEntityRendererRegistry.register(ModBlockEntities.CHEST, DyedChestRenderer::new);
        // Replaces vanilla's renderer for moving pistons, so dyed pistons draw their own head while retracting.
        BlockEntityRendererRegistry.register(BlockEntityType.PISTON, DyedPistonRenderer::new);
        // Replaces vanilla's sign renderer, which dyed signs share, so they are drawn in their color.
        BlockEntityRendererRegistry.register(BlockEntityType.SIGN, DyedSignRenderer::new);
        // Likewise for beds, banners and shulker boxes.
        BlockEntityRendererRegistry.register(BlockEntityType.BED, DyedBedRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntityType.BANNER, DyedBannerRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntityType.SHULKER_BOX, DyedShulkerBoxRenderer::new);
        // Llamas wear dyed carpets.
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((type, renderer, helper, context) -> {
            if (renderer instanceof LlamaRenderer llama) {
                helper.register(new DyedLlamaDecorLayer(llama, context.getModelSet()));
            }
        });
        // The dyed chest items are drawn as small chests, like vanilla's (their item model is builtin/entity).
        DyedChestItemRenderer chest = new DyedChestItemRenderer();
        for (Block block : ModBlocks.all(Kind.CHEST)) {
            BuiltinItemRendererRegistry.INSTANCE.register(block.asItem(), (stack, context, pose, buffer, light, overlay) -> {
                if (stack.getItem() instanceof BlockItem item) {
                    chest.render(item.getBlock(), pose, buffer, light, overlay);
                }
            });
        }
        // Dyed beds, banners and shulker boxes, drawn like their vanilla items.
        DyedItemRenderer others = new DyedItemRenderer();
        for (Kind kind : new Kind[]{Kind.BED, Kind.BANNER, Kind.SHULKER_BOX}) {
            for (Block block : ModBlocks.all(kind)) {
                BuiltinItemRendererRegistry.INSTANCE.register(block.asItem(),
                    (stack, context, pose, buffer, light, overlay) -> others.render(stack, pose, buffer, light, overlay));
            }
        }
    }
}
