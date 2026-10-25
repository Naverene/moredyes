package net.neverandy.moredyes.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.neverandy.moredyes.entity.DyedCollars;
import net.neverandy.moredyes.entity.DyedSheep;
import net.neverandy.moredyes.network.LlamaCarpetPacket;
import net.neverandy.moredyes.network.SheepColorPacket;

public final class ClientPackets
{
    private ClientPackets() {}

    public static void sheepColor(SheepColorPacket packet)
    {
        if (Minecraft.getInstance().world == null)
        {
            return;
        }
        Entity entity = Minecraft.getInstance().world.getEntityByID(packet.entityId);
        if (entity instanceof SheepEntity)
        {
            DyedSheep.setColor((SheepEntity) entity, packet.color);
        }
        else if (DyedCollars.hasCollar(entity))
        {
            DyedCollars.setColor((TameableEntity) entity, packet.color);
        }
    }

    public static void llamaCarpet(LlamaCarpetPacket packet)
    {
        if (Minecraft.getInstance().world == null)
        {
            return;
        }
        Entity entity = Minecraft.getInstance().world.getEntityByID(packet.entityId);
        if (entity instanceof LlamaEntity)
        {
            DyedLlamaDecorLayer.setCarpet((LlamaEntity) entity, packet.color);
        }
    }
}
