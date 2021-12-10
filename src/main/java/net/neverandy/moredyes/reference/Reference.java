package net.neverandy.moredyes.reference;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.neverandy.moredyes.MoreDyes;
import net.neverandy.moredyes.setup.ModSetup;
import net.neverandy.moredyes.utility.BlockInfo;

public class Reference
{
    public static final String MOD_ID="moredyes";
    public static final String MOD_NAME="More Dyes";
    public static final String MOD_VERSION="@VERSION@";

    public static final BlockInfo BLOCK_INFO_WOOL= new BlockInfo("wool", Material.WOOL,0.8f, SoundType.WOOL,null,0);
    public static final BlockInfo BLOCK_INFO_STONE=new BlockInfo("stone",Material.STONE,1.5f,SoundType.STONE,ToolType.PICKAXE,1,10.0f);
    public static final BlockInfo BLOCK_INFO_COBBLE=new BlockInfo("cobble",Material.STONE,1.5f,SoundType.STONE,ToolType.PICKAXE,1,10.0f);
    public static final BlockInfo BLOCK_INFO_STONE_BRICK_CARVED=new BlockInfo("stonebrickcarved",Material.STONE,1.5f,SoundType.STONE,ToolType.PICKAXE,1,10.0f);
    public static final BlockInfo BLOCK_INFO_STONE_BRICK_CRACKED=new BlockInfo("stonebrickcracked",Material.STONE,1.5f,SoundType.STONE,ToolType.PICKAXE,1,10.0f);
    public static final BlockInfo BLOCK_INFO_STONE_BRICK=new BlockInfo("stonebrick",Material.STONE,1.5f,SoundType.STONE,ToolType.PICKAXE,1,10.0f);
    public static final BlockInfo BLOCK_INFO_OBSIDIAN=new BlockInfo("obsidian",Material.STONE,50.0f,SoundType.METAL,ToolType.PICKAXE,3,2000.0f);
    public static final BlockInfo BLOCK_INFO_SOULSAND = new BlockInfo("soulsand",Material.SAND,0.5f,SoundType.SAND, ToolType.SHOVEL, 0);
    public static final BlockInfo BLOCK_INFO_QUARTZ = new BlockInfo("quartz",Material.STONE,0.8f,SoundType.METAL,ToolType.PICKAXE,2);
    public static final BlockInfo BLOCK_INFO_CLAY = new BlockInfo("clay",Material.CLAY,1.25f, SoundType.METAL, ToolType.PICKAXE, 1, 7.0f);
    public static final BlockInfo BLOCK_INFO_COAL = new BlockInfo("coal",Material.STONE,5.0f,SoundType.STONE,ToolType.PICKAXE,1,10.0f);
    public static final BlockInfo BLOCK_INFO_GLOWSTONE = new BlockInfo("glowstone",Material.GLASS,0.3f,SoundType.GLASS,ToolType.PICKAXE,1, 0, ModSetup.tabBlocks, 15);
    public static final BlockInfo BLOCK_INFO_REDSTONE = new BlockInfo("redstone",Material.METAL,5.0f,SoundType.METAL,ToolType.PICKAXE,1,10.0f);
    public static final BlockInfo BLOCK_INFO_LAPIS = new BlockInfo("lapis",Material.STONE,3.0f,SoundType.METAL,ToolType.PICKAXE,2,5.0f);
    public static final BlockInfo BLOCK_INFO_PLANK = new BlockInfo("plank",Material.WOOD,2.0f,SoundType.WOOD,ToolType.AXE,0,5.0f, ModSetup.tabTrees, 0);
    public static final BlockInfo BLOCK_INFO_TULIP = new BlockInfo("tulip",Material.PLANT,0.0f,SoundType.GRASS,null,0,1.0f,ModSetup.tabPlants, 0);
    public static final BlockInfo BLOCK_INFO_LOG = new BlockInfo("log",Material.WOOD,2.0f,SoundType.WOOD,ToolType.AXE,0,1.0f, ModSetup.tabTrees, 0);
    public static final BlockInfo BLOCK_INFO_LEAVES = new BlockInfo("leaf",Material.LEAVES,0.2f,SoundType.GRASS,ToolType.AXE,0,1.0f,ModSetup.tabTrees, 0);
    public static final BlockInfo BLOCK_INFO_SAPLING = new BlockInfo("sapling",Material.PLANT,0.0f,SoundType.GRASS,null,0,1.0f,ModSetup.tabTrees, 0);

    public static final BlockInfo BLOCK_INFO_GLASS = new BlockInfo("glass",Material.GLASS,0.4f,SoundType.GLASS,ToolType.PICKAXE,0,1.0f);
    public static final BlockInfo BLOCK_INFO_GLASS_FOGGY = new BlockInfo("glassfoggy",Material.GLASS,0.4f,SoundType.GLASS,ToolType.PICKAXE,0,1.0f);

    public static final BlockInfo BLOCK_INFO_SLAB = new BlockInfo("slab",Material.STONE,1.0f,SoundType.METAL,ToolType.PICKAXE,1,10.0f);
    public static final BlockInfo BLOCK_INFO_WORKBENCH = new BlockInfo("craftingtable",Material.WOOD,2.5f,SoundType.WOOD,ToolType.AXE,0);
    public static final BlockInfo BLOCK_INFO_BRICK = new BlockInfo("brick",Material.STONE,2.0f,SoundType.METAL,ToolType.PICKAXE,1);
    public static final BlockInfo BLOCK_INFO_SAND = new BlockInfo("sand", Material.SAND, 0.5f, SoundType.SAND, ToolType.SHOVEL, 0);


    public static final BlockInfo BLOCK_INFO_ROCK_WOOL= new BlockInfo("woolRock",Material.WOOL,0.8f,SoundType.WOOD,null,0);

    public static final String[] BLOCKTYPES = {"wool", "stone", "cobble", "stone_brick_carved","stone_brick_cracked",
            "stone_brick", "obsidian", "soulsand", "quartz", "clay", "coal", "glowstone", "redstone", "lapis", "plank", "tulip", "log",
            "leaf", "sapling", "glass", "glass_foggy", "slab", "workbench", "brick", "sand", "rock_wool"};
}
