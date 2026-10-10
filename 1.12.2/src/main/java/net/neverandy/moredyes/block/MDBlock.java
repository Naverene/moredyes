package net.neverandy.moredyes.block;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityColor;
import net.neverandy.moredyes.tileentity.TileEntityDyedChest;
import net.neverandy.moredyes.tileentity.TileEntityDyedPiston;
import net.neverandy.moredyes.utility.BlockInfo;

@EventBusSubscriber(modid=Reference.MOD_ID)
public class MDBlock
{
	// One block per color group; the metadata is the shade.
	public static Block[] wool;
	public static Block[] stonebrick;
	public static Block[] stonebrickCarved;
	public static Block[] stonebrickCracked;
	public static Block[] stone;
	public static Block[] cobble;
	public static Block[] diorite;
	public static Block[] obsidian;
	public static Block[] soulsand;
	public static Block[] quartz;
	public static Block[] clay;
	public static Block[] hardenedClay;
	public static Block[] coal;
	public static Block[] glowstone;
	public static Block[] lapis;
	public static Block[] redstone;
	public static Block[] brick;
	public static Block[] sand;
	public static Block[] sandstone;
	public static Block[] glass;
	public static Block[] glassFoggy;
	public static Block[] glassPane;
	public static Block[] glassFoggyPane;
	public static Block[] bookshelf;
	public static Block[] workbench;
	public static Block[] plank;
	public static Block[] sapling;
	public static Block[] tulip;
	/** Only when Thermal Foundation is installed, otherwise null. */
	public static Block[] rockwool;
	// Blocks from newer Minecraft versions, as in 1.7.10.
	public static Block[] granite;
	public static Block[] andesite;
	public static Block[] polishedGranite;
	public static Block[] polishedDiorite;
	public static Block[] polishedAndesite;
	public static Block[] concrete;
	public static Block[] concretePowder;
	public static Block[] soulSoil;
	public static Block[] basalt;
	public static Block[] polishedBasalt;
	public static Block[] smoothStone;
	public static Block[] smoothSandstone;
	public static Block[] smoothQuartz;
	public static Block[] cutSandstone;
	public static Block[] mossyCobble;
	public static Block[] mossyStonebrick;
	public static Block[] netherBrick;
	public static Block[] crackedNetherBrick;
	public static Block[] chiseledNetherBrick;
	public static Block[] endStoneBrick;
	public static Block[] boneBlock;
	public static Block[] glazedTerracotta;
	public static Block[] netheriteBlock;
	public static Block[] cryingObsidian;
	public static Block[] chain;
	public static Block[] cornflower;

	// One block per color, in the order of ColorStrings.ALL.
	public static Block[] log;
	public static Block[] leaf;
	public static Block[] chest;

	// One block for all colors, which keeps the color in a tile entity (see TileColor).
	public static Block stoneStairs,graniteStairs,polishedGraniteStairs,dioriteStairs,polishedDioriteStairs,andesiteStairs,
			polishedAndesiteStairs,mossyCobbleStairs,mossyStonebrickStairs,endStoneBrickStairs,smoothSandstoneStairs,
			smoothQuartzStairs,netherBrickStairs;
	public static Block stoneSlab,cutSandstoneSlab,dioriteSlab;
	public static Block brickWall,stonebrickWall,mossyStonebrickWall,graniteWall,dioriteWall,andesiteWall,netherBrickWall,
			sandstoneWall,endStoneBrickWall;
	public static Block ironTrapdoor,piston,stickyPiston,pistonHead;

	/** Every block of the mod with a color group per block, in the order it is registered and listed in the creative tabs. */
	public static final List<Block> ALL=new ArrayList<Block>();
	/** Every block of the mod that keeps its color in a tile entity and has an item, in the same order. */
	public static final List<Block> TILE_COLORED=new ArrayList<Block>();
	/** The vanilla block each dyed block turns back into when its color is washed out. */
	public static final Map<Block,ItemStack> WASHED=new LinkedHashMap<Block,ItemStack>();

