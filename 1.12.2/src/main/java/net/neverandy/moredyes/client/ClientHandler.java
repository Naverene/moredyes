package net.neverandy.moredyes.client;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderLlama;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerSheepWool;
import net.minecraft.client.renderer.entity.layers.LayerWolfCollar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.block.BlockColored;
import net.neverandy.moredyes.block.IColoredBlock;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.block.TileColor;
import net.neverandy.moredyes.entity.LlamaCarpets;
import net.neverandy.moredyes.handler.DyedCollarHandler;
import net.neverandy.moredyes.handler.DyedSheepHandler;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.item.MDItemDye;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityDyedBanner;
import net.neverandy.moredyes.tileentity.TileEntityDyedBed;
import net.neverandy.moredyes.tileentity.TileEntityDyedChest;
import net.neverandy.moredyes.tileentity.TileEntityDyedShulkerBox;
import net.neverandy.moredyes.tileentity.TileEntityDyedSign;
import net.neverandy.moredyes.utility.LogHelper;

/**
 * How the dyed blocks and dyes are drawn. Every color of a kind of block shares one model with a grey texture, and
 * the game multiplies the faces marked for tinting by the dye color.
 */
@SideOnly(Side.CLIENT)
@EventBusSubscriber(modid=Reference.MOD_ID,value=Side.CLIENT)
public class ClientHandler
{
	/** The faces and item layers that take the dye color are tint index 0. */
	private static final int TINTED=0;

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		for(Block block:MDBlock.ALL)
		{
			IColoredBlock colored=(IColoredBlock)block;
			final String model=Reference.MOD_ID+":"+colored.getTypeName();
			// Every color uses the blockstate file of its kind of block, so the color is left out of the lookup,
			// along with the properties that do not change how the block looks.
			ModelLoader.setCustomStateMapper(block,new StateMapperBase()
			{
				@Override
				protected ModelResourceLocation getModelResourceLocation(IBlockState state)
				{
					Map<IProperty<?>,Comparable<?>> properties=new LinkedHashMap<IProperty<?>,Comparable<?>>(state.getProperties());
					properties.remove(BlockColored.SHADE);
					properties.remove(BlockLeaves.CHECK_DECAY);
					properties.remove(BlockLeaves.DECAYABLE);
					properties.remove(BlockChest.FACING);
					return new ModelResourceLocation(model,this.getPropertyString(properties));
				}
			});
			Item item=Item.getItemFromBlock(block);
			for(int meta=0;meta<colored.getShadeCount();meta++)
			{
				ModelLoader.setCustomModelResourceLocation(item,meta,new ModelResourceLocation(model,"inventory"));
			}
		}
		// Blocks that keep their color in a tile entity use the blockstate file named after them, and one item model
		// for every color.
		for(Block block:MDBlock.TILE_COLORED)
		{
			Item item=Item.getItemFromBlock(block);
			ModelResourceLocation model=new ModelResourceLocation(block.getRegistryName(),"inventory");
			for(int meta=0;meta<ColorStrings.ALL.length;meta++)
			{
				ModelLoader.setCustomModelResourceLocation(item,meta,model);
			}
		}
		// The dyed piston head looks like the vanilla one.
		ModelLoader.setCustomStateMapper(MDBlock.pistonHead,new StateMapperBase()
		{
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state)
			{
				return new ModelResourceLocation("minecraft:piston_head",this.getPropertyString(state.getProperties()));
			}
		});
		// Signs are drawn by DyedSignRenderer; their model only gives the breaking particles.
		StateMapperBase signModel=new StateMapperBase()
		{
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state)
			{
				return new ModelResourceLocation(Reference.MOD_ID+":sign","normal");
			}
		};
		ModelLoader.setCustomStateMapper(MDBlock.standingSign,signModel);
		ModelLoader.setCustomStateMapper(MDBlock.wallSign,signModel);
		for(int meta=0;meta<ColorStrings.ALL.length;meta++)
		{
			ModelLoader.setCustomModelResourceLocation(MDItem.sign,meta,new ModelResourceLocation(Reference.MOD_ID+":sign","inventory"));
		}
		// Beds, banners and shulker boxes are drawn by their renderers too, items included.
		particleModel(MDBlock.bed,"bed");
		particleModel(MDBlock.standingBanner,"banner");
		particleModel(MDBlock.wallBanner,"banner");
		for(int meta=0;meta<ColorStrings.ALL.length;meta++)
		{
			ModelLoader.setCustomModelResourceLocation(MDItem.bed,meta,new ModelResourceLocation(Reference.MOD_ID+":bed","inventory"));
			ModelLoader.setCustomModelResourceLocation(MDItem.banner,meta,new ModelResourceLocation(Reference.MOD_ID+":banner","inventory"));
		}
		particleModel(MDBlock.shulkerBox,"shulker_box");
		Item shulkerBox=Item.getItemFromBlock(MDBlock.shulkerBox);
		for(int meta=0;meta<ColorStrings.ALL.length;meta++)
		{
			ModelLoader.setCustomModelResourceLocation(shulkerBox,meta,new ModelResourceLocation(Reference.MOD_ID+":shulker_box","inventory"));
		}
		for(Item dye:MDItem.dye)
		{
			for(int meta=0;meta<((MDItemDye)dye).getShadeCount();meta++)
			{
				ModelLoader.setCustomModelResourceLocation(dye,meta,new ModelResourceLocation(Reference.MOD_ID+":dye","inventory"));
			}
		}
	}

	/** A block drawn by a renderer: its model, named after it, only gives the breaking particles. */
	private static void particleModel(Block block,final String name)
	{
		ModelLoader.setCustomStateMapper(block,new StateMapperBase()
		{
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state)
			{
				return new ModelResourceLocation(Reference.MOD_ID+":"+name,"normal");
			}
		});
	}

	public static void registerColors()
	{
		Minecraft mc=Minecraft.getMinecraft();
		Block[] blocks=MDBlock.ALL.toArray(new Block[0]);
		mc.getBlockColors().registerBlockColorHandler((state,world,pos,tintIndex)->
		{
			Block block=state.getBlock();
			return tintIndex==TINTED?((IColoredBlock)block).getColor(block.getMetaFromState(state)):-1;
		},blocks);
		mc.getItemColors().registerItemColorHandler((stack,tintIndex)->
		{
			Block block=Block.getBlockFromItem(stack.getItem());
			return tintIndex==TINTED?((IColoredBlock)block).getColor(stack.getMetadata()):-1;
		},blocks);
		mc.getItemColors().registerItemColorHandler((stack,tintIndex)->
		{
			return tintIndex==TINTED?((MDItemDye)stack.getItem()).getColor(stack.getMetadata()):-1;
		},MDItem.dye);
		// The blocks that keep their color in a tile entity; a moving dyed piston carries it in its moving block.
		// The flower pot has no item; only its plant is tinted. Beds, banners and shulker boxes are drawn by their
		// renderers, and only their breaking particles take the color from here.
		Block[] tileColored=MDBlock.TILE_COLORED.toArray(new Block[0]);
		Block[] extra={MDBlock.flowerPot,MDBlock.bed,MDBlock.standingBanner,MDBlock.wallBanner,MDBlock.shulkerBox};
		Block[] tileColoredBlocks=Arrays.copyOf(tileColored,tileColored.length+extra.length);
		System.arraycopy(extra,0,tileColoredBlocks,tileColored.length,extra.length);
		mc.getBlockColors().registerBlockColorHandler((state,world,pos,tintIndex)->
		{
			return tintIndex==TINTED&&world!=null&&pos!=null?TileColor.rgb(TileColor.get(world,pos)):-1;
		},tileColoredBlocks);
		mc.getItemColors().registerItemColorHandler((stack,tintIndex)->
		{
			return tintIndex==TINTED?TileColor.rgb(stack.getMetadata()):-1;
		},tileColored);
		mc.getItemColors().registerItemColorHandler((stack,tintIndex)->
		{
			return tintIndex==TINTED?TileColor.rgb(stack.getMetadata()):-1;
		},MDItem.sign);
	}

	/** Swaps the wool layer of the vanilla sheep renderer for one that knows the mod's colors. */
	public static void registerSheepLayer()
	{
		Render<?> render=Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(EntitySheep.class);
		if(!(render instanceof RenderSheep))
		{
			LogHelper.warn("Another mod replaced the sheep renderer; sheep dyed with More Dyes keep their vanilla look");
			return;
		}
		List<LayerRenderer<EntitySheep>> layers=ObfuscationReflectionHelper.getPrivateValue(RenderLivingBase.class,(RenderLivingBase<?>)render,"field_177097_h");
		for(int i=0;i<layers.size();i++)
		{
			if(layers.get(i) instanceof LayerSheepWool)
			{
				layers.set(i,new LayerDyedSheepWool((RenderSheep)render));
			}
		}
	}
	/** Swaps the collar layer of the vanilla wolf renderer for one that knows the mod's colors. */
	public static void registerWolfLayer()
	{
		Render<?> render=Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(EntityWolf.class);
		if(!(render instanceof RenderWolf))
		{
			LogHelper.warn("Another mod replaced the wolf renderer; collars dyed with More Dyes keep their vanilla look");
			return;
		}
		List<LayerRenderer<EntityWolf>> layers=ObfuscationReflectionHelper.getPrivateValue(RenderLivingBase.class,(RenderLivingBase<?>)render,"field_177097_h");
		for(int i=0;i<layers.size();i++)
		{
			if(layers.get(i) instanceof LayerWolfCollar)
			{
				layers.set(i,new LayerDyedWolfCollar((RenderWolf)render));
			}
		}
	}
	/** Records the color the server sent for a sheep or a wolf's collar; the wool and collar layers read it from there. */
	public static void setSheepColor(final int entityId,final int color)
	{
		final Minecraft mc=Minecraft.getMinecraft();
		mc.addScheduledTask(()->
		{
			if(mc.world!=null)
			{
				Entity entity=mc.world.getEntityByID(entityId);
				if(entity instanceof EntitySheep)
				{
					DyedSheepHandler.storeColor((EntitySheep)entity,color);
				}
				else if(entity instanceof EntityWolf)
				{
					DyedCollarHandler.storeColor((EntityWolf)entity,color);
				}
			}
		});
	}

	/**
	 * Chests, beds, banners and shulker boxes are drawn by a renderer instead of a model, both in the world and as
	 * items; signs in the world.
	 */
	public static void registerChestRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDyedChest.class,new DyedChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDyedSign.class,new DyedSignRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDyedBed.class,new DyedBedRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDyedBanner.class,new DyedBannerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDyedShulkerBox.class,new DyedShulkerBoxRenderer());
		DyedChestItemRenderer itemRenderer=new DyedChestItemRenderer();
		for(Block chest:MDBlock.chest)
		{
			Item.getItemFromBlock(chest).setTileEntityItemStackRenderer(itemRenderer);
		}
		DyedFurnishingItemRenderer furnishings=new DyedFurnishingItemRenderer();
		MDItem.bed.setTileEntityItemStackRenderer(furnishings);
		MDItem.banner.setTileEntityItemStackRenderer(furnishings);
		Item.getItemFromBlock(MDBlock.shulkerBox).setTileEntityItemStackRenderer(furnishings);
	}

	/** Adds the dyed carpet layer to the vanilla llama renderer. */
	public static void registerLlamaLayer()
	{
		Render<?> render=Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(EntityLlama.class);
		if(!(render instanceof RenderLlama))
		{
			LogHelper.warn("Another mod replaced the llama renderer; llamas do not show dyed carpets");
			return;
		}
		((RenderLlama)render).addLayer(new LayerDyedLlamaDecor((RenderLlama)render));
	}
	/** Records the color the server sent for a llama's dyed carpet; the carpet layer reads it from there. */
	public static void setLlamaCarpet(final int entityId,final int color)
	{
		final Minecraft mc=Minecraft.getMinecraft();
		mc.addScheduledTask(()->
		{
			if(mc.world!=null)
			{
				Entity entity=mc.world.getEntityByID(entityId);
				if(entity instanceof EntityLlama)
				{
					LlamaCarpets.storeClientColor((EntityLlama)entity,color);
				}
			}
		});
	}
}
