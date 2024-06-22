package info.kg6jay.moredyes.item;

import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.block.MDBlockColored;
import info.kg6jay.moredyes.reference.ColorStrings;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class MDItem
{
	public static Item[] dye;
	public static void initialize()
	{
		dye = new Item[MDBlock.colors.length];
		for(int i=0;i<MDBlock.colors.length;i++)
		{
			dye[i]=new MDItemDye(MDBlock.colorStrings[i],MDBlock.colors[i]);
		}
	}
	public static void register()
	{
		for(int i=0;i<dye.length;i++)
		{
			GameRegistry.registerItem(dye[i], dye[i].getUnlocalizedName());
		}
	}
	public static void registerOreDict()
	{
		
	}
}
