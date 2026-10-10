package info.kg6jay.moredyes.world;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import cpw.mods.fml.common.IWorldGenerator;
import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.block.MDBlocks;

/**
 * Generates granite, diorite and andesite in the overworld the way Minecraft 1.8 does: ten veins of up to 33 blocks
 * of each per chunk, replacing stone between y 0 and 80.
 */
public class StoneGenerator implements IWorldGenerator {

    private static final int VEINS_PER_CHUNK = 10;
    private static final int VEIN_SIZE = 33;
    private static final int MAX_Y = 80;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
        IChunkProvider chunkProvider) {
        if (world.provider.dimensionId != 0 || !FlatWorlds.allowsDecoration(chunkGenerator)) {
            return;
        }
        if (MoreDyes.config.worldgenGranite) {
            this.veins(MDBlocks.granitePlain.blockID, random, chunkX, chunkZ, world);
        }
        if (MoreDyes.config.worldgenDiorite) {
            this.veins(MDBlocks.dioritePlain.blockID, random, chunkX, chunkZ, world);
        }
        if (MoreDyes.config.worldgenAndesite) {
            this.veins(MDBlocks.andesitePlain.blockID, random, chunkX, chunkZ, world);
        }
    }

    private void veins(int blockID, Random random, int chunkX, int chunkZ, World world) {
        WorldGenMinable vein = new WorldGenMinable(blockID, VEIN_SIZE);
        for (int i = 0; i < VEINS_PER_CHUNK; i++) {
            vein.generate(world, random, chunkX * 16 + random.nextInt(16), random.nextInt(MAX_Y),
                chunkZ * 16 + random.nextInt(16));
        }
    }
}
