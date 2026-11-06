package info.kg6jay.moredyes.block;

import java.util.Random;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Dyed redstone lamp: lit while powered, or while unpowered when inverted. Vanilla swaps between two block IDs for
 * off and on; here the color already keeps the metadata free, so bit 1 of the metadata says whether it is lit.
 */
public class BlockDyedLamp extends BlockDyed {

    private static final int LIT = 1;

    private final int offTexture, onTexture;
    private final boolean inverted;

    public BlockDyedLamp(int id, BlockInfo info, int offTexture, int onTexture, boolean inverted) {
        super(id, info, Faces.all(offTexture));
        this.offTexture = offTexture;
        this.onTexture = onTexture;
        this.inverted = inverted;
    }

    private static boolean isLit(int meta) {
        return (meta & LIT) != 0;
    }

    private boolean shouldBeLit(World world, int x, int y, int z) {
        return world.isBlockIndirectlyGettingPowered(x, y, z) != this.inverted;
    }

    /** For the item, which shows the lamp the way it is placed: an inverted lamp starts lit. */
    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        return this.inverted ? this.onTexture : this.offTexture;
    }

    @Override
    public int getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
        return isLit(world.getBlockMetadata(x, y, z)) ? this.onTexture : this.offTexture;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return world.getBlockId(x, y, z) == this.blockID && isLit(world.getBlockMetadata(x, y, z)) ? 15 : 0;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        if (!world.isRemote) {
            this.update(world, x, y, z, false);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighborId) {
        if (!world.isRemote) {
            this.update(world, x, y, z, true);
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote && isLit(world.getBlockMetadata(x, y, z)) && !this.shouldBeLit(world, x, y, z)) {
            this.setLit(world, x, y, z, false);
        }
    }

    /** Turns on at once, and off a few ticks later like vanilla, so a quick pulse still flashes. */
    private void update(World world, int x, int y, int z, boolean delayOff) {
        boolean lit = isLit(world.getBlockMetadata(x, y, z));
        boolean wanted = this.shouldBeLit(world, x, y, z);
        if (lit && !wanted) {
            if (delayOff) {
                world.scheduleBlockUpdate(x, y, z, this.blockID, 4);
            } else {
                this.setLit(world, x, y, z, false);
            }
        } else if (!lit && wanted) {
            this.setLit(world, x, y, z, true);
        }
    }

    private void setLit(World world, int x, int y, int z, boolean lit) {
        int meta = world.getBlockMetadata(x, y, z);
        world.setBlockMetadataWithNotify(x, y, z, lit ? meta | LIT : meta & ~LIT);
        world.updateAllLightTypes(x, y, z);
    }
}
