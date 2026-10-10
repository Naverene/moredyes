package net.neverandy.moredyes.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The five MoreDyes creative tabs. Each lists its items in the order they were registered. In 1.19.2 a tab is made
 * when its class loads and an item names its tab in its properties (see MDBlock.register), which also puts the item in
 * the creative search tab.
 */
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
    private final List<RegistryObject<Item>> items = new ArrayList<>();
    public final CreativeModeTab tab;

    MDTabs(String id, int icon)
    {
        this.id = id;
        this.icon = icon;
        this.tab = new CreativeModeTab(id)
        {
            @Override
            public ItemStack makeIcon()
            {
                return new ItemStack(items.get(Math.min(MDTabs.this.icon, items.size() - 1)).get());
            }

            /** The items in the order they were registered, rather than the registry's order. */
            @Override
            public void fillItemList(NonNullList<ItemStack> list)
            {
                for (RegistryObject<Item> item : items)
                {
                    item.get().fillItemCategory(this, list);
                }
            }
        };
    }

    public void add(RegistryObject<? extends Item> item)
    {
        @SuppressWarnings("unchecked")
        RegistryObject<Item> entry = (RegistryObject<Item>) item;
        items.add(entry);
    }
}
