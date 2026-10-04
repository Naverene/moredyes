package info.kg6jay.moredyes.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.common.Loader;
import info.kg6jay.moredyes.block.IBlockColored;
import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDColor;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDPiston;
import info.kg6jay.moredyes.compat.ironchest.DyedIronChestItem;
import info.kg6jay.moredyes.compat.ironchest.DyedIronChestTile;
import info.kg6jay.moredyes.compat.storagedrawers.DyedDrawersItem;
import info.kg6jay.moredyes.compat.storagedrawers.DyedDrawersTile;
import info.kg6jay.moredyes.entity.SheepColor;
import info.kg6jay.moredyes.handler.SheepHandler;
import info.kg6jay.moredyes.item.MDItemBlockTileColored;
import info.kg6jay.moredyes.item.MDItemDye;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.ColorIndex;

/**
 * The public API other mods can use to work with More Dyes colors. Everything else in More Dyes is internal and may
 * change between releases; this class keeps its methods.
 *
 * <p>
 * A More Dyes color is identified by its RGB value, 0xRRGGBB, which is also the hex code in the names of its dye and
 * dyed blocks ("334C59 Dye"). The dyes are one item per vanilla color they are mixed from, with the shades as damage
 * values, and the dyed blocks keep their color in their damage value, metadata or tile entity, so use
 * {@link #getColor(ItemStack)} and {@link #getColor(IBlockAccess, int, int, int)} rather than the item and damage.
 *
 * <p>
 * Every dye is in the ore dictionary as {@link #ORE_DYES} ("moredyesDye") and "dye".
 */
public final class MoreDyesAPI {

    /** Goes up when methods are added. */
    public static final int API_VERSION = 1;

    /** Ore dictionary name of every More Dyes dye. */
    public static final String ORE_DYES = "moredyesDye";

    private static final List<Integer> COLORS;

    static {
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < ColorIndex.count(); i++) {
            colors.add(ColorIndex.rgb(i));
        }
        COLORS = Collections.unmodifiableList(colors);
    }

    private MoreDyesAPI() {}

    /** Every More Dyes color as 0xRRGGBB, in the mod's own order (the order of the creative tab). */
    public static List<Integer> colors() {
        return COLORS;
    }

    /** Whether a color, 0xRRGGBB, is one of More Dyes' colors. */
    public static boolean isColor(int rgb) {
        return COLORS.contains(rgb);
    }

    /** Whether the stack is a More Dyes dye. */
    public static boolean isDye(ItemStack stack) {
        return stack != null && stack.getItem() instanceof MDItemDye;
    }

    /** The dye of a More Dyes color, or null if the color is not one of More Dyes' colors. */
    public static ItemStack getDye(int rgb, int count) {
        int index = COLORS.indexOf(rgb);
        if (index < 0) {
            return null;
        }
        ItemStack stack = ColorIndex.dye(index);
        stack.stackSize = count;
        return stack;
    }

    /**
     * The More Dyes color of a dye or of any dyed More Dyes item, such as dyed wool, planks, chests, stairs, pistons or
     * (with Iron Chests or Storage Drawers) dyed iron chests and drawers. Empty if the stack has no More Dyes color.
     */
    public static OptionalInt getColor(ItemStack stack) {
        if (stack == null) {
            return OptionalInt.empty();
        }
        Item item = stack.getItem();
        int damage = stack.getItemDamage();
        if (item instanceof MDItemDye dye) {
            return shade(dye.set, damage);
        }
        // Stairs, slabs, walls, trapdoors and pistons are one block for every color, numbered as in ColorIndex.
        if (item instanceof MDItemBlockTileColored || Loader.isModLoaded(Reference.IRON_CHESTS)
            && item instanceof DyedIronChestItem
            || Loader.isModLoaded(Reference.STORAGE_DRAWERS) && item instanceof DyedDrawersItem) {
            return index(damage);
        }
        Block block = Block.getBlockFromItem(item);
        return block instanceof IBlockColored colored ? shade(colored, damage) : OptionalInt.empty();
    }

    /** The More Dyes color of the block at a position, or empty if it is not a dyed More Dyes block. */
    public static OptionalInt getColor(IBlockAccess world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block instanceof IBlockColored colored) {
            return shade(colored, world.getBlockMetadata(x, y, z));
        }
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityMDColor color) {
            return index(color.getColor());
        }
        if (tile instanceof TileEntityMDPiston piston) {
            return index(piston.getColor());
        }
        if (Loader.isModLoaded(Reference.IRON_CHESTS) && tile instanceof DyedIronChestTile chest) {
            return index(chest.getColor());
        }
        if (Loader.isModLoaded(Reference.STORAGE_DRAWERS) && tile instanceof DyedDrawersTile drawers) {
            return index(drawers.getColor());
        }
        return OptionalInt.empty();
    }

    /** The More Dyes color of a sheep's wool, or empty if it has a vanilla color. */
    public static OptionalInt getSheepColor(EntitySheep sheep) {
        SheepColor color = SheepColor.of(sheep);
        if (color == null || !color.hasShade()) {
            return OptionalInt.empty();
        }
        return shade(SheepColor.setOf(color.get()), SheepColor.shadeOf(color.get()));
    }

    /**
     * Dyes a sheep's wool a More Dyes color, like using the dye on it. Call it on the server; players who can see the
     * sheep are told. Returns false, and does nothing, if the color is not one of More Dyes' colors.
     */
    public static boolean setSheepColor(EntitySheep sheep, int rgb) {
        int index = COLORS.indexOf(rgb);
        SheepColor color = SheepColor.of(sheep);
        if (index < 0 || color == null) {
            return false;
        }
        SheepHandler.applyShade(sheep, color, ColorIndex.set(index), ColorIndex.shade(index));
        return true;
    }

    /** Takes a sheep's More Dyes color away, so its wool shows its vanilla color again. Call it on the server. */
    public static void clearSheepColor(EntitySheep sheep) {
        SheepColor color = SheepColor.of(sheep);
        if (color != null) {
            SheepHandler.clearShade(sheep, color);
        }
    }

    /** The color a More Dyes color is drawn in. On 1.7.10 that is the color itself. */
    public static int displayColor(int rgb) {
        return rgb;
    }

    /**
     * The vanilla dye color that looks closest to a color, 0xRRGGBB (by CIEDE2000 distance), the same one the newer
     * versions of More Dyes use. Returned as the wool metadata, 0 for white to 15 for black; the vanilla dye item's
     * damage is 15 minus it.
     */
    public static int nearestVanillaColor(int rgb) {
        return NearestColor.of(rgb);
    }

    private static OptionalInt index(int index) {
        return index >= 0 && index < COLORS.size() ? OptionalInt.of(COLORS.get(index)) : OptionalInt.empty();
    }

    private static OptionalInt shade(int set, int shade) {
        if (set < 0 || set >= MDBlock.colorStrings.length || shade < 0 || shade >= MDBlock.colorStrings[set].length) {
            return OptionalInt.empty();
        }
        return index(ColorIndex.of(set, shade));
    }

    private static OptionalInt shade(IBlockColored block, int meta) {
        String set = block.getColorSet();
        for (int i = 0; i < MDBlock.colors.length; i++) {
            if (MDBlock.colors[i].equals(set)) {
                return shade(i, meta);
            }
        }
        return OptionalInt.empty();
    }
}
