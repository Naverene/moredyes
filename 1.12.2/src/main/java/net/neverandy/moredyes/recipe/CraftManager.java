package net.neverandy.moredyes.recipe;

import net.neverandy.moredyes.block.IColoredBlock;
import net.neverandy.moredyes.block.MDBlock;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class CraftManager
{
	public static void registerCraftingRecipes()
	{
		ItemStack WHITE=new ItemStack(Items.DYE,1,15);
		ItemStack ORANGE=new ItemStack(Items.DYE,1,14);
		ItemStack MAGENTA=new ItemStack(Items.DYE,1,13);
		ItemStack LBLUE=new ItemStack(Items.DYE,1,12);
		ItemStack YELLOW=new ItemStack(Items.DYE,1,11);
		ItemStack LIME=new ItemStack(Items.DYE,1,10);
		ItemStack PINK=new ItemStack(Items.DYE,1,9);
		ItemStack DGRAY=new ItemStack(Items.DYE,1,8);
		ItemStack LGRAY=new ItemStack(Items.DYE,1,7);
		ItemStack CYAN=new ItemStack(Items.DYE,1,6);
		ItemStack PURPLE=new ItemStack(Items.DYE,1,5);
		ItemStack BLUE=new ItemStack(Items.DYE,1,4);
		ItemStack BROWN=new ItemStack(Items.DYE,1,3);
		ItemStack GREEN=new ItemStack(Items.DYE,1,2);
		ItemStack RED=new ItemStack(Items.DYE,1,1);
		ItemStack BLACK=new ItemStack(Items.DYE,1,0);
		/*
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,0),WHITE,ORANGE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,1),WHITE,MAGENTA);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,2),WHITE,LBLUE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,3),WHITE,YELLOW);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,4),WHITE,LIME);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,5),WHITE,PINK);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,6),WHITE,DGRAY);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,7),WHITE,LGRAY);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,8),WHITE,CYAN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,9),WHITE,PURPLE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,10),WHITE,BLUE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,11),WHITE,BROWN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,12),WHITE,GREEN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,13),ORANGE,MAGENTA);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,14),ORANGE,LBLUE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[0],2,15),ORANGE,YELLOW);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,0),ORANGE,LIME);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,1),ORANGE,PINK);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,2),ORANGE,DGRAY);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,3),ORANGE,LGRAY);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,4),ORANGE,CYAN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,5),ORANGE,PURPLE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,6),ORANGE,BLUE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,7),ORANGE,BROWN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,8),ORANGE,GREEN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,9),ORANGE,RED);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,10),ORANGE,BLACK);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,11),MAGENTA,LBLUE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,12),MAGENTA,YELLOW);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,13),MAGENTA,LIME);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,14),MAGENTA,PINK);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[1],2,15),MAGENTA,DGRAY);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,0),MAGENTA,LGRAY);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,1),MAGENTA,CYAN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,2),MAGENTA,PURPLE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,3),MAGENTA,BLUE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,4),MAGENTA,BROWN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,5),MAGENTA,GREEN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,6),MAGENTA,RED);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,7),MAGENTA,BLACK);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,8),LBLUE,YELLOW);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,9),LBLUE,LIME);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,10),LBLUE,PINK);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,11),LBLUE,DGRAY);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,12),LBLUE,LGRAY);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,13),LBLUE,CYAN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,14),LBLUE,PURPLE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[2],2,15),LBLUE,BLUE);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,0),LBLUE,BROWN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,1),LBLUE,GREEN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,2),LBLUE,RED);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,3),LBLUE,BLACK);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,4),YELLOW,LIME);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,5),YELLOW,PINK);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,6),YELLOW,DGRAY);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,7),YELLOW,LGRAY);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,8),YELLOW,CYAN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,9),YELLOW,PURPLE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,10),YELLOW,BLUE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,11),YELLOW,BROWN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,12),YELLOW,GREEN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,13),YELLOW,RED);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,14),YELLOW,BLACK);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[3],2,15),LIME,PINK);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,0),LIME,DGRAY);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,1),LIME,LGRAY);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,2),LIME,CYAN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,3),LIME,PURPLE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,4),LIME,BLUE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,5),LIME,BROWN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,6),LIME,GREEN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,7),LIME,RED);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,8),LIME,BLACK);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,9),PINK,DGRAY);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,10),PINK,LGRAY);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,11),PINK,CYAN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,12),PINK,PURPLE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,13),PINK,BLUE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,14),PINK,BROWN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[4],2,15),PINK,GREEN);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,0),PINK,RED);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,1),PINK,BLACK);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,2),DGRAY,LGRAY);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,3),DGRAY,CYAN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,4),DGRAY,PURPLE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,5),DGRAY,BLUE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,6),DGRAY,BROWN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,7),DGRAY,GREEN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,8),DGRAY,RED);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,9),DGRAY,BLACK);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,10),LGRAY,CYAN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,11),LGRAY,PURPLE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,12),LGRAY,BLUE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,13),LGRAY,BROWN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,14),LGRAY,GREEN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[5],2,15),LGRAY,RED);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,0),LGRAY,BLACK);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,1),CYAN,PURPLE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,2),CYAN,BLUE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,3),CYAN,BROWN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,4),CYAN,GREEN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,5),CYAN,RED);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,6),CYAN,BLACK);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,7),PURPLE,BLUE);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,8),PURPLE,BROWN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,9),PURPLE,GREEN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,10),PURPLE,RED);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,11),PURPLE,BLACK);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,12),BLUE,BROWN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,13),BLUE,GREEN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,14),BLUE,RED);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[6],2,15),BLUE,BLACK);
		
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[7],2,0),BROWN,GREEN);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[7],2,1),BROWN,RED);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[7],2,2),BROWN,BLACK);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[7],2,3),GREEN,RED);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[7],2,4),GREEN,BLACK);
		GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[7],2,5),RED,BLACK);


		for(int i=0;i<8;i++)
		{
			for(int meta=0;meta<((IColoredBlock)MDBlock.sand[i]).getColorCount();meta++)
			{
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.cobble[i],8,meta),"SSS","SDS","SSS", 'S', "cobblestone", 'D', new ItemStack(MDItem.dye[i],1,meta)));
				CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(Blocks.COBBLESTONE, "cobblestone", new ItemStack(Items.WATER_BUCKET)));
				
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.sand[i],8,meta),"SSS","SDS","SSS", 'S', "sand", 'D', new ItemStack(MDItem.dye[i],1,meta)));
				CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(Blocks.SAND, "sand", new ItemStack(Items.WATER_BUCKET)));
				
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.wool[i],8,meta),"SSS","SDS","SSS", 'S', "blockWool", 'D', new ItemStack(MDItem.dye[i],1,meta)));
				CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(Blocks.WOOL, "blockWool", new ItemStack(Items.WATER_BUCKET)));
				
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.brick[i],8,meta),"SSS","SDS","SSS", 'S', "blockBrick", 'D', new ItemStack(MDItem.dye[i],1,meta)));
				CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(Blocks.BRICK_BLOCK, "blockBrick", new ItemStack(Items.WATER_BUCKET)));
				
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.clay[i],8,meta),"SSS","SDS","SSS", 'S', "stainedClay", 'D', new ItemStack(MDItem.dye[i],1,meta)));
				CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(Blocks.STAINED_HARDENED_CLAY, "stainedClay", new ItemStack(Items.WATER_BUCKET)));
				
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.lapis[i],8,meta),"SSS","SDS","SSS", 'S', "lapisBlock", 'D', new ItemStack(MDItem.dye[i],1,meta)));
				CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(Blocks.LAPIS_BLOCK, "lapisBlock", new ItemStack(Items.WATER_BUCKET)));
				CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Items.DYE,9,4),"lapisBlock"));
				
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.redstone[i],8,meta),"SSS","SDS","SSS", 'S', "blockRedstone", 'D', new ItemStack(MDItem.dye[i],1,meta)));
				CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(Blocks.REDSTONE_BLOCK, "blockRedstone", new ItemStack(Items.WATER_BUCKET)));
				CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Items.REDSTONE,9),"blockRedstone"));
				
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.coal[i],8,meta),"SSS","SDS","SSS", 'S', "blockCoal", 'D', new ItemStack(MDItem.dye[i],1,meta)));
				CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(Blocks.COAL_BLOCK, "blockCoal", new ItemStack(Items.WATER_BUCKET)));
				CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Items.COAL,9),"blockCoal"));
				
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.glowstone[i],8,meta),"SSS","SDS","SSS", 'S', "glowstone", 'D', new ItemStack(MDItem.dye[i],1,meta)));
				CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(Blocks.GLOWSTONE, "glowstone", new ItemStack(Items.WATER_BUCKET)));
				
				GameRegistry.addShapedRecipe(new ItemStack(MDBlock.stonebrick[i],4,meta), "SS","SS",'S',new ItemStack(MDBlock.stone[i],1,meta));
				
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.obsidian[i],8,meta),"SSS","SDS","SSS", 'S', "blockObsidian", 'D', new ItemStack(MDItem.dye[i],1,meta)));
				CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(Blocks.OBSIDIAN, "blockObsidian", new ItemStack(Items.WATER_BUCKET)));
				
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.soulsand[i],8,meta),"SSS","SDS","SSS", 'S', "soulsand", 'D', new ItemStack(MDItem.dye[i],1,meta)));
				CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(Blocks.SOUL_SAND, "soulsand", new ItemStack(Items.WATER_BUCKET)));
				
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.stonebrickCarved[i],8,meta),"SSS","SDS","SSS", 'S', "bricksStoneCarved", 'D', new ItemStack(MDItem.dye[i],1,meta)));
				CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Blocks.STONEBRICK,1,3), "bricksStoneCarved", new ItemStack(Items.WATER_BUCKET)));	
				
				CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(MDBlock.quartz[i],8,meta),"SSS","SDS","SSS", 'S', "blockQuartz", 'D', new ItemStack(MDItem.dye[i],1,meta)));
				CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(Blocks.QUARTZ_BLOCK,1), "blockQuartz", new ItemStack(Items.WATER_BUCKET)));	
			}
		}
		for(int i=0;i<118;i++)
		{
			//Gets block index and meta for meta based blocks
			int colorSet = ColorStrings.getColorSet(ColorStrings.ALL[i]);
			int meta = ColorStrings.getColorIndexInSet(ColorStrings.ALL[i], colorSet);
			
			GameRegistry.addShapelessRecipe(new ItemStack(MDBlock.plank[colorSet],4,meta), new ItemStack(MDBlock.log[i],1));
			GameRegistry.addShapelessRecipe(new ItemStack(MDBlock.sapling[i]), new ItemStack(MDItem.dye[colorSet],1,meta),new ItemStack(Blocks.SAPLING));
			GameRegistry.addShapelessRecipe(new ItemStack(MDItem.dye[colorSet],1,meta), new ItemStack(MDBlock.tulip[i]));
		}
		*/
	}
	public static void registerSmeltingRecipes()
	{
		for(int i=0;i<8;i++)
		{
			for(int meta=0;meta<((IColoredBlock)MDBlock.sand[i]).getColorCount();meta++)
			{
				FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MDBlock.cobble[i],1,meta), new ItemStack(MDBlock.stone[i],1,meta), 1.0F);
				FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MDBlock.sand[i],1,meta), new ItemStack(MDBlock.glass[i],1,meta), 1.0F);
				FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MDBlock.glass[i],1,meta), new ItemStack(MDBlock.glassFoggy[i],1,meta), 1.0F);
				FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MDBlock.stonebrick[i],1,meta), new ItemStack(MDBlock.stonebrickCracked[i],1,meta), 1.0F);
			}
		}
	}
}
