package net.neverandy.moredyes.mixin;

import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Sheep;
import net.neverandy.moredyes.entity.DyedSheep;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Once a player has been sent a sheep, also send its MoreDyes color. */
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
    }
}
