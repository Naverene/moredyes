package info.kg6jay.moredyes.block;

import net.minecraft.block.Block;

/** What the stairs and slabs take over from the dyed block they are made of, beyond its textures. */
final class Shapes {

    private Shapes() {}

    /**
     * Light passes through shapes of see-through blocks (glass, ice) as through the block, shapes of glowstone glow
     * like it, and shapes of ice are as slippery.
     */
    static void copyLook(Block shape, Block model) {
        if (!model.isOpaqueCube()) {
            Block.lightOpacity[shape.blockID] = Block.lightOpacity[model.blockID];
        }
        Block.lightValue[shape.blockID] = Block.lightValue[model.blockID];
        shape.slipperiness = model.slipperiness;
    }
}
