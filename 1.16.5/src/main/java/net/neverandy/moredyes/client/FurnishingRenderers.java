package net.neverandy.moredyes.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LlamaRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.neverandy.moredyes.reference.Reference;

/** Hooks up the renderers of dyed beds, banners and shulker boxes, and dyed carpets on llamas. */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class FurnishingRenderers
{
    private FurnishingRenderers() {}

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event)
    {
        // These replace vanilla's renderers, which the dyed blocks share; they draw vanilla's blocks as before.
        ClientRegistry.bindTileEntityRenderer(TileEntityType.BED, DyedBedRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityType.BANNER, DyedBannerRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityType.SHULKER_BOX, DyedShulkerBoxRenderer::new);
        event.enqueueWork(() ->
        {
            for (EntityType<?> type : new EntityType<?>[] {EntityType.LLAMA, EntityType.TRADER_LLAMA})
            {
                EntityRenderer<?> renderer = Minecraft.getInstance().getRenderManager().renderers.get(type);
                if (renderer instanceof LlamaRenderer)
                {
                    LlamaRenderer llama = (LlamaRenderer) renderer;
                    llama.addLayer(new DyedLlamaDecorLayer(llama));
                }
            }
        });
    }

    /** The grey bed and shulker box textures are not on their atlases unless we add them. */
    @SubscribeEvent
    public static void stitch(TextureStitchEvent.Pre event)
    {
        if (event.getMap().getTextureLocation().equals(Atlases.BED_ATLAS))
        {
            event.addSprite(DyedBedRenderer.BEDDING.getTextureLocation());
            event.addSprite(DyedBedRenderer.FRAME.getTextureLocation());
        }
        else if (event.getMap().getTextureLocation().equals(Atlases.SHULKER_BOX_ATLAS))
        {
            event.addSprite(DyedShulkerBoxRenderer.MATERIAL.getTextureLocation());
        }
    }
}
