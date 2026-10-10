package info.kg6jay.moredyes.block;

import java.util.Random;

import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

import info.kg6jay.moredyes.entity.EntityFallingDyed;

/** Dyed sand: falls like sand (keeping its color) and grows cactus, dead bushes and sugar cane like vanilla sand. */
public class BlockDyedSand extends BlockDyed {

    public BlockDyedSand(int id, BlockInfo info, Faces faces) {
        super(id, info, faces);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighbor) {
        world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
    }

    @Override
    public int tickRate() {
        return 5;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        if (world.isRemote || y < 0 || !BlockSand.canFallBelow(world, x, y - 1, z)) {
            return;
        }
        int range = 32;
        if (BlockSand.fallInstantly
            || !world.checkChunksExist(x - range, y - range, z - range, x + range, y + range, z + range)) {
            // Far from players, or during world generation: drop straight down, as vanilla sand does.
            int color = Dyed.get(world, x, y, z);
            int meta = world.getBlockMetadata(x, y, z);
            world.setBlockWithNotify(x, y, z, 0);
            int landY = y;
            while (landY > 0 && BlockSand.canFallBelow(world, x, landY - 1, z)) {
                --landY;
            }
            if (landY > 0) {
                Dyed.place(world, x, landY, z, this, meta, color, true);
                this.onLanded(world, x, landY, z);
            }
            return;
        }
        world.spawnEntityInWorld(new EntityFallingDyed(world, x, y, z, this.blockID, world.getBlockMetadata(x, y, z),
            Dyed.get(world, x, y, z)));
    }

    /** Called when the falling block lands and turns back into this block. */
    public void onLanded(World world, int x, int y, int z) {}

    @Override
    public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
        EnumPlantType type = plant.getPlantType(world, x, y + 1, z);
        if (type == EnumPlantType.Desert) {
            return true;
        }
        if (type == EnumPlantType.Beach) {
            return world.getBlockMaterial(x - 1, y, z) == Material.water
                || world.getBlockMaterial(x + 1, y, z) == Material.water
                || world.getBlockMaterial(x, y, z - 1) == Material.water
                || world.getBlockMaterial(x, y, z + 1) == Material.water;
        }
        return super.canSustainPlant(world, x, y, z, direction, plant);
    }
}
