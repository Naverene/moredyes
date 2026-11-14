package net.neverandy.moredyes.compat.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.item.ItemDrawers;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import net.neverandy.moredyes.color.MixColors;

/** A dyed drawer. Its color is in the item's {@code minecraft:block_state} component, which the placed block takes. */
public class DyedDrawersItem extends ItemDrawers {

    private final DyedDrawersBlock block;

    public DyedDrawersItem(DyedDrawersBlock block, Properties properties) {
        super(block, properties);
        this.block = block;
    }

    /**
     * "C56685 Drawers 1x1": Storage Drawers' own name for the size (in every language it has), with the hex code where
     * it puts the wood, like the names of the other dyed blocks.
     */
    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(block.getNameTypeKey(), MixColors.ALL.get(StorageDrawersCompat.colorOf(stack)).label());
    }
}
