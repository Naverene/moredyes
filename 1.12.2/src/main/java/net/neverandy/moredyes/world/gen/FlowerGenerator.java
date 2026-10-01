package net.neverandy.moredyes.world.gen;

import net.neverandy.moredyes.ConfigHandler;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.reference.ColorStrings;
import net.minecraft.block.BlockBush;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;


public class FlowerGenerator implements IWorldGenerator
{
	public void generate(Random random, int chunkX, int chunkZ, World world, net.minecraft.world.gen.IChunkGenerator chunkGenerator,IChunkProvider chunkProvider)
	{
		if(!ConfigHandler.worldGenFlower)
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
			if(random.nextInt(100)<=5)
			{
				int xGen = random.nextInt(16)+x;
				int zGen = random.nextInt(16)+z;
				BlockPos genPos = world.getTopSolidOrLiquidBlock(new BlockPos(xGen,0,zGen));
				if(world.isAirBlock(genPos))
				{
					if(world.provider.hasSkyLight())
					{
						if(genPos.getY()<255)
						{
							BlockPos dirtPos=genPos;
							dirtPos.offset(EnumFacing.DOWN);
							if(((BlockBush) MDBlock.tulip[b]).canBlockStay(world, dirtPos,MDBlock.tulip[b].getDefaultState()))
							{
								world.setBlockState(genPos, MDBlock.tulip[b].getDefaultState());
							}
						}
					}
				}
			}
		}
	}
}
