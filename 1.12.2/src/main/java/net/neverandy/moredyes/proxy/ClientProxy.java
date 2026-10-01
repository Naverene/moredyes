package net.neverandy.moredyes.proxy;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.neverandy.moredyes.block.*;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.item.MDItemDye;

public class ClientProxy extends CommonProxy
{
	@SubscribeEvent
	public void initializeModels(ModelRegistryEvent event)
	{
		for(int i=0;i<MDItem.dye.length;i++)
		{
			((MDItemDye)MDItem.dye[i]).initModel();
			((BasicBlock)MDBlock.cobble[i]).initModel(((BasicBlock)MDBlock.cobble[i]).blockName);
			((BasicBlock)MDBlock.coal[i]).initModel(((BasicBlock)MDBlock.coal[i]).blockName);
			((BasicBlock)MDBlock.brick[i]).initModel(((BasicBlock)MDBlock.brick[i]).blockName);
			((BasicBlock)MDBlock.clay[i]).initModel(((BasicBlock)MDBlock.clay[i]).blockName);
			((BasicBlock)MDBlock.wool[i]).initModel(((BasicBlock)MDBlock.wool[i]).blockName);
			((BasicBlock)MDBlock.lapis[i]).initModel(((BasicBlock)MDBlock.lapis[i]).blockName);
			((BasicBlock)MDBlock.obsidian[i]).initModel(((BasicBlock)MDBlock.obsidian[i]).blockName);
			((BasicBlock)MDBlock.plank[i]).initModel(((BasicBlock)MDBlock.plank[i]).blockName);
			((BasicBlock)MDBlock.glowstone[i]).initModel(((BasicBlock)MDBlock.glowstone[i]).blockName);
			((BasicBlock)MDBlock.soulsand[i]).initModel(((BasicBlock)MDBlock.soulsand[i]).blockName);
			((BasicBlock)MDBlock.quartz[i]).initModel(((BasicBlock)MDBlock.quartz[i]).blockName);
			((BasicBlock)MDBlock.stone[i]).initModel(((BasicBlock)MDBlock.stone[i]).blockName);
			((BasicBlock)MDBlock.stonebrick[i]).initModel(((BasicBlock)MDBlock.stonebrick[i]).blockName);
			((BasicBlock)MDBlock.stonebrickCarved[i]).initModel(((BasicBlock)MDBlock.stonebrickCarved[i]).blockName);
			((BasicBlock)MDBlock.stonebrickCracked[i]).initModel(((BasicBlock)MDBlock.stonebrickCracked[i]).blockName);
			((BasicBlock)MDBlock.redstone[i]).initModel(((BasicBlock)MDBlock.redstone[i]).blockName);
			
			((GlassBlock)MDBlock.glass[i]).initModel(((GlassBlock)MDBlock.glass[i]).blockName);
			((GlassBlock)MDBlock.glassFoggy[i]).initModel(((GlassBlock)MDBlock.glassFoggy[i]).blockName);
			
			((FallingBlock)MDBlock.sand[i]).initModel(((FallingBlock)MDBlock.sand[i]).blockName);

			((WorkbenchBlock) MDBlock.workbench[i]).initModel(((WorkbenchBlock)MDBlock.workbench[i]).blockName);
		}
		for(int i=0;i<118;i++)
		{
			((SaplingBlock)MDBlock.sapling[i]).initModel();
			((LogBlock)MDBlock.log[i]).initModel();
			((LeafBlock)MDBlock.leaf[i]).initModel();
			((FlowerBlock)MDBlock.tulip[i]).initModel();
		}
	}

}
