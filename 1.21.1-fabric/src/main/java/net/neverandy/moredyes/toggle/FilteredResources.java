package net.neverandy.moredyes.toggle;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neverandy.moredyes.MoreDyes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.WeakHashMap;

/** Runs DataFilter over the files a resource manager lists (see mixin/MultiPackResourceManagerMixin). */
public final class FilteredResources
{
    /** The resource managers whose world gen files were already gone over (see {@link #prepareWorldGen}). */
    private static final Set<ResourceManager> PREPARED = Collections.newSetFromMap(new WeakHashMap<>());
    /** Set while this class lists files itself, so that listing isn't filtered. */
    private static final ThreadLocal<Boolean> BUSY = ThreadLocal.withInitial(() -> false);

    private FilteredResources() {}

    /**
     * Goes over every world gen file once, before any is handed out, so that a feature dropped with its blocks also
     * drops the features and biome modifiers that use it. The game loads the world gen folders on several threads at
     * once, so the folders can't count on each other being filtered first.
     */
    private static void prepareWorldGen(ResourceManager manager, String path)
    {
        if (!path.startsWith("worldgen") && !path.startsWith("neoforge"))
        {
            return;
        }
        synchronized (PREPARED)
        {
            if (!PREPARED.add(manager))
            {
                return;
            }
            BUSY.set(true);
            try
            {
                Map<ResourceLocation, Resource> all = manager.listResources("worldgen", location -> location.getPath().endsWith(".json"));
                int leftOut;
                do
                {
                    leftOut = Toggles.leftOutCount();
                    for (Map.Entry<ResourceLocation, Resource> entry : all.entrySet())
                    {
                        filter(entry.getKey(), entry.getValue());
                    }
                }
                while (Toggles.leftOutCount() != leftOut);
            }
            finally
            {
                BUSY.set(false);
            }
        }
    }

    public static Map<ResourceLocation, Resource> filter(ResourceManager manager, String path, Map<ResourceLocation, Resource> resources)
    {
        if (BUSY.get() || !Toggles.anythingLeftOut())
        {
            return resources;
        }
        prepareWorldGen(manager, path);
        Map<ResourceLocation, Resource> kept;
        int leftOut;
        // A dropped world gen file can name another file of the same folder (a tree that a random selector picks),
        // which is then dropped too; so the folder is gone over again until nothing more drops.
        do
        {
            leftOut = Toggles.leftOutCount();
            kept = new TreeMap<>();
            for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet())
            {
                Resource resource = filter(entry.getKey(), entry.getValue());
                if (resource != null)
                {
                    kept.put(entry.getKey(), resource);
                }
            }
        }
        while (Toggles.leftOutCount() != leftOut);
        report(path, resources.size() - kept.size());
        return kept;
    }

    public static Map<ResourceLocation, List<Resource>> filterStacks(ResourceManager manager, String path, Map<ResourceLocation, List<Resource>> stacks)
    {
        if (BUSY.get() || !Toggles.anythingLeftOut())
        {
            return stacks;
        }
        prepareWorldGen(manager, path);
        Map<ResourceLocation, List<Resource>> kept;
        int leftOut;
        do
        {
            leftOut = Toggles.leftOutCount();
            kept = new TreeMap<>();
            for (Map.Entry<ResourceLocation, List<Resource>> entry : stacks.entrySet())
            {
                List<Resource> stack = new ArrayList<>();
                for (Resource resource : entry.getValue())
                {
                    Resource keptResource = filter(entry.getKey(), resource);
                    if (keptResource != null)
                    {
                        stack.add(keptResource);
                    }
                }
                if (!stack.isEmpty())
                {
                    kept.put(entry.getKey(), stack);
                }
            }
        }
        while (Toggles.leftOutCount() != leftOut);
        report(path, stacks.size() - kept.size());
        return kept;
    }

    private static void report(String path, int dropped)
    {
        if (dropped > 0)
        {
            MoreDyes.LOGGER.info("Left out {} files in {} for blocks turned off in the More Dyes config", dropped, path);
        }
    }

    /** The resource as DataFilter leaves it, or null to drop it. */
    private static Resource filter(ResourceLocation location, Resource resource)
    {
        if (!DataFilter.applies(location.getPath()))
        {
            return resource;
        }
        String json;
        try (InputStream in = resource.open())
        {
            json = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            MoreDyes.LOGGER.debug("Couldn't read {} to check it for turned-off blocks", location, e);
            return resource;
        }
        String kept = DataFilter.filter(location.getNamespace(), location.getPath(), json);
        if (kept == null)
        {
            return null;
        }
        // Read once already, so the loader gets the text from memory rather than reading the file again.
        byte[] bytes = kept.getBytes(StandardCharsets.UTF_8);
        return new Resource(resource.source(), () -> new ByteArrayInputStream(bytes), resource::metadata);
    }
}
