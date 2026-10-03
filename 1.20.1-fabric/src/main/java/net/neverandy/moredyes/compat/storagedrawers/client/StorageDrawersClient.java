package net.neverandy.moredyes.compat.storagedrawers.client;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.neverandy.moredyes.client.ColorHandlers;
import net.neverandy.moredyes.compat.storagedrawers.DyedDrawersBlock;
import net.neverandy.moredyes.compat.storagedrawers.DyedDrawersItem;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.neverandy.moredyes.reference.ColorStrings;

/**
 * Colors dyed drawers: their models are grey and mark every face with tintindex 0. Storage Drawers itself gives them its
 * render layer, label renderer and decoration models, as for its own drawers.
 */
public final class StorageDrawersClient
{
    private StorageDrawersClient() {}

    public static void register()
    {
        ColorProviderRegistry.BLOCK.register((state, level, pos, tintIndex) -> tintIndex == 0 ? color(state.getValue(DyedDrawersBlock.COLOR)) : 0xFFFFFF,
                StorageDrawersCompat.blocks().toArray(DyedDrawersBlock[]::new));
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? color(StorageDrawersCompat.colorOf(stack)) : 0xFFFFFF,
                StorageDrawersCompat.items().toArray(DyedDrawersItem[]::new));
    }

    private static int color(int index)
    {
        return ColorHandlers.vivid(Integer.parseInt(ColorStrings.ALL[index], 16));
    }
}
