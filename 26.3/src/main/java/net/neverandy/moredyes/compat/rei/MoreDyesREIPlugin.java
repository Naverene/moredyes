package net.neverandy.moredyes.compat.rei;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredItem;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.color.MixColor;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.neverandy.moredyes.registry.ModItems;

/**
 * Folds the 118 colors of each kind of item into one collapsible entry in REI's item list, such as "Dyed Oak Planks".
 * Each group is {@code moredyes:<kind id>} ({@code moredyes:dye} for the dyes, {@code moredyes:<size>} for the
 * dyed Storage Drawers), named by the translation key
 * {@code group.moredyes.<kind id>} that tools/generate_resources.py writes. REI finds this class by its annotation, so
 * it is only loaded when REI is installed.
 */
@REIPluginClient
public class MoreDyesREIPlugin implements REIClientPlugin {

    @Override
    public void registerCollapsibleEntries(CollapsibleEntryRegistry registry) {
        group(registry, "dye", ModItems::dye);
        for (Kind kind : Kind.values()) {
            if (kind.hasItem()) {
                group(registry, kind.id(), color -> ModItems.get(kind, color));
            }
        }
        if (ModList.get().isLoaded(StorageDrawersCompat.MOD_ID)) {
            // One item per drawer size, with the color in its block state.
            List<Item> drawers = StorageDrawersCompat.items().stream().map(Item.class::cast).toList();
            for (int i = 0; i < drawers.size(); i++) {
                List<EntryStack<?>> stacks = new ArrayList<>();
                for (int color = 0; color < MixColors.ALL.size(); color++) {
                    stacks.add(EntryStack.of(VanillaEntryTypes.ITEM, StorageDrawersCompat.stack(drawers.get(i), color)));
                }
                add(registry, StorageDrawersCompat.SIZES.get(i), stacks);
            }
        }
    }

    private static void group(CollapsibleEntryRegistry registry, String id, Function<MixColor, DeferredItem<? extends Item>> items) {
        List<EntryStack<?>> stacks = new ArrayList<>();
        for (MixColor color : MixColors.ALL) {
            stacks.add(EntryStack.of(VanillaEntryTypes.ITEM, new ItemStack(items.apply(color).get())));
        }
        add(registry, id, stacks);
    }

    private static void add(CollapsibleEntryRegistry registry, String id, List<EntryStack<?>> stacks) {
        registry.group(Identifier.fromNamespaceAndPath(MoreDyes.MOD_ID, id),
            Component.translatable("group." + MoreDyes.MOD_ID + "." + id), stacks);
    }
}
