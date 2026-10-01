package net.neverandy.moredyes.client;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.block.BlockColored;
import net.neverandy.moredyes.block.IColoredBlock;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.item.MDItemDye;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityDyedChest;

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
		for(Item dye:MDItem.dye)
		{
			for(int meta=0;meta<((MDItemDye)dye).getShadeCount();meta++)
			{
				ModelLoader.setCustomModelResourceLocation(dye,meta,new ModelResourceLocation(Reference.MOD_ID+":dye","inventory"));
			}
		}
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
	}

	/** Chests are drawn by a renderer instead of a model, both in the world and as items. */
	public static void registerChestRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDyedChest.class,new DyedChestRenderer());
		DyedChestItemRenderer itemRenderer=new DyedChestItemRenderer();
		for(Block chest:MDBlock.chest)
		{
			Item.getItemFromBlock(chest).setTileEntityItemStackRenderer(itemRenderer);
		}
	}
}
