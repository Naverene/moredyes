package info.kg6jay.moredyes.compat.storagedrawers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.jaquadro.minecraft.storagedrawers.core.ModBlocks;

import cpw.mods.fml.common.registry.GameRegistry;
import info.kg6jay.moredyes.recipe.KeepTagRecipe;
import info.kg6jay.moredyes.utility.ColorIndex;
import info.kg6jay.moredyes.utility.LogHelper;

/**
 * Dyed Storage Drawers, in every More Dyes color, in all five sizes Storage Drawers has in 1.7.10. There is one block
 * per size and the color is kept in the drawer's tile entity: block metadata only holds 16 values (Storage Drawers uses
 * it for the wood type) and there are 118 colors. The item damage is the color's number (see ColorIndex).
 * <p>
 * The drawers are Storage Drawers' own standard drawers with a grey texture tinted in the dye color, and their tile
 * entity is a subclass of Storage Drawers' standard one. 1.7.10 has no block entity types to join: hoppers, the drawer
 * controller, keys, upgrades and the item labels all work through that tile entity class, so no hook is needed.
 * <p>
 * Only loaded when Storage Drawers is installed (see MoreDyes), so nothing else may refer to this package.
 */
public final class StorageDrawersCompat {

    public enum Size {

        FULL_1("fullDrawers1", 1, false),
        FULL_2("fullDrawers2", 2, false),
        FULL_4("fullDrawers4", 4, false),
        HALF_2("halfDrawers2", 2, true),
        HALF_4("halfDrawers4", 4, true);

        /** Storage Drawers' name for this size, also used to read its block config (trim widths). */
        public final String name;
        public final int drawerCount;
        public final boolean halfDepth;
        public DyedDrawersBlock block;

        Size(String name, int drawerCount, boolean halfDepth) {
            this.name = name;
            this.drawerCount = drawerCount;
            this.halfDepth = halfDepth;
        }

        /** "dyedFullDrawers1". */
        public String registryName() {
            return "dyed" + Character.toUpperCase(this.name.charAt(0)) + this.name.substring(1);
        }

        /** Storage Drawers' own block of this size. */
        Block plain() {
            return switch (this) {
                case FULL_1 -> ModBlocks.fullDrawers1;
                case FULL_2 -> ModBlocks.fullDrawers2;
                case FULL_4 -> ModBlocks.fullDrawers4;
                case HALF_2 -> ModBlocks.halfDrawers2;
                case HALF_4 -> ModBlocks.halfDrawers4;
            };
        }
    }

    private StorageDrawersCompat() {}

    /** Registers the blocks and tile entity. Called in preInit, after Storage Drawers' own preInit. */
    public static void preInit() {
        for (Size size : Size.values()) {
            // A size turned off in Storage Drawers' config is left out here too.
            if (size.plain() == null || Item.getItemFromBlock(size.plain()) == null) {
                continue;
            }
            size.block = new DyedDrawersBlock(size);
            GameRegistry.registerBlock(size.block, DyedDrawersItem.class, size.registryName());
        }
        GameRegistry.registerTileEntity(DyedDrawersTile.class, "moredyes.dyedDrawers");
        LogHelper.info("Storage Drawers is loaded: added dyed drawers");
    }

    /**
     * Recipes: any of Storage Drawers' wooden drawers of a size (or a dyed one) and a dye make a dyed drawer of that
     * size. The result keeps a taped drawer's contents.
     */
    public static void init() {
        for (Size size : Size.values()) {
            if (size.block == null) {
                continue;
            }
            ItemStack plain = new ItemStack(size.plain(), 1, OreDictionary.WILDCARD_VALUE);
            ItemStack anyDyed = new ItemStack(size.block, 1, OreDictionary.WILDCARD_VALUE);
            for (int color = 0; color < ColorIndex.count(); color++) {
                ItemStack result = new ItemStack(size.block, 1, color);
                GameRegistry.addRecipe(new KeepTagRecipe(result, plain, ColorIndex.dye(color)));
                GameRegistry.addRecipe(new KeepTagRecipe(result, anyDyed, ColorIndex.dye(color)));
            }
        }
    }
}
