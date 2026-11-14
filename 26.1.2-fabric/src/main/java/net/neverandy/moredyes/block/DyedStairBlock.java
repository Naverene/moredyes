package net.neverandy.moredyes.block;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;

/** Dyed stairs. Vanilla's StairBlock constructor is protected, so this only makes it public. */
public class DyedStairBlock extends StairBlock {

    public DyedStairBlock(BlockState base, Properties properties) {
        super(base, properties);
    }
}
