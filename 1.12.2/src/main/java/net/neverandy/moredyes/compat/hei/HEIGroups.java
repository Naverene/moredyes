package net.neverandy.moredyes.compat.hei;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mezz.jei.api.ICollapsibleGroupRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.reference.Reference;

/**
 * The HEI groups: one per kind of item with all of its colors, such as all 118 "Dyed Wool", whichever of the blocks
 * of its color groups they are stored in. A kind is the last part of the item's translation key ("wool" for
 * tile.moredyes.wool), so the group is "moredyes:wool", named by the translation key "group.moredyes.wool".
 * Only {@link MoreDyesJEIPlugin} uses this class, and only when HEI calls it.
 */
final class HEIGroups
{
	private HEIGroups()
	{
	}

	static void register(ICollapsibleGroupRegistry registry)
	{
		Map<String,NonNullList<ItemStack>> kinds=new LinkedHashMap<String,NonNullList<ItemStack>>();
		for(Item dye:MDItem.dye)
		{
			add(kinds,dye);
		}
		for(Block block:MDBlock.ALL)
		{
			add(kinds,Item.getItemFromBlock(block));
		}
		for(Map.Entry<String,NonNullList<ItemStack>> kind:kinds.entrySet())
		{
			List<ItemStack> stacks=kind.getValue();
			registry.newGroup(Reference.MOD_ID+":"+kind.getKey(),"group."+Reference.MOD_ID+"."+kind.getKey())
					.add(stacks.toArray())
					.build();
		}
	}
	private static void add(Map<String,NonNullList<ItemStack>> kinds,Item item)
	{
		String key=item.getUnlocalizedName();
		String kind=key.substring(key.lastIndexOf('.')+1);
		NonNullList<ItemStack> stacks=kinds.get(kind);
		if(stacks==null)
		{
			stacks=NonNullList.create();
			kinds.put(kind,stacks);
		}
		// Every color of the item, as the search tab lists it.
		item.getSubItems(CreativeTabs.SEARCH,stacks);
	}
}
