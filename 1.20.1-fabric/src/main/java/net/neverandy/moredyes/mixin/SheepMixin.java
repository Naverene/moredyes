package net.neverandy.moredyes.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neverandy.moredyes.entity.DyedSheep;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Gives every sheep a MoreDyes color (see entity/DyedSheep), saved with the sheep as its hex code. */
@Mixin(Sheep.class)
public abstract class SheepMixin implements DyedSheep.Fleece
{
    @Unique
    private static final String KEY = Reference.MOD_ID + ":fleece";

    @Unique
    private int moredyes$fleece = -1;

    @Override
    public int moredyes$getFleece()
    {
        return moredyes$fleece;
    }

    @Override
    public void moredyes$setFleece(int color)
    {
        moredyes$fleece = color;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void moredyes$save(CompoundTag tag, CallbackInfo ci)
    {
        if (moredyes$fleece >= 0)
        {
            tag.putString(KEY, ColorStrings.ALL[moredyes$fleece]);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void moredyes$load(CompoundTag tag, CallbackInfo ci)
    {
        moredyes$fleece = -1;
        String hex = tag.getString(KEY);
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            if (ColorStrings.ALL[i].equals(hex))
            {
                moredyes$fleece = i;
            }
        }
    }

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void moredyes$spawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData data,
                                CompoundTag tag, CallbackInfoReturnable<SpawnGroupData> cir)
    {
        DyedSheep.spawned((Sheep) (Object) this, reason);
    }

    @Inject(method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Sheep;",
            at = @At("RETURN"))
    private void moredyes$breed(ServerLevel level, AgeableMob other, CallbackInfoReturnable<Sheep> cir)
    {
        if (cir.getReturnValue() != null && other instanceof Sheep mate)
        {
            DyedSheep.bred((Sheep) (Object) this, mate, cir.getReturnValue());
        }
    }
}
