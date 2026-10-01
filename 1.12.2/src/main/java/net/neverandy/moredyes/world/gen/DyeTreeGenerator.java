package net.neverandy.moredyes.world.gen;

import net.neverandy.moredyes.ConfigHandler;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.world.feature.WorldGenDyeTree;
import net.neverandy.moredyes.world.feature.WorldGenDyeTreeBig;
import net.neverandy.moredyes.world.feature.WorldGenDyeTreeHuge;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;


public class DyeTreeGenerator implements IWorldGenerator
{
	public void generate(Random random, int chunkX, int chunkZ, World world, net.minecraft.world.gen.IChunkGenerator chunkGenerator,IChunkProvider chunkProvider)
	{
		if(!ConfigHandler.worldGenTree)
		{
			return;
		}

		switch(world.provider.getDimension())
		{
		case -1:
			break;
		case 0:
			generateSurface(random,chunkX*16,chunkZ*16,world);
			break;
		case 1:
			break;
		default:
			generateSurface(random,chunkX*16,chunkZ*16,world);
		}
	}
	private void generateSurface(Random random, int x, int z, World world)
	{
		for(int b = 0; b< ColorStrings.ALL.length; b++)
		{
			if(random.nextInt(100)==0)
			{
				int xGen = random.nextInt(16)+x;
				int zGen = random.nextInt(16)+z;
				BlockPos genPos = world.getTopSolidOrLiquidBlock(new BlockPos(xGen,0,zGen));
				int r=random.nextInt(100);
				if(r>10)
				{
					new WorldGenDyeTree(true, false, b).generate(world, random, genPos);
				}
				else if(r>1)
				{
					new WorldGenDyeTreeBig(true, b).generate(world, random, genPos);
				}
				else
				{
					new WorldGenDyeTreeHuge(true, b).generate(world, random, genPos);
				}
			}
		}
	}
}
