package net.neverandy.moredyes.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import net.neverandy.moredyes.block.IColoredBlock;
import net.neverandy.moredyes.block.ITileColoredBlock;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.compat.GregTechCompat;
import net.neverandy.moredyes.handler.ConfigHandler;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.LogHelper;

/**
 * The recipes of the mod:
 * <ul>
 * <li>dyes are mixed from two vanilla dyes, and a tulip makes one dye of its color;</li>
 * <li>eight vanilla blocks around a dye make eight dyed blocks;</li>
 * <li>a dyed block with a water bucket gives the vanilla block back;</li>
 * <li>dyed blocks turn into each other the way their vanilla blocks do (logs to planks, cobble to stone...).</li>
 * </ul>
 */
@EventBusSubscriber(modid=Reference.MOD_ID)
public class CraftManager
{
	/**
	 * The vanilla dyes in the order the color groups follow. A group holds the mixes of its own dye with each dye
	 * after it, so the shade of a mix is the position of the second dye among those.
	 */
	private static final String[] DYES={"dyeWhite","dyeOrange","dyeMagenta","dyeLightBlue","dyeYellow","dyeLime","dyePink","dyeGray",
			"dyeLightGray","dyeCyan","dyePurple","dyeBlue","dyeBrown","dyeGreen","dyeRed","dyeBlack"};
	private static final int WHITE=0,YELLOW=4,PINK=6,GRAY=7,PURPLE=10,BLUE=11,GREEN=13,RED=14,BLACK=15;

	private static IForgeRegistry<IRecipe> registry;

	/** One crafting table dye mix: {@code count} of each of two vanilla dyes (by ore name) make {@code result}. */
	public static class DyeMix
	{
		public final String first,second;
		public final int count;
		public final ItemStack result;

		DyeMix(String first,String second,int count,ItemStack result)
		{
			this.first=first;
			this.second=second;
			this.count=count;
			this.result=result;
		}
	}
	/** Every dye mix, for the GregTech mixer recipes. */
	public static final List<DyeMix> DYE_MIXES=new ArrayList<DyeMix>();

