package info.kg6jay.moredyes.block;

import net.minecraft.world.World;

/** Dyed chain (Minecraft 1.16): a thin upright post drawn as two crossed squares, like iron bars seen from the side. */
public class BlockDyedChain extends BlockDyed {

    private static final float MIN = 6.5F / 16.0F, MAX = 9.5F / 16.0F;

    public BlockDyedChain(int id, BlockInfo info, Faces faces) {
        super(id, info, faces);
        this.setBlockBounds(MIN, 0.0F, MIN, MAX, 1.0F, MAX);
    }

    @Override
    public int getRenderType() {
        return 1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(World world, int x, int y, int z) {
        return false;
    }
}
