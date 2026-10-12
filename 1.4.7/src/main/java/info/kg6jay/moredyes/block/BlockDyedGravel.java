package info.kg6jay.moredyes.block;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;

/** Dyed gravel: falls like dyed sand and keeps its color, but like vanilla gravel grows no plants. */
public class BlockDyedGravel extends BlockDyedSand {

    public BlockDyedGravel(int id, BlockInfo info, Faces faces) {
        super(id, info, faces);
    }

    @Override
    public boolean canSustainPlant(World world, int x, int y, int z, ForgeDirection direction, IPlantable plant) {
        return false;
    }
}
