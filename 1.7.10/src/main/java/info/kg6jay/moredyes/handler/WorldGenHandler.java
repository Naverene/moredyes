package info.kg6jay.moredyes.handler;

import info.kg6jay.moredyes.world.gen.FlowerGenerator;
import info.kg6jay.moredyes.world.gen.TreeGenerator;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class WorldGenHandler
{
	public static void initializeWorldGen()
	{
		if(ConfigHandler.worldgen_flower)
			registerWorldGen(new FlowerGenerator(),1);
		if(ConfigHandler.worldgen_tree)
			registerWorldGen(new TreeGenerator(),1);
	}
	public static void registerWorldGen(IWorldGenerator worldGenerator,int weightedProbability)
	{
		GameRegistry.registerWorldGenerator(worldGenerator, weightedProbability);
	}
}
