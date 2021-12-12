package net.neverandy.moredyes;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;


@Mod.EventBusSubscriber
public class ConfigHandler
{
    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static boolean worldGenTree = true;
    public static boolean worldGenFlower = true;

    static
    {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent)
    {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent)
    {

    }
}
