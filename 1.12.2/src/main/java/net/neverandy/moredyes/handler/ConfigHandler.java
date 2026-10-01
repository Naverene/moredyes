package net.neverandy.moredyes.handler;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.neverandy.moredyes.reference.Reference;

public class ConfigHandler
{
	public static Configuration config;

	public static boolean worldGenTree=true;
	public static boolean worldGenFlower=true;
	public static boolean preventMobSpawning=true;

	public static void loadConfig(File configFile)
	{
		if(config==null)
		{
			config=new Configuration(configFile);
			load();
		}
	}
	@SubscribeEvent
	public void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.getModID().equalsIgnoreCase(Reference.MOD_ID))
		{
			load();
		}
	}
	public static void load()
	{
		worldGenTree=config.getBoolean("worldGenTree",Configuration.CATEGORY_GENERAL,true,"Set to false to disable tree generation");
		worldGenFlower=config.getBoolean("worldGenFlower",Configuration.CATEGORY_GENERAL,true,"Set to false to disable flower generation");
		preventMobSpawning=config.getBoolean("preventMobSpawning",Configuration.CATEGORY_GENERAL,true,"If true, mobs cannot spawn on any block from this mod. If false, the blocks follow the vanilla rules.");
		if(config.hasChanged())
		{
			config.save();
		}
	}
}
