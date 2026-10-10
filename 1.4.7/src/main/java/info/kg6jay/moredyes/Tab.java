package info.kg6jay.moredyes;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** A More Dyes creative tab, named "More Dyes Blocks" and so on. Its icon is set once the blocks exist. */
public class Tab extends CreativeTabs {

    private final String label;
    private ItemStack icon;

    public Tab(String name) {
        super("MoreDyes" + name);
        this.label = "More Dyes " + name;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    @Override
    public ItemStack getIconItemStack() {
        return this.icon != null ? this.icon : new ItemStack(Block.cloth);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslatedTabLabel() {
        return this.label;
    }
}
