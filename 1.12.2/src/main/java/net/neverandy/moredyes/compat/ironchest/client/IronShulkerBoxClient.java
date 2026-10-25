package net.neverandy.moredyes.compat.ironchest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.compat.ironchest.IronShulkerBoxCompat;
import net.neverandy.moredyes.compat.ironchest.TileEntityDyedIronShulkerBox;
import net.neverandy.moredyes.reference.ColorStrings;

/** Client setup for dyed Iron Chests shulker boxes: the box renderer, in the world and for the items, and its textures. */
@SideOnly(Side.CLIENT)
public final class IronShulkerBoxClient
{
	private IronShulkerBoxClient(){}

	public static void preInit()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDyedIronShulkerBox.class,new DyedIronShulkerBoxRenderer());
		DyedIronShulkerBoxItemRenderer itemRenderer=new DyedIronShulkerBoxItemRenderer();
		for(IronShulkerBoxCompat.Tier tier:IronShulkerBoxCompat.Tier.values())
		{
			tier.item.setTileEntityItemStackRenderer(itemRenderer);
		}
		IResourceManager resources=Minecraft.getMinecraft().getResourceManager();
		if(resources instanceof IReloadableResourceManager)
		{
			((IReloadableResourceManager)resources).registerReloadListener(DyedIronShulkerBoxTextures.INSTANCE);
		}
		MinecraftForge.EVENT_BUS.register(IronShulkerBoxClient.class);
	}

	/** Every color of a tier's item uses one model, which hands the drawing to the box renderer. */
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		for(IronShulkerBoxCompat.Tier tier:IronShulkerBoxCompat.Tier.values())
		{
			ModelResourceLocation model=new ModelResourceLocation(tier.item.getRegistryName(),"inventory");
			for(int color=0;color<ColorStrings.ALL.length;color++)
			{
				ModelLoader.setCustomModelResourceLocation(tier.item,color,model);
			}
		}
	}
}
