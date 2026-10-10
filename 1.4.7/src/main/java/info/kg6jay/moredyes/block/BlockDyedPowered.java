package info.kg6jay.moredyes.block;

import net.minecraft.world.IBlockAccess;

/** Dyed redstone block: always gives a full redstone signal, like the vanilla block of redstone. */
public class BlockDyedPowered extends BlockDyed {

    public BlockDyedPowered(int id, BlockInfo info, Faces faces) {
        super(id, info, faces);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public boolean isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        return true;
    }
}
