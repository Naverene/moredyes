package net.neverandy.moredyes.paint;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** Sent by the paint gun's screen: the color, mode and brush size the player picked for the gun in their hand. */
public class GunSettingsPacket
{
    public final InteractionHand hand;
    public final int color;
    public final PaintGunItem.Mode mode;
    public final int brush;

    public GunSettingsPacket(InteractionHand hand, int color, PaintGunItem.Mode mode, int brush)
    {
        this.hand = hand;
        this.color = color;
        this.mode = mode;
        this.brush = brush;
    }

    public static void encode(GunSettingsPacket packet, FriendlyByteBuf buffer)
    {
        buffer.writeEnum(packet.hand);
        buffer.writeInt(packet.color);
        buffer.writeEnum(packet.mode);
        buffer.writeVarInt(packet.brush);
    }

    public static GunSettingsPacket decode(FriendlyByteBuf buffer)
    {
        return new GunSettingsPacket(buffer.readEnum(InteractionHand.class), buffer.readInt(),
                buffer.readEnum(PaintGunItem.Mode.class), buffer.readVarInt());
    }

    public static void handle(GunSettingsPacket packet, Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() ->
        {
            ServerPlayer player = context.get().getSender();
            if (player == null)
            {
                return;
            }
            ItemStack stack = player.getItemInHand(packet.hand);
            if (stack.getItem() instanceof PaintGunItem)
            {
                PaintGunItem.setColor(stack, packet.color);
                PaintGunItem.setMode(stack, packet.mode);
                PaintGunItem.setBrush(stack, packet.brush);
            }
        });
        context.get().setPacketHandled(true);
    }
}
