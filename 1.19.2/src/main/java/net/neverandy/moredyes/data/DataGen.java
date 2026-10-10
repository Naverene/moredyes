package net.neverandy.moredyes.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.neverandy.moredyes.data.client.ModBlockStateProvider;
import net.neverandy.moredyes.data.client.ModItemModelProvider;
import net.neverandy.moredyes.data.client.ModLangProvider;
import net.neverandy.moredyes.data.server.ModBlockTagsProvider;
import net.neverandy.moredyes.data.server.ModChiselProvider;
import net.neverandy.moredyes.data.server.ModItemTagsProvider;
import net.neverandy.moredyes.data.server.ModLootTableProvider;
import net.neverandy.moredyes.data.server.ModRecipeProvider;
import net.neverandy.moredyes.data.server.ModWorldGenProvider;
import net.neverandy.moredyes.reference.Reference;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGen
{
    private DataGen() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        gen.addProvider(event.includeClient(), new ModBlockStateProvider(gen, existingFileHelper));
        gen.addProvider(event.includeClient(), new ModItemModelProvider(gen, existingFileHelper));
        gen.addProvider(event.includeClient(), new ModLangProvider(gen, Reference.MOD_ID, "en_us"));
        ModBlockTagsProvider blockTags = new ModBlockTagsProvider(gen, existingFileHelper);
        gen.addProvider(event.includeServer(), blockTags);
        gen.addProvider(event.includeServer(), new ModItemTagsProvider(gen, blockTags, existingFileHelper));
        gen.addProvider(event.includeServer(), new ModRecipeProvider(gen));
        gen.addProvider(event.includeServer(), new ModLootTableProvider(gen));
        gen.addProvider(event.includeServer(), new ModChiselProvider(gen));
        gen.addProvider(event.includeServer(), new ModWorldGenProvider(gen));
    }
}