	public static void initialize()
	{
		int groups=ColorStrings.GROUPS.length;
		int colors=ColorStrings.ALL.length;
		wool=new Block[groups];
		stonebrick=new Block[groups];
		stonebrickCarved=new Block[groups];
		stonebrickCracked=new Block[groups];
		stone=new Block[groups];
		cobble=new Block[groups];
		diorite=new Block[groups];
		obsidian=new Block[groups];
		soulsand=new Block[groups];
		quartz=new Block[groups];
		clay=new Block[groups];
		hardenedClay=new Block[groups];
		coal=new Block[groups];
		glowstone=new Block[groups];
		lapis=new Block[groups];
		redstone=new Block[groups];
		brick=new Block[groups];
		sand=new Block[groups];
		sandstone=new Block[groups];
		glass=new Block[groups];
		glassFoggy=new Block[groups];
		glassPane=new Block[groups];
		glassFoggyPane=new Block[groups];
		bookshelf=new Block[groups];
		workbench=new Block[groups];
		plank=new Block[groups];
		sapling=new Block[groups];
		tulip=new Block[groups];
		log=new Block[colors];
		leaf=new Block[colors];
		chest=new Block[colors];
		granite=new Block[groups];
		andesite=new Block[groups];
		polishedGranite=new Block[groups];
		polishedDiorite=new Block[groups];
		polishedAndesite=new Block[groups];
		concrete=new Block[groups];
		concretePowder=new Block[groups];
		soulSoil=new Block[groups];
		basalt=new Block[groups];
		polishedBasalt=new Block[groups];
		smoothStone=new Block[groups];
		smoothSandstone=new Block[groups];
		smoothQuartz=new Block[groups];
		cutSandstone=new Block[groups];
		mossyCobble=new Block[groups];
		mossyStonebrick=new Block[groups];
		netherBrick=new Block[groups];
		crackedNetherBrick=new Block[groups];
		chiseledNetherBrick=new Block[groups];
		endStoneBrick=new Block[groups];
		boneBlock=new Block[groups];
		glazedTerracotta=new Block[groups];
		netheriteBlock=new Block[groups];
		cryingObsidian=new Block[groups];
		chain=new Block[groups];
		cornflower=new Block[groups];
		boolean thermal=Loader.isModLoaded("thermalfoundation");
		if(thermal)
		{
			rockwool=new Block[groups];
		}
		for(int g=0;g<groups;g++)
		{
			wool[g]=new BlockColored(Reference.BLOCK_INFO_WOOL,g);
			stonebrick[g]=new BlockColored(Reference.BLOCK_INFO_STONE_BRICK,g);
			stonebrickCarved[g]=new BlockColored(Reference.BLOCK_INFO_STONE_BRICK_CARVED,g);
			stonebrickCracked[g]=new BlockColored(Reference.BLOCK_INFO_STONE_BRICK_CRACKED,g);
			stone[g]=new BlockColoredStone(Reference.BLOCK_INFO_STONE,g);
			cobble[g]=new BlockColored(Reference.BLOCK_INFO_COBBLE,g);
			diorite[g]=new BlockColored(Reference.BLOCK_INFO_DIORITE,g);
			obsidian[g]=new BlockColoredObsidian(Reference.BLOCK_INFO_OBSIDIAN,g);
			soulsand[g]=new BlockColoredSoulSand(Reference.BLOCK_INFO_SOULSAND,g);
			quartz[g]=new BlockColored(Reference.BLOCK_INFO_QUARTZ,g);
			clay[g]=new BlockColored(Reference.BLOCK_INFO_CLAY,g);
			hardenedClay[g]=new BlockColored(Reference.BLOCK_INFO_HARDENED_CLAY,g);
			coal[g]=new BlockColored(Reference.BLOCK_INFO_COAL,g);
			glowstone[g]=new BlockColoredGlowstone(Reference.BLOCK_INFO_GLOWSTONE,g);
			lapis[g]=new BlockColored(Reference.BLOCK_INFO_LAPIS,g);
			redstone[g]=new BlockColoredPowered(Reference.BLOCK_INFO_REDSTONE,g);
			brick[g]=new BlockColored(Reference.BLOCK_INFO_BRICK,g);
			sand[g]=new BlockColoredSand(Reference.BLOCK_INFO_SAND,g);
			sandstone[g]=new BlockColored(Reference.BLOCK_INFO_SANDSTONE,g);
			glass[g]=new BlockColoredGlass(Reference.BLOCK_INFO_GLASS,g,false);
			glassFoggy[g]=new BlockColoredGlass(Reference.BLOCK_INFO_GLASS_FOGGY,g,true);
			glassPane[g]=new BlockColoredPane(Reference.BLOCK_INFO_GLASS_PANE,g,false);
			glassFoggyPane[g]=new BlockColoredPane(Reference.BLOCK_INFO_GLASS_FOGGY_PANE,g,true);
			bookshelf[g]=new BlockColoredBookshelf(Reference.BLOCK_INFO_BOOKSHELF,g);
			workbench[g]=new BlockColoredWorkbench(Reference.BLOCK_INFO_WORKBENCH,g);
			plank[g]=new BlockColored(Reference.BLOCK_INFO_PLANK,g);
			sapling[g]=new BlockColoredSapling(Reference.BLOCK_INFO_SAPLING,g);
			tulip[g]=new BlockColoredFlower(Reference.BLOCK_INFO_TULIP,g);
			if(thermal)
			{
				rockwool[g]=new BlockColored(Reference.BLOCK_INFO_ROCK_WOOL,g);
			}
			granite[g]=new BlockColored(Reference.BLOCK_INFO_GRANITE,g);
			andesite[g]=new BlockColored(Reference.BLOCK_INFO_ANDESITE,g);
			polishedGranite[g]=new BlockColored(Reference.BLOCK_INFO_POLISHED_GRANITE,g);
			polishedDiorite[g]=new BlockColored(Reference.BLOCK_INFO_POLISHED_DIORITE,g);
			polishedAndesite[g]=new BlockColored(Reference.BLOCK_INFO_POLISHED_ANDESITE,g);
			concrete[g]=new BlockColored(Reference.BLOCK_INFO_CONCRETE,g);
			concretePowder[g]=new BlockColoredConcretePowder(Reference.BLOCK_INFO_CONCRETE_POWDER,g);
			soulSoil[g]=new BlockColored(Reference.BLOCK_INFO_SOUL_SOIL,g);
			basalt[g]=new BlockColored(Reference.BLOCK_INFO_BASALT,g);
			polishedBasalt[g]=new BlockColored(Reference.BLOCK_INFO_POLISHED_BASALT,g);
			smoothStone[g]=new BlockColored(Reference.BLOCK_INFO_SMOOTH_STONE,g);
			smoothSandstone[g]=new BlockColored(Reference.BLOCK_INFO_SMOOTH_SANDSTONE,g);
			smoothQuartz[g]=new BlockColored(Reference.BLOCK_INFO_SMOOTH_QUARTZ,g);
			cutSandstone[g]=new BlockColored(Reference.BLOCK_INFO_CUT_SANDSTONE,g);
			mossyCobble[g]=new BlockColored(Reference.BLOCK_INFO_MOSSY_COBBLE,g);
			mossyStonebrick[g]=new BlockColored(Reference.BLOCK_INFO_MOSSY_STONE_BRICK,g);
			netherBrick[g]=new BlockColored(Reference.BLOCK_INFO_NETHER_BRICK,g);
			crackedNetherBrick[g]=new BlockColored(Reference.BLOCK_INFO_CRACKED_NETHER_BRICK,g);
			chiseledNetherBrick[g]=new BlockColored(Reference.BLOCK_INFO_CHISELED_NETHER_BRICK,g);
			endStoneBrick[g]=new BlockColored(Reference.BLOCK_INFO_END_STONE_BRICK,g);
			boneBlock[g]=new BlockColored(Reference.BLOCK_INFO_BONE_BLOCK,g);
			glazedTerracotta[g]=new BlockColored(Reference.BLOCK_INFO_GLAZED_TERRACOTTA,g);
			netheriteBlock[g]=new BlockColored(Reference.BLOCK_INFO_NETHERITE_BLOCK,g);
			cryingObsidian[g]=new BlockColoredObsidian(Reference.BLOCK_INFO_CRYING_OBSIDIAN,g).setLightLevel(10.0f/15.0f);
			chain[g]=new BlockColoredChain(Reference.BLOCK_INFO_CHAIN,g);
			cornflower[g]=new BlockColoredFlower(Reference.BLOCK_INFO_CORNFLOWER,g);
		}
		for(int i=0;i<colors;i++)
		{
			log[i]=new BlockDyedLog(Reference.BLOCK_INFO_LOG,i);
			leaf[i]=new BlockDyedLeaves(Reference.BLOCK_INFO_LEAVES,i);
			chest[i]=new BlockDyedChest(Reference.BLOCK_INFO_CHEST,i);
		}
		add(wool,stone,cobble,stonebrick,stonebrickCracked,stonebrickCarved,diorite,brick,clay,hardenedClay,sand,sandstone,
				glass,glassFoggy,glassPane,glassFoggyPane,quartz,obsidian,soulsand,glowstone,coal,lapis,redstone,rockwool,
				bookshelf,workbench,chest,sapling,log,leaf,plank,tulip,
				granite,polishedGranite,polishedDiorite,andesite,polishedAndesite,smoothStone,mossyCobble,mossyStonebrick,
				smoothSandstone,cutSandstone,smoothQuartz,concrete,concretePowder,glazedTerracotta,netherBrick,
				crackedNetherBrick,chiseledNetherBrick,soulSoil,basalt,polishedBasalt,endStoneBrick,boneBlock,netheriteBlock,
				cryingObsidian,chain,cornflower);
		initializeTileColored();
		registerWashing();
	}
	/** Stairs, slabs and walls of the dyed blocks with the vanilla shapes, the iron trapdoor and pistons. */
	private static void initializeTileColored()
	{
		stoneStairs=stairs(stone,Reference.BLOCK_INFO_STONE);
		graniteStairs=stairs(granite,Reference.BLOCK_INFO_GRANITE);
		polishedGraniteStairs=stairs(polishedGranite,Reference.BLOCK_INFO_POLISHED_GRANITE);
		dioriteStairs=stairs(diorite,Reference.BLOCK_INFO_DIORITE);
		polishedDioriteStairs=stairs(polishedDiorite,Reference.BLOCK_INFO_POLISHED_DIORITE);
		andesiteStairs=stairs(andesite,Reference.BLOCK_INFO_ANDESITE);
		polishedAndesiteStairs=stairs(polishedAndesite,Reference.BLOCK_INFO_POLISHED_ANDESITE);
		mossyCobbleStairs=stairs(mossyCobble,Reference.BLOCK_INFO_MOSSY_COBBLE);
		mossyStonebrickStairs=stairs(mossyStonebrick,Reference.BLOCK_INFO_MOSSY_STONE_BRICK);
		endStoneBrickStairs=stairs(endStoneBrick,Reference.BLOCK_INFO_END_STONE_BRICK);
		smoothSandstoneStairs=stairs(smoothSandstone,Reference.BLOCK_INFO_SMOOTH_SANDSTONE);
		smoothQuartzStairs=stairs(smoothQuartz,Reference.BLOCK_INFO_SMOOTH_QUARTZ);
		netherBrickStairs=stairs(netherBrick,Reference.BLOCK_INFO_NETHER_BRICK);

		stoneSlab=shape(new BlockDyedSlab(Reference.BLOCK_INFO_STONE,"stone_slab"),stone,Shape.Kind.SLAB);
		cutSandstoneSlab=shape(new BlockDyedSlab(Reference.BLOCK_INFO_CUT_SANDSTONE,"cut_sandstone_slab"),cutSandstone,Shape.Kind.SLAB);
		dioriteSlab=shape(new BlockDyedSlab(Reference.BLOCK_INFO_DIORITE,"diorite_slab"),diorite,Shape.Kind.SLAB);

		brickWall=wall(brick,Reference.BLOCK_INFO_BRICK);
		stonebrickWall=wall(stonebrick,Reference.BLOCK_INFO_STONE_BRICK);
		mossyStonebrickWall=wall(mossyStonebrick,Reference.BLOCK_INFO_MOSSY_STONE_BRICK);
		graniteWall=wall(granite,Reference.BLOCK_INFO_GRANITE);
		dioriteWall=wall(diorite,Reference.BLOCK_INFO_DIORITE);
		andesiteWall=wall(andesite,Reference.BLOCK_INFO_ANDESITE);
		netherBrickWall=wall(netherBrick,Reference.BLOCK_INFO_NETHER_BRICK);
		sandstoneWall=wall(sandstone,Reference.BLOCK_INFO_SANDSTONE);
		endStoneBrickWall=wall(endStoneBrick,Reference.BLOCK_INFO_END_STONE_BRICK);

		ironTrapdoor=new BlockDyedTrapdoor(Reference.BLOCK_INFO_IRON_TRAPDOOR);
		piston=new BlockDyedPiston(false,"piston");
		stickyPiston=new BlockDyedPiston(true,"sticky_piston");
		pistonHead=new BlockDyedPistonHead();
		TILE_COLORED.add(ironTrapdoor);
		TILE_COLORED.add(piston);
		TILE_COLORED.add(stickyPiston);
	}

