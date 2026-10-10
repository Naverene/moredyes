package net.neverandy.moredyes.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.portal.PortalShape;

import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.registry.ModBlocks;

/**
 * Lets dyed obsidian build nether portals. Vanilla's portal frame check only takes {@code minecraft:obsidian} (NeoForge
 * asks the block, which is how the NeoForge build does it), so this widens the check to the dyed obsidian too.
 */
@Mixin(PortalShape.class)
abstract class PortalShapeMixin {

    @Shadow
    @Final
    @Mutable
    private static BlockBehaviour.StatePredicate FRAME;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void moredyes$dyedObsidianFrame(CallbackInfo ci) {
        BlockBehaviour.StatePredicate vanilla = FRAME;
        FRAME = (state, level, pos) -> vanilla.test(state, level, pos) || ModBlocks.isKind(state, Kind.OBSIDIAN);
    }
}
