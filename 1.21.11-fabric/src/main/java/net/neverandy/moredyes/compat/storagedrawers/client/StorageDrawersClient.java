package net.neverandy.moredyes.compat.storagedrawers.client;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

import net.neverandy.moredyes.client.Tints;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.compat.storagedrawers.DyedDrawersBlock;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;

/**
 * Colors the dyed drawer blocks: their models are grey and mark every face with tintindex 0, which takes the color of
 * the block's {@code color} property. (The items are colored by {@link Tints.StateColor}.) Only loaded when Storage
 * Drawers is installed (see MoreDyesClient).
 */
public final class StorageDrawersClient {

    private StorageDrawersClient() {}

    public static void register() {
        ColorProviderRegistry.BLOCK.register(
            (state, level, pos, tintIndex) -> tintIndex == 0 ? argb(state.getValue(DyedDrawersBlock.COLOR)) : -1,
            StorageDrawersCompat.blocks().toArray(DyedDrawersBlock[]::new));
    }

    private static int argb(int color) {
        return Tints.argb(MixColors.ALL.get(color));
    }
}