	/** A shape made from a dyed block, for its recipe: stairs, slab or wall, and the dyed block it is made of. */
	public static class Shape
	{
		public enum Kind
		{
			STAIRS,SLAB,WALL
		}

		public final Block block;
		public final Block[] base;
		public final Kind kind;

		Shape(Block block,Block[] base,Kind kind)
		{
			this.block=block;
			this.base=base;
			this.kind=kind;
		}
	}
	public static final List<Shape> SHAPES=new ArrayList<Shape>();

	private static Block shape(Block block,Block[] base,Shape.Kind kind)
	{
		SHAPES.add(new Shape(block,base,kind));
		TILE_COLORED.add(block);
		return block;
	}
	private static Block stairs(Block[] base,BlockInfo info)
	{
		return shape(new BlockDyedStairs(base[0],info,info.blockName+"_stairs"),base,Shape.Kind.STAIRS);
	}
	private static Block wall(Block[] base,BlockInfo info)
	{
		return shape(new BlockDyedWall(info,info.blockName+"_wall"),base,Shape.Kind.WALL);
	}

	private static void add(Block[]... kinds)
	{
		for(Block[] kind:kinds)
		{
			if(kind!=null)
			{
				for(Block block:kind)
				{
					ALL.add(block);
				}
			}
		}
	}
	@SubscribeEvent
	public static void onBlockRegister(final RegistryEvent.Register<Block> event)
	{
		for(Block block:ALL)
		{
			event.getRegistry().register(block);
		}
		for(Block block:TILE_COLORED)
		{
			event.getRegistry().register(block);
		}
		event.getRegistry().register(pistonHead);
		GameRegistry.registerTileEntity(TileEntityDyedChest.class,new ResourceLocation(Reference.MOD_ID,"chest"));
		GameRegistry.registerTileEntity(TileEntityColor.class,new ResourceLocation(Reference.MOD_ID,"color"));
		GameRegistry.registerTileEntity(TileEntityDyedPiston.class,new ResourceLocation(Reference.MOD_ID,"moving_piston"));
	}

