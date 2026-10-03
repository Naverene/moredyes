package net.neverandy.moredyes.compat.ironchest.client;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;
import net.neverandy.moredyes.compat.ironchest.ItemDyedIronChest;
import net.neverandy.moredyes.compat.ironchest.TileEntityDyedIronChest;

/** Draws a dyed Iron Chests chest item with the chest renderer, as a closed chest in the item's color. */
@SideOnly(Side.CLIENT)
public class DyedIronChestItemRenderer extends TileEntityItemStackRenderer
{
	private final Map<IronChestCompat.Tier,TileEntityDyedIronChest> chests=new EnumMap<IronChestCompat.Tier,TileEntityDyedIronChest>(IronChestCompat.Tier.class);

	@Override
	public void renderByItem(ItemStack stack,float partialTicks)
	{
		if(!(stack.getItem() instanceof ItemDyedIronChest))
		{
			return;
		}
		IronChestCompat.Tier tier=((ItemDyedIronChest)stack.getItem()).getTier();
		TileEntityDyedIronChest chest=this.chests.get(tier);
		if(chest==null)
		{
			chest=(TileEntityDyedIronChest)tier.block.createTileEntity(null,tier.block.getDefaultState());
			this.chests.put(tier,chest);
		}
		chest.setColor(stack.getMetadata());
		TileEntityRendererDispatcher.instance.render(chest,0.0D,0.0D,0.0D,0.0F,partialTicks);
	}
}
