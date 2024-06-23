package info.kg6jay.moredyes.recipe;

import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.block.MDBlockColored;
import info.kg6jay.moredyes.block.MDBlockColoredPowered;
import info.kg6jay.moredyes.item.MDItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class CraftManager
{
	public static void addCraftingRecipes()
	{
		ItemStack dyeWhite=new ItemStack(Items.dye,1,15);
		ItemStack dyeOrange=new ItemStack(Items.dye,1,14);
		ItemStack dyeMagenta=new ItemStack(Items.dye,1,13);
		ItemStack dyeLightBlue=new ItemStack(Items.dye,1,12);
		ItemStack dyeYellow=new ItemStack(Items.dye,1,11);
		ItemStack dyeLime=new ItemStack(Items.dye,1,10);
		ItemStack dyePink=new ItemStack(Items.dye,1,9);
		ItemStack dyeGray=new ItemStack(Items.dye,1,8);
		ItemStack dyeLightGray=new ItemStack(Items.dye,1,7);
		ItemStack dyeCyan=new ItemStack(Items.dye,1,6);
		ItemStack dyePurple=new ItemStack(Items.dye,1,5);
		ItemStack dyeLapis=new ItemStack(Items.dye,1,4);
		ItemStack dyeBrown=new ItemStack(Items.dye,1,3);
		ItemStack dyeGreen=new ItemStack(Items.dye,1,2);
		ItemStack dyeRed=new ItemStack(Items.dye,1,1);
		ItemStack dyeBlack=new ItemStack(Items.dye,1,0);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,0),dyeWhite,dyeOrange);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,1),dyeWhite,dyeMagenta);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,2),dyeWhite,dyeLightBlue);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,3),dyeWhite,dyeYellow);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,4),dyeWhite,dyeLime);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,5),dyeWhite,dyePink);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,6),dyeWhite,dyeLightGray);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,7),dyeWhite,dyeCyan);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,8),dyeWhite,dyePurple);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,9),dyeWhite,dyeLapis);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,10),dyeWhite,dyeBrown);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,11),dyeWhite,dyeGreen);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],4,12),dyeWhite,dyeWhite,dyeRed,dyeRed);

		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,0),dyeOrange,dyeMagenta);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,1),dyeOrange,dyeLightBlue);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,2),dyeOrange,dyeYellow);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,3),dyeOrange,dyeLime);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,4),dyeOrange,dyePink);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,5),dyeOrange,dyeGray);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,6),dyeOrange,dyeLightGray);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,7),dyeOrange,dyeCyan);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,8),dyeOrange,dyePurple);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,9),dyeOrange,dyeLapis);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,10),dyeOrange,dyeBrown);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,11),dyeOrange,dyeGreen);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,12),dyeOrange,dyeRed);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,13),dyeOrange,dyeBlack);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,0),dyeMagenta,dyeLightBlue);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,1),dyeMagenta,dyeYellow);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,2),dyeMagenta,dyeLime);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,3),dyeMagenta,dyePink);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,4),dyeMagenta,dyeGray);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,5),dyeMagenta,dyeLightGray);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,6),dyeMagenta,dyeCyan);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,7),dyeMagenta,dyePurple);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,8),dyeMagenta,dyeLapis);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,9),dyeMagenta,dyeBrown);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,10),dyeMagenta,dyeGreen);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,11),dyeMagenta,dyeRed);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,12),dyeMagenta,dyeBlack);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,0),dyeLightBlue,dyeYellow);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,1),dyeLightBlue,dyeLime);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,2),dyeLightBlue,dyePink);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,3),dyeLightBlue,dyeGray);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,4),dyeLightBlue,dyeLightGray);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,5),dyeLightBlue,dyeCyan);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,6),dyeLightBlue,dyePurple);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,7),dyeLightBlue,dyeLapis);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,8),dyeLightBlue,dyeBrown);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,9),dyeLightBlue,dyeGreen);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,10),dyeLightBlue,dyeRed);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,11),dyeLightBlue,dyeBlack);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,0),dyeYellow,dyeLime);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,1),dyeYellow,dyePink);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,2),dyeYellow,dyeGray);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,3),dyeYellow,dyeLightGray);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,4),dyeYellow,dyeCyan);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,5),dyeYellow,dyePurple);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,6),dyeYellow,dyeLapis);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,7),dyeYellow,dyeBrown);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,8),dyeYellow,dyeGreen);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,9),dyeYellow,dyeRed);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,10),dyeYellow,dyeBlack);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,0),dyeLime,dyePink);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,1),dyeLime,dyeGray);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,2),dyeLime,dyeLightGray);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,3),dyeLime,dyeCyan);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,4),dyeLime,dyePurple);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,5),dyeLime,dyeLapis);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,6),dyeLime,dyeBrown);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,7),dyeLime,dyeGreen);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,8),dyeLime,dyeRed);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,9),dyeLime,dyeBlack);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,0),dyePink,dyeGray);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,1),dyePink,dyeLightGray);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,2),dyePink,dyeCyan);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,3),dyePink,dyePurple);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,4),dyePink,dyeLapis);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,5),dyePink,dyeBrown);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,6),dyePink,dyeGreen);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,7),dyePink,dyeRed);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,8),dyePink,dyeBlack);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[7],2,0),dyeGray,dyeLightGray);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[7],2,1),dyeGray,dyeCyan);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[7],2,2),dyeGray,dyePurple);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[7],2,3),dyeGray,dyeLapis);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[7],2,4),dyeGray,dyeBrown);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[7],2,5),dyeGray,dyeGreen);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[7],2,6),dyeGray,dyeRed);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[7],2,7),dyeGray,dyeBlack);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[8],2,0),dyeLightGray,dyeCyan);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[8],2,1),dyeLightGray,dyePurple);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[8],2,2),dyeLightGray,dyeLapis);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[8],2,3),dyeLightGray,dyeBrown);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[8],2,4),dyeLightGray,dyeGreen);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[8],2,5),dyeLightGray,dyeRed);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[8],2,6),dyeLightGray,dyeBlack);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[9],2,0),dyeCyan,dyePurple);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[9],2,1),dyeCyan,dyeLapis);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[9],2,2),dyeCyan,dyeBrown);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[9],2,3),dyeCyan,dyeGreen);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[9],2,4),dyeCyan,dyeRed);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[9],2,5),dyeCyan,dyeBlack);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[10],2,0),dyePurple,dyeLapis);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[10],2,1),dyePurple,dyeBrown);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[10],2,2),dyePurple,dyeGreen);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[10],2,3),dyePurple,dyeRed);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[10],2,4),dyePurple,dyeBlack);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[11],2,0),dyeLapis,dyeBrown);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[11],2,1),dyeLapis,dyeGreen);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[11],2,2),dyeLapis,dyeRed);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[11],2,3),dyeLapis,dyeBlack);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[12],2,0),dyeBrown,dyeGreen);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[12],2,1),dyeBrown,dyeRed);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[12],2,2),dyeBrown,dyeBlack);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[13],2,0),dyeGreen,dyeRed);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[13],2,1),dyeGreen,dyeBlack);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[14],2,0),dyeRed,dyeBlack);
		
		//TODO: Add recipes for all the rest of the dyes.
		
		
		for(int a=0;a<MDBlock.wool.length;a++)
		{
			for(int i=0;i<=((MDBlockColored)MDBlock.wool[a]).getMaxMeta();i++)
			{
				CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(MDBlock.wool[a],1,i),new ItemStack(MDItem.dye[a],1,i),"blockWool"));
				GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[a],1,i),new ItemStack(MDBlock.tulip[a],1,i));

				GameRegistry.addShapedRecipe(new ItemStack(MDBlock.stoneBrick[a],4,i), "SS","SS",'S',new ItemStack(MDBlock.stone[a],1,i));
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.stoneBrick[a],8,i),"SSS","SDS","SSS",'S',Blocks.stonebrick,'D',new ItemStack(MDItem.dye[a],1,i)));
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.stone[a],8,i),"SSS","SDS","SSS",'S',"stone",'D',new ItemStack(MDItem.dye[a],1,i)));
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.cobble[a],8,i),"SSS","SDS","SSS",'S',"cobblestone",'D',new ItemStack(MDItem.dye[a],1,i)));
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.redstone[a],8,i),"SSS","SDS","SSS",'S',"blockRedstone",'D',new ItemStack(MDItem.dye[a],1,i)));
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.lapis[a],8,i),"SSS","SDS","SSS",'S',Blocks.lapis_block,'D',new ItemStack(MDItem.dye[a],1,i)));
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.glowstone[a],8,i),"SSS","SDS","SSS",'S',Blocks.glowstone,'D',new ItemStack(MDItem.dye[a],1,i)));
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.clay[a],8,i),"SSS","SDS","SSS",'S',Blocks.hardened_clay,'D',new ItemStack(MDItem.dye[a],1,i)));
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.clay[a],8,i),"SSS","SDS","SSS",'S',"stainedClay",'D',new ItemStack(MDItem.dye[a],1,i)));
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.clay[a],8,i),"SSS","SDS","SSS",'S',Blocks.stained_hardened_clay,'D',new ItemStack(MDItem.dye[a],1,i)));
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.obsidian[a],8,i),"SSS","SDS","SSS",'S',Blocks.obsidian,'D',new ItemStack(MDItem.dye[a],1,i)));
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.soulsand[a],8,i),"SSS","SDS","SSS",'S',Blocks.soul_sand,'D',new ItemStack(MDItem.dye[a],1,i)));
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.quartz[a],8,i),"SSS","SDS","SSS",'S',"blockQuartz",'D',new ItemStack(MDItem.dye[a],1,i)));
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.coal[a],8,i),"SSS","SDS","SSS",'S',"blockCoal",'D',new ItemStack(MDItem.dye[a],1,i)));
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.plank[a],8,i),"SSS","SDS","SSS",'S',"plankWood",'D',new ItemStack(MDItem.dye[a],1,i)));
				GameRegistry.addShapelessRecipe(new ItemStack(MDBlock.sapling[a],1,i),new ItemStack(MDItem.dye[a],1,i),new ItemStack(Blocks.sapling,1,0));
				GameRegistry.addShapelessRecipe(new ItemStack(MDBlock.plank[a],4,i),new ItemStack(MDBlock.log[a],1,i));
				GameRegistry.addShapedRecipe(new ItemStack(MDBlock.glassClear[a],8,i),"GGG","GDG","GGG",'G',new ItemStack(Blocks.glass),'D',new ItemStack(MDItem.dye[a],1,i));
				GameRegistry.addShapelessRecipe(new ItemStack(Items.redstone,9),new ItemStack(MDBlock.redstone[a],1,i));
				GameRegistry.addShapelessRecipe(new ItemStack(Items.dye,9,4),new ItemStack(MDBlock.lapis[a],1,i));
				GameRegistry.addShapelessRecipe(new ItemStack(Blocks.glass,1), new ItemStack(MDBlock.glassClear[a],1,i),new ItemStack(Items.water_bucket));
				GameRegistry.addShapedRecipe(new ItemStack(MDBlock.stoneBrickCarved[a],8,i),"SSS","SDS","SSS",'S',new ItemStack(Blocks.stonebrick,1,3),'D',new ItemStack(MDItem.dye[a],1,i));
				GameRegistry.addShapedRecipe(new ItemStack(MDBlock.sand[a],8,i), "SSS","SDS","SSS",'S',new ItemStack(Blocks.sand),'D',new ItemStack(MDItem.dye[a],1,i));
				GameRegistry.addShapedRecipe(new ItemStack(MDBlock.brick[a],8,i), "SSS","SDS","SSS",'S',new ItemStack(Blocks.brick_block),'D',new ItemStack(MDItem.dye[a],1,i));
				GameRegistry.addShapedRecipe(new ItemStack(MDBlock.hardenedClay[a],8,i), "SSS","SDS","SSS",'S',new ItemStack(Blocks.hardened_clay),'D',new ItemStack(MDItem.dye[a],1,i));

				//Cleaning Recipes
				GameRegistry.addShapelessRecipe(new ItemStack(Blocks.sand,1,0),new ItemStack(MDBlock.sand[a],1,i),new ItemStack(Items.water_bucket));
				GameRegistry.addShapelessRecipe(new ItemStack(Blocks.stone,1,0),new ItemStack(MDBlock.stone[a],1,i),new ItemStack(Items.water_bucket));
				GameRegistry.addShapelessRecipe(new ItemStack(Blocks.cobblestone,1,0),new ItemStack(MDBlock.cobble[a],1,i),new ItemStack(Items.water_bucket));
				GameRegistry.addShapelessRecipe(new ItemStack(Blocks.wool,1,0),new ItemStack(MDBlock.wool[a],1,i),new ItemStack(Items.water_bucket));
				GameRegistry.addShapelessRecipe(new ItemStack(Blocks.planks,1,0),new ItemStack(MDBlock.plank[a],1,i),new ItemStack(Items.water_bucket));
				GameRegistry.addShapelessRecipe(new ItemStack(Blocks.glowstone,1,0),new ItemStack(MDBlock.glowstone[a],1,i),new ItemStack(Items.water_bucket));
				GameRegistry.addShapelessRecipe(new ItemStack(Blocks.obsidian,1,0),new ItemStack(MDBlock.obsidian[a],1,i),new ItemStack(Items.water_bucket));
				GameRegistry.addShapelessRecipe(new ItemStack(Blocks.stonebrick,1,0),new ItemStack(MDBlock.stoneBrick[a],1,i),new ItemStack(Items.water_bucket));
				GameRegistry.addShapelessRecipe(new ItemStack(Blocks.soul_sand,1,0),new ItemStack(MDBlock.soulsand[a],1,i),new ItemStack(Items.water_bucket));
				GameRegistry.addShapelessRecipe(new ItemStack(Blocks.quartz_block,1,0),new ItemStack(MDBlock.quartz[a],1,i),new ItemStack(Items.water_bucket));
				GameRegistry.addShapelessRecipe(new ItemStack(Blocks.hardened_clay,1,0),new ItemStack(MDBlock.hardenedClay[a],1,i),new ItemStack(Items.water_bucket));
				
			}
		}
		//CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Blocks.stonebrick,1,0),"bricksStone",new ItemStack(Items.water_bucket)));
		//CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Blocks.stonebrick,1,3),"bricksStoneCarved",new ItemStack(Items.water_bucket)));
		//CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Blocks.stonebrick,1,2),"bricksStoneCracked",new ItemStack(Items.water_bucket)))
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Blocks.quartz_block,1),"blockQuartz",new ItemStack(Items.water_bucket)));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Blocks.hardened_clay,1),"stainedClay",new ItemStack(Items.water_bucket)));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Blocks.log,1,0),"logWood",new ItemStack(Items.water_bucket)));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Items.coal,9,0),"blockCoal"));


	}
	public static void addSmeltingRecipes()
	{
		for(int a=0;a<MDBlock.cobble.length;a++)
		{
			for(int i=0;i<=((MDBlockColored)MDBlock.cobble[a]).getMaxMeta();i++)
			{
				FurnaceRecipes.smelting().func_151394_a(new ItemStack(MDBlock.cobble[a],1,i),new ItemStack(MDBlock.stone[a],1,i),1.0f);
				FurnaceRecipes.smelting().func_151394_a(new ItemStack(MDBlock.stoneBrick[a],1,i),new ItemStack(MDBlock.stoneBrickCracked[a],1,i),1.0f);
				FurnaceRecipes.smelting().func_151394_a(new ItemStack(MDBlock.sand[a],1,i), new ItemStack(MDBlock.glassClear[a],1,i), 1.0f);
				FurnaceRecipes.smelting().func_151394_a(new ItemStack(MDBlock.glassClear[a],1,i), new ItemStack(MDBlock.glassFoggy[a],1,i), 1.0f);
				FurnaceRecipes.smelting().func_151394_a(new ItemStack(MDBlock.clay[a],1,i), new ItemStack(MDBlock.hardenedClay[a],1,i), 1.0f);
			}
		}
	}
}