package net.neverandy.moredyes.block;

import net.minecraft.world.level.block.Block;

/**
 * A dyed bookshelf. Like a vanilla bookshelf it powers enchanting tables (it is in the enchantment_power_provider
 * block tag) and burns (registered with Fabric's flammable block registry in MDBlock).
 */
public class BlockBookshelf extends Block
{
    public BlockBookshelf(Properties properties)
    {
        super(properties);
    }
}
