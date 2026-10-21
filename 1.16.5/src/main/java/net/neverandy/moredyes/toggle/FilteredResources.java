package net.neverandy.moredyes.toggle;

import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.ResourceLocation;
import net.neverandy.moredyes.MoreDyes;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Runs DataFilter over the files a resource manager hands out (see mixin/SimpleReloadableResourceManagerMixin). A
 * folder is listed first and each file fetched after, so listing leaves out the files DataFilter drops, and fetching
 * hands out the rest with the turned-off blocks taken out.
 *
 * <p>The location of each file is passed along, since IResource.getLocation() only exists on the client.
 */
public final class FilteredResources
{
    private FilteredResources() {}

    /** The listed files without those DataFilter drops in every pack. */
    public static Collection<ResourceLocation> filterList(IResourceManager manager, String path, Collection<ResourceLocation> locations)
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

    private static boolean allDropped(IResourceManager manager, ResourceLocation location)
    {
        try
        {
            for (IResource resource : manager.getAllResources(location))
            {
                try
                {
                    if (DataFilter.filter(location.getNamespace(), location.getPath(), read(resource)) != null)
                    {
                        return false;
                    }
                }
                finally
                {
                    resource.close();
                }
            }
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    /** The resource at {@code location} with the turned-off blocks taken out, or null when DataFilter drops it. */
    @Nullable
    public static IResource filter(ResourceLocation location, @Nullable IResource resource)
    {
        if (resource == null || !Toggles.anythingLeftOut() || !DataFilter.applies(location.getPath()))
        {
            return resource;
        }
        String json;
        try
        {
            json = read(resource);
        }
        catch (IOException e)
        {
            MoreDyes.LOGGER.debug("Couldn't read {} to check it for turned-off blocks", location, e);
            return resource;
        }
        finally
        {
            close(resource);
        }
        String kept = DataFilter.filter(location.getNamespace(), location.getPath(), json);
        return kept == null ? null : new Filtered(location, resource, kept.getBytes(StandardCharsets.UTF_8));
    }

    public static List<IResource> filter(ResourceLocation location, List<IResource> resources)
    {
        if (!Toggles.anythingLeftOut())
        {
            return resources;
        }
        List<IResource> kept = new ArrayList<>();
        for (IResource resource : resources)
        {
            IResource filtered = filter(location, resource);
            if (filtered != null)
            {
                kept.add(filtered);
            }
        }
        return kept;
    }

    private static String read(IResource resource) throws IOException
    {
        InputStream in = resource.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int n;
        while ((n = in.read(buffer)) > 0)
        {
            out.write(buffer, 0, n);
        }
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }

    private static void close(IResource resource)
    {
        try
        {
            resource.close();
        }
        catch (IOException ignored)
        {
        }
    }

    /** A resource whose contents were already read and changed. */
    private static final class Filtered implements IResource
    {
        private final ResourceLocation location;
        private final IResource original;
        private final InputStream stream;

        Filtered(ResourceLocation location, IResource original, byte[] contents)
        {
            this.location = location;
            this.original = original;
            this.stream = new ByteArrayInputStream(contents);
        }

        // Client only in IResource, so not marked @Override; on a server nothing calls it.
        public ResourceLocation getLocation()
        {
            return location;
        }

        @Override
        public InputStream getInputStream()
        {
            return stream;
        }

        @Nullable
        public <T> T getMetadata(IMetadataSectionSerializer<T> serializer)
        {
            return null;
        }

        @Override
        public String getPackName()
        {
            return original.getPackName();
        }

        @Override
        public void close()
        {
        }
    }
}
