package net.neverandy.moredyes.mixin;

import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.horse.Llama;
import net.neverandy.moredyes.entity.DyedSheep;
import net.neverandy.moredyes.entity.LlamaCarpets;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Once a player has been sent a sheep or a llama, also send its MoreDyes color or dyed carpet. */
@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin
{
    @Shadow
    @Final
    private Entity entity;

    @Inject(method = "addPairing", at = @At("TAIL"))
    private void moredyes$sheepColor(ServerPlayer player, CallbackInfo ci)
    {
        if (entity instanceof Sheep sheep)
        {
            DyedSheep.startedSeeing(sheep, player);
        }
        else if (entity instanceof Llama llama)
        {
            LlamaCarpets.startedSeeing(llama, player);
        }
    }
}
