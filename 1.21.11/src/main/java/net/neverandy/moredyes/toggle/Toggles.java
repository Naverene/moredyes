package net.neverandy.moredyes.toggle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Which parts of the mod are turned on, from the config (see ConfigHandler). Blocks that are turned off are never
 * registered, which is what saves the memory and loading time. Their recipes, loot tables and tag entries are then
 * dropped as the data loads (see DataFilter), so turning blocks off doesn't fill the log with errors.
 *
 * <p>A block type is the part of a block's id before its color, such as "concretepowder" for
 * moredyes:concretepowder_334c59.
 */
public final class Toggles
{
    public static boolean stairs = true;
    public static boolean slabs = true;
    public static boolean walls = true;
    public static boolean fences = true;
    public static boolean panes = true;
    public static boolean ironChests = true;
    public static boolean storageDrawers = true;

    private static final Set<String> disabledTypes = new HashSet<>();
    private static final Set<String> knownTypes = Collections.synchronizedSet(new TreeSet<>());

    /** The ids ("moredyes:stoneslab_334c59") of every block and item that was left out. */
    private static final Set<String> disabledIds = ConcurrentHashMap.newKeySet();

    private Toggles() {}

    /** Sets the block types named in the config's disabled_blocks list. */
    public static void setDisabledTypes(Collection<? extends String> types)
    {
        disabledTypes.clear();
        for (String type : types)
        {
            String name = type.trim().toLowerCase(Locale.ROOT);
            // Accept a full id too, such as "moredyes:stone" or "moredyes:stone_334c59".
            if (name.startsWith("moredyes:"))
            {
                name = name.substring("moredyes:".length());
            }
            name = name.replaceFirst("_[0-9a-f]{6}$", "");
            if (!name.isEmpty())
            {
                disabledTypes.add(name);
            }
        }
    }

    /** Whether a block type is on. Also notes the type as one that exists, for {@link #unknownTypes()}. */
    public static boolean typeEnabled(String type)
    {
        knownTypes.add(type);
        return !disabledTypes.contains(type);
    }

    /** Notes that the block or item "moredyes:{@code name}" was left out. */
    public static void leftOut(String name)
    {
        disabledIds.add("moredyes:" + name);
    }

    /** Notes another id that no longer exists, such as a world gen feature dropped by DataFilter. */
    static void leftOutId(String id)
    {
        disabledIds.add(id);
    }

    public static boolean isLeftOut(String id)
    {
        return disabledIds.contains(id);
    }

    public static boolean anythingLeftOut()
    {
        return !disabledIds.isEmpty();
    }

    /** How many ids were left out so far; it grows as DataFilter drops world gen files. */
    static int leftOutCount()
    {
        return disabledIds.size();
    }

    /** Names in disabled_blocks that aren't block types, to warn about once the blocks are registered. */
    public static List<String> unknownTypes()
    {
        List<String> unknown = new ArrayList<>();
        for (String type : disabledTypes)
        {
            if (!knownTypes.contains(type))
            {
                unknown.add(type);
            }
        }
        return unknown;
    }

    /** Every block type, for the warning about unknown names. */
    public static String knownTypes()
    {
        synchronized (knownTypes)
        {
            return String.join(", ", knownTypes);
        }
    }
}
