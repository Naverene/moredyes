package net.neverandy.moredyes.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.toggle.Toggles;

/**
 * Registers one block of every {@link Kind} in every {@link MixColor}, and flower pots for the plants. Kinds the config
 * leaves out (see {@link Kind#enabled()}) are not registered: {@link #all} is empty for them and {@link #get} null.
 */
public final class ModBlocks {

    /** The kinds that can be put in a flower pot. */
    public static final List<Kind> POTTABLE = List.of(Kind.TULIP, Kind.ALLIUM, Kind.AZURE_BLUET, Kind.BLUE_ORCHID,
        Kind.CORNFLOWER, Kind.DANDELION, Kind.LILY_OF_THE_VALLEY, Kind.OXEYE_DAISY, Kind.POPPY, Kind.OAK_SAPLING,
        Kind.BIRCH_SAPLING, Kind.SPRUCE_SAPLING, Kind.JUNGLE_SAPLING, Kind.ACACIA_SAPLING, Kind.DARK_OAK_SAPLING);

    private static final Map<Kind, Map<MixColor, Block>> BY_KIND = new EnumMap<>(Kind.class);
    private static final List<FlowerPotBlock> POTTED = new ArrayList<>();
    /** Every block this class registers, in order. */
    private static final List<Block> ENTRIES = new ArrayList<>();
    private static volatile Map<Block, Kind> KINDS;

    private ModBlocks() {}

    /** Registers the blocks. Kinds are registered in order, so a slab finds the dyed block it copies already there. */
    public static void register() {
        for (Kind kind : Kind.values()) {
            Map<MixColor, Block> byColor = new LinkedHashMap<>();
            BY_KIND.put(kind, byColor);
            if (!kind.enabled()) {
                // Turned off in the config. Its recipes, drops and tags are dropped as they load (see toggle/DataFilter).
                MixColors.ALL.forEach(color -> Toggles.leftOut(color.id(kind.id())));
                BY_KIND.put(kind, Map.of());
                continue;
            }
            for (MixColor color : MixColors.ALL) {
                // A slab, stairs or wall copies the dyed block it is made from, which is registered before it.
                BlockBehaviour.Properties properties = kind.base() != null
                    ? BlockBehaviour.Properties.ofFullCopy(get(kind.base(), color))
                    : BlockBehaviour.Properties.ofFullCopy(kind.vanilla());
                byColor.put(color, register(color.id(kind.id()), p -> kind.create(color, p), properties));
            }
            BY_KIND.put(kind, Collections.unmodifiableMap(byColor));
        }
        // Vanilla's empty pot finds these when a plant is used on it: each pot adds itself to vanilla's map of pots.
        for (Kind kind : POTTABLE) {
            for (MixColor color : MixColors.ALL) {
                Block plant = get(kind, color);
                if (plant == null) {
                    Toggles.leftOut("potted_" + color.id(kind.id()));
                    continue;
                }
                POTTED.add((FlowerPotBlock) register("potted_" + color.id(kind.id()), p -> new FlowerPotBlock(plant, p),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_WHITE_TULIP)));
            }
        }
    }

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> factory,
        BlockBehaviour.Properties properties) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID, name));
        Block block = Registry.register(BuiltInRegistries.BLOCK, key, factory.apply(properties));
        ENTRIES.add(block);
        return block;
    }

    /** The block of a kind in a color, or null when the config leaves the kind out. */
    public static Block get(Kind kind, MixColor color) {
        return BY_KIND.get(kind).get(color);
    }

    /** All colors of one kind, in color order. */
    public static List<Block> all(Kind kind) {
        return List.copyOf(BY_KIND.get(kind).values());
    }

    /** Every block of the mod, in the order they were registered. */
    public static List<Block> entries() {
        return Collections.unmodifiableList(ENTRIES);
    }

    /** Whether a block state is one of the colors of the given kind. */
    public static boolean isKind(BlockState state, Kind kind) {
        return kindOf(state.getBlock()) == kind;
    }

    /** The kind of a More Dyes block, or null for any other block. */
    public static Kind kindOf(Block block) {
        Map<Block, Kind> kinds = KINDS;
        if (kinds == null) {
            kinds = new IdentityHashMap<>();
            for (Map.Entry<Kind, Map<MixColor, Block>> entry : BY_KIND.entrySet()) {
                for (Block member : entry.getValue().values()) {
                    kinds.put(member, entry.getKey());
                }
            }
            KINDS = kinds;
        }
        return kinds.get(block);
    }

    public static List<FlowerPotBlock> potted() {
        return Collections.unmodifiableList(POTTED);
    }
}
