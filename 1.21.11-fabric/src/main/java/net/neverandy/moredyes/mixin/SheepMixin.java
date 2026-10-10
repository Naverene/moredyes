package net.neverandy.moredyes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.level.ServerLevelAccessor;

import net.neverandy.moredyes.entity.DyedSheep;

/** Colors spawned and bred sheep (see DyedSheep), where NeoForge has spawn and breeding events. */
@Mixin(Sheep.class)
abstract class SheepMixin {

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void moredyes$spawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason,
        SpawnGroupData data, CallbackInfoReturnable<SpawnGroupData> cir) {
        DyedSheep.onSpawn((Sheep) (Object) this, reason);
    }

    @Inject(
        method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/sheep/Sheep;",
        at = @At("RETURN"))
    private void moredyes$breed(ServerLevel level, AgeableMob other, CallbackInfoReturnable<Sheep> cir) {
        if (cir.getReturnValue() != null && other instanceof Sheep mate) {
            DyedSheep.onBreed((Sheep) (Object) this, mate, cir.getReturnValue());
        }
    }
}
