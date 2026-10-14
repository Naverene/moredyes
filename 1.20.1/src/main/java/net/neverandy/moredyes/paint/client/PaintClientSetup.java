package net.neverandy.moredyes.paint.client;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.paint.PaintGunItem;
import net.neverandy.moredyes.reference.Reference;

import java.util.Map;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class PaintClientSetup
{
    private PaintClientSetup() {}

    /** Wraps every block model (not item models) so painted blocks show their paint. Runs last, over other mods' models. */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void wrapModels(ModelEvent.ModifyBakingResult event)
    {
        GreySprites.clear();
        Map<ResourceLocation, BakedModel> models = event.getModels();
        for (Map.Entry<ResourceLocation, BakedModel> entry : models.entrySet())
        {
            if (entry.getKey() instanceof ModelResourceLocation location && !location.getVariant().equals("inventory")
                    && !(entry.getValue() instanceof PaintedModel))
            {
                entry.setValue(new PaintedModel(entry.getValue()));
            }
        }
    }

    /** The gun's paint window (layer 1 of its model) shows the color it is set to. */
    @SubscribeEvent
    public static void itemColors(RegisterColorHandlersEvent.Item event)
    {
        event.register((stack, tintIndex) -> tintIndex == 1 ? PaintGunItem.getColor(stack) : 0xFFFFFF, MDItem.PAINT_GUN.get());
    }
}
