package net.neverandy.moredyes.client;

import java.util.List;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.client.renderer.blockentity.state.BedRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;
import net.neverandy.moredyes.compat.ironchest.client.IronChestClient;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.neverandy.moredyes.compat.storagedrawers.client.StorageDrawersClient;
import net.neverandy.moredyes.registry.ModBlockEntities;
import net.neverandy.moredyes.registry.ModBlocks;

/**
 * The client side: render layers, tints and renderers. The sheep's wool color is drawn by SheepRendererMixin and
 * SheepRenderStateMixin; wolf and cat collar colors by Wolf/CatRendererMixin, CollarRenderStateMixin and
 * Wolf/CatCollarLayerMixin.
 */
public class MoreDyesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        blockTints();
        // The item tints and the chest's item model have no Fabric API, so they go straight into vanilla's lists of
        // types (made accessible in moredyes.accesswidener). This runs before any resources are loaded.
        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dye_color"), Tints.DyeColor.MAP_CODEC);
        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dye_glow"), Tints.DyeGlow.MAP_CODEC);
        ItemTintSources.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "state_color"), Tints.StateColor.MAP_CODEC);
        SpecialModelRenderers.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed_chest"),
            DyedChestSpecialRenderer.Unbaked.MAP_CODEC);
        SpecialModelRenderers.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed_bed"),
            DyedBedSpecialRenderer.Unbaked.MAP_CODEC);
        SpecialModelRenderers.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed_banner"),
            DyedBannerSpecialRenderer.Unbaked.MAP_CODEC);
        SpecialModelRenderers.ID_MAPPER.put(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyed_shulker_box"),
            DyedShulkerBoxSpecialRenderer.Unbaked.MAP_CODEC);
        renderers();
        // Optional compat, only touched when the other mod is installed.
        if (FabricLoader.getInstance().isModLoaded(StorageDrawersCompat.MOD_ID)) {
            StorageDrawersClient.register();
        }
        if (FabricLoader.getInstance().isModLoaded(IronChestCompat.MOD_ID)) {
            IronChestClient.register();
        }
    }

    private static void blockTints() {
        for (Block block : ModBlocks.entries()) {
            int tint = Tints.blockTint(block);
            if (tint != -1) {
                BlockColorRegistry.register(List.of(BlockTintSources.constant(tint), BlockTintSources.constant(Tints.glow(tint))), block);
            }
        }
    }

    private static void renderers() {
        BlockEntityRendererRegistry.register(ModBlockEntities.CHEST, DyedChestRenderer::new);
        // Replaces vanilla's renderer for moving pistons, so dyed pistons draw their own head while retracting.
        BlockEntityRendererRegistry.register(BlockEntityType.PISTON, context -> new DyedPistonRenderer());
        // Replaces vanilla's sign renderer, which dyed signs share, so they are drawn in their color.
        BlockEntityRendererRegistry.register(BlockEntityType.SIGN, DyedSignRenderer::new);
        // Likewise for beds, banners and shulker boxes.
        BlockEntityRendererRegistry.register(BlockEntityType.BED, DyedBedRenderer::new);
        BlockEntityRendererRegistry.register(ModBlockEntities.BED,
            (BlockEntityRendererProvider<BedBlockEntity, BedRenderState>) DyedBedRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntityType.BANNER, DyedBannerRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntityType.SHULKER_BOX, DyedShulkerBoxRenderer::new);
    }
}
