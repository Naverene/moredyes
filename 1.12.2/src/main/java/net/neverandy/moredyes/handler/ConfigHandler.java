package net.neverandy.moredyes.handler;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.neverandy.moredyes.reference.Reference;

public class ConfigHandler
{
	public static Configuration config;

	public static boolean worldGenTree=true;
	public static boolean worldGenFlower=true;
	public static boolean preventMobSpawning=true;
	public static float sheepSpawnChance=0.05f;
	public static boolean gregtechRecipes=true;
	public static boolean ironChests=true;
	public static boolean storageDrawers=true;

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
	/** Whether the dyed Iron Chests are added: Iron Chests is installed and the config keeps them. */
	public static boolean ironChestsAdded()
	{
		return ironChests&&Loader.isModLoaded(IronChestCompat.MOD_ID);
	}
	/** Whether the dyed Storage Drawers are added: Storage Drawers is installed and the config keeps them. */
	public static boolean storageDrawersAdded()
	{
		return storageDrawers&&Loader.isModLoaded(StorageDrawersCompat.MOD_ID);
	}
	public static void load()
	{
		worldGenTree=config.getBoolean("worldGenTree",Configuration.CATEGORY_GENERAL,true,"Set to false to disable tree generation");
		worldGenFlower=config.getBoolean("worldGenFlower",Configuration.CATEGORY_GENERAL,true,"Set to false to disable flower generation");
		preventMobSpawning=config.getBoolean("preventMobSpawning",Configuration.CATEGORY_GENERAL,true,"If true, mobs cannot spawn on any block from this mod. If false, the blocks follow the vanilla rules.");
		sheepSpawnChance=config.getFloat("sheepSpawnChance",Configuration.CATEGORY_GENERAL,0.05f,0.0f,1.0f,"The chance that a sheep gets a random More Dyes color when it first appears in the world. 0 turns it off.");
		gregtechRecipes=config.getBoolean("gregtechRecipes",Configuration.CATEGORY_GENERAL,true,"Set to false to leave out the GregTech mixer and chemical bath recipes. Only matters when GregTech is installed, and takes effect after a restart.");
		ironChests=config.getBoolean("ironChests",Configuration.CATEGORY_GENERAL,true,"Set to false to leave out the dyed Iron Chests. Only matters when Iron Chests is installed, and takes effect after a restart.");
		storageDrawers=config.getBoolean("storageDrawers",Configuration.CATEGORY_GENERAL,true,"Set to false to leave out the dyed Storage Drawers. Only matters when Storage Drawers is installed, and takes effect after a restart.");
		if(config.hasChanged())
		{
			config.save();
		}
	}
}
