package net.neverandy.moredyes.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.horse.Llama;
import net.neverandy.moredyes.entity.DyedCollars;
import net.neverandy.moredyes.entity.DyedSheep;
import net.neverandy.moredyes.network.LlamaCarpetPacket;
import net.neverandy.moredyes.network.SheepColorPacket;

public final class ClientPackets
{
    private ClientPackets() {}

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
            DyedLlamaDecorLayer.setCarpet(llama, packet.color);
        }
    }
}
