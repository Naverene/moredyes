package net.neverandy.moredyes.compat.storagedrawers.client;

import java.util.List;
import java.util.Set;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

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

    public static void register(IEventBus modBus) {
        modBus.addListener(StorageDrawersClient::blockTints);
    }

    private static int argb(int color) {
        return Tints.argb(MixColors.ALL.get(color));
    }

    private static void blockTints(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(new BlockTintSource() {
            @Override
            public int color(BlockState state) {
                return argb(state.getValue(DyedDrawersBlock.COLOR));
            }

            @Override
            public Set<Property<?>> relevantProperties() {
                return Set.of(DyedDrawersBlock.COLOR);
            }
        }), StorageDrawersCompat.blocks().toArray(DyedDrawersBlock[]::new));
    }
}
