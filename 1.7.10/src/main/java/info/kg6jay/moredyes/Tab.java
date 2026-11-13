package info.kg6jay.moredyes;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Tab extends CreativeTabs {

    private Item tabIconItem;
    private String tabLabel;
    private ItemStack tabIconStack;

    public Tab(String tabID) {
        super(CreativeTabs.getNextID(), "MoreDyes" + tabID);
        tabIconItem = Items.diamond;
        tabLabel = "More Dyes " + tabID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return tabIconItem;
    }

    public String getTranslatedTabLabel() {
        return tabLabel;
    }

    public void setTabIconItem(Item tabItem) {
        tabIconItem = tabItem;
        tabIconStack = null;
    }

    /** For an item whose icon depends on its damage, such as the shared dyed block item. */
    public void setTabIconStack(ItemStack stack) {
        tabIconItem = stack.getItem();
        tabIconStack = stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        return tabIconStack != null ? tabIconStack : super.getIconItemStack();
    }
}
