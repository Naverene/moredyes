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

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Leaves the data files of blocks the config turned off out of what the game loads (see toggle/DataFilter). Every
 * folder of JSON files (recipes, loot tables, tags, world gen) is listed through these two methods.
 */
@Mixin(MultiPackResourceManager.class)
abstract class MultiPackResourceManagerMixin
{
    @Inject(method = "listResources", at = @At("RETURN"), cancellable = true)
    private void moredyes$filterResources(String path, Predicate<ResourceLocation> filter,
            CallbackInfoReturnable<Map<ResourceLocation, Resource>> cir)
    {
        cir.setReturnValue(FilteredResources.filter((ResourceManager) (Object) this, path, cir.getReturnValue()));
    }

    @Inject(method = "listResourceStacks", at = @At("RETURN"), cancellable = true)
    private void moredyes$filterStacks(String path, Predicate<ResourceLocation> filter,
            CallbackInfoReturnable<Map<ResourceLocation, List<Resource>>> cir)
    {
        cir.setReturnValue(FilteredResources.filterStacks((ResourceManager) (Object) this, path, cir.getReturnValue()));
    }
}
