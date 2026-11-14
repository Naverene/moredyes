package net.neverandy.moredyes.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import net.neverandy.moredyes.MoreDyes;

/** The mod's item components. */
public final class ModComponents {

    /**
     * {@code moredyes:wood}: the planks a bed was made from, by block id, which its frame is drawn in (see
     * block/DyedBedBlockEntity). A bed without it has vanilla's oak frame.
     */
    public static final DataComponentType<Identifier> WOOD = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE,
        Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, "wood"), DataComponentType.<Identifier>builder()
            .persistent(Identifier.CODEC).networkSynchronized(Identifier.STREAM_CODEC).build());

    private ModComponents() {}

    /** Registers the components (loading this class does it). */
    public static void register() {}
}
