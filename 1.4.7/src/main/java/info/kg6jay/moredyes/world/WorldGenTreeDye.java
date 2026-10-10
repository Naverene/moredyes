package info.kg6jay.moredyes.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.ForgeDirection;

import info.kg6jay.moredyes.block.BlockDyedSapling;
import info.kg6jay.moredyes.block.Dyed;
import info.kg6jay.moredyes.block.MDBlocks;

/** A dye tree: shaped like a small oak, with dyed logs and leaves of one color. */
public class WorldGenTreeDye extends WorldGenerator {

    private static final int RANDOM_HEIGHT = 7;

    private final boolean notify;
    private final int minHeight;
    private final int color;

    public WorldGenTreeDye(boolean notify, int minHeight, int color) {
        super(notify);
        this.notify = notify;
        this.minHeight = minHeight;
        this.color = color;
    }

    @Override
    public boolean generate(World world, Random rand, int x, int y, int z) {
        int height = rand.nextInt(RANDOM_HEIGHT) + this.minHeight;
        if (y < 1 || y + height + 1 > 256) {
            return false;
        }
        for (int cy = y; cy <= y + 1 + height; ++cy) {
            int radius = cy == y ? 0 : cy >= y + 1 + height - 2 ? 2 : 1;
            for (int cx = x - radius; cx <= x + radius; ++cx) {
                for (int cz = z - radius; cz <= z + radius; ++cz) {
                    if (cy < 0 || cy >= 256 || !isReplaceable(world, cx, cy, cz)) {
                        return false;
                    }
                }
            }
        }

        Block soil = Block.blocksList[world.getBlockId(x, y - 1, z)];
        BlockDyedSapling sapling = (BlockDyedSapling) MDBlocks.sapling;
        if (soil == null || !soil.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, sapling)
            || y >= 256 - height - 1) {
            return false;
        }
        soil.onPlantGrow(world, x, y - 1, z, x, y, z);

        for (int cy = y - 3 + height; cy <= y + height; ++cy) {
            int above = cy - (y + height);
            int radius = 1 - above / 2;
            for (int cx = x - radius; cx <= x + radius; ++cx) {
                for (int cz = z - radius; cz <= z + radius; ++cz) {
                    boolean corner = Math.abs(cx - x) == radius && Math.abs(cz - z) == radius;
                    if (!corner || rand.nextInt(2) != 0 && above != 0) {
                        Block block = Block.blocksList[world.getBlockId(cx, cy, cz)];
                        if (block == null || block.canBeReplacedByLeaves(world, cx, cy, cz)) {
                            Dyed.place(world, cx, cy, cz, MDBlocks.leaves, 0, this.color, this.notify);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < height; ++i) {
            Block block = Block.blocksList[world.getBlockId(x, y + i, z)];
            if (block == null || block.isLeaves(world, x, y + i, z) || block == sapling) {
                Dyed.place(world, x, y + i, z, MDBlocks.log, 0, this.color, this.notify);
            }
        }
        return true;
    }

    private static boolean isReplaceable(World world, int x, int y, int z) {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        return block == null || block.isLeaves(world, x, y, z) || block.blockID == Block.grass.blockID
            || block.blockID == Block.dirt.blockID || block.isWood(world, x, y, z)
            || block instanceof BlockDyedSapling;
    }
}
