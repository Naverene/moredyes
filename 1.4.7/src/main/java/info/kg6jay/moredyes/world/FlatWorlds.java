package info.kg6jay.moredyes.world;

import java.lang.reflect.Field;

import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.FlatGeneratorInfo;

/**
 * Lets the world generators leave superflat worlds alone unless their settings ask for decoration, the way vanilla
 * trees, flowers and ores do.
 */
public final class FlatWorlds {

    /** ChunkProviderFlat's settings. It is private, so it is found by type, which works with any mappings. */
    private static Field settings;
    private static boolean searched;

    private FlatWorlds() {}

    /** Whether More Dyes may decorate chunks made by this chunk provider. */
    public static boolean allowsDecoration(IChunkProvider chunkGenerator) {
        if (!(chunkGenerator instanceof ChunkProviderFlat)) {
            return true;
        }
        FlatGeneratorInfo info = settingsOf((ChunkProviderFlat) chunkGenerator);
        return info == null || info.getWorldFeatures().containsKey("decoration");
    }

    private static FlatGeneratorInfo settingsOf(ChunkProviderFlat provider) {
        if (!searched) {
            searched = true;
            for (Field field : ChunkProviderFlat.class.getDeclaredFields()) {
                if (field.getType() == FlatGeneratorInfo.class) {
                    field.setAccessible(true);
                    settings = field;
                    break;
                }
            }
        }
        if (settings == null) {
            return null;
        }
        try {
            return (FlatGeneratorInfo) settings.get(provider);
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
