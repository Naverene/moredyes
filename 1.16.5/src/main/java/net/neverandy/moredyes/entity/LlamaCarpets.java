package net.neverandy.moredyes.entity;

import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.network.PacketDistributor;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.network.LlamaCarpetPacket;
import net.neverandy.moredyes.network.ModNetwork;
import net.neverandy.moredyes.reference.Reference;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Tells clients which dyed carpet a llama wears. Vanilla only sends a llama's carpet as a dye color, which a dyed
 * carpet doesn't have, so the server sends the carpet's color itself (see client/DyedLlamaDecorLayer).
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class LlamaCarpets
{
    /** AbstractHorseEntity.horseChest; slot 1 is a llama's carpet. */
    private static final Field INVENTORY = ObfuscationReflectionHelper.findField(AbstractHorseEntity.class, "field_110296_bG");
    /** The color last sent for each llama on the server. */
    private static final Map<LlamaEntity, Integer> SENT = new WeakHashMap<>();

    private LlamaCarpets() {}

    /** The index of the dyed carpet a llama wears, or -1 for none or a vanilla carpet. */
    public static int carpetColor(LlamaEntity llama)
    {
        ItemStack carpet;
        try
        {
            carpet = ((Inventory) INVENTORY.get(llama)).getStackInSlot(1);
        }
        catch (IllegalAccessException e)
        {
            throw new IllegalStateException("Could not read a llama's carpet", e);
        }
        if (carpet.isEmpty())
        {
            return -1;
        }
        for (int i = 0; i < MDBlock.carpetArray.length; i++)
        {
            if (MDBlock.carpetArray[i] != null && carpet.getItem() == MDBlock.carpetArray[i].asItem())
            {
                return i;
            }
        }
        return -1;
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event)
    {
        if (event.getEntity() instanceof LlamaEntity && !event.getEntity().world.isRemote && event.getEntity().ticksExisted % 10 == 0)
        {
            LlamaEntity llama = (LlamaEntity) event.getEntity();
            int color = carpetColor(llama);
            Integer sent = SENT.get(llama);
            if (sent == null ? color != -1 : sent != color)
            {
                SENT.put(llama, color);
                ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> llama), new LlamaCarpetPacket(llama.getEntityId(), color));
            }
        }
    }

    /** A player who starts seeing a llama with a dyed carpet is told its color. */
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event)
    {
        if (event.getTarget() instanceof LlamaEntity && event.getPlayer() instanceof ServerPlayerEntity)
        {
            LlamaEntity llama = (LlamaEntity) event.getTarget();
            int color = carpetColor(llama);
            if (color >= 0)
            {
                ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()),
                        new LlamaCarpetPacket(llama.getEntityId(), color));
            }
        }
    }
}
