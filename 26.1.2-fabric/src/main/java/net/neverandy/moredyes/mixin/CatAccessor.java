package net.neverandy.moredyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.item.DyeColor;

/** Vanilla's private setter for a cat's collar color, used by {@code entity/DyedCollars}. */
@Mixin(Cat.class)
public interface CatAccessor {

    @Invoker("setCollarColor")
    void moredyes$setCollarColor(DyeColor color);
}
