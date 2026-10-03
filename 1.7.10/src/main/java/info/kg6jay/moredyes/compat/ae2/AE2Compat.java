package info.kg6jay.moredyes.compat.ae2;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.item.MDItem;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.LogHelper;

/**
 * Applied Energistics 2's cables, paint balls and Color Applicator only know the 16 vanilla colors, so each More Dyes
 * dye counts as the vanilla color it looks closest to ({@link NearestDye}). Written against AE2 rv3.
 * <ul>
 * <li>Every dye is in the ore dictionary as "moredyes" and its vanilla color, such as moredyesLightBlue. Not as
 * AE2's dyeLightBlue: vanilla's recipes take those (Forge swaps vanilla dyes for their ore names), so eight white wool
 * and a More Dyes dye would make vanilla wool, and our dyes would turn into other things.</li>
 * <li>Recipes like AE2's own take those names: eight fluix cables (glass, covered, smart or dense) around a dye make
 * eight cables of its color, and eight matter balls make eight paint balls.</li>
 * <li>The Color Applicator finds a dye's color through a private map from ore name to AE2 color; the names are added
 * to it, so it takes our dyes too.</li>
 * </ul>
 * More Dyes doesn't compile against AE2: AE2's items are looked up by name and its map is reached by reflection. Only
 * used when AE2 is installed.
 */
public class AE2Compat {

    public static final String MOD_ID = "appliedenergistics2";

    /** The vanilla colors in wool metadata order, which is also AE2's color order and its AEColor names. */
    private static final String[] COLORS = { "White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink",
        "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black" };
    /** AE2's part damage of the white cable of each kind; the next 15 are the other colors, then the fluix one. */
    private static final int[] CABLES = { 0, 20, 40, 60 };
    private static final int FLUIX = 16;
    private static final int MATTER_BALL = 6;

    private static String oreName(int color) {
        return Reference.MOD_ID + COLORS[color];
    }

    /** Registers the ore names and recipes, and lets the Color Applicator take our dyes. Run in postInit. */
    public static void postInit() {
        for (int group = 0; group < MDBlock.colorStrings.length; group++) {
            for (int shade = 0; shade < MDBlock.colorStrings[group].length; shade++) {
                OreDictionary.registerOre(
                    oreName(NearestDye.of(MDBlock.colorStrings[group][shade])),
                    new ItemStack(MDItem.dye[group], 1, shade));
            }
        }
        Item part = GameRegistry.findItem(MOD_ID, "item.ItemMultiPart");
        Item material = GameRegistry.findItem(MOD_ID, "item.ItemMultiMaterial");
        Item paintBall = GameRegistry.findItem(MOD_ID, "item.ItemPaintBall");
        if (part == null || material == null || paintBall == null) {
            LogHelper.warn("Applied Energistics 2's items weren't found, so More Dyes dyes can't color its cables");
            return;
        }
        for (int color = 0; color < COLORS.length; color++) {
            for (int cable : CABLES) {
                ring(new ItemStack(part, 8, cable + color), new ItemStack(part, 1, cable + FLUIX), color);
            }
            ring(new ItemStack(paintBall, 8, color), new ItemStack(material, 1, MATTER_BALL), color);
        }
        addColorApplicatorDyes();
    }

    /** Eight of {@code around} around a dye of a color make {@code result}. */
    private static void ring(ItemStack result, ItemStack around, int color) {
        GameRegistry.addRecipe(new ShapedOreRecipe(result, "aaa", "aba", "aaa", 'a', around, 'b', oreName(color)));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void addColorApplicatorDyes() {
        try {
            Class<? extends Enum> aeColor = (Class<? extends Enum>) Class.forName("appeng.api.util.AEColor");
            Field field = Class.forName("appeng.items.tools.powered.ToolColorApplicator")
                .getDeclaredField("ORE_TO_COLOR");
            field.setAccessible(true);
            Map<Integer, Object> oreToColor = (Map<Integer, Object>) field.get(null);
            for (int color = 0; color < COLORS.length; color++) {
                oreToColor.put(OreDictionary.getOreID(oreName(color)), Enum.valueOf(aeColor, COLORS[color]));
            }
            LogHelper.info("Applied Energistics 2's Color Applicator takes More Dyes dyes");
        } catch (ReflectiveOperationException | RuntimeException e) {
            LogHelper.warn("Couldn't add More Dyes dyes to Applied Energistics 2's Color Applicator: " + e);
        }
    }
}
