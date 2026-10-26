package net.neverandy.moredyes.registry;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
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
                Item.Properties properties = properties(kind, color, new Item.Properties().useBlockDescriptionPrefix());
                if (kind == Kind.BANNER) {
                    byColor.put(color, (BlockItem) register(color.id(kind.id()), p -> new BannerItem(
                        ModBlocks.get(Kind.BANNER, color), ModBlocks.get(Kind.WALL_BANNER, color), p), properties));
                } else if (kind == Kind.BED) {
                    byColor.put(color, (BlockItem) register(color.id(kind.id()),
                        p -> new BedItem(ModBlocks.get(Kind.BED, color), p), properties));
                } else if (kind == Kind.SIGN) {
                    // Places the standing sign, or the wall sign when used on the side of a block.
                    byColor.put(color, (BlockItem) register(color.id(kind.id()), p -> new SignItem(
                        ModBlocks.get(Kind.SIGN, color), ModBlocks.get(Kind.WALL_SIGN, color), p), properties));
                } else {
                    byColor.put(color, (BlockItem) register(color.id(kind.id()),
                        p -> new BlockItem(ModBlocks.get(kind, color), p), properties));
                }
            }
            BLOCK_ITEMS.put(kind, Collections.unmodifiableMap(byColor));
        }
        // The beds in the vanilla colors; not in a creative tab, since they only differ from vanilla's by their wood.
        for (DyeColor color : DyeColor.values()) {
            if (Kind.BED.enabled()) {
                register("bed_" + color.getName(), p -> new BedItem(ModBlocks.vanillaColorBed(color), p),
                    new Item.Properties().useBlockDescriptionPrefix().stacksTo(1));
            }
        }
    }

    /** Registers an item the way vanilla does, which also tells a block which item it is. */
    private static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, name));
        return Items.registerItem(key, factory, properties);
    }

    /**
     * The item components of the vanilla item a kind copies that matter here. Burning in a furnace and composting are
     * NeoForge data maps in the shared resources, which MoreDyes hands to Fabric's registries.
     */
    private static Item.Properties properties(Kind kind, MixColor color, Item.Properties p) {
        return switch (kind) {
            case CHEST -> p.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
            case SHULKER_BOX -> p.stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
            case SIGN -> p.stacksTo(16);
            case BED -> p.stacksTo(1);
            case BANNER -> p.stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
            // A llama wears it. Its equipment asset (assets/moredyes/equipment/carpet_<hex>.json) colors the grey
            // llama carpet texture with the carpet's color.
            case CARPET -> p.component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.BODY)
                .setEquipSound(SoundEvents.LLAMA_SWAG)
                .setAsset(ResourceKey.create(EquipmentAssets.ROOT_ID,
                    Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, color.id(kind.id()))))
                .setAllowedEntities(EntityType.LLAMA, EntityType.TRADER_LLAMA)
                .setCanBeSheared(true)
                .setShearingSound(SoundEvents.LLAMA_CARPET_UNEQUIP)
                .build());
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
