package net.neverandy.moredyes.api;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.Tags;
import net.neverandy.moredyes.entity.DyedSheep;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.item.MDItemDye;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The public API other mods can use to work with More Dyes colors. Everything else in More Dyes is internal and may
 * change between releases; this class keeps its methods.
 *
 * <p>A More Dyes color is identified by its RGB value, 0xRRGGBB, which is also the hex code in the registry names of
 * its dye and dyed blocks ("moredyes:334c59_dye", "moredyes:wool_334c59"). Blocks are drawn a little brighter than
 * that value, see {@link #displayColor(int)}.
 *
 * <p>Every dye is also in the item tag {@link #DYES} ("moredyes:dyes") and in the Forge tag of the vanilla dye color
 * it looks closest to ("forge:dyes/red" and so on), which {@link #nearestVanillaColor(int)} also gives.
 */
public final class MoreDyesAPI
{
    /** Goes up when methods are added. */
    public static final int API_VERSION = 1;

    /** Every More Dyes dye. */
    public static final Tags.IOptionalNamedTag<Item> DYES = ItemTags.createOptional(new ResourceLocation(Reference.MOD_ID, "dyes"));

    private static final Pattern HEX = Pattern.compile("(?:^|_)([0-9a-f]{6})(?:_|$)");
    private static final List<Integer> COLORS;

    static
    {
        List<Integer> colors = new ArrayList<>();
        for (String hex : ColorStrings.ALL)
        {
            colors.add(Integer.parseInt(hex, 16));
        }
        COLORS = Collections.unmodifiableList(colors);
    }

    private MoreDyesAPI() {}

    /** Every More Dyes color as 0xRRGGBB, in the mod's own order (the order of the creative tab). */
    public static List<Integer> colors()
    {
        return COLORS;
    }

    /** Whether a color, 0xRRGGBB, is one of More Dyes' colors. */
    public static boolean isColor(int rgb)
    {
        return COLORS.contains(rgb);
    }

    /** Whether the stack is a More Dyes dye. */
    public static boolean isDye(ItemStack stack)
    {
        return stack.getItem() instanceof MDItemDye;
    }

    /** The dye of a More Dyes color, or an empty stack if the color is not one of More Dyes' colors. */
    public static ItemStack getDye(int rgb, int count)
    {
        int index = COLORS.indexOf(rgb);
        return index < 0 ? ItemStack.EMPTY : new ItemStack(MDItem.dye[index], count);
    }

    /**
     * The More Dyes color of a dye or of any dyed More Dyes item, such as dyed wool, planks, chests or (with Iron
     * Chests or Storage Drawers) dyed iron chests and drawers. Empty if the stack has no More Dyes color.
     */
    public static OptionalInt getColor(ItemStack stack)
    {
        ResourceLocation name = stack.getItem().getRegistryName();
        OptionalInt color = colorOf(name);
        if (color.isPresent() || !(stack.getItem() instanceof BlockItem) || !isOurs(name))
        {
            return color;
        }
        // Dyed blocks of other mods (Iron Chests, Storage Drawers) are one block in every color, kept in the state.
        IntegerProperty property = colorProperty(((BlockItem) stack.getItem()).getBlock());
        if (property == null)
        {
            return OptionalInt.empty();
        }
        CompoundNBT state = stack.getChildTag("BlockStateTag");
        int index = 0;
        if (state != null && state.contains(property.getName()))
        {
            index = property.parseValue(state.getString(property.getName())).orElse(0);
        }
        return OptionalInt.of(COLORS.get(index));
    }

    /** The More Dyes color of a placed block, or empty if it is not a dyed More Dyes block. */
    public static OptionalInt getColor(BlockState state)
    {
        ResourceLocation name = state.getBlock().getRegistryName();
        OptionalInt color = colorOf(name);
        if (color.isPresent() || !isOurs(name))
        {
            return color;
        }
        IntegerProperty property = colorProperty(state.getBlock());
        return property == null ? OptionalInt.empty() : OptionalInt.of(COLORS.get(state.get(property)));
    }

    /** The More Dyes color of a sheep's wool, or empty if it has a vanilla color. */
    public static OptionalInt getSheepColor(SheepEntity sheep)
    {
        int index = DyedSheep.getColor(sheep);
        return index < 0 ? OptionalInt.empty() : OptionalInt.of(COLORS.get(index));
    }

    /**
     * Dyes a sheep's wool a More Dyes color, like using the dye on it. Call it on the server; players who can see the
     * sheep are told. Returns false, and does nothing, if the color is not one of More Dyes' colors.
     */
    public static boolean setSheepColor(SheepEntity sheep, int rgb)
    {
        int index = COLORS.indexOf(rgb);
        if (index >= 0)
        {
            DyedSheep.setColor(sheep, index);
        }
        return index >= 0;
    }

    /** Takes a sheep's More Dyes color away, so its wool shows its vanilla color again. Call it on the server. */
    public static void clearSheepColor(SheepEntity sheep)
    {
        DyedSheep.setColor(sheep, -1);
    }

    /**
     * The color a More Dyes color is drawn in. The palette comes from the muted 1.7 colors, so blocks, items and sheep
     * are drawn brighter and more saturated than the color itself.
     */
    public static int displayColor(int rgb)
    {
        float r = (rgb >> 16 & 255) / 255.0F, g = (rgb >> 8 & 255) / 255.0F, b = (rgb & 255) / 255.0F;
        float max = Math.max(r, Math.max(g, b)), min = Math.min(r, Math.min(g, b));
        if (max == 0.0F)
        {
            return rgb;
        }
        float hue;
        if (max == min)
        {
            hue = 0.0F;
        }
        else if (max == r)
        {
            hue = ((g - b) / (max - min) + 6.0F) % 6.0F / 6.0F;
        }
        else if (max == g)
        {
            hue = ((b - r) / (max - min) + 2.0F) / 6.0F;
        }
        else
        {
            hue = ((r - g) / (max - min) + 4.0F) / 6.0F;
        }
        float saturation = Math.min(1.0F, (max - min) / max * 1.25F);
        float value = Math.min(1.0F, max / 0.8F);
        return MathHelper.hsvToRGB(hue, saturation, value);
    }

    /**
     * The vanilla dye color that looks closest to a color, 0xRRGGBB (by CIEDE2000 distance). For a More Dyes color this
     * is the "forge:dyes/..." tag its dye is in.
     */
    public static DyeColor nearestVanillaColor(int rgb)
    {
        return NearestColor.of(rgb);
    }

    private static boolean isOurs(ResourceLocation name)
    {
        return name != null && Reference.MOD_ID.equals(name.getNamespace());
    }

    private static OptionalInt colorOf(ResourceLocation name)
    {
        if (!isOurs(name))
        {
            return OptionalInt.empty();
        }
        Matcher matcher = HEX.matcher(name.getPath());
        while (matcher.find())
        {
            int rgb = Integer.parseInt(matcher.group(1), 16);
            if (COLORS.contains(rgb))
            {
                return OptionalInt.of(rgb);
            }
        }
        return OptionalInt.empty();
    }

    private static IntegerProperty colorProperty(Block block)
    {
        Property<?> property = block.getStateContainer().getProperty("color");
        return property instanceof IntegerProperty && property.getAllowedValues().size() == COLORS.size() ? (IntegerProperty) property : null;
    }
}
