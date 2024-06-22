package info.kg6jay.moredyes.handler;

import info.kg6jay.moredyes.reference.Reference;

import java.io.File;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler
{
	public static Configuration config;
	public static boolean chisel_plank=true;
	public static boolean chisel_support=true;
	public static boolean worldgen_flower=true;
	public static boolean worldgen_tree=true;
	
	public static void init(File configFile)
	{
		if(config==null)
		{
			config = new Configuration(configFile);
			loadConfiguration();
		}
	}
	@SubscribeEvent
	public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.modID.equalsIgnoreCase(Reference.MOD_ID)){
			loadConfiguration();
		}
	}
	private static void loadConfiguration()
	{
		chisel_support = config.getBoolean("ChiselSupport_Overall",Configuration.CATEGORY_GENERAL,true,"Set to false to disable Chisel support entirely");
		chisel_plank = config.getBoolean("ChiselSupport_Plank",Configuration.CATEGORY_GENERAL,true,"Set to false to disable Chisel support for dyed planks");
		worldgen_flower=config.getBoolean("WorldGen_Flower",Configuration.CATEGORY_GENERAL,true,"Set to false to disable world gen of flowers");
		worldgen_tree=config.getBoolean("WorldGen_Tree", Configuration.CATEGORY_GENERAL, true, "Set to false to disable world gen of dye trees");
		if(config.hasChanged())
		{
			config.save();
		}
	}
}
