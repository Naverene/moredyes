package net.neverandy.moredyes.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.item.ItemStack;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.network.LlamaCarpetPacket;

/**
 * Tells clients which dyed carpet a llama wears. Vanilla only sends a llama's carpet as a dye color, which a dyed
 * carpet doesn't have, so the server sends the carpet's color itself (see client/DyedLlamaDecorLayer). The llama keeps
 * the color last sent, or on the client the color it was told, in a field mixin/LlamaMixin adds.
 */
public final class LlamaCarpets
{
    /** The carpet slot of a llama, as Entity.getSlot numbers it (AbstractHorse's armor slot). */
    private static final int CARPET_SLOT = 401;

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

    /** Called on the server when a llama's carpet may have changed; tells the players who can see it. */
    public static void changed(Llama llama)
    {
        if (llama.level().isClientSide)
        {
            return;
        }
        int color = carpetColor(llama);
        Carpet carpet = (Carpet) llama;
        if (carpet.moredyes$getCarpet() != color)
        {
            carpet.moredyes$setCarpet(color);
            for (ServerPlayer player : PlayerLookup.tracking(llama))
            {
                LlamaCarpetPacket.send(player, llama.getId(), color);
            }
        }
    }

    /** A player who starts seeing a llama with a dyed carpet is told its color. */
    public static void startedSeeing(Llama llama, ServerPlayer player)
    {
        int color = carpetColor(llama);
        if (color >= 0)
        {
            LlamaCarpetPacket.send(player, llama.getId(), color);
        }
    }

    /** The dyed carpet a llama wears, as a color index (-1 for none); added to Llama by mixin/LlamaMixin. */
    public interface Carpet
    {
        int moredyes$getCarpet();

        void moredyes$setCarpet(int color);
    }
}
