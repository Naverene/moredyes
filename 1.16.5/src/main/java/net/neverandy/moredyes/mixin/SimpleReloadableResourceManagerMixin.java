package net.neverandy.moredyes.mixin;

import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.neverandy.moredyes.toggle.FilteredResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * Leaves the data files of blocks the config turned off out of what the game loads (see toggle/DataFilter). Every
 * folder of JSON files (recipes, loot tables, tags) is listed and read through these methods.
 *
 * <p>Each method is named twice: by its MCP name for the dev game, and by its SRG name for the released game, since
 * this mod's build makes no mixin refmap.
 */
@Mixin(SimpleReloadableResourceManager.class)
abstract class SimpleReloadableResourceManagerMixin
{
    @Inject(method = {"getAllResourceLocations", "func_199003_a"}, at = @At("RETURN"), cancellable = true, remap = false, require = 1)
    private void moredyes$filterList(String path, Predicate<String> filter, CallbackInfoReturnable<Collection<ResourceLocation>> cir)
    {
        cir.setReturnValue(FilteredResources.filterList((IResourceManager) this, path, cir.getReturnValue()));
    }

    @Inject(method = {"getResource", "func_199002_a"}, at = @At("RETURN"), cancellable = true, remap = false, require = 1)
    private void moredyes$filterResource(ResourceLocation location, CallbackInfoReturnable<IResource> cir) throws IOException
    {
        IResource resource = FilteredResources.filter(location, cir.getReturnValue());
        if (resource == null)
        {
            throw new FileNotFoundException(location.toString());
        }
        cir.setReturnValue(resource);
    }

    @Inject(method = {"getAllResources", "func_199004_b"}, at = @At("RETURN"), cancellable = true, remap = false, require = 1)
    private void moredyes$filterResources(ResourceLocation location, CallbackInfoReturnable<List<IResource>> cir)
    {
        cir.setReturnValue(FilteredResources.filter(location, cir.getReturnValue()));
    }
}
