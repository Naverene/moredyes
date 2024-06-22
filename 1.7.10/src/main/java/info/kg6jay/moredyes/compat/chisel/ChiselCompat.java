package info.kg6jay.moredyes.compat.chisel;

import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.block.MDBlockColored;
import info.kg6jay.moredyes.compat.chisel.variations.Variation;
import info.kg6jay.moredyes.handler.ConfigHandler;
import info.kg6jay.moredyes.item.MDItemBlockColored;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.carving.ICarvingGroup;
import com.cricketcraft.chisel.api.carving.ICarvingRegistry;

import cpw.mods.fml.common.registry.GameRegistry;

public class ChiselCompat
{
	public static void registerChisel()
	{
		if(ConfigHandler.chisel_support)
		{
			ICarvingRegistry chisel = CarvingUtils.getChiselRegistry();
			ICarvingGroup[] groups = new ICarvingGroup[]
				{
					new CarvingGroup("blockWool","wool"),
					new CarvingGroup("stone","stone"),
					new CarvingGroup("cobblestone","cobblestone"),
					new CarvingGroup("blockObsidian","obsidian"),
					new CarvingGroup("bricksStone","stoneBrick"),
					new CarvingGroup("bricksStoneCarved","stoneBrickCarved"),
					new CarvingGroup("bricksStoneCracked","stoneBrickCracked"),
					new CarvingGroup("soul_sand","soul_sand"),
					new CarvingGroup("blockQuartz","quartz"),
					new CarvingGroup("blockCoal","blockCoal"),
					new CarvingGroup("blockRedstone","blockRedstone"),
					new CarvingGroup("plankWood", "plankWood"),
					new CarvingGroup("glowstone","glowstone"),
					new CarvingGroup("lapisBlock","lapisBlock"),
					new CarvingGroup("stainedClay","stainedClay")
				};
			for(int i=0;i<MDBlock.wool.length;i++)
			{
				groups[0].addVariation(new Variation(MDBlock.wool[i]));
				groups[1].addVariation(new Variation(MDBlock.stone[i]));
				groups[2].addVariation(new Variation(MDBlock.cobble[i]));
				groups[3].addVariation(new Variation(MDBlock.obsidian[i]));
				groups[4].addVariation(new Variation(MDBlock.stoneBrick[i]));
				groups[5].addVariation(new Variation(MDBlock.stoneBrickCarved[i]));
				groups[6].addVariation(new Variation(MDBlock.stoneBrickCracked[i]));
				groups[7].addVariation(new Variation(MDBlock.soulsand[i]));
				groups[8].addVariation(new Variation(MDBlock.quartz[i]));
				groups[9].addVariation(new Variation(MDBlock.coal[i]));
				groups[10].addVariation(new Variation(MDBlock.redstone[i]));
				if(ConfigHandler.chisel_plank)
					groups[11].addVariation(new Variation(MDBlock.plank[i]));
				groups[12].addVariation(new Variation(MDBlock.glowstone[i]));
				groups[13].addVariation(new Variation(MDBlock.lapis[i]));
				groups[14].addVariation(new Variation(MDBlock.clay[i]));
			}
			for(int i=0;i<groups.length;i++)
			{
				chisel.addGroup(groups[i]);
			}
		}
	}
}