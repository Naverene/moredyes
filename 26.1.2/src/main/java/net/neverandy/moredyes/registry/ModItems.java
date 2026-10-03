package net.neverandy.moredyes.registry;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.item.MixDyeItem;

/** Registers the mixed dyes and an item for every block that has one. */
public final class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MoreDyes.MOD_ID);

    private static final Map<MixColor, DeferredItem<MixDyeItem>> DYES = new LinkedHashMap<>();
    private static final Map<Kind, Map<MixColor, DeferredItem<BlockItem>>> BLOCK_ITEMS = new LinkedHashMap<>();

    static {
        for (MixColor color : MixColors.ALL) {
            DYES.put(color, ITEMS.registerItem(color.id("dye"), p -> new MixDyeItem(color, p)));
        }
        for (Kind kind : Kind.values()) {
            if (!kind.hasItem()) {
                continue;
            }
            Map<MixColor, DeferredItem<BlockItem>> byColor = new LinkedHashMap<>();
            for (MixColor color : MixColors.ALL) {
                if (kind == Kind.SIGN) {
                    // Places the standing sign, or the wall sign when used on the side of a block.
                    byColor.put(color, ITEMS.registerItem(color.id(kind.id()), p -> new SignItem(
                        ModBlocks.get(Kind.SIGN, color).get(), ModBlocks.get(Kind.WALL_SIGN, color).get(),
                        properties(kind, p.useBlockDescriptionPrefix()))));
                } else {
                    byColor.put(color, ITEMS.registerSimpleBlockItem(ModBlocks.get(kind, color), p -> properties(kind, p)));
                }
            }
            BLOCK_ITEMS.put(kind, Collections.unmodifiableMap(byColor));
        }
    }

    private ModItems() {}

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

    /**
     * The item components of the vanilla item a kind copies that matter here. Burning in a furnace and composting are
     * NeoForge data maps in 26.1 ({@code data/neoforge/data_maps/item}), written by tools/generate_resources.py.
     */
    private static Item.Properties properties(Kind kind, Item.Properties p) {
        return switch (kind) {
            case CHEST -> p.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
            case SIGN -> p.stacksTo(16);
            default -> p;
        };
    }

    public static DeferredItem<MixDyeItem> dye(MixColor color) {
        return DYES.get(color);
    }

    public static DeferredItem<BlockItem> get(Kind kind, MixColor color) {
        return BLOCK_ITEMS.get(kind).get(color);
    }
}
