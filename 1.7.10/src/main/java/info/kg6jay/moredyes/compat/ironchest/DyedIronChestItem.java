package info.kg6jay.moredyes.compat.ironchest;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import info.kg6jay.moredyes.utility.ColorIndex;

/**
 * A dyed Iron Chests chest. The item damage is the color's number (see ColorIndex), which the block puts into its tile
 * entity when placed (DyedIronChestBlock.onBlockPlacedBy); the block itself is placed with metadata 0.
 */
public class DyedIronChestItem extends ItemBlock {

    public DyedIronChestItem(Block block) {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public int getMetadata(int damage) {
        return 0;
    }

    /** "ECBF99 Iron Chest", like the names of the other dyed blocks. */
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return StatCollector
            .translateToLocalFormatted(this.getUnlocalizedName() + ".name", ColorIndex.hex(stack.getItemDamage()))
            .trim();
    }
}
