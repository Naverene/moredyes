package net.neverandy.moredyes.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
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
        if (kind.tab() == Kind.Tab.PLANTS || id.endsWith("_sapling") || id.equals("glass") || id.contains("piston")) {
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
    }

    /** The dyed chest items are drawn as small chests, like vanilla's. */
    private static void clientExtensions(RegisterClientExtensionsEvent event) {
        IClientItemExtensions chest = new IClientItemExtensions() {
            private DyedChestItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = new DyedChestItemRenderer();
                }
                return renderer;
            }
        };
        event.registerItem(chest, ModBlocks.all(Kind.CHEST).stream().map(DeferredBlock::get).map(Block::asItem)
            .toArray(Item[]::new));
    }
}
