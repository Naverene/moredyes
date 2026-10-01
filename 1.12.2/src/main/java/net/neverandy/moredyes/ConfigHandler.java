package net.neverandy.moredyes;

import java.io.File;

import javax.swing.event.ChangeListener;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler
{
	public static Configuration config;
	
	public static boolean worldGenTree=true;
	public static boolean worldGenFlower=true;
	
	public static void loadConfig(File configFile)
	{
		config = new Configuration(configFile);
		config.load();
		load();
		MinecraftForge.EVENT_BUS.register(ChangeListener.class);
	}
	public static void load()
	{
		worldGenTree = config.getBoolean("worldGenTree", Configuration.CATEGORY_GENERAL, true, "Set to false to disable tree generation");
		worldGenFlower=config.getBoolean("worldGenFlower", Configuration.CATEGORY_GENERAL, true, "Set to false to disable flower generation");
		if(config.hasChanged())
		{
			config.save();
		}
	}
}
