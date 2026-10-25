package net.neverandy.moredyes.client;

import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.tileentity.TileEntityDyedBanner;
import net.neverandy.moredyes.tileentity.TileEntityDyedBed;
import net.neverandy.moredyes.tileentity.TileEntityDyedShulkerBox;

/** Draws the dyed bed, banner and shulker box items with their renderers, like the vanilla items. */
@SideOnly(Side.CLIENT)
public class DyedFurnishingItemRenderer extends TileEntityItemStackRenderer
{
	private final TileEntityDyedBed bed=new TileEntityDyedBed();
	private final TileEntityDyedBanner banner=new TileEntityDyedBanner();
	private final TileEntityDyedShulkerBox shulkerBox=new TileEntityDyedShulkerBox();

	@Override
	public void renderByItem(ItemStack stack,float partialTicks)
	{
		Item item=stack.getItem();
		TileEntity tile;
		if(item==MDItem.bed)
		{
			this.bed.setColorIndex(stack.getMetadata());
			tile=this.bed;
		}
		else if(item==MDItem.banner)
		{
			this.banner.setItemValues(stack,false);
			this.banner.setColorIndex(stack.getMetadata());
			tile=this.banner;
		}
		else
		{
			this.shulkerBox.setColorIndex(stack.getMetadata());
			tile=this.shulkerBox;
		}
		TileEntityRendererDispatcher.instance.render(tile,0.0,0.0,0.0,0.0f,partialTicks);
	}
}
