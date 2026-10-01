package net.neverandy.moredyes.world.gen;

import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WorldGenerator
{
	public static void initializeWorldGen()
	{
		registerWorldGen(new FlowerGenerator(),1);
		registerWorldGen(new DyeTreeGenerator(),1);
	}
	public static void registerWorldGen(IWorldGenerator worldGenerator,int weightedProbability)
	{
		GameRegistry.registerWorldGenerator(worldGenerator, weightedProbability);
	}
}
