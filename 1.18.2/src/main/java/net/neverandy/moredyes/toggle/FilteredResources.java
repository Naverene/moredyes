package net.neverandy.moredyes.toggle;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neverandy.moredyes.MoreDyes;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Runs DataFilter over the files a resource manager hands out (see mixin/MultiPackResourceManagerMixin). A folder is
 * listed first and each file fetched after, so listing leaves out the files DataFilter drops, and fetching hands out
 * the rest with the turned-off blocks taken out.
 */
public final class FilteredResources
{
    private FilteredResources() {}

    /** The listed files without those DataFilter drops in every pack. */
    public static Collection<ResourceLocation> filterList(ResourceManager manager, String path, Collection<ResourceLocation> locations)
    {
        if (!Toggles.anythingLeftOut())
        {
            return locations;
        }
        List<ResourceLocation> kept = new ArrayList<>();
        for (ResourceLocation location : locations)
        {
            if (!DataFilter.applies(location.getPath()) || !allDropped(manager, location))
            {
                kept.add(location);
            }
        }
        int dropped = locations.size() - kept.size();
        if (dropped > 0)
        {
            MoreDyes.LOGGER.info("Left out {} files in {} for blocks turned off in the More Dyes config", dropped, path);
        }
        return kept;
    }

    private static boolean allDropped(ResourceManager manager, ResourceLocation location)
    {
        try
        {
            for (Resource resource : manager.getResources(location))
            {
                try (resource)
                {
                    if (DataFilter.filter(location.getNamespace(), location.getPath(), read(resource)) != null)
                    {
                        return false;
                    }
                }
            }
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    /** The resource with the turned-off blocks taken out, or null when DataFilter drops it. */
    @Nullable
    public static Resource filter(@Nullable Resource resource)
    {
        if (resource == null || !Toggles.anythingLeftOut() || !DataFilter.applies(resource.getLocation().getPath()))
        {
            return resource;
        }
        ResourceLocation location = resource.getLocation();
        String json;
        try (resource)
        {
            json = read(resource);
        }
        catch (IOException e)
        {
            MoreDyes.LOGGER.debug("Couldn't read {} to check it for turned-off blocks", location, e);
            return resource;
        }
        String kept = DataFilter.filter(location.getNamespace(), location.getPath(), json);
        return kept == null ? null : new Filtered(resource, kept.getBytes(StandardCharsets.UTF_8));
    }

    public static List<Resource> filter(List<Resource> resources)
    {
        if (!Toggles.anythingLeftOut())
        {
            return resources;
        }
        List<Resource> kept = new ArrayList<>();
        for (Resource resource : resources)
        {
            Resource filtered = filter(resource);
            if (filtered != null)
            {
                kept.add(filtered);
            }
        }
        return kept;
    }

    private static String read(Resource resource) throws IOException
    {
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    /** A resource whose contents were already read and changed. */
    private static final class Filtered implements Resource
    {
        private final Resource original;
        private final InputStream stream;

        Filtered(Resource original, byte[] contents)
        {
            this.original = original;
            this.stream = new ByteArrayInputStream(contents);
        }

        @Override
        public ResourceLocation getLocation()
        {
            return original.getLocation();
        }

        @Override
        public InputStream getInputStream()
        {
            return stream;
        }

        @Override
        public boolean hasMetadata()
        {
            return false;
        }

        @Nullable
        @Override
        public <T> T getMetadata(MetadataSectionSerializer<T> serializer)
        {
            return null;
        }

        @Override
        public String getSourceName()
        {
            return original.getSourceName();
        }

        @Override
        public void close()
        {
        }
    }
}
