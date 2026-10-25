package net.neverandy.moredyes.reference;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.utility.BlockInfo;

public class Reference
{
	public static final String MOD_ID="moredyes";
	public static final String MOD_NAME="More Dyes";
	public static final String MOD_VERSION="@VERSION@";

	public static final String CLIENT_PROXY="net.neverandy.moredyes.proxy.ClientProxy";
	public static final String SERVER_PROXY="net.neverandy.moredyes.proxy.ServerProxy";

	/**
	 * The Iron Chests tiers that have dyed versions (compat/ironchest), by the name Iron Chests gives them, for code that
	 * must not load Iron Chests' classes. The dirt chest is a joke chest with one slot, so it is left out.
	 */
	public static final String[] IRON_CHEST_TIERS={"iron","gold","diamond","copper","silver","crystal","obsidian"};
	/** The Storage Drawers sizes that have dyed versions (compat/storagedrawers), by the name Storage Drawers gives them. */
	public static final String[] DRAWER_SIZES={"full1","full2","full4","half2","half4"};

	public static final BlockInfo BLOCK_INFO_WOOL=new BlockInfo("wool",Material.CLOTH,0.8f,SoundType.CLOTH);
	public static final BlockInfo BLOCK_INFO_CARPET=new BlockInfo("carpet",Material.CARPET,0.1f,SoundType.CLOTH);
	public static final BlockInfo BLOCK_INFO_ROCK_WOOL=new BlockInfo("rockwool",Material.ROCK,0.8f,SoundType.CLOTH).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_STONE=new BlockInfo("stone",Material.ROCK,1.5f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_COBBLE=new BlockInfo("cobble",Material.ROCK,2.0f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_STONE_BRICK=new BlockInfo("stonebrick",Material.ROCK,1.5f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_STONE_BRICK_CARVED=new BlockInfo("stonebrick_carved",Material.ROCK,1.5f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_STONE_BRICK_CRACKED=new BlockInfo("stonebrick_cracked",Material.ROCK,1.5f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_DIORITE=new BlockInfo("diorite",Material.ROCK,1.5f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_OBSIDIAN=new BlockInfo("obsidian",Material.ROCK,50.0f,SoundType.STONE).tool("pickaxe",3).resistance(2000.0f);
	public static final BlockInfo BLOCK_INFO_SOULSAND=new BlockInfo("soulsand",Material.SAND,0.5f,SoundType.SAND).tool("shovel",0);
	public static final BlockInfo BLOCK_INFO_QUARTZ=new BlockInfo("quartz",Material.ROCK,0.8f,SoundType.STONE).tool("pickaxe",0);
	public static final BlockInfo BLOCK_INFO_CLAY=new BlockInfo("clay",Material.ROCK,1.25f,SoundType.STONE).tool("pickaxe",0).resistance(7.0f);
	public static final BlockInfo BLOCK_INFO_HARDENED_CLAY=new BlockInfo("hardened_clay",Material.ROCK,1.25f,SoundType.STONE).tool("pickaxe",0).resistance(7.0f);
	public static final BlockInfo BLOCK_INFO_COAL=new BlockInfo("coal",Material.ROCK,5.0f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_GLOWSTONE=new BlockInfo("glowstone",Material.GLASS,0.3f,SoundType.GLASS);
	public static final BlockInfo BLOCK_INFO_REDSTONE=new BlockInfo("redstone",Material.IRON,5.0f,SoundType.METAL).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_LAPIS=new BlockInfo("lapis",Material.IRON,3.0f,SoundType.STONE).tool("pickaxe",1).resistance(5.0f);
	public static final BlockInfo BLOCK_INFO_BRICK=new BlockInfo("brick",Material.ROCK,2.0f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_SAND=new BlockInfo("sand",Material.SAND,0.5f,SoundType.SAND).tool("shovel",0);
	public static final BlockInfo BLOCK_INFO_SANDSTONE=new BlockInfo("sandstone",Material.ROCK,0.8f,SoundType.STONE).tool("pickaxe",0);
	public static final BlockInfo BLOCK_INFO_GLASS=new BlockInfo("glass",Material.GLASS,0.3f,SoundType.GLASS);
	public static final BlockInfo BLOCK_INFO_GLASS_FOGGY=new BlockInfo("glass_foggy",Material.GLASS,0.3f,SoundType.GLASS);
	public static final BlockInfo BLOCK_INFO_GLASS_PANE=new BlockInfo("glass_pane",Material.GLASS,0.3f,SoundType.GLASS);
	public static final BlockInfo BLOCK_INFO_GLASS_FOGGY_PANE=new BlockInfo("glass_foggy_pane",Material.GLASS,0.3f,SoundType.GLASS);
	public static final BlockInfo BLOCK_INFO_BOOKSHELF=new BlockInfo("bookshelf",Material.WOOD,1.5f,SoundType.WOOD).tool("axe",0);
	public static final BlockInfo BLOCK_INFO_WORKBENCH=new BlockInfo("workbench",Material.WOOD,2.5f,SoundType.WOOD).tool("axe",0);
	public static final BlockInfo BLOCK_INFO_CHEST=new BlockInfo("chest",Material.WOOD,2.5f,SoundType.WOOD).tool("axe",0);

	// Blocks from newer Minecraft versions, dyed like the others (as in 1.7.10).
	public static final BlockInfo BLOCK_INFO_GRANITE=new BlockInfo("granite",Material.ROCK,1.5f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_ANDESITE=new BlockInfo("andesite",Material.ROCK,1.5f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_POLISHED_GRANITE=new BlockInfo("polished_granite",Material.ROCK,1.5f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_SPECKLED_GRANITE=new BlockInfo("speckled_granite",Material.ROCK,1.5f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_SMOOTH_SPECKLED_GRANITE=new BlockInfo("smooth_speckled_granite",Material.ROCK,1.5f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_POLISHED_DIORITE=new BlockInfo("polished_diorite",Material.ROCK,1.5f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_POLISHED_ANDESITE=new BlockInfo("polished_andesite",Material.ROCK,1.5f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_CONCRETE=new BlockInfo("concrete",Material.ROCK,1.8f,SoundType.STONE).tool("pickaxe",0);
	public static final BlockInfo BLOCK_INFO_CONCRETE_POWDER=new BlockInfo("concrete_powder",Material.SAND,0.5f,SoundType.SAND).tool("shovel",0);
	public static final BlockInfo BLOCK_INFO_SOUL_SOIL=new BlockInfo("soul_soil",Material.GROUND,0.5f,SoundType.SAND).tool("shovel",0);
	public static final BlockInfo BLOCK_INFO_BASALT=new BlockInfo("basalt",Material.ROCK,1.25f,SoundType.STONE).tool("pickaxe",0).resistance(7.0f);
	public static final BlockInfo BLOCK_INFO_POLISHED_BASALT=new BlockInfo("polished_basalt",Material.ROCK,1.25f,SoundType.STONE).tool("pickaxe",0).resistance(7.0f);
	public static final BlockInfo BLOCK_INFO_SMOOTH_STONE=new BlockInfo("smooth_stone",Material.ROCK,2.0f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_SMOOTH_SANDSTONE=new BlockInfo("smooth_sandstone",Material.ROCK,2.0f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_SMOOTH_QUARTZ=new BlockInfo("smooth_quartz",Material.ROCK,2.0f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_CUT_SANDSTONE=new BlockInfo("cut_sandstone",Material.ROCK,0.8f,SoundType.STONE).tool("pickaxe",0);
	public static final BlockInfo BLOCK_INFO_MOSSY_COBBLE=new BlockInfo("mossy_cobble",Material.ROCK,2.0f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_MOSSY_STONE_BRICK=new BlockInfo("mossy_stonebrick",Material.ROCK,1.5f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_NETHER_BRICK=new BlockInfo("nether_brick",Material.ROCK,2.0f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_CRACKED_NETHER_BRICK=new BlockInfo("cracked_nether_brick",Material.ROCK,2.0f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_CHISELED_NETHER_BRICK=new BlockInfo("chiseled_nether_brick",Material.ROCK,2.0f,SoundType.STONE).tool("pickaxe",0).resistance(10.0f);
	public static final BlockInfo BLOCK_INFO_END_STONE_BRICK=new BlockInfo("end_stone_brick",Material.ROCK,0.8f,SoundType.STONE).tool("pickaxe",0).resistance(15.0f);
	public static final BlockInfo BLOCK_INFO_BONE_BLOCK=new BlockInfo("bone_block",Material.ROCK,2.0f,SoundType.STONE).tool("pickaxe",0);
	public static final BlockInfo BLOCK_INFO_GLAZED_TERRACOTTA=new BlockInfo("glazed_terracotta",Material.ROCK,1.4f,SoundType.STONE).tool("pickaxe",0);
	public static final BlockInfo BLOCK_INFO_NETHERITE_BLOCK=new BlockInfo("netherite_block",Material.IRON,50.0f,SoundType.METAL).tool("pickaxe",3).resistance(1200.0f);
	public static final BlockInfo BLOCK_INFO_CRYING_OBSIDIAN=new BlockInfo("crying_obsidian",Material.ROCK,50.0f,SoundType.STONE).tool("pickaxe",3).resistance(2000.0f);
	public static final BlockInfo BLOCK_INFO_CHAIN=new BlockInfo("chain",Material.IRON,5.0f,SoundType.METAL).tool("pickaxe",0).resistance(6.0f);
	public static final BlockInfo BLOCK_INFO_CORNFLOWER=new BlockInfo("cornflower",Material.PLANTS,0.0f,SoundType.PLANT).tab(MoreDyes.tabPlants);

	// The rest of the blocks the newer versions have (as in 1.16.5). Quartz bricks and lily of the valley are not in 1.12.
	public static final BlockInfo BLOCK_INFO_END_STONE=new BlockInfo("end_stone",Material.ROCK,3.0f,SoundType.STONE).tool("pickaxe",0).resistance(15.0f);
	public static final BlockInfo BLOCK_INFO_CHISELED_SANDSTONE=new BlockInfo("chiseled_sandstone",Material.ROCK,0.8f,SoundType.STONE).tool("pickaxe",0);
	public static final BlockInfo BLOCK_INFO_QUARTZ_BRICKS=new BlockInfo("quartz_bricks",Material.ROCK,0.8f,SoundType.STONE).tool("pickaxe",0);
	public static final BlockInfo BLOCK_INFO_CHISELED_QUARTZ=new BlockInfo("chiseled_quartz",Material.ROCK,0.8f,SoundType.STONE).tool("pickaxe",0);
	public static final BlockInfo BLOCK_INFO_QUARTZ_PILLAR=new BlockInfo("quartz_pillar",Material.ROCK,0.8f,SoundType.STONE).tool("pickaxe",0);
	public static final BlockInfo BLOCK_INFO_GRAVEL=new BlockInfo("gravel",Material.SAND,0.6f,SoundType.GROUND).tool("shovel",0);
	public static final BlockInfo BLOCK_INFO_ICE=new BlockInfo("ice",Material.ICE,0.5f,SoundType.GLASS).tool("pickaxe",0);
	public static final BlockInfo BLOCK_INFO_PACKED_ICE=new BlockInfo("packed_ice",Material.PACKED_ICE,0.5f,SoundType.GLASS).tool("pickaxe",0);
	public static final BlockInfo BLOCK_INFO_SNOW=new BlockInfo("snow",Material.CRAFTED_SNOW,0.2f,SoundType.SNOW).tool("shovel",0);
	public static final BlockInfo BLOCK_INFO_ALLIUM=new BlockInfo("allium",Material.PLANTS,0.0f,SoundType.PLANT).tab(MoreDyes.tabPlants);
	public static final BlockInfo BLOCK_INFO_AZURE_BLUET=new BlockInfo("azure_bluet",Material.PLANTS,0.0f,SoundType.PLANT).tab(MoreDyes.tabPlants);
	public static final BlockInfo BLOCK_INFO_BLUE_ORCHID=new BlockInfo("blue_orchid",Material.PLANTS,0.0f,SoundType.PLANT).tab(MoreDyes.tabPlants);
	public static final BlockInfo BLOCK_INFO_DANDELION=new BlockInfo("dandelion",Material.PLANTS,0.0f,SoundType.PLANT).tab(MoreDyes.tabPlants);
	public static final BlockInfo BLOCK_INFO_LILY_OF_THE_VALLEY=new BlockInfo("lily_of_the_valley",Material.PLANTS,0.0f,SoundType.PLANT).tab(MoreDyes.tabPlants);
	public static final BlockInfo BLOCK_INFO_OXEYE_DAISY=new BlockInfo("oxeye_daisy",Material.PLANTS,0.0f,SoundType.PLANT).tab(MoreDyes.tabPlants);
	public static final BlockInfo BLOCK_INFO_POPPY=new BlockInfo("poppy",Material.PLANTS,0.0f,SoundType.PLANT).tab(MoreDyes.tabPlants);
	public static final BlockInfo BLOCK_INFO_BIRCH_PLANK=new BlockInfo("birch_plank",Material.WOOD,2.0f,SoundType.WOOD).tool("axe",0).resistance(5.0f).tab(MoreDyes.tabTrees);
	public static final BlockInfo BLOCK_INFO_SPRUCE_PLANK=new BlockInfo("spruce_plank",Material.WOOD,2.0f,SoundType.WOOD).tool("axe",0).resistance(5.0f).tab(MoreDyes.tabTrees);
	public static final BlockInfo BLOCK_INFO_JUNGLE_PLANK=new BlockInfo("jungle_plank",Material.WOOD,2.0f,SoundType.WOOD).tool("axe",0).resistance(5.0f).tab(MoreDyes.tabTrees);
	public static final BlockInfo BLOCK_INFO_ACACIA_PLANK=new BlockInfo("acacia_plank",Material.WOOD,2.0f,SoundType.WOOD).tool("axe",0).resistance(5.0f).tab(MoreDyes.tabTrees);
	public static final BlockInfo BLOCK_INFO_DARK_OAK_PLANK=new BlockInfo("dark_oak_plank",Material.WOOD,2.0f,SoundType.WOOD).tool("axe",0).resistance(5.0f).tab(MoreDyes.tabTrees);

	// Blocks that keep their color in a tile entity, one block for all colors (as in 1.7.10).
	public static final BlockInfo BLOCK_INFO_IRON_TRAPDOOR=new BlockInfo("iron_trapdoor",Material.IRON,5.0f,SoundType.METAL).tool("pickaxe",0).tab(MoreDyes.tabShapes);
	public static final BlockInfo BLOCK_INFO_LILAC=new BlockInfo("lilac",Material.PLANTS,0.0f,SoundType.PLANT).tab(MoreDyes.tabPlants);
	public static final BlockInfo BLOCK_INFO_PEONY=new BlockInfo("peony",Material.PLANTS,0.0f,SoundType.PLANT).tab(MoreDyes.tabPlants);
	public static final BlockInfo BLOCK_INFO_ROSE_BUSH=new BlockInfo("rose_bush",Material.PLANTS,0.0f,SoundType.PLANT).tab(MoreDyes.tabPlants);
	public static final BlockInfo BLOCK_INFO_SIGN=new BlockInfo("sign",Material.WOOD,1.0f,SoundType.WOOD).tool("axe",0);

	public static final BlockInfo BLOCK_INFO_PLANK=new BlockInfo("plank",Material.WOOD,2.0f,SoundType.WOOD).tool("axe",0).resistance(5.0f).tab(MoreDyes.tabTrees);
	public static final BlockInfo BLOCK_INFO_LOG=new BlockInfo("log",Material.WOOD,2.0f,SoundType.WOOD).tool("axe",0).tab(MoreDyes.tabTrees);
	public static final BlockInfo BLOCK_INFO_LEAVES=new BlockInfo("leaf",Material.LEAVES,0.2f,SoundType.PLANT).tab(MoreDyes.tabTrees);
	public static final BlockInfo BLOCK_INFO_SAPLING=new BlockInfo("sapling",Material.PLANTS,0.0f,SoundType.PLANT).tab(MoreDyes.tabTrees);
	public static final BlockInfo BLOCK_INFO_TULIP=new BlockInfo("tulip",Material.PLANTS,0.0f,SoundType.PLANT).tab(MoreDyes.tabPlants);
}
