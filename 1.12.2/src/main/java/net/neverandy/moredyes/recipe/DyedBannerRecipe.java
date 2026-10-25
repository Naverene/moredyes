package net.neverandy.moredyes.recipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.neverandy.moredyes.item.MDItem;

/**
 * One of the vanilla banner recipes (adding a pattern, copying one) for dyed banners. The vanilla recipes only take the
 * vanilla banner, so the dyed banners are handed to them as vanilla banners with the same patterns, and the result
 * turns back into a dyed banner of the same color. All the banners in the grid must be dyed ones of one color.
 */
public class DyedBannerRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
	private static final Container NO_CONTAINER=new Container()
	{
		@Override
		public boolean canInteractWith(EntityPlayer player)
		{
			return false;
		}
	};

	private final IRecipe vanilla;

	public DyedBannerRecipe(IRecipe vanilla)
	{
		this.vanilla=vanilla;
	}
	/** The color of the dyed banners in the grid, or -1 if there are none, other banners, or several colors. */
	private static int color(InventoryCrafting inv)
	{
		int color=-1;
		for(int i=0;i<inv.getSizeInventory();i++)
		{
			ItemStack stack=inv.getStackInSlot(i);
			if(stack.getItem()==Items.BANNER)
			{
				return -1;
			}
			if(stack.getItem()==MDItem.banner)
			{
				if(color>=0&&color!=stack.getMetadata())
				{
					return -1;
				}
				color=stack.getMetadata();
			}
		}
		return color;
	}
	/** The grid with every dyed banner swapped for a vanilla one with the same patterns. */
	private static InventoryCrafting asVanilla(InventoryCrafting inv)
	{
		InventoryCrafting copy=new InventoryCrafting(NO_CONTAINER,inv.getWidth(),inv.getHeight());
		for(int i=0;i<inv.getSizeInventory();i++)
		{
			copy.setInventorySlotContents(i,swap(inv.getStackInSlot(i),MDItem.banner,Items.BANNER,0));
		}
		return copy;
	}
	private static ItemStack swap(ItemStack stack,Item from,Item to,int meta)
	{
		if(stack.getItem()!=from)
		{
			return stack.copy();
		}
		ItemStack swapped=new ItemStack(to,stack.getCount(),meta);
		swapped.setTagCompound(stack.hasTagCompound()?stack.getTagCompound().copy():null);
		return swapped;
	}
	@Override
	public boolean matches(InventoryCrafting inv,World world)
	{
		return color(inv)>=0&&this.vanilla.matches(asVanilla(inv),world);
	}
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		int color=color(inv);
		return color<0?ItemStack.EMPTY:swap(this.vanilla.getCraftingResult(asVanilla(inv)),Items.BANNER,MDItem.banner,color);
	}
	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		int color=color(inv);
		NonNullList<ItemStack> remaining=this.vanilla.getRemainingItems(asVanilla(inv));
		for(int i=0;i<remaining.size();i++)
		{
			remaining.set(i,swap(remaining.get(i),Items.BANNER,MDItem.banner,color));
		}
		return remaining;
	}
	@Override
	public boolean canFit(int width,int height)
	{
		return this.vanilla.canFit(width,height);
	}
	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}
	@Override
	public boolean isDynamic()
	{
		return true;
	}
}
