package net.neverandy.moredyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.item.DyeColor;

/** Vanilla's private setter for a wolf's collar color, used by {@code entity/DyedCollars}. */
@Mixin(Wolf.class)
public interface WolfAccessor {

    @Invoker("setCollarColor")
    void moredyes$setCollarColor(DyeColor color);
}
