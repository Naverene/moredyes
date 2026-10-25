package info.kg6jay.moredyes.compat.etfuturum;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;

import cpw.mods.fml.common.registry.GameRegistry;
import ganymedes01.etfuturum.ModBlocks;
import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.recipe.KeepTagRecipe;
import info.kg6jay.moredyes.utility.ColorIndex;
import info.kg6jay.moredyes.utility.LogHelper;

/**
 * Dyed Et Futurum Requiem shulker boxes, in every More Dyes color. Et Futurum (in GT: New Horizons) has shulker boxes
 * in 16 colors and, with Iron Chests installed, in the Iron Chests tiers. The dyed ones are one block for every color
 * and tier: the color is kept in the tile entity and is the item damage (see ColorIndex), and the tier is Et Futurum's
 * "Type" tag. Only the shell takes the dye; the metal trim of a tier keeps its own colors.
 * <p>
 * Only loaded when Et Futurum Requiem is installed (see MoreDyes), so nothing else may refer to this package.
 */
public final class EtFuturumCompat {

    public static DyedShulkerBoxBlock block;

    private EtFuturumCompat() {}

    /** Registers the block and tile entity, if Et Futurum's own shulker boxes are turned on. Called in preInit. */
    public static void preInit() {
        if (!ModBlocks.SHULKER_BOX.isEnabled()) {
            LogHelper.info("Et Futurum Requiem's shulker boxes are turned off, so there are no dyed ones");
            return;
        }
        block = new DyedShulkerBoxBlock();
        GameRegistry.registerBlock(block, DyedShulkerBoxItem.class, "dyedShulkerBox");
        GameRegistry.registerTileEntity(DyedShulkerBoxTile.class, "moredyes.dyedShulkerBox");
        LogHelper.info("Et Futurum Requiem is loaded: added dyed shulker boxes");
    }

    public static boolean enabled() {
        return block != null;
    }

    /**
     * Recipes: an Et Futurum shulker box (of any color or tier) or a dyed one, and a dye, make a dyed box with the same
     * contents and tier. Washing one in a cauldron gives back the plain Et Futurum box, still with its contents.
     */
    public static void init() {
        if (!enabled()) {
            return;
        }
        // Forge's recipe sorter only looks one class up for a recipe class it does not know, and loops forever when
        // that
        // one is not known either, so this subclass of KeepTagRecipe has to be registered.
        RecipeSorter.register(
            "moredyes:dyeShulkerBox",
            DyeBoxRecipe.class,
            RecipeSorter.Category.SHAPELESS,
            "after:minecraft:shapeless before:forge:shapelessore");
        ItemStack plain = ModBlocks.SHULKER_BOX.newItemStack();
        ItemStack anyDyed = new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE);
        for (int color = 0; color < ColorIndex.count(); color++) {
            ItemStack result = new ItemStack(block, 1, color);
            // Et Futurum's own dyeing recipe takes any "dye" from the ore dictionary, so More Dyes' dyes would make a
            // white box with it. These are sorted before it (see the sorter entry above).
            GameRegistry.addRecipe(new DyeBoxRecipe(result, plain, ColorIndex.dye(color)));
            GameRegistry.addRecipe(new DyeBoxRecipe(result, anyDyed, ColorIndex.dye(color)));
        }
        MDBlock.WASHED.put(block, plain);
    }

    /** Removes Et Futurum's own color (its 16 colors) from a box's tag, as a dyed box has a More Dyes color instead. */
    public static void clearVanillaColor(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            tag.removeTag("Color");
            if (tag.hasNoTags()) {
                stack.setTagCompound(null);
            }
        }
    }

    /** Dyeing keeps the box's tag (contents, tier, name) but not an Et Futurum color. */
    public static final class DyeBoxRecipe extends KeepTagRecipe {

        DyeBoxRecipe(ItemStack result, ItemStack... ingredients) {
            super(result, ingredients);
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting inventory) {
            ItemStack result = super.getCraftingResult(inventory);
            clearVanillaColor(result);
            return result;
        }
    }
}
