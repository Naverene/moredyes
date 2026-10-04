package net.neverandy.moredyes.api;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.entity.DyedSheep;
import net.neverandy.moredyes.item.MixDyeItem;
import net.neverandy.moredyes.registry.ModItems;

/**
 * The public API other mods can use to work with More Dyes colors. Everything else in More Dyes is internal and may
 * change between releases; this class keeps its methods.
 *
 * <p>A More Dyes color is identified by its RGB value, 0xRRGGBB, which is also the hex code in the registry names of
 * its dye and dyed blocks ({@code moredyes:dye_334c59}, {@code moredyes:wool_334c59}). Blocks are drawn a little
 * brighter than that value, see {@link #displayColor(int)}.
 *
 * <p>Every dye is in the item tags {@link #DYES} ({@code moredyes:dyes}) and {@code c:dyes}. It is not in the
 * {@code c:dyes/<color>} tags, because the vanilla recipes read those and would then make vanilla-colored blocks from
 * it; {@link #nearestVanillaColor(int)} gives the vanilla color it looks closest to instead.
 */
public final class MoreDyesAPI {

    /** Goes up when methods are added. */
    public static final int API_VERSION = 1;

    /** Every More Dyes dye. */
    public static final TagKey<Item> DYES = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "dyes"));

    private static final List<Integer> COLORS = MixColors.ALL.stream().map(MixColor::rgb).toList();

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
        return stack.getItem() instanceof MixDyeItem;
    }

    /** The dye of a More Dyes color, or an empty stack if the color is not one of More Dyes' colors. */
    public static ItemStack getDye(int rgb, int count) {
        return color(rgb).map(color -> new ItemStack(ModItems.dye(color).get(), count)).orElse(ItemStack.EMPTY);
    }

    /**
     * The More Dyes color of a dye or of any dyed More Dyes item, such as dyed wool, planks, chests or (with Iron
     * Chests or Storage Drawers) dyed iron chests and drawers. Empty if the stack has no More Dyes color.
     */
    public static OptionalInt getColor(ItemStack stack) {
        Identifier name = BuiltInRegistries.ITEM.getKey(stack.getItem());
        Optional<MixColor> color = MixColors.of(name);
        if (color.isPresent()) {
            return OptionalInt.of(color.get().rgb());
        }
        if (!MoreDyes.MOD_ID.equals(name.getNamespace()) || !(stack.getItem() instanceof BlockItem item)
            || colorProperty(item.getBlock()) == null) {
            return OptionalInt.empty();
        }
        // Dyed blocks of other mods (Iron Chests, Storage Drawers) are one block in every color, kept in the state.
        BlockItemStateProperties state = stack.get(DataComponents.BLOCK_STATE);
        String index = state != null ? state.properties().get("color") : null;
        try {
            return OptionalInt.of(COLORS.get(index != null ? Integer.parseInt(index) : 0));
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return OptionalInt.of(COLORS.get(0));
        }
    }

    /** The More Dyes color of a placed block, or empty if it is not a dyed More Dyes block. */
    public static OptionalInt getColor(BlockState state) {
        Identifier name = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        Optional<MixColor> color = MixColors.of(name);
        if (color.isPresent()) {
            return OptionalInt.of(color.get().rgb());
        }
        IntegerProperty property = MoreDyes.MOD_ID.equals(name.getNamespace()) ? colorProperty(state.getBlock()) : null;
        return property == null ? OptionalInt.empty() : OptionalInt.of(COLORS.get(state.getValue(property)));
    }

    /** The More Dyes color of a sheep's wool, or empty if it has a vanilla color. */
    public static OptionalInt getSheepColor(Sheep sheep) {
        return DyedSheep.getColor(sheep).map(color -> OptionalInt.of(color.rgb())).orElse(OptionalInt.empty());
    }

    /**
     * Dyes a sheep's wool a More Dyes color, like using the dye on it. Call it on the server; players who can see the
     * sheep are told. Returns false, and does nothing, if the color is not one of More Dyes' colors.
     */
    public static boolean setSheepColor(Sheep sheep, int rgb) {
        Optional<MixColor> color = color(rgb);
        color.ifPresent(c -> DyedSheep.setColor(sheep, c));
        return color.isPresent();
    }

    /** Takes a sheep's More Dyes color away, so its wool shows its vanilla color again. Call it on the server. */
    public static void clearSheepColor(Sheep sheep) {
        DyedSheep.setColor(sheep, null);
    }

    /**
     * The color a More Dyes color is drawn in. The palette comes from the muted 1.7 colors, so blocks, items and sheep
     * are drawn brighter and more saturated than the color itself.
     */
    public static int displayColor(int rgb) {
        return MixColors.vivid(rgb);
    }

    /** The vanilla dye color that looks closest to a color, 0xRRGGBB (by CIEDE2000 distance). */
    public static DyeColor nearestVanillaColor(int rgb) {
        return NearestColor.of(rgb);
    }

    private static Optional<MixColor> color(int rgb) {
        return MixColors.byHex(String.format("%06x", rgb & 0xFFFFFF)).filter(color -> color.rgb() == rgb);
    }

    private static IntegerProperty colorProperty(Block block) {
        Property<?> property = block.getStateDefinition().getProperty("color");
        return property instanceof IntegerProperty integer && integer.getPossibleValues().size() == COLORS.size() ? integer : null;
    }
}
