package net.neverandy.moredyes.registry;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.item.MixDyeItem;

/** Registers the mixed dyes and an item for every block that has one. */
public final class ModItems {

    private static final Map<MixColor, MixDyeItem> DYES = new LinkedHashMap<>();
    private static final Map<Kind, Map<MixColor, BlockItem>> BLOCK_ITEMS = new LinkedHashMap<>();

    private ModItems() {}

    /** Registers the items. Call after {@link ModBlocks#register()}. */
    public static void register() {
        for (MixColor color : MixColors.ALL) {
            DYES.put(color, (MixDyeItem) register(color.id("dye"), p -> new MixDyeItem(color, p), new Item.Properties()));
        }
        for (Kind kind : Kind.values()) {
            if (!kind.hasItem() || !kind.enabled()) {
                continue;
            }
            Map<MixColor, BlockItem> byColor = new LinkedHashMap<>();
            for (MixColor color : MixColors.ALL) {
                Item.Properties properties = properties(kind, new Item.Properties());
                if (kind == Kind.BANNER) {
                    byColor.put(color, (BlockItem) register(color.id(kind.id()), p -> new BannerItem(
                        ModBlocks.get(Kind.BANNER, color), ModBlocks.get(Kind.WALL_BANNER, color), p), properties));
                } else if (kind == Kind.BED) {
                    byColor.put(color, (BlockItem) register(color.id(kind.id()),
                        p -> new BedItem(ModBlocks.get(Kind.BED, color), p), properties));
                } else if (kind == Kind.SIGN) {
                    // Places the standing sign, or the wall sign when used on the side of a block.
                    byColor.put(color, (BlockItem) register(color.id(kind.id()), p -> new SignItem(p,
                        ModBlocks.get(Kind.SIGN, color), ModBlocks.get(Kind.WALL_SIGN, color)), properties));
                } else {
                    byColor.put(color, (BlockItem) register(color.id(kind.id()),
                        p -> new BlockItem(ModBlocks.get(kind, color), p), properties));
                }
            }
            BLOCK_ITEMS.put(kind, Collections.unmodifiableMap(byColor));
        }
    }

    /** Registers an item the way vanilla does, which also tells a block which item it is. */
    private static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        return Items.registerItem(ResourceLocation.fromNamespaceAndPath(MoreDyes.MOD_ID, name), factory.apply(properties));
    }

    /**
     * The item components of the vanilla item a kind copies that matter here. Burning in a furnace and composting are
     * NeoForge data maps in the shared resources, which MoreDyes hands to Fabric's registries.
     */
    private static Item.Properties properties(Kind kind, Item.Properties p) {
        return switch (kind) {
            case CHEST -> p.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
            case SHULKER_BOX -> p.stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
            case SIGN -> p.stacksTo(16);
            case BED -> p.stacksTo(1);
            case BANNER -> p.stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
            default -> p;
        };
    }

    public static MixDyeItem dye(MixColor color) {
        return DYES.get(color);
    }

    /** The item of a kind in a color, or null when the kind has no item or the config leaves it out. */
    public static BlockItem get(Kind kind, MixColor color) {
        Map<MixColor, BlockItem> byColor = BLOCK_ITEMS.get(kind);
        return byColor == null ? null : byColor.get(color);
    }
}
