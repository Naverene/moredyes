package net.neverandy.moredyes.reference;

import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.utility.BlockInfo;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class Reference
{
	public static final String MOD_ID="moredyes";
	public static final String MOD_NAME="More Dyes";
	public static final String MOD_VERSION="@VERSION@";
	
	public static final String CLIENT_PROXY="net.neverandy.moredyes.proxy.ClientProxy";
	public static final String SERVER_PROXY="net.neverandy.moredyes.proxy.ServerProxy";
	
	public static final BlockInfo BLOCK_INFO_WOOL= new BlockInfo("wool",Material.CLOTH,0.8f,SoundType.CLOTH,"sword",0);
	public static final BlockInfo BLOCK_INFO_STONE=new BlockInfo("stone",Material.ROCK,1.5f,SoundType.STONE,"pickaxe",1,10.0f);
	public static final BlockInfo BLOCK_INFO_COBBLE=new BlockInfo("cobble",Material.ROCK,1.5f,SoundType.STONE,"pickaxe",1,10.0f);
	public static final BlockInfo BLOCK_INFO_STONE_BRICK_CARVED=new BlockInfo("stonebrickcarved",Material.ROCK,1.5f,SoundType.STONE,"pickaxe",1,10.0f);
	public static final BlockInfo BLOCK_INFO_STONE_BRICK_CRACKED=new BlockInfo("stonebrickcracked",Material.ROCK,1.5f,SoundType.STONE,"pickaxe",1,10.0f);
	public static final BlockInfo BLOCK_INFO_STONE_BRICK=new BlockInfo("stonebrick",Material.ROCK,1.5f,SoundType.STONE,"pickaxe",1,10.0f);
	public static final BlockInfo BLOCK_INFO_OBSIDIAN=new BlockInfo("obsidian",Material.ROCK,50.0f,SoundType.METAL,"pickaxe",3,2000.0f);
	public static final BlockInfo BLOCK_INFO_SOULSAND = new BlockInfo("soulsand",Material.SAND,0.5f,SoundType.SAND, "shovel", 0);
	public static final BlockInfo BLOCK_INFO_QUARTZ = new BlockInfo("quartz",Material.ROCK,0.8f,SoundType.METAL,"pickaxe",2);
	public static final BlockInfo BLOCK_INFO_CLAY = new BlockInfo("clay",Material.CLAY,1.25f, SoundType.METAL, "pickaxe", 1, 7.0f);
	public static final BlockInfo BLOCK_INFO_COAL = new BlockInfo("coal",Material.ROCK,5.0f,SoundType.STONE,"pickaxe",1,10.0f);
	public static final BlockInfo BLOCK_INFO_GLOWSTONE = new BlockInfo("glowstone",Material.GLASS,0.3f,SoundType.GLASS,"pickaxe",1);
	public static final BlockInfo BLOCK_INFO_REDSTONE = new BlockInfo("redstone",Material.IRON,5.0f,SoundType.METAL,"pickaxe",1,10.0f);
	public static final BlockInfo BLOCK_INFO_LAPIS = new BlockInfo("lapis",Material.ROCK,3.0f,SoundType.METAL,"pickaxe",2,5.0f);
	public static final BlockInfo BLOCK_INFO_PLANK = new BlockInfo("plank",Material.WOOD,2.0f,SoundType.WOOD,"axe",0,5.0f, MoreDyes.tabTrees);
	public static final BlockInfo BLOCK_INFO_TULIP = new BlockInfo("tulip",Material.PLANTS,0.0f,SoundType.GROUND,"",0,1.0f,MoreDyes.tabPlants);
	public static final BlockInfo BLOCK_INFO_LOG = new BlockInfo("log",Material.WOOD,2.0f,SoundType.WOOD,"axe",0,1.0f,MoreDyes.tabTrees);
	public static final BlockInfo BLOCK_INFO_LEAVES = new BlockInfo("leaf",Material.LEAVES,0.2f,SoundType.GROUND,"axe",0,1.0f,MoreDyes.tabTrees);
	public static final BlockInfo BLOCK_INFO_SAPLING = new BlockInfo("sapling",Material.PLANTS,0.0f,SoundType.GROUND,"",0,1.0f,MoreDyes.tabTrees);
	
	public static final BlockInfo BLOCK_INFO_GLASS = new BlockInfo("glass",Material.GLASS,0.4f,SoundType.GLASS,"pickaxe",0,1.0f);
	public static final BlockInfo BLOCK_INFO_GLASS_FOGGY = new BlockInfo("glassfoggy",Material.GLASS,0.4f,SoundType.GLASS,"pickaxe",0,1.0f);
	
	public static final BlockInfo BLOCK_INFO_SLAB = new BlockInfo("slab",Material.ROCK,1.0f,SoundType.METAL,"pickaxe",1,10.0f);
	public static final BlockInfo BLOCK_INFO_WORKBENCH = new BlockInfo("craftingtable",Material.WOOD,2.5f,SoundType.WOOD,"axe",0);
	public static final BlockInfo BLOCK_INFO_BRICK = new BlockInfo("brick",Material.ROCK,2.0f,SoundType.METAL,"pickaxe",1);
	public static final BlockInfo BLOCK_INFO_SAND = new BlockInfo("sand", Material.SAND, 0.5f, SoundType.SAND, "shovel", 0);
	

	public static final BlockInfo BLOCK_INFO_ROCK_WOOL= new BlockInfo("woolRock",Material.CLOTH,0.8f,SoundType.CLOTH,"sword",0);
}
