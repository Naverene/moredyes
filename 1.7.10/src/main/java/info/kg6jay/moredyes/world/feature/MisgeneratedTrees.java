package info.kg6jay.moredyes.world.feature;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.item.MDItem;

/**
 * Until November 2026, TreeGenerator passed the color set as the tree's shade and the shade as its color set. Most
 * of those trees just came out in another real color, but the green and red mix trees (sets 13 and 14, shade 0) were
 * placed as white mix logs and leaves with shades 13 and 14, which white mix does not have. Their logs, leaves,
 * saplings and dyes had no name ("tile.whiteMixSapling_13.name"), were drawn in the wrong color, and had no ore
 * dictionary entry, so storage that sorts by it refused them.
 *
 * Such a shade is turned back into the tree it was meant to be: set and shade swapped back. Blocks still in the world
 * drop the fixed item, saplings fix themselves when they tick, and items fix themselves in a player's inventory.
 */
public final class MisgeneratedTrees {

    private MisgeneratedTrees() {}

    /**
     * For a shade that does not exist in its color set, the {set, shade} the tree should have had; null for a real
     * shade, or one that cannot be mapped.
     */
    public static int[] fix(int set, int meta) {
        String[][] sets = MDBlock.colorStrings;
        if (set < 0 || set >= sets.length || meta >= 0 && meta < sets[set].length) {
            return null;
        }
        if (meta < 0 || meta >= sets.length || set >= sets[meta].length) {
            return null;
        }
        return new int[] { meta, set };
    }

    /** The fixed stack for a misgenerated log, leaf, sapling or dye; null if the stack is fine. */
    public static ItemStack fix(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return null;
        }
        Item item = stack.getItem();
        Block block = Block.getBlockFromItem(item);
        Object[][] kinds = { MDBlock.log, MDBlock.leaf, MDBlock.sapling, MDItem.dye };
        for (Object[] kind : kinds) {
            if (kind == null) {
                continue;
            }
            for (int set = 0; set < kind.length; set++) {
                if (kind[set] != item && kind[set] != block) {
                    continue;
                }
                int[] fixed = fix(set, stack.getItemDamage());
                if (fixed == null) {
                    return null;
                }
                Object target = kind[fixed[0]];
                Item targetItem = target instanceof Block b ? Item.getItemFromBlock(b) : (Item) target;
                ItemStack result = new ItemStack(targetItem, stack.stackSize, fixed[1]);
                if (stack.hasTagCompound()) {
                    result.setTagCompound(stack.getTagCompound());
                }
                return result;
            }
        }
        return null;
    }

    /** Replaces misgenerated stacks in a drop list with fixed ones. */
    public static <T extends List<ItemStack>> T fixAll(T drops) {
        for (int i = 0; i < drops.size(); i++) {
            ItemStack fixed = fix(drops.get(i));
            if (fixed != null) {
                drops.set(i, fixed);
            }
        }
        return drops;
    }
}
