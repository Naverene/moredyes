package net.neverandy.moredyes.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.TamableAnimal;
import net.neverandy.moredyes.entity.DyedCollars;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Gives tamable animals a MoreDyes collar color, saved with them. Only wolves and cats use it (see DyedCollars). */
@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin implements DyedCollars.Collar
{
    @Unique
    private static final String KEY = Reference.MOD_ID + ":collar";

    @Unique
    private int moredyes$collar = -1;

    @Override
    public int moredyes$getCollar()
    {
        return moredyes$collar;
    }

    @Override
    public void moredyes$setCollar(int color)
    {
        moredyes$collar = color;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void moredyes$save(CompoundTag tag, CallbackInfo ci)
    {
        if (moredyes$collar >= 0)
        {
            tag.putString(KEY, ColorStrings.ALL[moredyes$collar]);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void moredyes$load(CompoundTag tag, CallbackInfo ci)
    {
        moredyes$collar = -1;
        String hex = tag.getString(KEY);
        for (int i = 0; i < ColorStrings.ALL.length; i++)
        {
            if (ColorStrings.ALL[i].equals(hex))
            {
                moredyes$collar = i;
            }
        }
    }
}
