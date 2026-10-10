package net.neverandy.moredyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.ItemStack;

import net.neverandy.moredyes.entity.DyedSheep;

/**
 * Every item an entity drops goes through spawnAtLocation: the wool from shears and dispensers (Sheep.shear) and the
 * loot of a killed sheep. A sheep with a More Dyes color drops its dyed wool instead of vanilla wool. The NeoForge
 * build does this with a global loot modifier, which Fabric has no equivalent of for 1.21.1.
 */
@Mixin(Entity.class)
abstract class EntityMixin {

    @ModifyVariable(
        method = "spawnAtLocation(Lnet/minecraft/world/item/ItemStack;F)Lnet/minecraft/world/entity/item/ItemEntity;",
        at = @At("HEAD"), argsOnly = true)
    private ItemStack moredyes$dyedWool(ItemStack stack) {
        return (Object) this instanceof Sheep sheep ? DyedSheep.drop(sheep, stack) : stack;
    }
}
