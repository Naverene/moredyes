package net.neverandy.moredyes.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
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
 * <p>Each method is named twice: by its Mojang name for the dev game, and by its SRG name for the released game, since
 * this mod's build makes no mixin refmap.
 */
@Mixin(MultiPackResourceManager.class)
abstract class MultiPackResourceManagerMixin
{
    @Inject(method = {"listResources", "m_6540_"}, at = @At("RETURN"), cancellable = true, remap = false, require = 1)
    private void moredyes$filterList(String path, Predicate<String> filter, CallbackInfoReturnable<Collection<ResourceLocation>> cir)
    {
        cir.setReturnValue(FilteredResources.filterList((ResourceManager) this, path, cir.getReturnValue()));
    }

    @Inject(method = {"getResource", "m_142591_"}, at = @At("RETURN"), cancellable = true, remap = false, require = 1)
    private void moredyes$filterResource(ResourceLocation location, CallbackInfoReturnable<Resource> cir) throws IOException
    {
        Resource resource = FilteredResources.filter(cir.getReturnValue());
        if (resource == null)
        {
            throw new FileNotFoundException(location.toString());
        }
        cir.setReturnValue(resource);
    }

    @Inject(method = {"getResources", "m_7396_"}, at = @At("RETURN"), cancellable = true, remap = false, require = 1)
    private void moredyes$filterResources(ResourceLocation location, CallbackInfoReturnable<List<Resource>> cir)
    {
        cir.setReturnValue(FilteredResources.filter(cir.getReturnValue()));
    }
}
