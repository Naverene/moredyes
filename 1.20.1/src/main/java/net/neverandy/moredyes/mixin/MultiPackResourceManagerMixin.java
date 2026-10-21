package net.neverandy.moredyes.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.Resource;
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
 *
 * <p>Each method is named twice: by its Mojang name for the dev game, and by its SRG name for the released game, since
 * this mod's build makes no mixin refmap.
 */
@Mixin(MultiPackResourceManager.class)
abstract class MultiPackResourceManagerMixin
{
    @Inject(method = {"listResources", "m_214159_"}, at = @At("RETURN"), cancellable = true, remap = false, require = 1)
    private void moredyes$filterResources(String path, Predicate<ResourceLocation> filter,
            CallbackInfoReturnable<Map<ResourceLocation, Resource>> cir)
    {
        cir.setReturnValue(FilteredResources.filter(path, cir.getReturnValue()));
    }

    @Inject(method = {"listResourceStacks", "m_214160_"}, at = @At("RETURN"), cancellable = true, remap = false, require = 1)
    private void moredyes$filterStacks(String path, Predicate<ResourceLocation> filter,
            CallbackInfoReturnable<Map<ResourceLocation, List<Resource>>> cir)
    {
        cir.setReturnValue(FilteredResources.filterStacks(path, cir.getReturnValue()));
    }
}
