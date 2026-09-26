package info.kg6jay.moredyes.world.gen;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import cpw.mods.fml.common.IWorldGenerator;
import info.kg6jay.moredyes.block.MDBlock;

/**
 * Generates diorite in the overworld the way Minecraft 1.8 does: ten veins of up to 33 blocks per chunk, replacing
 * stone between y 0 and 80. Does nothing when another mod provides diorite (see MDBlock.useOwnDiorite).
 */
public class DioriteGenerator implements IWorldGenerator {

    private static final int VEINS_PER_CHUNK = 10;
    private static final int VEIN_SIZE = 33;
    private static final int MAX_Y = 80;

    private WorldGenMinable vein;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
        IChunkProvider chunkProvider) {
        if (!MDBlock.useOwnDiorite || world.provider.dimensionId != 0) {
            return;
        }
        if (this.vein == null) {
            this.vein = new WorldGenMinable(MDBlock.dioritePlain, 0, VEIN_SIZE, Blocks.stone);
        }
        for (int i = 0; i < VEINS_PER_CHUNK; i++) {
            int x = chunkX * 16 + random.nextInt(16);
            int y = random.nextInt(MAX_Y);
            int z = chunkZ * 16 + random.nextInt(16);
            this.vein.generate(world, random, x, y, z);
        }
    }
}
