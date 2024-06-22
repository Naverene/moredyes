package info.kg6jay.moredyes;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;


public class Tab extends CreativeTabs
{
	private Item tabIconItem;
	private String tabLabel;
	public Tab(String tabID)
	{
		super(CreativeTabs.getNextID(), "MoreDyes"+tabID);
		tabIconItem = Items.diamond;
		tabLabel="More Dyes "+tabID;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		return tabIconItem;
	}
	public String getTranslatedTabLabel()
	{
		return tabLabel;
	}
	public void setTabIconItem(Item tabItem)
	{
		tabIconItem = tabItem;
	}
}
