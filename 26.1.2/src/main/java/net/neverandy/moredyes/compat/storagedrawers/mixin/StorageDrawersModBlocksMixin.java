package net.neverandy.moredyes.compat.storagedrawers.mixin;

import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.jaquadro.minecraft.storagedrawers.core.ModBlocks;

import net.minecraft.world.level.block.Block;

import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;

/**
 * Adds the dyed drawers to the drawer blocks Storage Drawers lists from its own registry. Storage Drawers builds its
 * drawer block entity types from this list (so hoppers, pipes and controllers see our drawers), as well as its label
 * and count positions and the overlays on drawer models (locks, upgrades, missing drawers). 1.20.1's Storage Drawers
 * had a hook for this ({@code ModBlocks.tryAddExternalRegistry}); 26.3's doesn't.
 *
 * <p>
 * This mixin config only loads when Storage Drawers is installed (see {@code requiredMods} in neoforge.mods.toml).
 */
@Mixin(ModBlocks.class)
abstract class StorageDrawersModBlocksMixin {

    @Inject(method = "getBlocksOfType", at = @At("RETURN"), cancellable = true)
    private static <B extends Block> void moredyes$addDyedDrawers(Class<B> blockClass, CallbackInfoReturnable<Stream<B>> cir) {
        cir.setReturnValue(Stream.concat(cir.getReturnValue(),
            StorageDrawersCompat.blocks().stream().filter(blockClass::isInstance).map(blockClass::cast)));
    }
}
