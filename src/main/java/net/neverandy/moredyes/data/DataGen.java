package net.neverandy.moredyes.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.data.client.ModBlockStateProvider;
import net.neverandy.moredyes.data.client.ModItemModelProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = MoreDyes.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGen {
    public static final Logger LOGGER = LogManager.getLogger();
    private DataGen(){}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        LOGGER.info("add provider blocks");
        gen.addProvider(new ModBlockStateProvider(gen, existingFileHelper));
        LOGGER.info("add provider items");
        gen.addProvider(new ModItemModelProvider(gen, existingFileHelper));
    }
}
