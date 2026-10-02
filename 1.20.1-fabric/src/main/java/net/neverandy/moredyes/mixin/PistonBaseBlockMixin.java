package net.neverandy.moredyes.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neverandy.moredyes.block.BlockPiston;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * PistonBaseBlock.isPushable only treats vanilla pistons as pistons; every other block is moved according to its
 * push reaction. A retracted dyed piston can be pushed and pulled like any block, an extended one cannot.
 */
@Mixin(PistonBaseBlock.class)
public abstract class PistonBaseBlockMixin
{
    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    private static void moredyes$extendedDyedPiston(BlockState state, Level level, BlockPos pos, Direction direction, boolean allowDestroy,
                                                   Direction pistonFacing, CallbackInfoReturnable<Boolean> cir)
    {
        if (state.getBlock() instanceof BlockPiston && state.getValue(PistonBaseBlock.EXTENDED))
        {
            cir.setReturnValue(false);
        }
    }
}
