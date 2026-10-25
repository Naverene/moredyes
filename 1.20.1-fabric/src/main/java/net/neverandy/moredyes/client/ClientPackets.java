package net.neverandy.moredyes.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.horse.Llama;
import net.neverandy.moredyes.entity.DyedCollars;
import net.neverandy.moredyes.entity.DyedSheep;
import net.neverandy.moredyes.entity.LlamaCarpets;
import net.neverandy.moredyes.network.LlamaCarpetPacket;
import net.neverandy.moredyes.network.SheepColorPacket;

public final class ClientPackets
{
    private ClientPackets() {}

    public static void register()
    {
        ClientPlayNetworking.registerGlobalReceiver(SheepColorPacket.ID, (client, handler, buffer, sender) ->
        {
            SheepColorPacket packet = SheepColorPacket.decode(buffer);
            client.execute(() -> sheepColor(packet));
        });
        ClientPlayNetworking.registerGlobalReceiver(LlamaCarpetPacket.ID, (client, handler, buffer, sender) ->
        {
            LlamaCarpetPacket packet = LlamaCarpetPacket.decode(buffer);
            client.execute(() -> llamaCarpet(packet));
        });
    }

    public static void sheepColor(SheepColorPacket packet)
    {
        if (Minecraft.getInstance().level == null)
        {
            return;
        }
        Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId);
        if (entity instanceof Sheep sheep)
        {
            DyedSheep.setColor(sheep, packet.color);
        }
        else if (entity instanceof TamableAnimal pet && DyedCollars.hasCollar(pet))
        {
            DyedCollars.setColor(pet, packet.color);
        }
    }

    public static void llamaCarpet(LlamaCarpetPacket packet)
    {
        if (Minecraft.getInstance().level == null)
        {
            return;
        }
        Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId);
        if (entity instanceof Llama llama)
        {
            ((LlamaCarpets.Carpet) llama).moredyes$setCarpet(packet.color);
        }
    }
}
