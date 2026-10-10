package net.neverandy.moredyes.compat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.color.MixColors;

/**
 * The groups that recipe viewers able to collapse items (REI) fold the mod's items into: one per kind of item, holding
 * every color of it, such as all 118 "Dyed Oak Planks". A kind is the registry name without its color, so
 * {@code wool_334c59} is in the group {@code moredyes:wool} and {@code dye_334c59} in {@code moredyes:dye}. Each group is
 * named by the translation key {@code group.moredyes.<kind>}, which tools/generate_resources.py writes.
 */
public final class ItemGroups {

    public static final String TRANSLATION_PREFIX = "group." + MoreDyes.MOD_ID + ".";

    private ItemGroups() {}

    /** The kind of an item from its registry path: {@code oak_planks_334c59} is {@code oak_planks}. */
    public static String kind(String path) {
        int end = path.lastIndexOf('_');
        return end > 0 && MixColors.byHex(path.substring(end + 1)).isPresent() ? path.substring(0, end) : path;
    }

    /** Every item of the mod by kind, in the order they were registered. */
    public static Map<String, List<Item>> byKind() {
        Map<String, List<Item>> groups = new LinkedHashMap<>();
        for (Item item : BuiltInRegistries.ITEM) {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            if (MoreDyes.MOD_ID.equals(id.getNamespace())) {
                groups.computeIfAbsent(kind(id.getPath()), k -> new ArrayList<>()).add(item);
            }
        }
        return groups;
    }

    public static ResourceLocation id(String kind) {
        return ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID, kind);
    }
}
