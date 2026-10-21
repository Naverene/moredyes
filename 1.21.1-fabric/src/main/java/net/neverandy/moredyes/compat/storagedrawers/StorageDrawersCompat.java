package net.neverandy.moredyes.compat.storagedrawers;

import java.util.ArrayList;
import java.util.List;

import com.jaquadro.minecraft.storagedrawers.api.config.IDrawerConfig;
import com.jaquadro.minecraft.storagedrawers.config.ModCommonConfig;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.neverandy.moredyes.Config;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.toggle.Toggles;
import net.neverandy.moredyes.color.MixColors;

/**
 * Dyed Storage Drawers, in every More Dyes color. Each drawer size is one block whose color is a block state property
 * (see {@link DyedDrawersBlock#COLOR}), so there are six blocks rather than six times 118. They are Storage Drawers'
 * own standard drawers with a grey model tinted in the dye color. {@code StorageDrawersModBlocksMixin} adds them to
 * Storage Drawers' list of drawer blocks, so they share its block entity types (hoppers, controllers and labels work)
 * and get its label positions and model overlays.
 *
 * <p>
 * Fabric runs the mods' initializers in no set order, and Storage Drawers makes its block entity types from its list
 * of drawer blocks while it initializes, so the drawers are registered from Storage Drawers' initializer
 * ({@code StorageDrawersBlockEntitiesMixin}) rather than from More Dyes'. Only loaded when Storage Drawers is installed
 * (see MoreDyes), so nothing outside this package may refer to it.
 */
public final class StorageDrawersCompat {

    public static final String MOD_ID = "storagedrawers";

    /**
     * The drawer sizes that have dyed versions, named as Storage Drawers names them. Also the registry names of the
     * dyed drawers ({@code moredyes:full_drawers_1}...).
     */
    public static final List<String> SIZES = List.of("full_drawers_1", "full_drawers_2", "full_drawers_4",
        "half_drawers_1", "half_drawers_2", "half_drawers_4");

    private static final List<DyedDrawersBlock> DRAWERS = new ArrayList<>();
    private static final List<DyedDrawersItem> DRAWER_ITEMS = new ArrayList<>();

    private StorageDrawersCompat() {}

    /** Called from More Dyes' initializer. */
    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ResourceKey.create(Registries.CREATIVE_MODE_TAB,
            ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID, "blocks"))).register(entries -> {
                // Every color of every size at the end of the More Dyes blocks tab.
                for (DyedDrawersItem item : DRAWER_ITEMS) {
                    for (int color = 0; color < MixColors.ALL.size(); color++) {
                        entries.accept(stack(item, color));
                    }
                }
            });
    }

    /** Registers the drawers, once. Called just before Storage Drawers makes its block entity types. */
    public static synchronized void registerBlocks() {
        Config.load();
        if (!DRAWERS.isEmpty() || !Toggles.storageDrawers) {
            return;
        }
        for (String size : SIZES) {
            int count = size.charAt(size.length() - 1) - '0';
            boolean half = size.startsWith("half");
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID, size);
            ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);
            // The same properties and capacity settings as Storage Drawers' own wooden drawers.
            BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(3.0F, 4.0F)
                .isSuffocating((state, level, pos) -> false).isRedstoneConductor((state, level, pos) -> false);
            DyedDrawersBlock block = Registry.register(BuiltInRegistries.BLOCK, key,
                new DyedDrawersBlock(count, half, config(count, half), properties));
            DRAWERS.add(block);
            DRAWER_ITEMS.add((DyedDrawersItem) Items.registerItem(id, new DyedDrawersItem(block, new Item.Properties())));
        }
    }

    private static IDrawerConfig config(int count, boolean half) {
        ModCommonConfig.Drawers drawers = ModCommonConfig.INSTANCE.DRAWERS;
        return switch (count) {
            case 1 -> half ? drawers.halfDrawers1x1 : drawers.fullDrawers1x1;
            case 2 -> half ? drawers.halfDrawers1x2 : drawers.fullDrawers1x2;
            default -> half ? drawers.halfDrawers2x2 : drawers.fullDrawers2x2;
        };
    }

    /** Every dyed drawer block, in the order of {@link #SIZES}. */
    public static List<DyedDrawersBlock> blocks() {
        return List.copyOf(DRAWERS);
    }

    public static List<DyedDrawersItem> items() {
        return List.copyOf(DRAWER_ITEMS);
    }

    /** A dyed drawer item in a color, by its position in {@link MixColors#ALL}. */
    public static ItemStack stack(Item item, int color) {
        ItemStack stack = new ItemStack(item);
        stack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(DyedDrawersBlock.COLOR, color));
        return stack;
    }

    /** The color of a dyed drawer item, by its position in {@link MixColors#ALL}, or 0 if it has none. */
    public static int colorOf(ItemStack stack) {
        Integer color = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY)
            .get(DyedDrawersBlock.COLOR);
        return color != null ? color : 0;
    }
}
