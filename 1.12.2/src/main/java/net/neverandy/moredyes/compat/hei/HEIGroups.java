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
import net.minecraftforge.fml.common.Loader;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.reference.Reference;

/**
 * The HEI groups: one per kind of item with all of its colors, such as all 118 "Dyed Wool", whichever of the blocks
 * of its color groups they are stored in. A kind is the item's translation key after "tile.moredyes." or
 * "item.moredyes.", with dots as underscores: "wool" for tile.moredyes.wool, "dyed_iron_chest" for a dyed Iron Chests
 * chest and "dyed_drawers_fulldrawers2" for a size of dyed drawers. The group is "moredyes:&lt;kind&gt;", named by the
 * translation key "group.moredyes.&lt;kind&gt;". Only {@link MoreDyesJEIPlugin} uses this class, and only when HEI calls it.
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
		// The compat items exist only with their mod, and their classes are only touched then.
		if(Loader.isModLoaded(IronChestCompat.MOD_ID))
		{
			for(IronChestCompat.Tier tier:IronChestCompat.Tier.values())
			{
				add(kinds,tier.item);
			}
		}
		if(Loader.isModLoaded(StorageDrawersCompat.MOD_ID))
		{
			add(kinds,StorageDrawersCompat.drawersItem);
		}
		for(Map.Entry<String,NonNullList<ItemStack>> kind:kinds.entrySet())
		{
			List<ItemStack> stacks=kind.getValue();
			registry.newGroup(Reference.MOD_ID+":"+kind.getKey(),"group."+Reference.MOD_ID+"."+kind.getKey())
					.add(stacks.toArray())
					.build();
		}
	}
	/** Every color of the item, as the search tab lists it, each under its own kind. */
	private static void add(Map<String,NonNullList<ItemStack>> kinds,Item item)
	{
		NonNullList<ItemStack> all=NonNullList.create();
		item.getSubItems(CreativeTabs.SEARCH,all);
		for(ItemStack stack:all)
		{
			String key=stack.getUnlocalizedName();
			String kind=key.substring(key.indexOf(Reference.MOD_ID+".")+Reference.MOD_ID.length()+1).replace('.','_');
			NonNullList<ItemStack> stacks=kinds.get(kind);
			if(stacks==null)
			{
				stacks=NonNullList.create();
				kinds.put(kind,stacks);
			}
			stacks.add(stack);
		}
	}
}
