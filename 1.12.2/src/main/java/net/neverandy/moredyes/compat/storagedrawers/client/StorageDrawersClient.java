package net.neverandy.moredyes.compat.storagedrawers.client;

import java.util.ArrayList;
import java.util.List;

import com.jaquadro.minecraft.chameleon.Chameleon;
import com.jaquadro.minecraft.chameleon.model.CachedBuilderModel;
import com.jaquadro.minecraft.chameleon.resources.register.IBlockModelRegister;
import com.jaquadro.minecraft.storagedrawers.api.storage.EnumBasicDrawer;
import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.BlockStandardDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.client.model.BasicDrawerModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.ColorUtil;

/**
 * Client setup for dyed drawers: their models are grey and mark every face with tint index 0, which takes the color
 * of the drawer's material. Storage Drawers' lock, void and other markers are added to the models the way Storage
 * Drawers adds them to its own (BasicDrawerModel), and its label and item renderer finds the drawer slots through
 * the block's status data (initDynamic).
 */
@SideOnly(Side.CLIENT)
public final class StorageDrawersClient
{
	private static final int TINTED=0;

	private StorageDrawersClient(){}

	public static void preInit()
	{
		MinecraftForge.EVENT_BUS.register(StorageDrawersClient.class);
	}

	private static int color(int index)
	{
		return ColorUtil.fromHex(ColorStrings.ALL[index]);
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		StorageDrawersCompat.drawers.initDynamic();
		for(EnumBasicDrawer size:EnumBasicDrawer.values())
		{
			ModelLoader.setCustomModelResourceLocation(StorageDrawersCompat.drawersItem,size.getMetadata(),
					new ModelResourceLocation(Reference.MOD_ID+":dyed_drawers_"+size.getName(),"inventory"));
		}
		Chameleon.instance.modelRegistry.registerModel(new IBlockModelRegister()
		{
			@Override
			public List<IBlockState> getBlockStates()
			{
				List<IBlockState> states=new ArrayList<IBlockState>();
				for(EnumBasicDrawer size:EnumBasicDrawer.values())
				{
					for(EnumFacing facing:EnumFacing.HORIZONTALS)
					{
						states.add(StorageDrawersCompat.drawers.getDefaultState()
								.withProperty(BlockStandardDrawers.BLOCK,size).withProperty(BlockDrawers.FACING,facing));
					}
				}
				return states;
			}
			@Override
			public IBakedModel getModel(IBlockState state,IBakedModel existing)
			{
				return new CachedBuilderModel(new BasicDrawerModel.Model(existing));
			}
		});
	}

	@SubscribeEvent
	public static void onBlockColors(ColorHandlerEvent.Block event)
	{
		event.getBlockColors().registerBlockColorHandler((state,world,pos,tintIndex)->
		{
			if(tintIndex!=TINTED||world==null||pos==null)
			{
				return -1;
			}
			TileEntity tile=world.getTileEntity(pos);
			return color(tile instanceof TileEntityDrawers?StorageDrawersCompat.colorOf((TileEntityDrawers)tile):0);
		},StorageDrawersCompat.drawers);
	}
	@SubscribeEvent
	public static void onItemColors(ColorHandlerEvent.Item event)
	{
		event.getItemColors().registerItemColorHandler((stack,tintIndex)->
				tintIndex==TINTED?color(StorageDrawersCompat.colorOf(stack)):-1,StorageDrawersCompat.drawersItem);
	}
}
