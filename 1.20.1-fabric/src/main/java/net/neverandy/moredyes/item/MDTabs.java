package net.neverandy.moredyes.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neverandy.moredyes.reference.Reference;

import java.util.ArrayList;
import java.util.List;

/** The five MoreDyes creative tabs. Each lists its items in the order they were registered. */
public enum MDTabs
{
    TREES("trees", 78),
    PLANTS("plants", 56),
    DYES("dyes", 99),
    BLOCKS("blocks", 100),
    SHAPES("shapes", 100);

    /** The translation key's last part, as in "itemGroup.trees". */
    public final String id;
    /** Which of the tab's items is its icon. */
    private final int icon;
    private final List<Item> items = new ArrayList<>();

    MDTabs(String id, int icon)
    {
        this.id = id;
        this.icon = icon;
    }

    public void add(Item item)
    {
        items.add(item);
    }

    public static void register()
    {
        for (MDTabs tab : values())
        {
            Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(Reference.MOD_ID, tab.id), FabricItemGroup.builder()
                    .title(Component.translatable("itemGroup." + tab.id))
                    .icon(() -> new ItemStack(tab.items.get(Math.min(tab.icon, tab.items.size() - 1))))
                    .displayItems((parameters, output) -> tab.items.forEach(output::accept))
                    .build());
        }
    }
}