	private static void registerWashing()
	{
		washed(wool,new ItemStack(Blocks.WOOL,1,0));
		washed(stonebrick,new ItemStack(Blocks.STONEBRICK,1,0));
		washed(stonebrickCracked,new ItemStack(Blocks.STONEBRICK,1,2));
		washed(stonebrickCarved,new ItemStack(Blocks.STONEBRICK,1,3));
		washed(stone,new ItemStack(Blocks.STONE,1,0));
		washed(cobble,new ItemStack(Blocks.COBBLESTONE));
		washed(diorite,new ItemStack(Blocks.STONE,1,3));
		washed(obsidian,new ItemStack(Blocks.OBSIDIAN));
		washed(soulsand,new ItemStack(Blocks.SOUL_SAND));
		washed(quartz,new ItemStack(Blocks.QUARTZ_BLOCK,1,0));
		washed(clay,new ItemStack(Blocks.STAINED_HARDENED_CLAY,1,0));
		washed(hardenedClay,new ItemStack(Blocks.HARDENED_CLAY));
		washed(coal,new ItemStack(Blocks.COAL_BLOCK));
		washed(glowstone,new ItemStack(Blocks.GLOWSTONE));
		washed(lapis,new ItemStack(Blocks.LAPIS_BLOCK));
		washed(redstone,new ItemStack(Blocks.REDSTONE_BLOCK));
		washed(brick,new ItemStack(Blocks.BRICK_BLOCK));
		washed(sand,new ItemStack(Blocks.SAND,1,0));
		washed(sandstone,new ItemStack(Blocks.SANDSTONE,1,0));
		washed(glass,new ItemStack(Blocks.GLASS));
		washed(glassPane,new ItemStack(Blocks.GLASS_PANE));
		washed(bookshelf,new ItemStack(Blocks.BOOKSHELF));
		washed(workbench,new ItemStack(Blocks.CRAFTING_TABLE));
		washed(chest,new ItemStack(Blocks.CHEST));
		washed(plank,new ItemStack(Blocks.PLANKS,1,0));
		washed(log,new ItemStack(Blocks.LOG,1,0));
		washed(leaf,new ItemStack(Blocks.LEAVES,1,0));
		washed(sapling,new ItemStack(Blocks.SAPLING,1,0));
		washed(granite,new ItemStack(Blocks.STONE,1,1));
		washed(polishedGranite,new ItemStack(Blocks.STONE,1,2));
		washed(polishedDiorite,new ItemStack(Blocks.STONE,1,4));
		washed(andesite,new ItemStack(Blocks.STONE,1,5));
		washed(polishedAndesite,new ItemStack(Blocks.STONE,1,6));
		washed(concrete,new ItemStack(Blocks.CONCRETE,1,0));
		washed(concretePowder,new ItemStack(Blocks.CONCRETE_POWDER,1,0));
		washed(cutSandstone,new ItemStack(Blocks.SANDSTONE,1,2));
		washed(mossyCobble,new ItemStack(Blocks.MOSSY_COBBLESTONE));
		washed(mossyStonebrick,new ItemStack(Blocks.STONEBRICK,1,1));
		washed(netherBrick,new ItemStack(Blocks.NETHER_BRICK));
		washed(endStoneBrick,new ItemStack(Blocks.END_BRICKS));
		washed(boneBlock,new ItemStack(Blocks.BONE_BLOCK));
		washed(glazedTerracotta,new ItemStack(Blocks.WHITE_GLAZED_TERRACOTTA));
	}
	private static void washed(Block[] dyed,ItemStack vanilla)
	{
		for(Block block:dyed)
		{
			WASHED.put(block,vanilla);
		}
	}

