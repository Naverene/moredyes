package info.kg6jay.moredyes.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import cpw.mods.fml.common.IWorldGenerator;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.MoreDyes;
import info.kg6jay.moredyes.block.Dyed;
import info.kg6jay.moredyes.block.MDBlocks;

/**
 * Scatters dyed tulips and cornflowers, and grows dye trees, on the surface of the overworld and of other dimensions
 * that are not the Nether or the End. Each color gets its own chance per chunk, as in the other versions.
 */
public class PlantGenerator implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
        IChunkProvider chunkProvider) {
        int dimension = world.provider.dimensionId;
        if (dimension == -1 || dimension == 1 || !FlatWorlds.allowsDecoration(chunkGenerator)) {
            return;
        }
        int x = chunkX * 16, z = chunkZ * 16;
        if (MoreDyes.config.worldgenFlower) {
            this.flowers(MDBlocks.tulip, random, x, z, world);
            this.flowers(MDBlocks.cornflower, random, x, z, world);
        }
        if (MoreDyes.config.worldgenTree) {
            for (int color = 0; color < Colors.COUNT; color++) {
                if (random.nextInt(100) == 0) {
                    int tx = x + random.nextInt(16) + 8, tz = z + random.nextInt(16) + 8;
                    new WorldGenTreeDye(false, 4, color).generate(world, random, tx,
                        world.getTopSolidOrLiquidBlock(tx, tz), tz);
                }
            }
        }
    }

    private void flowers(Block flower, Random random, int x, int z, World world) {
        for (int color = 0; color < Colors.COUNT; color++) {
            if (random.nextInt(100) <= 5) {
                int fx = x + random.nextInt(16) + 8, fz = z + random.nextInt(16) + 8;
                int fy = world.getTopSolidOrLiquidBlock(fx, fz);
                if (fy < 255 && world.isAirBlock(fx, fy, fz) && flower.canBlockStay(world, fx, fy, fz)) {
                    Dyed.place(world, fx, fy, fz, flower, 0, color, false);
                }
            }
        }
    }
}
