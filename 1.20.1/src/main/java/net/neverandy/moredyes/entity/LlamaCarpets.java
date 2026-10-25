package net.neverandy.moredyes.entity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.network.LlamaCarpetPacket;
import net.neverandy.moredyes.network.ModNetwork;
import net.neverandy.moredyes.reference.Reference;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Tells clients which dyed carpet a llama wears. Vanilla only sends a llama's carpet as a dye color, which a dyed
 * carpet doesn't have, so the server sends the carpet's color itself (see client/DyedLlamaDecorLayer).
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class LlamaCarpets
{
    /** The carpet slot of a llama, as Entity.getSlot numbers it (AbstractHorse's armor slot). */
    private static final int CARPET_SLOT = 401;
    /** The color last sent for each llama on the server. */
    private static final Map<Llama, Integer> SENT = new WeakHashMap<>();

    private LlamaCarpets() {}

    /** The index of the dyed carpet a llama wears, or -1 for none or a vanilla carpet. */
    public static int carpetColor(Llama llama)
    {
        ItemStack carpet = llama.getSlot(CARPET_SLOT).get();
        if (carpet.isEmpty())
        {
            return -1;
        }
        for (int i = 0; i < MDBlock.COLORS; i++)
        {
            if (MDBlock.carpetArray[i] != null && carpet.is(MDBlock.carpetArray[i].asItem()))
            {
                return i;
            }
        }
        return -1;
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event)
    {
        if (event.getEntity() instanceof Llama llama && !llama.level().isClientSide && llama.tickCount % 10 == 0)
        {
            int color = carpetColor(llama);
            Integer sent = SENT.get(llama);
            if (sent == null ? color != -1 : sent != color)
            {
                SENT.put(llama, color);
                ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> llama), new LlamaCarpetPacket(llama.getId(), color));
            }
        }
    }

    /** A player who starts seeing a llama with a dyed carpet is told its color. */
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event)
    {
        if (event.getTarget() instanceof Llama llama && event.getEntity() instanceof ServerPlayer player)
        {
            int color = carpetColor(llama);
            if (color >= 0)
            {
                ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new LlamaCarpetPacket(llama.getId(), color));
            }
        }
    }
}
