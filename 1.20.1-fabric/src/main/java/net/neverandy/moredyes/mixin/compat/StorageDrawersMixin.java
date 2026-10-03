package net.neverandy.moredyes.mixin.compat;

import com.jaquadro.minecraft.storagedrawers.core.ModBlockEntities;
import com.texelsaurus.minecraft.chameleon.api.ChameleonInit;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Registers the dyed drawers just before Storage Drawers makes its block entity types from its list of drawer blocks,
 * so the types accept ours. Fabric runs the mods' initializers in no set order, so More Dyes' own initializer may come
 * too late for that. Only applied when Storage Drawers is installed (see CompatMixinPlugin).
 */
@Pseudo
@Mixin(value = ModBlockEntities.class, remap = false)
public abstract class StorageDrawersMixin
{
    @Inject(method = "init", at = @At("HEAD"))
    private static void moredyes$registerDyedDrawers(ChameleonInit.InitContext context, CallbackInfo ci)
    {
        StorageDrawersCompat.registerBlocks();
    }
}
