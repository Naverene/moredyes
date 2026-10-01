package net.neverandy.moredyes;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.proxy.IProxy;
import net.neverandy.moredyes.recipe.CraftManager;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.LogHelper;
import net.neverandy.moredyes.world.gen.WorldGenerator;

@Mod(modid= Reference.MOD_ID,name=Reference.MOD_NAME,version=Reference.MOD_VERSION)
public class MoreDyes
{
	@Mod.Instance(Reference.MOD_ID)
	public static MoreDyes instance;
	
	@SidedProxy(clientSide=Reference.CLIENT_PROXY,serverSide=Reference.SERVER_PROXY)
	public static IProxy proxy;
	
	//Creative Tabs
	public static CreativeTabs tabDyes= new Tab("Dyes");
	public static CreativeTabs tabBlocks = new Tab("Blocks");
	public static CreativeTabs tabPlants = new Tab("Plants");
	public static CreativeTabs tabTrees = new Tab("Trees");
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ConfigHandler.loadConfig(event.getSuggestedConfigurationFile());
		MDItem.initialize();
		MDBlock.initialize();
		//proxy.initializeModels();
		//MDBlock.registerOreDictionary();
		WorldGenerator.initializeWorldGen();
		LogHelper.info("Pre Initialization Complete");
	}
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		CraftManager.registerCraftingRecipes();
		CraftManager.registerSmeltingRecipes();
		LogHelper.info("Initialization Complete");
	}
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		((Tab) tabDyes).setTabIconItem(MDItem.dye[0]);
		((Tab) tabBlocks).setTabIconItem(Item.getItemFromBlock(MDBlock.sand[0]));
		((Tab) tabPlants).setTabIconItem(Item.getItemFromBlock(MDBlock.tulip[30]));
		((Tab) tabTrees).setTabIconItem(Item.getItemFromBlock(MDBlock.sapling[30]));
		LogHelper.info("Post Initialization Complete");
	}
}