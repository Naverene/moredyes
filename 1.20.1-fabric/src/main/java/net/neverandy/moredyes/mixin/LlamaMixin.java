package net.neverandy.moredyes.mixin;

import net.minecraft.world.entity.animal.horse.Llama;
import net.neverandy.moredyes.entity.LlamaCarpets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Keeps which dyed carpet a llama wears, and tells the players who see it when that changes (entity/LlamaCarpets). */
@Mixin(Llama.class)
public abstract class LlamaMixin implements LlamaCarpets.Carpet
{
    @Unique
    private int moredyes$carpet = -1;

    @Override
    public int moredyes$getCarpet()
    {
        return moredyes$carpet;
    }

    @Override
    public void moredyes$setCarpet(int color)
    {
        moredyes$carpet = color;
    }

    /** Vanilla calls this on the server when the llama's inventory changes and when it is loaded. */
    @Inject(method = "updateContainerEquipment", at = @At("TAIL"))
    private void moredyes$carpetChanged(CallbackInfo ci)
    {
        LlamaCarpets.changed((Llama) (Object) this);
    }
}
