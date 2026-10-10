package net.neverandy.moredyes.block;

import net.minecraft.world.level.block.Block;

/**
 * Dyed obsidian, which builds nether portals like the vanilla block. 1.21.1 has no portal frame tag, so
 * {@code PortalShapeMixin} lets the dyed obsidian into the portal frame check.
 */
public class DyedObsidianBlock extends Block {

    public DyedObsidianBlock(Properties properties) {
        super(properties);
    }
}
