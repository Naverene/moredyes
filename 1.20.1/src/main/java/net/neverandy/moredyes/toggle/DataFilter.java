package net.neverandy.moredyes.toggle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Drops the data files that name blocks or items the config turned off (see Toggles), as they load. Without this every
 * recipe, loot table and tag of a missing block would fail to load and log an error, tens of thousands of them when a
 * whole shape is off.
 *
 * <p>A file is dropped when a block it needs is gone, such as a recipe's result or a key of a shaped recipe. Where a
 * list only offers choices (a tag's values, a loot pool's entries, the items an ingredient accepts), only the missing
 * entries are taken out, and the file is dropped only when nothing is left. Everything else is left as it is.
 *
 * <p>Only active when something is turned off, so with the default config no file is touched.
 */
public final class DataFilter
{
    private static final Pattern ID = Pattern.compile("moredyes:[a-z0-9_./-]+");

    /** Folders of client resources, which never name a block by id; they are skipped to save the work. */
    private static final String[] SKIPPED = {"models/", "blockstates/", "textures/", "lang/", "font/", "sounds/", "particles/",
            "shaders/", "texts/", "atlases/"};

    private DataFilter() {}

    /** Whether a file at this path inside a pack's namespace should be looked at. */
    public static boolean applies(String path)
    {
        if (!Toggles.anythingLeftOut() || !path.endsWith(".json"))
        {
            return false;
        }
        for (String skipped : SKIPPED)
        {
            if (path.startsWith(skipped))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * The file's contents with the turned-off blocks taken out: {@code json} itself when nothing names them, a changed
     * copy, or null when the file should be dropped. {@code namespace} and {@code path} are where the file is, such as
     * "moredyes" and "recipes/stone_334c59.json".
     */
    @SuppressWarnings("deprecation")
    public static String filter(String namespace, String path, String json)
    {
        if (!mentionsLeftOut(json))
        {
            return json;
        }
        JsonElement root;
        try
        {
            root = new JsonParser().parse(json);
        }
        catch (RuntimeException e)
        {
            // Not ours to judge; let the game report it as usual.
            return json;
        }
        JsonElement kept = prune(root, "");
        if (kept == null)
        {
            dropped(namespace, path);
            return null;
        }
        return kept.toString();
    }

    private static boolean mentionsLeftOut(String json)
    {
        Matcher matcher = ID.matcher(json);
        while (matcher.find())
        {
            if (Toggles.isLeftOut(matcher.group()))
            {
                return true;
            }
        }
        return false;
    }

    /** A world gen feature that was dropped is gone too, so the files that place it are dropped in turn. */
    private static void dropped(String namespace, String path)
    {
        if ("moredyes".equals(namespace) && path.startsWith("worldgen/"))
        {
            int registryEnd = path.indexOf('/', "worldgen/".length());
            if (registryEnd > 0)
            {
                Toggles.leftOutId(namespace + ":" + path.substring(registryEnd + 1, path.length() - ".json".length()));
            }
        }
    }

    /** The element without the turned-off ids, or null when it can't do without them. {@code key} is where it sits. */
    private static JsonElement prune(JsonElement element, String key)
    {
        if (element.isJsonPrimitive())
        {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            return primitive.isString() && Toggles.isLeftOut(primitive.getAsString()) ? null : element;
        }
        if (element.isJsonObject())
        {
            JsonObject kept = new JsonObject();
            for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet())
            {
                String childKey = key.equals("key") ? "key." + entry.getKey() : entry.getKey();
                JsonElement child = prune(entry.getValue(), childKey);
                if (child == null)
                {
                    return null;
                }
                kept.add(entry.getKey(), child);
            }
            return kept;
        }
        if (element.isJsonArray())
        {
            JsonArray array = element.getAsJsonArray();
            JsonArray kept = new JsonArray();
            boolean optional = isChoice(key);
            for (JsonElement child : array)
            {
                JsonElement keptChild = prune(child, key + "[]");
                if (keptChild != null)
                {
                    kept.add(keptChild);
                }
                else if (!optional)
                {
                    return null;
                }
            }
            return array.size() > 0 && kept.size() == 0 ? null : kept;
        }
        return element;
    }

    /**
     * Whether a list under this key is a list of choices, any of which can go: tag values, loot entries, world gen
     * features and weighted entries, and the items an ingredient accepts. A shapeless recipe's own list of ingredients
     * isn't one, since the recipe would change without them; a list nested in it is (one ingredient's choices).
     */
    private static boolean isChoice(String key)
    {
        switch (key)
        {
            case "values":
            case "entries":
            case "children":
            case "features":
            case "ingredient":
            case "ingredients[]":
                return true;
            default:
                return key.startsWith("key.");
        }
    }
}
