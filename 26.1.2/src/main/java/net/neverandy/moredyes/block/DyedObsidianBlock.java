package net.neverandy.moredyes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Dyed obsidian, which builds nether portals like the vanilla block. 26.1 has no portal frame tag, so the block says
 * so itself through NeoForge.
 */
public class DyedObsidianBlock extends Block {

    public DyedObsidianBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isPortalFrame(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }
}
