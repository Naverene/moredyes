package net.neverandy.moredyes.compat.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.api.storage.EnumBasicDrawer;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.core.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.neverandy.moredyes.compat.storagedrawers.client.StorageDrawersClient;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

/**
 * Dyed Storage Drawers, in every More Dyes color and all five sizes. Like Storage Drawers' own drawers, it is one block
 * whose metadata is the size, and the color is kept where Storage Drawers keeps the wood: as the "material" of the
 * tile entity and of the item's NBT, here a color's hex code. So there is one block rather than five times 118. The
 * block is Storage Drawers' standard drawer with grey models tinted in the dye color, and it makes Storage Drawers'
 * own tile entities, so hoppers, keys, upgrades, labels and drawer controllers all work with it.
 * <p>
 * Only loaded when Storage Drawers is installed (see MoreDyes), so nothing else may refer to this package.
 */
public final class StorageDrawersCompat
{
	public static final String MOD_ID="storagedrawers";
	/** Where Storage Drawers keeps the wood of a drawer item, and where this keeps the color. */
	public static final String MATERIAL="material";

	public static BlockDyedDrawers drawers;
	public static ItemDyedDrawers drawersItem;

	private StorageDrawersCompat(){}

	public static void preInit()
	{
		drawers=new BlockDyedDrawers();
		drawersItem=new ItemDyedDrawers(drawers);
		drawersItem.setRegistryName(drawers.getRegistryName());
		MinecraftForge.EVENT_BUS.register(StorageDrawersCompat.class);
		if(FMLCommonHandler.instance().getSide()==Side.CLIENT)
		{
			StorageDrawersClient.preInit();
		}
	}

	/** A dyed drawer item of a size in a color, by its position in ColorStrings.ALL. */
	public static ItemStack stack(EnumBasicDrawer size,int color)
	{
		ItemStack stack=new ItemStack(drawersItem,1,size.getMetadata());
		NBTTagCompound tag=new NBTTagCompound();
		tag.setString(MATERIAL,ColorStrings.ALL[color]);
		stack.setTagCompound(tag);
		return stack;
	}
	/** The color a material names, by its position in ColorStrings.ALL, or the first color for anything else. */
	public static int colorOf(String material)
	{
		for(int color=0;color<ColorStrings.ALL.length;color++)
		{
			if(ColorStrings.ALL[color].equals(material))
			{
				return color;
			}
		}
		return 0;
	}
	public static int colorOf(ItemStack stack)
	{
		NBTTagCompound tag=stack.getTagCompound();
		return colorOf(tag==null?null:tag.getString(MATERIAL));
	}
	public static int colorOf(TileEntityDrawers tile)
	{
		return colorOf(tile.getMaterial());
	}

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().register(drawers);
	}
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().register(drawersItem);
		// Storage Drawers' own name for its drawers, which recipes from it and other mods ask for.
		OreDictionary.registerOre("drawerBasic",new ItemStack(drawersItem,1,OreDictionary.WILDCARD_VALUE));
	}
	/** Any Storage Drawers wooden drawer of a size, or a dyed one, with a dye gives a dyed drawer of that size. */
	@SubscribeEvent
	public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event)
	{
		for(EnumBasicDrawer size:EnumBasicDrawer.values())
		{
			// Ingredients match items by their damage value, so these take any wood and any color.
			Ingredient drawer=Ingredient.fromStacks(
					new ItemStack(ModBlocks.basicDrawers,1,size.getMetadata()),
					new ItemStack(drawersItem,1,size.getMetadata()));
			ResourceLocation group=new ResourceLocation(Reference.MOD_ID,"dyed_drawers_"+size.getName());
			for(int color=0;color<ColorStrings.ALL.length;color++)
			{
				event.getRegistry().register(new ShapelessOreRecipe(group,stack(size,color),drawer,MDItem.dyeStack(color,1))
						.setRegistryName(Reference.MOD_ID,"storage_drawers/dyed_drawers_"+size.getName()+"_"+ColorStrings.ALL[color]));
			}
		}
	}
}