	/**
	 * Ore dictionary names, matching the names Forge gives the vanilla blocks, so recipes from other mods accept the
	 * dyed blocks wherever they accept the vanilla ones. Runs once the items exist.
	 */
	public static void registerOreDictionary()
	{
		// Names Forge has not given the vanilla blocks, so they can be dyed through the same recipes.
		OreDictionary.registerOre("bricksStone",new ItemStack(Blocks.STONEBRICK,1,0));
		OreDictionary.registerOre("bricksStoneCracked",new ItemStack(Blocks.STONEBRICK,1,2));
		OreDictionary.registerOre("bricksStoneCarved",new ItemStack(Blocks.STONEBRICK,1,3));
		OreDictionary.registerOre("blockBrick",new ItemStack(Blocks.BRICK_BLOCK));
		OreDictionary.registerOre("soulsand",new ItemStack(Blocks.SOUL_SAND));
		OreDictionary.registerOre("bookshelf",new ItemStack(Blocks.BOOKSHELF));
		OreDictionary.registerOre("hardenedClay",new ItemStack(Blocks.HARDENED_CLAY));
		OreDictionary.registerOre("stainedClay",new ItemStack(Blocks.STAINED_HARDENED_CLAY,1,OreDictionary.WILDCARD_VALUE));
		ore(wool,"wool","blockWool");
		ore(stonebrick,"bricksStone");
		ore(stonebrickCracked,"bricksStoneCracked");
		ore(stonebrickCarved,"bricksStoneCarved");
		ore(stone,"stone");
		ore(cobble,"cobblestone");
		ore(diorite,"stoneDiorite");
		ore(obsidian,"obsidian");
		ore(soulsand,"soulsand");
		ore(quartz,"blockQuartz");
		ore(clay,"stainedClay");
		ore(hardenedClay,"hardenedClay");
		ore(coal,"blockCoal");
		ore(glowstone,"glowstone");
		ore(lapis,"blockLapis");
		ore(redstone,"blockRedstone");
		ore(brick,"blockBrick");
		ore(sand,"sand");
		ore(sandstone,"sandstone");
		ore(glass,"blockGlass");
		ore(glassFoggy,"blockGlass");
		ore(glassPane,"paneGlass");
		ore(glassFoggyPane,"paneGlass");
		ore(bookshelf,"bookshelf");
		ore(workbench,"workbench");
		ore(chest,"chest","chestWood");
		ore(plank,"plankWood");
		ore(log,"logWood");
		ore(leaf,"treeLeaves");
		ore(sapling,"treeSapling");
		ore(granite,"stoneGranite");
		ore(andesite,"stoneAndesite");
		ore(polishedGranite,"stoneGranitePolished");
		ore(polishedDiorite,"stoneDioritePolished");
		ore(polishedAndesite,"stoneAndesitePolished");
		ore(smoothQuartz,"blockQuartz");
		ore(smoothSandstone,"sandstone");
		ore(cutSandstone,"sandstone");
		if(rockwool!=null)
		{
			// Thermal Foundation registers its own rockwool under the same name.
			ore(rockwool,"blockRockwool");
			// Washed rockwool comes out light gray, the color Thermal Foundation's rockwool is made in.
			Block thermal=ForgeRegistries.BLOCKS.getValue(new ResourceLocation("thermalfoundation","rockwool"));
			if(thermal!=null&&thermal!=Blocks.AIR)
			{
				washed(rockwool,new ItemStack(thermal,1,7));
			}
		}
	}
	private static void ore(Block[] blocks,String... names)
	{
		for(Block block:blocks)
		{
			for(String name:names)
			{
				OreDictionary.registerOre(name,new ItemStack(block,1,OreDictionary.WILDCARD_VALUE));
			}
		}
	}

	/** Same fire settings as the vanilla blocks (encouragement, flammability). */
	public static void registerFlammability()
	{
		fire(wool,30,60);
		fire(plank,5,20);
		fire(log,5,5);
		fire(leaf,30,60);
		fire(bookshelf,30,20);
		fire(tulip,60,100);
		fire(cornflower,60,100);
		fire(coal,5,5);
	}
	private static void fire(Block[] blocks,int encouragement,int flammability)
	{
		for(Block block:blocks)
		{
			Blocks.FIRE.setFireInfo(block,encouragement,flammability);
		}
	}
}
