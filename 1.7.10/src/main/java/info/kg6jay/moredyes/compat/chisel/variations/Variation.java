package info.kg6jay.moredyes.compat.chisel.variations;

import net.minecraft.block.Block;

import com.cricketcraft.chisel.api.carving.ICarvingVariation;

public class Variation implements ICarvingVariation {

    private Block block;

    public Variation(Block b) {
        this.block = b;
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public int getBlockMeta() {
        return 0;
    }

    @Override
    public int getItemMeta() {
        return 0;
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
