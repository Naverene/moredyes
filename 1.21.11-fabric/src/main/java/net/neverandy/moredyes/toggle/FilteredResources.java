package net.neverandy.moredyes.toggle;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.neverandy.moredyes.MoreDyes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** Runs DataFilter over the files a resource manager lists (see mixin/MultiPackResourceManagerMixin). */
public final class FilteredResources
{
    private FilteredResources() {}

    public static Map<Identifier, Resource> filter(String path, Map<Identifier, Resource> resources)
    {
        if (!Toggles.anythingLeftOut())
        {
            return resources;
        }
        Map<Identifier, Resource> kept;
        int leftOut;
        // A dropped world gen file can name another file of the same folder (a tree that a random selector picks),
        // which is then dropped too; so the folder is gone over again until nothing more drops.
        do
        {
            leftOut = Toggles.leftOutCount();
            kept = new TreeMap<>();
            for (Map.Entry<Identifier, Resource> entry : resources.entrySet())
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

    public static Map<Identifier, List<Resource>> filterStacks(String path, Map<Identifier, List<Resource>> stacks)
    {
        if (!Toggles.anythingLeftOut())
        {
            return stacks;
        }
        Map<Identifier, List<Resource>> kept;
        int leftOut;
        do
        {
            leftOut = Toggles.leftOutCount();
            kept = new TreeMap<>();
            for (Map.Entry<Identifier, List<Resource>> entry : stacks.entrySet())
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
    private static Resource filter(Identifier location, Resource resource)
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
