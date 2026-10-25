package net.neverandy.moredyes.compat.ironchest;

import java.util.Locale;

import cpw.mods.ironchest.common.blocks.shulker.BlockIronShulkerBox;
import cpw.mods.ironchest.common.blocks.shulker.IronShulkerBoxType;
import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.compat.ironchest.client.IronShulkerBoxClient;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.recipe.KeepNbtShapelessRecipe;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;

/**
 * Dyed Iron Chests shulker boxes, in every More Dyes color. Like the dyed Iron Chests (IronChestCompat), each tier is
 * one block, and the color is kept by its tile entity and by the damage value of its item. They hold as much as Iron
 * Chests' box of the same tier and use its screen. The shell is drawn in the dye color and the metal trim keeps its
 * own (client/DyedIronShulkerBoxRenderer).
 * <p>
 * Only loaded when Iron Chests is installed (see MoreDyes), so nothing else may refer to this class.
 */
public final class IronShulkerBoxCompat
{
	/** The tiers, in the order of Reference.IRON_SHULKER_BOX_TIERS. */
	public enum Tier
	{
		IRON(IronShulkerBoxType.IRON,TileEntityDyedIronShulkerBox.Iron.class),
		GOLD(IronShulkerBoxType.GOLD,TileEntityDyedIronShulkerBox.Gold.class),
		DIAMOND(IronShulkerBoxType.DIAMOND,TileEntityDyedIronShulkerBox.Diamond.class),
		COPPER(IronShulkerBoxType.COPPER,TileEntityDyedIronShulkerBox.Copper.class),
		SILVER(IronShulkerBoxType.SILVER,TileEntityDyedIronShulkerBox.Silver.class),
		CRYSTAL(IronShulkerBoxType.CRYSTAL,TileEntityDyedIronShulkerBox.Crystal.class),
		OBSIDIAN(IronShulkerBoxType.OBSIDIAN,TileEntityDyedIronShulkerBox.Obsidian.class);

		public final IronShulkerBoxType type;
		final Class<? extends TileEntityDyedIronShulkerBox> tileClass;
		public BlockDyedIronShulkerBox block;
		public ItemDyedIronShulkerBox item;

		Tier(IronShulkerBoxType type,Class<? extends TileEntityDyedIronShulkerBox> tileClass)
		{
			this.type=type;
			this.tileClass=tileClass;
		}
		/** The name Iron Chests gives this tier, as in "iron". */
		public String id()
		{
			return this.name().toLowerCase(Locale.ROOT);
		}
		/** The dyed tier of an Iron Chests shulker box tier, or null for the vanilla box. */
		public static Tier of(IronShulkerBoxType type)
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

	private IronShulkerBoxCompat(){}

	public static void preInit()
	{
		for(Tier tier:Tier.values())
		{
			tier.block=new BlockDyedIronShulkerBox(tier);
			tier.item=new ItemDyedIronShulkerBox(tier.block);
			tier.item.setRegistryName(tier.block.getRegistryName());
		}
		MinecraftForge.EVENT_BUS.register(IronShulkerBoxCompat.class);
		MinecraftForge.EVENT_BUS.register(ShulkerBoxUpgrades.class);
		if(FMLCommonHandler.instance().getSide()==Side.CLIENT)
		{
			IronShulkerBoxClient.preInit();
		}
	}

	/** Washing a dyed box in a cauldron gives Iron Chests' purple box of the tier, as vanilla washes its boxes purple. */
	@SubscribeEvent(priority=EventPriority.HIGH)
	public static void registerWashing(RegistryEvent.Register<IRecipe> event)
	{
		// Before CraftManager makes the washing recipes; Iron Chests' blocks are filled in by then.
		for(Tier tier:Tier.values())
		{
			MDBlock.WASH_KEEPS_NBT.add(tier.block);
			MDBlock.WASHED.put(tier.block,new ItemStack(BlockIronShulkerBox.getBlockByColor(EnumDyeColor.PURPLE),1,tier.type.ordinal()));
		}
	}

	/** A dyed box of a tier in a color, by its position in ColorStrings.ALL. */
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
			GameRegistry.registerTileEntity(tier.tileClass,new ResourceLocation(Reference.MOD_ID,"dyed_"+tier.id()+"_shulker_box"));
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
	 * An Iron Chests shulker box of any color, or a dyed one of the same tier, with a dye gives a dyed box of that tier
	 * holding the same things. Iron Chests' upgrades work on dyed boxes too and keep the color (ShulkerBoxUpgrades).
	 */
	@SubscribeEvent
	public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event)
	{
		for(Tier tier:Tier.values())
		{
			NonNullList<ItemStack> boxes=NonNullList.create();
			for(EnumDyeColor color:EnumDyeColor.values())
			{
				boxes.add(new ItemStack(BlockIronShulkerBox.getBlockByColor(color),1,tier.type.ordinal()));
			}
			boxes.add(new ItemStack(tier.item,1,OreDictionary.WILDCARD_VALUE));
			Ingredient box=Ingredient.fromStacks(boxes.toArray(new ItemStack[0]));
			ResourceLocation group=new ResourceLocation(Reference.MOD_ID,"dyed_"+tier.id()+"_shulker_box");
			for(int color=0;color<ColorStrings.ALL.length;color++)
			{
				event.getRegistry().register(new KeepNbtShapelessRecipe(group,stack(tier,color),box,MDItem.dyeStack(color,1))
						.setRegistryName(Reference.MOD_ID,"iron_chests/dyed_"+tier.id()+"_shulker_box_"+ColorStrings.ALL[color]));
			}
		}
	}
}
