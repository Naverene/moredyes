package net.neverandy.moredyes.client;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.LlamaRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.ModTileEntities;

import java.util.List;

/**
 * Hooks up the renderers for dyed chests, signs, beds, banners and shulker boxes, dyed sheep, carpets on llamas and
 * moving dyed pistons.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientRenderers
{
    private ClientRenderers() {}

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerBlockEntityRenderer(ModTileEntities.CHEST.get(), ChestRenderer::new);
        event.registerBlockEntityRenderer(ModTileEntities.SIGN.get(), DyedSignRenderer::new);
        event.registerEntityRenderer(EntityType.SHEEP, DyedSheepRenderer::new);
        // Replaces the vanilla renderer for blocks being moved by pistons, so dyed pistons move their own heads.
        event.registerBlockEntityRenderer(BlockEntityType.PISTON, DyedPistonRenderer::new);
        // Dyed beds, banners and shulker boxes share vanilla's block entities, so these replace vanilla's renderers.
        event.registerBlockEntityRenderer(BlockEntityType.BED, DyedBedRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityType.BANNER, DyedBannerRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityType.SHULKER_BOX, DyedShulkerBoxRenderer::new);
    }

    /** In 1.18.2 the bed and shulker box atlases only hold the textures added to them, so the grey dyed ones are added. */
    @SubscribeEvent
    public static void stitch(TextureStitchEvent.Pre event)
    {
        if (event.getAtlas().location().equals(Sheets.BED_SHEET))
        {
            event.addSprite(DyedBedRenderer.BEDDING.texture());
            event.addSprite(DyedBedRenderer.FRAME.texture());
        }
        else if (event.getAtlas().location().equals(Sheets.SHULKER_SHEET))
        {
            event.addSprite(DyedShulkerBoxRenderer.MATERIAL.texture());
        }
    }

    /** Llamas wear dyed carpets. */
    @SubscribeEvent
    public static void layers(EntityRenderersEvent.AddLayers event)
    {
        for (EntityType<? extends Llama> type : List.of(EntityType.LLAMA, EntityType.TRADER_LLAMA))
        {
            if (event.getRenderer(type) instanceof LlamaRenderer renderer)
            {
                renderer.addLayer(new DyedLlamaDecorLayer(renderer, event.getEntityModels()));
            }
        }
    }
}
