package info.kg6jay.moredyes.block;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

/** Dyed soul sand slows entities walking on it and grows nether wart, like vanilla soul sand. */
public class BlockDyedSoulSand extends BlockDyed {

    public BlockDyedSoulSand(int id, BlockInfo info, Faces faces) {
        super(id, info, faces);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(x, y, z, x + 1, y + 1 - 0.125F, z + 1);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        entity.motionX *= 0.4D;
        entity.motionZ *= 0.4D;
    }

    @Override
    public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
        return plant.getPlantType(world, x, y + 1, z) == EnumPlantType.Nether
            || super.canSustainPlant(world, x, y, z, direction, plant);
    }
}
