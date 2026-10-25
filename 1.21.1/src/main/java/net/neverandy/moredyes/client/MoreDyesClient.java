package net.neverandy.moredyes.client;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LlamaRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.neverandy.moredyes.compat.storagedrawers.client.StorageDrawersClient;
import net.neverandy.moredyes.registry.ModBlockEntities;
import net.neverandy.moredyes.registry.ModBlocks;
import net.neverandy.moredyes.registry.ModItems;

/**
 * Client setup: the colors of the dyed blocks and items, which blocks are see-through, and the renderers for dyed
 * chests, signs and pistons. Dyed sheep are drawn by {@code mixin/SheepFurLayerMixin}.
 */
@Mod(value = MoreDyes.MOD_ID, dist = Dist.CLIENT)
public class MoreDyesClient {

    public MoreDyesClient(IEventBus modBus) {
        modBus.addListener(MoreDyesClient::setup);
        modBus.addListener(MoreDyesClient::blockColors);
        modBus.addListener(MoreDyesClient::itemColors);
        modBus.addListener(MoreDyesClient::renderers);
        modBus.addListener(MoreDyesClient::layers);
        modBus.addListener(MoreDyesClient::clientExtensions);
        // Optional compat, only touched when the other mod is installed.
        if (ModList.get().isLoaded(StorageDrawersCompat.MOD_ID)) {
            StorageDrawersClient.register(modBus);
        }
    }

    @SuppressWarnings("deprecation")
    private static void setup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            for (Kind kind : Kind.values()) {
                RenderType layer = layer(kind);
                if (layer != null) {
                    ModBlocks.all(kind).forEach(block -> ItemBlockRenderTypes.setRenderLayer(block.get(), layer));
                }
            }
            ModBlocks.potted().forEach(block -> ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout()));
        });
    }

    /**
     * The layer a kind of block is drawn in, as vanilla draws its vanilla block, or null for solid blocks. Pistons are
     * cut out too: their wood and iron are an untinted layer over the tinted cobblestone, with see-through gaps.
     */
    private static RenderType layer(Kind kind) {
        if (kind.base() != null) {
            return layer(kind.base());
        }
        String id = kind.id();
        if (kind.tab() == Kind.Tab.PLANTS || id.endsWith("_sapling") || id.equals("glass") || id.contains("piston")
            || id.equals("chain")) {
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

    private static void blockColors(RegisterColorHandlersEvent.Block event) {
        for (var entry : ModBlocks.BLOCKS.getEntries()) {
            Block block = entry.get();
            int tint = Tints.blockTint(block);
            if (tint != -1) {
                event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? tint : -1, block);
            }
        }
    }

    /** A flat item model gives each layer its own tint index; only the first is the dyed one. */
    private static void itemColors(RegisterColorHandlersEvent.Item event) {
        for (var entry : ModItems.ITEMS.getEntries()) {
            Item item = entry.get();
            int tint = Tints.itemTint(item);
            if (tint != -1) {
                event.register((stack, tintIndex) -> tintIndex == 0 ? tint : -1, item);
            }
        }
    }

    private static void renderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.CHEST.get(), DyedChestRenderer::new);
        // Replaces vanilla's renderer for moving pistons, so dyed pistons draw their own head while retracting.
        event.registerBlockEntityRenderer(BlockEntityType.PISTON, DyedPistonRenderer::new);
        // Replaces vanilla's sign renderer, which dyed signs share, so they are drawn in their color.
        event.registerBlockEntityRenderer(BlockEntityType.SIGN, DyedSignRenderer::new);
        // Likewise for beds, banners and shulker boxes.
        event.registerBlockEntityRenderer(BlockEntityType.BED, DyedBedRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityType.BANNER, DyedBannerRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityType.SHULKER_BOX, DyedShulkerBoxRenderer::new);
    }

    /** Llamas wear dyed carpets. */
    private static void layers(EntityRenderersEvent.AddLayers event) {
        for (EntityType<? extends Llama> type : List.of(EntityType.LLAMA, EntityType.TRADER_LLAMA)) {
            if (event.getRenderer(type) instanceof LlamaRenderer renderer) {
                renderer.addLayer(new DyedLlamaDecorLayer(renderer, event.getEntityModels()));
            }
        }
    }

    /**
     * The dyed chest items are drawn as small chests, like vanilla's, and the dyed beds, banners and shulker boxes
     * like their vanilla items.
     */
    private static void clientExtensions(RegisterClientExtensionsEvent event) {
        register(event, DyedChestItemRenderer::new, Kind.CHEST);
        register(event, DyedItemRenderer::new, Kind.BED, Kind.BANNER, Kind.SHULKER_BOX);
    }

    private static void register(RegisterClientExtensionsEvent event,
        Supplier<BlockEntityWithoutLevelRenderer> factory, Kind... kinds) {
        Item[] items = Stream.of(kinds).flatMap(kind -> ModBlocks.all(kind).stream()).map(DeferredBlock::get)
            .map(Block::asItem).toArray(Item[]::new);
        // With the kinds turned off in the config there are none, and NeoForge refuses an empty list.
        if (items.length == 0) {
            return;
        }
        event.registerItem(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = factory.get();
                }
                return renderer;
            }
        }, items);
    }
}
