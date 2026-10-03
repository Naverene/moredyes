package net.neverandy.moredyes.compat.ironchest;

import java.util.Locale;

import cpw.mods.ironchest.common.blocks.chest.IronChestType;
import cpw.mods.ironchest.common.core.IronChestBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.neverandy.moredyes.compat.ironchest.client.IronChestClient;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

/**
 * Dyed Iron Chests, in every More Dyes color. Each tier is one block, and the color is kept by its tile entity (like
 * Iron Chests keeps a chest's facing) and by the damage value of its item, so there are seven blocks rather than seven
 * times 118: block metadata only holds 16 values, and a block per color or per color group would use up block ids,
 * which 1.12.2 has only 4096 of. They hold as much as the Iron Chests chest of the same tier and use its screen. The
 * panels are drawn in the dye color and the edges, latch and inside keep their own (client/DyedIronChestRenderer).
 * <p>
 * Only loaded when Iron Chests is installed (see MoreDyes), so nothing else may refer to this package.
 */
public final class IronChestCompat
{
	public static final String MOD_ID="ironchest";

	/** The tiers that have dyed versions, in the order of Reference.IRON_CHEST_TIERS. */
	public enum Tier
	{
		IRON(IronChestType.IRON,TileEntityDyedIronChest.Iron.class),
		GOLD(IronChestType.GOLD,TileEntityDyedIronChest.Gold.class),
		DIAMOND(IronChestType.DIAMOND,TileEntityDyedIronChest.Diamond.class),
		COPPER(IronChestType.COPPER,TileEntityDyedIronChest.Copper.class),
		SILVER(IronChestType.SILVER,TileEntityDyedIronChest.Silver.class),
		CRYSTAL(IronChestType.CRYSTAL,TileEntityDyedIronChest.Crystal.class),
		OBSIDIAN(IronChestType.OBSIDIAN,TileEntityDyedIronChest.Obsidian.class);

		public final IronChestType type;
		final Class<? extends TileEntityDyedIronChest> tileClass;
		public BlockDyedIronChest block;
		public ItemDyedIronChest item;

		Tier(IronChestType type,Class<? extends TileEntityDyedIronChest> tileClass)
		{
			this.type=type;
			this.tileClass=tileClass;
		}
		/** The name Iron Chests gives this tier, as in "iron". */
		public String id()
		{
			return this.name().toLowerCase(Locale.ROOT);
		}
		/** The dyed tier of an Iron Chests tier, or null if it has none. */
		public static Tier of(IronChestType type)
		{
			for(Tier tier:values())
			{
				if(tier.type==type)
				{
					return tier;
				}
			}
			return null;
		}
	}

	private IronChestCompat(){}

	public static void preInit()
	{
		for(Tier tier:Tier.values())
		{
			tier.block=new BlockDyedIronChest(tier);
			tier.item=new ItemDyedIronChest(tier.block);
			tier.item.setRegistryName(tier.block.getRegistryName());
		}
		MinecraftForge.EVENT_BUS.register(IronChestCompat.class);
		MinecraftForge.EVENT_BUS.register(ChestUpgrades.class);
		if(FMLCommonHandler.instance().getSide()==Side.CLIENT)
		{
			IronChestClient.preInit();
		}
	}

	/** A dyed chest of a tier in a color, by its position in ColorStrings.ALL. */
	public static ItemStack stack(Tier tier,int color)
	{
		return new ItemStack(tier.item,1,color);
	}
	/** Keeps a color inside ColorStrings.ALL, falling back to the first color. */
	public static int clampColor(int color)
	{
		return color<0||color>=ColorStrings.ALL.length?0:color;
	}

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event)
	{
		for(Tier tier:Tier.values())
		{
			event.getRegistry().register(tier.block);
			GameRegistry.registerTileEntity(tier.tileClass,new ResourceLocation(Reference.MOD_ID,"dyed_"+tier.id()+"_chest"));
		}
	}
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event)
	{
		for(Tier tier:Tier.values())
		{
			event.getRegistry().register(tier.item);
		}
	}
	/**
	 * An Iron Chests chest, or a dyed one of the same tier, with a dye gives a dyed chest of that tier. Iron Chests'
	 * upgrades work on dyed chests too and keep the color (ChestUpgrades).
	 */
	@SubscribeEvent
	public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event)
	{
		for(Tier tier:Tier.values())
		{
			Ingredient chest=Ingredient.fromStacks(
					new ItemStack(IronChestBlocks.ironChestBlock,1,tier.type.ordinal()),
					new ItemStack(tier.item,1,OreDictionary.WILDCARD_VALUE));
			ResourceLocation group=new ResourceLocation(Reference.MOD_ID,"dyed_"+tier.id()+"_chest");
			for(int color=0;color<ColorStrings.ALL.length;color++)
			{
				event.getRegistry().register(new ShapelessOreRecipe(group,stack(tier,color),chest,MDItem.dyeStack(color,1))
						.setRegistryName(Reference.MOD_ID,"iron_chests/dyed_"+tier.id()+"_chest_"+ColorStrings.ALL[color]));
			}
		}
	}
}
