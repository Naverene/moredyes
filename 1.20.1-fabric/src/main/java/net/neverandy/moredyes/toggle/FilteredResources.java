package net.neverandy.moredyes.toggle;

import net.minecraft.resources.ResourceLocation;
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

    public static Map<ResourceLocation, Resource> filter(String path, Map<ResourceLocation, Resource> resources)
    {
        if (!Toggles.anythingLeftOut())
        {
            return resources;
        }
        Map<ResourceLocation, Resource> kept = new TreeMap<>();
        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet())
        {
            Resource resource = filter(entry.getKey(), entry.getValue());
            if (resource != null)
            {
                kept.put(entry.getKey(), resource);
            }
        }
        report(path, resources.size() - kept.size());
        return kept;
    }

    public static Map<ResourceLocation, List<Resource>> filterStacks(String path, Map<ResourceLocation, List<Resource>> stacks)
    {
        if (!Toggles.anythingLeftOut())
        {
            return stacks;
        }
        Map<ResourceLocation, List<Resource>> kept = new TreeMap<>();
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
