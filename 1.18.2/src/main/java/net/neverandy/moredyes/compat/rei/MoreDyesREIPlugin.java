package net.neverandy.moredyes.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neverandy.moredyes.compat.ItemGroups;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Folds every color of each kind of item into one collapsible entry in REI's item list. REI finds this class by its
 * annotation, so it is only loaded when REI is installed.
 */
@REIPluginClient
public class MoreDyesREIPlugin implements REIClientPlugin
{
    @Override
    public void registerCollapsibleEntries(CollapsibleEntryRegistry registry)
    {
        for (Map.Entry<String, List<Item>> group : ItemGroups.byKind().entrySet())
        {
            List<EntryStack<?>> stacks = new ArrayList<>();
            for (Item item : group.getValue())
            {
                stacks.add(EntryStack.of(VanillaEntryTypes.ITEM, new ItemStack(item)));
            }
            registry.group(ItemGroups.id(group.getKey()), new TranslatableComponent(ItemGroups.TRANSLATION_PREFIX + group.getKey()), stacks);
        }
    }
}
