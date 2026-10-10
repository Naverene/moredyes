package info.kg6jay.moredyes.block;

/** A dyed flower (tulip or cornflower): tinted petals on a green stem. Grows on grass, dirt and farmland. */
public class BlockDyedFlower extends BlockDyedPlant {

    public BlockDyedFlower(int id, BlockInfo info, int petals, int stem) {
        super(id, info, petals, stem);
        float f = 0.2F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 3.0F, 0.5F + f);
    }
}
