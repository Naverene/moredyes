package info.kg6jay.moredyes.block;

import net.minecraft.block.Block;

/** A dyed flower (tulip or cornflower): tinted petals on a green stem. Grows on grass, dirt, farmland and sand. */
public class BlockDyedFlower extends BlockDyedPlant {

    public BlockDyedFlower(int id, BlockInfo info, int petals, int stem) {
        super(id, info, petals, stem);
        float f = 0.2F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 3.0F, 0.5F + f);
    }

    @Override
    protected boolean canThisPlantGrowOnThisBlockID(int id) {
        return super.canThisPlantGrowOnThisBlockID(id) || isSand(id);
    }

    /** Dyed flowers can also be planted on sand, vanilla or dyed. */
    public static boolean isSand(int id) {
        return id == Block.sand.blockID || MDBlocks.sand != null && id == MDBlocks.sand.blockID;
    }
}
