package info.kg6jay.moredyes.block;

import java.util.Random;

import info.kg6jay.moredyes.utility.BlockInfo;

/** Dyed packed ice: slippery like ice but solid, never melts, and drops itself only with Silk Touch, like vanilla. */
public class MDBlockPackedIce extends MDBlockColored {

    public MDBlockPackedIce(String[] colors, BlockInfo info, String colorSet) {
        super(colors, info, colorSet);
        this.slipperiness = 0.98F;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
}
