package info.kg6jay.moredyes.block;

import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import info.kg6jay.moredyes.item.MDItem;
import info.kg6jay.moredyes.utility.ColorIndex;

/**
 * The planks a dyed bed was made from, kept as a block id in the bed item's "Wood" tag and in its tile entity
 * (TileEntityMDBed): "minecraft:spruce_planks" for vanilla planks, "moredyes:planks_&lt;hex&gt;" for dyed ones, and no
 * tag
 * for oak. A bed of vanilla wool and planks other than oak is a dyed bed too, with the "Vanilla" tag, so it keeps the
 * red vanilla bedding and its wood.
 */
public final class BedWood {

    public static final String TAG = "Wood";
    public static final String VANILLA_TAG = "Vanilla";
    public static final String OAK = "minecraft:oak_planks";
    /** The vanilla woods, by planks metadata. */
    public static final String[] WOODS = { "oak", "spruce", "birch", "jungle", "acacia", "dark_oak" };
    private static final String DYED = "moredyes:planks_";

    private BedWood() {}

    /** The id of a stack of planks, or null for any other item. */
    public static String of(ItemStack stack) {
        if (stack == null) {
            return null;
        }
        Block block = Block.getBlockFromItem(stack.getItem());
        int meta = stack.getItemDamage();
        if (block == Blocks.planks) {
            return meta >= 0 && meta < WOODS.length ? "minecraft:" + WOODS[meta] + "_planks" : null;
        }
        for (Block[] planks : new Block[][] { MDBlock.plank, MDBlock.sprucePlank, MDBlock.birchPlank,
            MDBlock.junglePlank, MDBlock.acaciaPlank, MDBlock.darkOakPlank }) {
            for (int set = 0; set < planks.length; set++) {
                if (planks[set] != null && block == planks[set]) {
                    try {
                        return DYED + ColorIndex.hex(ColorIndex.of(set, meta))
                            .toLowerCase(Locale.ROOT);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /** The position in WOODS of a vanilla wood, or -1 for dyed or unknown planks. Null (no wood) is oak. */
    public static int vanilla(String wood) {
        if (wood == null) {
            return 0;
        }
        for (int i = 0; i < WOODS.length; i++) {
            if (wood.equals("minecraft:" + WOODS[i] + "_planks")) {
                return i;
            }
        }
        return -1;
    }

    /** The color number (see ColorIndex) of dyed planks, or -1 for any other wood. */
    public static int dyed(String wood) {
        if (wood == null || !wood.startsWith(DYED)) {
            return -1;
        }
        String hex = wood.substring(DYED.length());
        for (int i = 0; i < ColorIndex.count(); i++) {
            if (ColorIndex.hex(i)
                .equalsIgnoreCase(hex)) {
                return i;
            }
        }
        return -1;
    }

    /** The planks a bed item was made from, or null for oak. */
    public static String woodOf(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.hasKey(TAG, 8) ? tag.getString(TAG) : null;
    }

    public static void setWood(ItemStack stack, String wood) {
        NBTTagCompound tag = stack.getTagCompound();
        if (wood == null || OAK.equals(wood)) {
            if (tag != null) {
                tag.removeTag(TAG);
                if (tag.hasNoTags()) {
                    stack.setTagCompound(null);
                }
            }
            return;
        }
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        tag.setString(TAG, wood);
    }

    /** Whether a bed item has the red vanilla bedding. */
    public static boolean isVanilla(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag != null && tag.getBoolean(VANILLA_TAG);
    }

    /** A dyed bed item with the vanilla bedding, for a vanilla bed made from planks other than oak. */
    public static ItemStack vanillaBed() {
        ItemStack stack = new ItemStack(MDItem.bed);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean(VANILLA_TAG, true);
        stack.setTagCompound(tag);
        return stack;
    }
}
