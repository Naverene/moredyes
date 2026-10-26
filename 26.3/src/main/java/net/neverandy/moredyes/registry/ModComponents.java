package net.neverandy.moredyes.registry;

import java.util.function.Supplier;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.neverandy.moredyes.MoreDyes;

/** The mod's item components. */
public final class ModComponents {

    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister
        .createDataComponents(Registries.DATA_COMPONENT_TYPE, MoreDyes.MOD_ID);

    /**
     * {@code moredyes:wood}: the planks a bed was made from, by block id, which its frame is drawn in (see
     * block/DyedBedBlockEntity). A bed without it has vanilla's oak frame.
     */
    public static final Supplier<DataComponentType<Identifier>> WOOD = COMPONENTS.registerComponentType("wood",
        builder -> builder.persistent(Identifier.CODEC).networkSynchronized(Identifier.STREAM_CODEC));

    private ModComponents() {}

    public static void register(IEventBus modBus) {
        COMPONENTS.register(modBus);
    }
}
