package info.kg6jay.moredyes.block;

import java.util.Random;

import net.minecraft.world.World;

import info.kg6jay.moredyes.world.WorldGenTreeDye;

/**
 * A dyed sapling. Grows into a dye tree of its color: dyed logs and leaves, the leaves dropping saplings and dyes of
 * the same color. Like a vanilla sapling it first gets ready to grow (bit 8 of its metadata), then grows.
 */
public class BlockDyedSapling extends BlockDyedPlant {

    private static final int READY = 8;

    public BlockDyedSapling(int id, BlockInfo info, int leaves, int trunk) {
        super(id, info, leaves, trunk);
        float f = 0.4F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (world.isRemote) {
            return;
        }
        super.updateTick(world, x, y, z, random);
        if (world.getBlockId(x, y, z) == this.blockID && world.getBlockLightValue(x, y + 1, z) >= 9
            && random.nextInt(7) == 0) {
            int meta = world.getBlockMetadata(x, y, z);
            if ((meta & READY) == 0) {
                world.setBlockMetadataWithNotify(x, y, z, meta | READY);
            } else {
                this.growTree(world, x, y, z, random);
            }
        }
    }

    /** Replaces the sapling with a tree of its color, if there is room. */
    public void growTree(World world, int x, int y, int z, Random random) {
        int color = Dyed.get(world, x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        world.setBlock(x, y, z, 0);
        if (!new WorldGenTreeDye(true, 3, color).generate(world, random, x, y, z)) {
            Dyed.place(world, x, y, z, this, meta, color, false);
        }
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
    }
}
