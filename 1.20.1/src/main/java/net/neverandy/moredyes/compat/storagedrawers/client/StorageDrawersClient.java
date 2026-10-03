package net.neverandy.moredyes.compat.storagedrawers.client;

import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.neverandy.moredyes.client.ColorHandlers;
import net.neverandy.moredyes.compat.storagedrawers.DyedDrawersBlock;
import net.neverandy.moredyes.compat.storagedrawers.DyedDrawersItem;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.neverandy.moredyes.reference.ColorStrings;

/** Colors dyed drawers: their models are grey and mark every face with tintindex 0. */
public final class StorageDrawersClient
{
    private StorageDrawersClient() {}

    public static void register(IEventBus modBus)
    {
        modBus.addListener(StorageDrawersClient::blockColors);
        modBus.addListener(StorageDrawersClient::itemColors);
    }

    private static int color(int index)
    {
        return ColorHandlers.vivid(Integer.parseInt(ColorStrings.ALL[index], 16));
    }

    private static void blockColors(RegisterColorHandlersEvent.Block event)
    {
        event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? color(state.getValue(DyedDrawersBlock.COLOR)) : 0xFFFFFF,
                StorageDrawersCompat.blocks().toArray(DyedDrawersBlock[]::new));
    }

    private static void itemColors(RegisterColorHandlersEvent.Item event)
    {
        event.register((stack, tintIndex) -> tintIndex == 0 ? color(StorageDrawersCompat.colorOf(stack)) : 0xFFFFFF,
                StorageDrawersCompat.items().toArray(DyedDrawersItem[]::new));
    }
}
