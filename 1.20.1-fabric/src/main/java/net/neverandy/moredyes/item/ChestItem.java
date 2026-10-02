package net.neverandy.moredyes.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

/**
 * A dyed chest item. It burns like a vanilla chest (registered as fuel in MDItem), and the client draws it with
 * client/ChestItemRenderer.
 */
public class ChestItem extends BlockItem
{
    public ChestItem(Block block, Properties properties)
    {
        super(block, properties);
    }
}
