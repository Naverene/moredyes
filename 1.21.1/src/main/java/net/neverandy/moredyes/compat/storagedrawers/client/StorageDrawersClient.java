package net.neverandy.moredyes.compat.storagedrawers.client;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import net.neverandy.moredyes.client.Tints;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.compat.storagedrawers.DyedDrawersBlock;
import net.neverandy.moredyes.compat.storagedrawers.DyedDrawersItem;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;

/**
 * Colors the dyed drawers: their models are grey and mark every face with tintindex 0, which takes the color of the
 * block's {@code color} property, or of the item's {@code minecraft:block_state} component. Only loaded when Storage
 * Drawers is installed (see MoreDyesClient).
 */
public final class StorageDrawersClient {

    private StorageDrawersClient() {}

    public static void register(IEventBus modBus) {
        modBus.addListener(StorageDrawersClient::blockColors);
        modBus.addListener(StorageDrawersClient::itemColors);
    }

    private static int argb(int color) {
        return Tints.argb(MixColors.ALL.get(color));
    }

    private static void blockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> tintIndex == 0 ? argb(state.getValue(DyedDrawersBlock.COLOR)) : -1,
            StorageDrawersCompat.blocks().toArray(DyedDrawersBlock[]::new));
    }

    private static void itemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> tintIndex == 0 ? argb(StorageDrawersCompat.colorOf(stack)) : -1,
            StorageDrawersCompat.items().toArray(DyedDrawersItem[]::new));
    }
}
