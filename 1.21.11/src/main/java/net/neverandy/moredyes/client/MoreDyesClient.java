package net.neverandy.moredyes.client;

import com.google.common.reflect.TypeToken;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import net.minecraft.client.renderer.entity.state.WolfRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;
import net.neverandy.moredyes.compat.ironchest.client.IronChestClient;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.neverandy.moredyes.compat.storagedrawers.client.StorageDrawersClient;
import net.neverandy.moredyes.entity.DyedCollars;
import net.neverandy.moredyes.entity.DyedSheep;
import net.neverandy.moredyes.registry.ModBlockEntities;
import net.neverandy.moredyes.registry.ModBlocks;

@Mod(value = MoreDyes.MOD_ID, dist = Dist.CLIENT)
public class MoreDyesClient {

    /** The More Dyes color of a sheep being drawn, if it has one. Read by SheepRenderStateMixin. */
    public static final ContextKey<MixColor> SHEEP_COLOR = new ContextKey<>(
        Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "sheep_color"));

    /** The More Dyes collar color of a wolf or cat being drawn, if it has one. Read by Wolf/CatCollarLayerMixin. */
    public static final ContextKey<MixColor> COLLAR_COLOR = new ContextKey<>(
        Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "collar_color"));

    public MoreDyesClient(IEventBus modBus) {
        modBus.addListener(MoreDyesClient::renderLayers);
        modBus.addListener(MoreDyesClient::blockTints);
        modBus.addListener(MoreDyesClient::itemTints);
        modBus.addListener(MoreDyesClient::specialRenderers);
        modBus.addListener(MoreDyesClient::renderers);
        modBus.addListener(MoreDyesClient::renderStateModifiers);
        // Optional compat, only touched when the other mod is installed.
        if (ModList.get().isLoaded(StorageDrawersCompat.MOD_ID)) {
            StorageDrawersClient.register(modBus);
        }
        if (ModList.get().isLoaded(IronChestCompat.MOD_ID)) {
            IronChestClient.register(modBus);
        }
    }

    /**
     * Draws each dyed block in the same render layer as its vanilla block: glass, ice and glass panes translucent,
     * plants cut out. (Leaves pick their layer from the graphics setting by themselves, being LeavesBlocks.)
     */
    @SuppressWarnings("deprecation")
    private static void renderLayers(FMLClientSetupEvent event) {
        for (Kind kind : Kind.values()) {
            ChunkSectionLayer layer = ItemBlockRenderTypes.getChunkRenderType(kind.vanilla().defaultBlockState());
            if (layer != ChunkSectionLayer.SOLID) {
                ModBlocks.all(kind).forEach(block -> ItemBlockRenderTypes.setRenderLayer(block.get(), layer));
            }
        }
        ChunkSectionLayer potted = ItemBlockRenderTypes.getChunkRenderType(Blocks.POTTED_WHITE_TULIP.defaultBlockState());
        ModBlocks.potted().forEach(block -> ItemBlockRenderTypes.setRenderLayer(block.get(), potted));
    }

    private static void blockTints(RegisterColorHandlersEvent.Block event) {
        for (var entry : ModBlocks.BLOCKS.getEntries()) {
            Block block = entry.get();
            int tint = Tints.blockTint(block);
            if (tint != -1) {
                event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? tint : -1, block);
            }
        }
    }

    private static void itemTints(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dye_color"), Tints.DyeColor.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "state_color"), Tints.StateColor.MAP_CODEC);
    }

    private static void specialRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed_chest"),
            DyedChestSpecialRenderer.Unbaked.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed_bed"),
            DyedBedSpecialRenderer.Unbaked.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed_banner"),
            DyedBannerSpecialRenderer.Unbaked.MAP_CODEC);
        event.register(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed_shulker_box"),
            DyedShulkerBoxSpecialRenderer.Unbaked.MAP_CODEC);
    }

    private static void renderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.CHEST.get(), DyedChestRenderer::new);
        // Replaces vanilla's renderer for moving pistons, so dyed pistons draw their own head while retracting.
        event.registerBlockEntityRenderer(BlockEntityType.PISTON, context -> new DyedPistonRenderer());
        // Replaces vanilla's sign renderer, which dyed signs share, so they are drawn in their color.
        event.registerBlockEntityRenderer(BlockEntityType.SIGN, DyedSignRenderer::new);
        // Likewise for beds, banners and shulker boxes.
        event.registerBlockEntityRenderer(BlockEntityType.BED, DyedBedRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.BED.get(), DyedBedRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityType.BANNER, DyedBannerRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityType.SHULKER_BOX, DyedShulkerBoxRenderer::new);
    }

    private static void renderStateModifiers(RegisterRenderStateModifiersEvent event) {
        event.registerEntityModifier(new TypeToken<SheepRenderer>() {}, (Sheep sheep, SheepRenderState state) ->
            state.setRenderData(SHEEP_COLOR, DyedSheep.getColor(sheep).orElse(null)));
        event.registerEntityModifier(new TypeToken<WolfRenderer>() {}, (Wolf wolf, WolfRenderState state) ->
            state.setRenderData(COLLAR_COLOR, DyedCollars.getColor(wolf).orElse(null)));
        event.registerEntityModifier(new TypeToken<CatRenderer>() {}, (Cat cat, CatRenderState state) ->
            state.setRenderData(COLLAR_COLOR, DyedCollars.getColor(cat).orElse(null)));
    }
}
