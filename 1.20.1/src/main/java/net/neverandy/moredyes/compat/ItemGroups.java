package net.neverandy.moredyes.compat;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The groups that recipe viewers able to collapse items (REI) fold the mod's items into: one per kind of item, holding
 * every color of it, such as all 118 "Dyed Oak Planks". A kind is the registry name without its color, so
 * "wool_334c59" is in the group "moredyes:wool" and "334c59_dye" in "moredyes:dye". Each group is named by the
 * translation key "group.moredyes.&lt;kind&gt;", which ModLangProvider fills in with {@link #englishName}.
 */
public final class ItemGroups
{
    public static final String TRANSLATION_PREFIX = "group." + Reference.MOD_ID + ".";

    private static final Set<String> COLORS = new HashSet<>(Arrays.asList(ColorStrings.ALL));
    /** Last words of names that read the same in the plural, such as "Glass" or "Concrete Powder". */
    private static final Set<String> UNCOUNTABLE = new HashSet<>(Arrays.asList("Andesite", "Clay", "Cobblestone",
            "Concrete", "Diorite", "Glass", "Glowstone", "Granite", "Gravel", "Ice", "Obsidian", "Powder", "Rockwool",
            "Sand", "Sandstone", "Stone", "Terracotta", "Wood", "Wool"));

    private ItemGroups()
    {
    }

    /** The kind of an item from its registry path: "oakplanks_334c59" is "oakplanks". */
    public static String kind(String path)
    {
        List<String> parts = new ArrayList<>(Arrays.asList(path.split("_")));
        parts.removeIf(COLORS::contains);
        return String.join("_", parts);
    }

    /** Every item of the mod by kind, in the order they were registered. */
    public static Map<String, List<Item>> byKind()
    {
        Map<String, List<Item>> groups = new LinkedHashMap<>();
        for (Item item : BuiltInRegistries.ITEM)
        {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            if (Reference.MOD_ID.equals(id.getNamespace()))
            {
                groups.computeIfAbsent(kind(id.getPath()), k -> new ArrayList<>()).add(item);
            }
        }
        return groups;
    }

    public static ResourceLocation id(String kind)
    {
        return new ResourceLocation(Reference.MOD_ID, kind);
    }

    /**
     * The English name of a group from the name of one of its items: "334C59 Oak Planks" gives "Dyed Oak Planks",
     * "334C59 Poppy" gives "Dyed Poppies", and the dyes are "Mixed Dyes".
     */
    public static String englishName(String kind, String itemName)
    {
        if (kind.equals("dye"))
        {
            return "Mixed Dyes";
        }
        return "Dyed " + plural(itemName.substring(itemName.indexOf(' ') + 1));
    }

    static String plural(String name)
    {
        int of = name.indexOf(" of ");
        if (of > 0)
        {
            // "Block of Coal", "Lily of the Valley"
            return plural(name.substring(0, of)) + name.substring(of);
        }
        String last = name.substring(name.lastIndexOf(' ') + 1);
        if (UNCOUNTABLE.contains(last) || name.endsWith("s"))
        {
            return name;
        }
        if (name.endsWith("shelf"))
        {
            return name.substring(0, name.length() - 1) + "ves";
        }
        if (name.endsWith("sh") || name.endsWith("ch") || name.endsWith("x"))
        {
            return name + "es";
        }
        if (name.endsWith("y") && "aeiou".indexOf(name.charAt(name.length() - 2)) < 0)
        {
            return name.substring(0, name.length() - 1) + "ies";
        }
        return name + "s";
    }
}