	// Runs after the other recipes are registered, so the vanilla recipes replaced below exist.
	@SubscribeEvent(priority=EventPriority.LOW)
	public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event)
	{
		registry=event.getRegistry();
		registerDyeMixes();
		List<IRecipe> chests=new ArrayList<IRecipe>();
		List<IRecipe> workbenches=new ArrayList<IRecipe>();
		List<IRecipe> bookshelves=new ArrayList<IRecipe>();
		for(int i=0;i<ColorStrings.ALL.length;i++)
		{
			registerColor(i,chests,workbenches,bookshelves);
		}
		registerWashing();
		guard("chest",chests);
		guard("crafting_table",workbenches);
		guard("bookshelf",bookshelves);
		registry=null;
		if(ConfigHandler.gregtechRecipes&&Loader.isModLoaded(GregTechCompat.GREGTECH))
		{
			try
			{
				GregTechCompat.registerRecipes();
			}
			catch(LinkageError e)
			{
				// Another GregTech than the one this was written against.
				LogHelper.warn("This version of GregTech is not supported, so no machine recipes were added for the dyed blocks: "+e);
			}
		}
	}

	private static void registerDyeMixes()
	{
		DYE_MIXES.clear();
		for(int group=0;group<ColorStrings.GROUPS.length;group++)
		{
			int shade=0;
			for(int other=group+1;other<DYES.length;other++)
			{
				// White with gray or black are the vanilla light gray and gray, so those two have no shade.
				if(group==WHITE&&(other==GRAY||other==BLACK))
				{
					continue;
				}
				ItemStack result=new ItemStack(MDItem.dye[group],2,shade);
				String name="dye/"+ColorStrings.GROUPS[group][shade];
				if(isVanillaMix(group,other))
				{
					// One of each already makes a vanilla dye, so these take two of each and give four.
					result.setCount(4);
					shapeless(name,"dye",result,DYES[group],DYES[group],DYES[other],DYES[other]);
					DYE_MIXES.add(new DyeMix(DYES[group],DYES[other],2,result));
				}
				else
				{
					shapeless(name,"dye",result,DYES[group],DYES[other]);
					DYE_MIXES.add(new DyeMix(DYES[group],DYES[other],1,result));
				}
				shade++;
			}
		}
	}
	/** True for the pairs of dyes that vanilla already mixes into one of its own dyes. */
	private static boolean isVanillaMix(int first,int second)
	{
		return first==WHITE&&(second==RED||second==GREEN||second==BLUE)
				||first==YELLOW&&second==RED
				||first==PINK&&second==PURPLE
				||first==BLUE&&(second==GREEN||second==RED);
	}

	private static void registerColor(int index,List<IRecipe> chests,List<IRecipe> workbenches,List<IRecipe> bookshelves)
	{
		int g=ColorStrings.groupOf(index);
		int s=ColorStrings.shadeOf(index);
		String color=ColorStrings.ALL[index];
		ItemStack dye=new ItemStack(MDItem.dye[g],1,s);
		ItemStack plank=new ItemStack(MDBlock.plank[g],1,s);

		shapeless("dye_from_tulip/"+color,"dye_from_tulip",dye.copy(),new ItemStack(MDBlock.tulip[g],1,s));

		// Eight blocks around a dye. The ore names also take dyed blocks, so a block can be dyed again.
		dyeing(MDBlock.wool,g,s,"wool");
		dyeing(MDBlock.stone,g,s,"stone");
		dyeing(MDBlock.cobble,g,s,"cobblestone");
		dyeing(MDBlock.stonebrick,g,s,"bricksStone");
		dyeing(MDBlock.stonebrickCracked,g,s,"bricksStoneCracked");
		dyeing(MDBlock.stonebrickCarved,g,s,"bricksStoneCarved");
		dyeing(MDBlock.diorite,g,s,"stoneDiorite");
		dyeing(MDBlock.obsidian,g,s,"obsidian");
		dyeing(MDBlock.soulsand,g,s,"soulsand");
		dyeing(MDBlock.quartz,g,s,"blockQuartz");
		dyeing(MDBlock.clay,g,s,"stainedClay");
		dyeing(MDBlock.hardenedClay,g,s,"hardenedClay");
		dyeing(MDBlock.coal,g,s,"blockCoal");
		dyeing(MDBlock.glowstone,g,s,"glowstone");
		dyeing(MDBlock.lapis,g,s,"blockLapis");
		dyeing(MDBlock.redstone,g,s,"blockRedstone");
		dyeing(MDBlock.brick,g,s,"blockBrick");
		dyeing(MDBlock.sand,g,s,"sand");
		dyeing(MDBlock.sandstone,g,s,"sandstone");
		dyeing(MDBlock.glass,g,s,"blockGlassColorless");
		dyeing(MDBlock.glassPane,g,s,"paneGlassColorless");
		dyeing(MDBlock.bookshelf,g,s,"bookshelf");
		dyeing(MDBlock.plank,g,s,"plankWood");
		if(MDBlock.rockwool!=null)
		{
			dyeing(MDBlock.rockwool,g,s,"blockRockwool");
		}

		// One at a time
		shapeless("dyeing/sapling_"+color,"sapling",new ItemStack(MDBlock.sapling[g],1,s),dye,"treeSapling");
		shapeless("dyeing/chest_"+color,"chest",new ItemStack(MDBlock.chest[index]),dye,"chestWood");
		shapeless("dyeing/workbench_"+color,"workbench",new ItemStack(MDBlock.workbench[g],1,s),dye,"workbench");

		// Like the vanilla blocks
		shapeless("plank/"+color,"plank",new ItemStack(MDBlock.plank[g],4,s),new ItemStack(MDBlock.log[index]));
		shaped("stonebrick/"+color,"stonebrick",new ItemStack(MDBlock.stonebrick[g],4,s),"SS","SS",'S',new ItemStack(MDBlock.stone[g],1,s));
		shaped("sandstone/"+color,"sandstone",new ItemStack(MDBlock.sandstone[g],1,s),"SS","SS",'S',new ItemStack(MDBlock.sand[g],1,s));
		shaped("glass_pane/"+color,"glass_pane",new ItemStack(MDBlock.glassPane[g],16,s),"GGG","GGG",'G',new ItemStack(MDBlock.glass[g],1,s));
		shaped("glass_foggy_pane/"+color,"glass_foggy_pane",new ItemStack(MDBlock.glassFoggyPane[g],16,s),"GGG","GGG",'G',new ItemStack(MDBlock.glassFoggy[g],1,s));
		shapeless("redstone/"+color,"redstone",new ItemStack(Items.REDSTONE,9),new ItemStack(MDBlock.redstone[g],1,s));
		shapeless("lapis/"+color,"lapis",new ItemStack(Items.DYE,9,4),new ItemStack(MDBlock.lapis[g],1,s));
		shapeless("coal/"+color,"coal",new ItemStack(Items.COAL,9),new ItemStack(MDBlock.coal[g],1,s));

		// The vanilla recipes in dyed planks of one shade give the dyed block
		chests.add(shaped("chest/"+color,"chest",new ItemStack(MDBlock.chest[index]),"PPP","P P","PPP",'P',plank));
		workbenches.add(shaped("workbench/"+color,"workbench",new ItemStack(MDBlock.workbench[g],1,s),"PP","PP",'P',plank));
		bookshelves.add(shaped("bookshelf/"+color,"bookshelf",new ItemStack(MDBlock.bookshelf[g],1,s),"PPP","BBB","PPP",'P',plank,'B',Items.BOOK));

		smelting(new ItemStack(MDBlock.log[index]),new ItemStack(Items.COAL,1,1),0.15F);
		smelting(new ItemStack(MDBlock.cobble[g],1,s),new ItemStack(MDBlock.stone[g],1,s),0.1F);
		smelting(new ItemStack(MDBlock.stonebrick[g],1,s),new ItemStack(MDBlock.stonebrickCracked[g],1,s),0.1F);
		smelting(new ItemStack(MDBlock.sand[g],1,s),new ItemStack(MDBlock.glass[g],1,s),0.1F);
		smelting(new ItemStack(MDBlock.glass[g],1,s),new ItemStack(MDBlock.glassFoggy[g],1,s),0.1F);
		smelting(new ItemStack(MDBlock.clay[g],1,s),new ItemStack(MDBlock.hardenedClay[g],1,s),0.1F);

		registerNewerBlocks(index,g,s,dye);
	}
	/**
	 * The blocks from newer Minecraft versions, as in 1.7.10. Those with a vanilla 1.12 block are dyed like the other
	 * blocks: eight around a dye. Where a block has a vanilla recipe made from blocks this mod dyes, the same recipe
	 * works with dyed blocks of one shade (four dyed granite make four dyed polished granite). The rest have recipes of
	 * their own, noted below.
	 */
	private static void registerNewerBlocks(int index,int g,int s,ItemStack dye)
	{
		String color=ColorStrings.ALL[index];
		dyeing(MDBlock.granite,g,s,"stoneGranite");
		dyeing(MDBlock.andesite,g,s,"stoneAndesite");
		dyeing(MDBlock.polishedGranite,g,s,"stoneGranitePolished");
		dyeing(MDBlock.polishedDiorite,g,s,"stoneDioritePolished");
		dyeing(MDBlock.polishedAndesite,g,s,"stoneAndesitePolished");
		dyeing(MDBlock.concrete,g,s,new ItemStack(Blocks.CONCRETE,1,OreDictionary.WILDCARD_VALUE));
		dyeing(MDBlock.concretePowder,g,s,new ItemStack(Blocks.CONCRETE_POWDER,1,OreDictionary.WILDCARD_VALUE));
		dyeing(MDBlock.mossyCobble,g,s,new ItemStack(Blocks.MOSSY_COBBLESTONE));
		dyeing(MDBlock.mossyStonebrick,g,s,new ItemStack(Blocks.STONEBRICK,1,1));
		dyeing(MDBlock.netherBrick,g,s,new ItemStack(Blocks.NETHER_BRICK));
		dyeing(MDBlock.cutSandstone,g,s,new ItemStack(Blocks.SANDSTONE,1,2));
		dyeing(MDBlock.endStoneBrick,g,s,new ItemStack(Blocks.END_BRICKS));
		dyeing(MDBlock.boneBlock,g,s,new ItemStack(Blocks.BONE_BLOCK));

		square("polished_granite/"+color,MDBlock.polishedGranite,MDBlock.granite,g,s);
		square("polished_diorite/"+color,MDBlock.polishedDiorite,MDBlock.diorite,g,s);
		square("polished_andesite/"+color,MDBlock.polishedAndesite,MDBlock.andesite,g,s);
		square("polished_basalt/"+color,MDBlock.polishedBasalt,MDBlock.basalt,g,s);
		square("cut_sandstone/"+color,MDBlock.cutSandstone,MDBlock.sandstone,g,s);

		// Concrete powder: four sand, four gravel and a dye, like vanilla
		shapeless("concrete_powder/"+color,"concrete_powder",new ItemStack(MDBlock.concretePowder[g],8,s),dye,
				"sand","sand","sand","sand",Blocks.GRAVEL,Blocks.GRAVEL,Blocks.GRAVEL,Blocks.GRAVEL);
		// Soul soil (not in 1.12): dyed soul sand and dirt
		shapeless("soul_soil/"+color,"soul_soil",new ItemStack(MDBlock.soulSoil[g],2,s),new ItemStack(MDBlock.soulsand[g],1,s),
				new ItemStack(Blocks.DIRT,1,0));
		// Chiseled nether bricks (not in 1.12): two dyed nether bricks on top of each other
		shaped("chiseled_nether_brick/"+color,"chiseled_nether_brick",new ItemStack(MDBlock.chiseledNetherBrick[g],1,s),"N","N",
				'N',new ItemStack(MDBlock.netherBrick[g],1,s));
		// A dyed bone block gives its bone meal back, like vanilla
		shapeless("bone_meal/"+color,"bone_meal",new ItemStack(Items.DYE,9,15),new ItemStack(MDBlock.boneBlock[g],1,s));
		// Crying obsidian (not in 1.12): eight dyed obsidian around a ghast tear
		shaped("crying_obsidian/"+color,"crying_obsidian",new ItemStack(MDBlock.cryingObsidian[g],8,s),"OOO","OTO","OOO",
				'O',new ItemStack(MDBlock.obsidian[g],1,s),'T',Items.GHAST_TEAR);
		// Netherite (not in 1.12): obsidian, gold and a dye
		shaped("netherite_block/"+color,"netherite_block",new ItemStack(MDBlock.netheriteBlock[g],4,s),"OGO","GDG","OGO",
				'O',"obsidian",'G',"ingotGold",'D',dye);
		// Chain (not in 1.12): two iron ingots and a dye
		shaped("chain/"+color,"chain",new ItemStack(MDBlock.chain[g],4,s),"I","D","I",'I',"ingotIron",'D',dye);
		shapeless("dye_from_cornflower/"+color,"dye_from_cornflower",dye.copy(),new ItemStack(MDBlock.cornflower[g],1,s));

		// Iron trapdoor and pistons: the vanilla one and a dye, and a dyed piston with a slimeball on top
		ItemStack piston=new ItemStack(MDBlock.piston,1,index);
		ItemStack stickyPiston=new ItemStack(MDBlock.stickyPiston,1,index);
		shapeless("dyeing/iron_trapdoor_"+color,"iron_trapdoor",new ItemStack(MDBlock.ironTrapdoor,1,index),Blocks.IRON_TRAPDOOR,dye);
		shapeless("dyeing/piston_"+color,"piston",piston,Blocks.PISTON,dye);
		shapeless("dyeing/sticky_piston_"+color,"sticky_piston",stickyPiston,Blocks.STICKY_PISTON,dye);
		shaped("sticky_piston/"+color,"sticky_piston",stickyPiston.copy(),"S","P",'S',"slimeball",'P',piston);

		smelting(new ItemStack(MDBlock.stone[g],1,s),new ItemStack(MDBlock.smoothStone[g],1,s),0.1F);
		smelting(new ItemStack(MDBlock.sandstone[g],1,s),new ItemStack(MDBlock.smoothSandstone[g],1,s),0.1F);
		smelting(new ItemStack(MDBlock.quartz[g],1,s),new ItemStack(MDBlock.smoothQuartz[g],1,s),0.1F);
		smelting(new ItemStack(MDBlock.netherBrick[g],1,s),new ItemStack(MDBlock.crackedNetherBrick[g],1,s),0.1F);
		smelting(new ItemStack(MDBlock.hardenedClay[g],1,s),new ItemStack(MDBlock.glazedTerracotta[g],1,s),0.1F);
		// Basalt forms from lava over soul soil in 1.16; here it is smelted from dyed soul soil, as in 1.7.10.
		smelting(new ItemStack(MDBlock.soulSoil[g],1,s),new ItemStack(MDBlock.basalt[g],1,s),0.1F);

		// Stairs, slabs and walls from dyed blocks of one shade, with the vanilla shapes and amounts
		for(MDBlock.Shape shape:MDBlock.SHAPES)
		{
			ItemStack base=new ItemStack(shape.base[g],1,s);
			String name=((ITileColoredBlock)shape.block).getTypeName();
			switch(shape.kind)
			{
			case STAIRS:
				shaped(name+"/"+color,name,new ItemStack(shape.block,4,index),"S  ","SS ","SSS",'S',base);
				break;
			case SLAB:
				shaped(name+"/"+color,name,new ItemStack(shape.block,6,index),"SSS",'S',base);
				break;
			case WALL:
				shaped(name+"/"+color,name,new ItemStack(shape.block,6,index),"SSS","SSS",'S',base);
				break;
			}
		}
	}
	/** Four dyed blocks of one shade in a square make four of another dyed block, like vanilla polished stone. */
	private static void square(String name,Block[] output,Block[] input,int group,int shade)
	{
		shaped(name,name.substring(0,name.indexOf('/')),new ItemStack(output[group],4,shade),"SS","SS",'S',new ItemStack(input[group],1,shade));
	}
	/** Eight of the given blocks (an ore name or a stack) around a dye make eight dyed blocks of its shade. */
	private static void dyeing(Block[] dyed,int group,int shade,Object ore)
	{
		String type=((IColoredBlock)dyed[group]).getTypeName();
		shaped("dyeing/"+type+"_"+ColorStrings.GROUPS[group][shade],type,new ItemStack(dyed[group],8,shade),
				"SSS","SDS","SSS",'S',ore,'D',new ItemStack(MDItem.dye[group],1,shade));
	}
	/** A dyed block (any shade) and a water bucket give the vanilla block back; the bucket is returned empty. */
	private static void registerWashing()
	{
		for(Map.Entry<Block,ItemStack> entry:MDBlock.WASHED.entrySet())
		{
			Block dyed=entry.getKey();
			shapeless("washing/"+dyed.getRegistryName().getResourcePath(),"washing",entry.getValue().copy(),
					new ItemStack(dyed,1,OreDictionary.WILDCARD_VALUE),Items.WATER_BUCKET);
		}
	}
	/** Makes a vanilla recipe step aside for the mod's recipes that use the same shape. */
	private static void guard(String vanillaName,List<IRecipe> preferred)
	{
		IRecipe original=registry.getValue(new ResourceLocation("minecraft",vanillaName));
		if(original!=null&&original.getClass()==ShapedRecipes.class)
		{
			registry.register(new GuardedShapedRecipe((ShapedRecipes)original,preferred));
		}
		else
		{
			LogHelper.warn("The vanilla recipe "+vanillaName+" was changed by another mod; dyed planks of one shade may give its result");
		}
	}

	private static IRecipe shaped(String name,String group,ItemStack result,Object... recipe)
	{
		IRecipe r=new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,group),result,recipe).setRegistryName(Reference.MOD_ID,name);
		registry.register(r);
		return r;
	}
	private static IRecipe shapeless(String name,String group,ItemStack result,Object... recipe)
	{
		IRecipe r=new ShapelessOreRecipe(new ResourceLocation(Reference.MOD_ID,group),result,recipe).setRegistryName(Reference.MOD_ID,name);
		registry.register(r);
		return r;
	}
	private static void smelting(ItemStack input,ItemStack output,float xp)
	{
		GameRegistry.addSmelting(input,output,xp);
	}
}
