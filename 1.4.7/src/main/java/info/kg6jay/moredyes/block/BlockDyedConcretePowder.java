package info.kg6jay.moredyes.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

/**
 * Dyed concrete powder (Minecraft 1.12): falls like sand, and sets into dyed concrete of the same color as soon as
 * water touches it, whether it is placed next to water or lands in it.
 */
public class BlockDyedConcretePowder extends BlockDyedSand {

    public BlockDyedConcretePowder(int id, BlockInfo info, Faces faces) {
        super(id, info, faces);
    }

    /**
     * Checked on the block's next tick rather than when it is placed: a placed block only gets its color after it is
     * added, so setting it at once would lose the color.
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!this.setIfWet(world, x, y, z)) {
            super.updateTick(world, x, y, z, random);
        }
    }

    @Override
    public void onLanded(World world, int x, int y, int z) {
        this.setIfWet(world, x, y, z);
    }

    /** Turns into concrete if water touches any side but the bottom, like vanilla. */
    private boolean setIfWet(World world, int x, int y, int z) {
        if (world.isRemote) {
            return false;
        }
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (side != ForgeDirection.DOWN && world.getBlockMaterial(x + side.offsetX, y + side.offsetY,
                z + side.offsetZ) == Material.water) {
                int color = Dyed.get(world, x, y, z);
                Dyed.place(world, x, y, z, MDBlocks.concrete, 0, color, true);
                return true;
            }
        }
        return false;
    }

    /** Unlike sand, grows no plants. */
    @Override
    public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
        return false;
    }
}
