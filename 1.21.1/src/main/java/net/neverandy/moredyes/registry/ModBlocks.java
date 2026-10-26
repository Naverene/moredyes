package net.neverandy.moredyes.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.DyedBedBlock;
import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.toggle.Toggles;

/**
 * Registers one block of every {@link Kind} in every {@link MixColor}, and flower pots for the plants. Kinds the config
 * leaves out (see {@link Kind#enabled()}) are not registered: {@link #all} is empty for them and {@link #get} null.
 */
public final class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MoreDyes.MOD_ID);

    /** The kinds that can be put in a flower pot. */
    public static final List<Kind> POTTABLE = List.of(Kind.TULIP, Kind.ALLIUM, Kind.AZURE_BLUET, Kind.BLUE_ORCHID,
        Kind.CORNFLOWER, Kind.DANDELION, Kind.LILY_OF_THE_VALLEY, Kind.OXEYE_DAISY, Kind.POPPY, Kind.OAK_SAPLING,
        Kind.BIRCH_SAPLING, Kind.SPRUCE_SAPLING, Kind.JUNGLE_SAPLING, Kind.ACACIA_SAPLING, Kind.DARK_OAK_SAPLING);

    private static final Map<Kind, Map<MixColor, DeferredBlock<Block>>> BY_KIND = new EnumMap<>(Kind.class);
    private static final List<DeferredBlock<FlowerPotBlock>> POTTED = new ArrayList<>();
    /** The plant in each pot of POTTED. */
    private static final List<DeferredBlock<Block>> POTTED_PLANTS = new ArrayList<>();
    private static volatile Map<Block, Kind> KINDS;
    /**
     * A More Dyes bed in each vanilla color, {@code bed_<color>}. Crafting a vanilla bed from planks other than oak
     * gives one of these, which can remember the planks (see item/crafting/BedRecipe).
     */
    private static final Map<DyeColor, DeferredBlock<Block>> VANILLA_COLOR_BEDS = new EnumMap<>(DyeColor.class);

    static {
        for (Kind kind : Kind.values()) {
            Map<MixColor, DeferredBlock<Block>> byColor = new LinkedHashMap<>();
            if (!kind.enabled()) {
                // Turned off in the config. Its recipes, drops and tags are dropped as they load (see toggle/DataFilter).
                MixColors.ALL.forEach(color -> Toggles.leftOut(color.id(kind.id())));
                BY_KIND.put(kind, Map.of());
                continue;
            }
            for (MixColor color : MixColors.ALL) {
                // A slab, stairs or wall copies the dyed block it is made from, which is registered before it.
                Supplier<BlockBehaviour.Properties> properties = kind.base() != null
                    ? () -> BlockBehaviour.Properties.ofFullCopy(get(kind.base(), color).get())
                    : () -> BlockBehaviour.Properties.ofFullCopy(kind.vanilla());
                byColor.put(color, BLOCKS.register(color.id(kind.id()), () -> kind.create(color, properties.get())));
            }
            BY_KIND.put(kind, Collections.unmodifiableMap(byColor));
        }
        for (DyeColor color : DyeColor.values()) {
            String id = "bed_" + color.getName();
            if (!Kind.BED.enabled()) {
                Toggles.leftOut(id);
                continue;
            }
            Block vanilla = BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace(color.getName() + "_bed"));
            VANILLA_COLOR_BEDS.put(color, BLOCKS.register(id,
                () -> new DyedBedBlock(color, BlockBehaviour.Properties.ofFullCopy(vanilla))));
        }
        // Vanilla's empty pot is told about these in addPlantsToPot, so a dyed plant used on it fills it.
        for (Kind kind : POTTABLE) {
            for (MixColor color : MixColors.ALL) {
                DeferredBlock<Block> plant = get(kind, color);
                if (plant == null) {
                    Toggles.leftOut("potted_" + color.id(kind.id()));
                    continue;
                }
                POTTED_PLANTS.add(plant);
                POTTED.add(BLOCKS.register("potted_" + color.id(kind.id()),
                    () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, plant,
                        BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_WHITE_TULIP))));
            }
        }
    }

    private ModBlocks() {}

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
    }

    /** The block of a kind in a color, or null when the config leaves the kind out. */
    public static DeferredBlock<Block> get(Kind kind, MixColor color) {
        return BY_KIND.get(kind).get(color);
    }

    /** All colors of one kind, in color order. */
    public static List<DeferredBlock<Block>> all(Kind kind) {
        return List.copyOf(BY_KIND.get(kind).values());
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
            for (Map.Entry<Kind, Map<MixColor, DeferredBlock<Block>>> entry : BY_KIND.entrySet()) {
                for (DeferredBlock<Block> holder : entry.getValue().values()) {
                    kinds.put(holder.get(), entry.getKey());
                }
            }
            KINDS = kinds;
        }
        return kinds.get(block);
    }

    /** Lets the vanilla empty flower pot take the dyed plants. Call once, on the main thread, at common setup. */
    public static void addPlantsToPot() {
        FlowerPotBlock empty = (FlowerPotBlock) Blocks.FLOWER_POT;
        for (int i = 0; i < POTTED.size(); i++) {
            empty.addPlant(POTTED_PLANTS.get(i).getId(), POTTED.get(i));
        }
    }

    /** The More Dyes bed in a vanilla color, or null when the config leaves beds out. */
    public static Block vanillaColorBed(DyeColor color) {
        DeferredBlock<Block> bed = VANILLA_COLOR_BEDS.get(color);
        return bed == null ? null : bed.get();
    }

    /** The More Dyes beds in the vanilla colors. */
    public static List<DeferredBlock<Block>> vanillaColorBeds() {
        return List.copyOf(VANILLA_COLOR_BEDS.values());
    }

    /** Every More Dyes bed: the dyed ones and those in the vanilla colors. */
    public static List<DeferredBlock<Block>> beds() {
        List<DeferredBlock<Block>> beds = new ArrayList<>(all(Kind.BED));
        beds.addAll(VANILLA_COLOR_BEDS.values());
        return beds;
    }

    public static List<DeferredBlock<FlowerPotBlock>> potted() {
        return Collections.unmodifiableList(POTTED);
    }
}
