package net.neverandy.moredyes.registry;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
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
            if (!kind.hasItem() || !kind.enabled()) {
                continue;
            }
            Map<MixColor, DeferredItem<BlockItem>> byColor = new LinkedHashMap<>();
            for (MixColor color : MixColors.ALL) {
                if (kind == Kind.BANNER) {
                    byColor.put(color, ITEMS.registerItem(color.id(kind.id()), p -> new BannerItem(
                        ModBlocks.get(Kind.BANNER, color).get(), ModBlocks.get(Kind.WALL_BANNER, color).get(),
                        properties(kind, color, p.useBlockDescriptionPrefix()))));
                } else if (kind == Kind.BED) {
                    byColor.put(color, ITEMS.registerItem(color.id(kind.id()), p -> new BedItem(
                        ModBlocks.get(Kind.BED, color).get(), properties(kind, color, p.useBlockDescriptionPrefix()))));
                } else if (kind == Kind.SIGN) {
                    // Places the standing sign, or the wall sign when used on the side of a block.
                    byColor.put(color, ITEMS.registerItem(color.id(kind.id()), p -> new SignItem(
                        ModBlocks.get(Kind.SIGN, color).get(), ModBlocks.get(Kind.WALL_SIGN, color).get(),
                        properties(kind, color, p.useBlockDescriptionPrefix()))));
                } else {
                    byColor.put(color, ITEMS.registerSimpleBlockItem(ModBlocks.get(kind, color), p -> properties(kind, color, p)));
                }
            }
            BLOCK_ITEMS.put(kind, Collections.unmodifiableMap(byColor));
        }
        // The beds in the vanilla colors; not in a creative tab, since they only differ from vanilla's by their wood.
        for (DyeColor color : DyeColor.values()) {
            if (Kind.BED.enabled()) {
                ITEMS.registerItem("bed_" + color.getName(), p -> new BedItem(ModBlocks.vanillaColorBed(color),
                    p.useBlockDescriptionPrefix().stacksTo(1)));
            }
        }
    }

    private ModItems() {}

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

    /**
     * The item components of the vanilla item a kind copies that matter here. Burning in a furnace and composting are
     * NeoForge data maps ({@code data/neoforge/data_maps/item}), written by tools/generate_resources.py.
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

    public static DeferredItem<MixDyeItem> dye(MixColor color) {
        return DYES.get(color);
    }

    /** The item of a kind in a color, or null when the kind has no item or the config leaves it out. */
    public static DeferredItem<BlockItem> get(Kind kind, MixColor color) {
        Map<MixColor, DeferredItem<BlockItem>> byColor = BLOCK_ITEMS.get(kind);
        return byColor == null ? null : byColor.get(color);
    }
}
