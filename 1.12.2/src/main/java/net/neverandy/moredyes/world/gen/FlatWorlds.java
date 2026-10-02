package net.neverandy.moredyes.world.gen;

import java.lang.reflect.Field;

import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.IChunkGenerator;

/**
 * Lets the world generators leave superflat worlds alone unless their settings ask for decoration, the way vanilla
 * trees and flowers do. This covers flat dimensions made by other mods (such as SimpleDimensions) too, since those use
 * the same chunk generator with their own settings.
 */
public final class FlatWorlds
{
	/** ChunkGeneratorFlat's settings. It is private, so it is found by type, which works with any mappings. */
	private static Field settings;
	private static boolean searched;

	private FlatWorlds()
	{
	}

	/** Whether More Dyes may decorate chunks made by this chunk generator. */
	public static boolean allowsDecoration(IChunkGenerator chunkGenerator)
	{
		if(!(chunkGenerator instanceof ChunkGeneratorFlat))
		{
			return true;
		}
		FlatGeneratorInfo info=settingsOf((ChunkGeneratorFlat)chunkGenerator);
		return info==null||info.getWorldFeatures().containsKey("decoration");
	}

	private static FlatGeneratorInfo settingsOf(ChunkGeneratorFlat generator)
	{
		if(!searched)
		{
			searched=true;
			for(Field field:ChunkGeneratorFlat.class.getDeclaredFields())
			{
				if(field.getType()==FlatGeneratorInfo.class)
				{
					field.setAccessible(true);
					settings=field;
					break;
				}
			}
		}
		if(settings==null)
		{
			return null;
		}
		try
		{
			return (FlatGeneratorInfo)settings.get(generator);
		}
		catch(IllegalAccessException e)
		{
			return null;
		}
	}
}
