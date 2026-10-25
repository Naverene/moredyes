package net.neverandy.moredyes.compat.ironchest.client;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.compat.ironchest.IronShulkerBoxCompat;
import net.neverandy.moredyes.compat.ironchest.ItemDyedIronShulkerBox;
import net.neverandy.moredyes.compat.ironchest.TileEntityDyedIronShulkerBox;

/** Draws a dyed Iron Chests shulker box item with the box renderer, as a closed box in the item's color. */
@SideOnly(Side.CLIENT)
public class DyedIronShulkerBoxItemRenderer extends TileEntityItemStackRenderer
{
	private final Map<IronShulkerBoxCompat.Tier,TileEntityDyedIronShulkerBox> boxes=new EnumMap<IronShulkerBoxCompat.Tier,TileEntityDyedIronShulkerBox>(IronShulkerBoxCompat.Tier.class);

	@Override
	public void renderByItem(ItemStack stack,float partialTicks)
	{
		if(!(stack.getItem() instanceof ItemDyedIronShulkerBox))
		{
			return;
		}
		IronShulkerBoxCompat.Tier tier=((ItemDyedIronShulkerBox)stack.getItem()).getTier();
		TileEntityDyedIronShulkerBox box=this.boxes.get(tier);
		if(box==null)
		{
			box=(TileEntityDyedIronShulkerBox)tier.block.createTileEntity(null,tier.block.getDefaultState());
			this.boxes.put(tier,box);
		}
		box.setColor(stack.getMetadata());
		TileEntityRendererDispatcher.instance.render(box,0.0D,0.0D,0.0D,0.0F,partialTicks);
	}
}
