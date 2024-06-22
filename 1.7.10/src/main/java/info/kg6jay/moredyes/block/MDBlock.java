package info.kg6jay.moredyes.block;

import cpw.mods.fml.common.registry.GameRegistry;
import info.kg6jay.moredyes.item.MDItemBlockColored;
import info.kg6jay.moredyes.reference.ColorStrings;
import info.kg6jay.moredyes.reference.Reference;
import info.kg6jay.moredyes.utility.BlockInfo;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MDBlock
{
	public static String[][] colorStrings=new String[][]
			{
			ColorStrings.WHITE,
			ColorStrings.ORANGE,
			ColorStrings.MAGENTA,
			ColorStrings.LBLUE,
			ColorStrings.YELLOW,
			ColorStrings.LIME,
			ColorStrings.PINK,
			ColorStrings.DGRAY,
			ColorStrings.LGRAY,
			ColorStrings.CYAN,
			ColorStrings.PURPLE,
			ColorStrings.BLUE,
			ColorStrings.BROWN,
			ColorStrings.GREEN,
			ColorStrings.RED
			};
	public static BlockInfo[] info=new BlockInfo[]
			{
			Reference.BLOCK_INFO_WOOL,
			Reference.BLOCK_INFO_STONE_BRICK,
			Reference.BLOCK_INFO_STONE_BRICK_CARVED,
			Reference.BLOCK_INFO_STONE_BRICK_CRACKED,
			Reference.BLOCK_INFO_STONE,
			Reference.BLOCK_INFO_COBBLE,
			Reference.BLOCK_INFO_OBSIDIAN,
			Reference.BLOCK_INFO_SOULSAND,
			Reference.BLOCK_INFO_QUARTZ,
			Reference.BLOCK_INFO_CLAY,
			Reference.BLOCK_INFO_COAL,
			Reference.BLOCK_INFO_GLOWSTONE,
			Reference.BLOCK_INFO_LAPIS,
			Reference.BLOCK_INFO_REDSTONE,
			Reference.BLOCK_INFO_PLANK,
			Reference.BLOCK_INFO_TULIP,
			Reference.BLOCK_INFO_LOG,
			Reference.BLOCK_INFO_LEAVES,
			Reference.BLOCK_INFO_SAPLING,
			Reference.BLOCK_INFO_GLASS,
			Reference.BLOCK_INFO_WORKBENCH,
			Reference.BLOCK_INFO_BRICK,
			Reference.BLOCK_INFO_SAND,
            Reference.BLOCK_INFO_CHEST,
			Reference.BLOCK_INFO_HARDENED_CLAY
			};
	public static String[] colors=new String[]{"white","orange","magenta","lightBlue","yellow","lime","pink","darkGray","lightGray","cyan","purple","blue","brown","green","red"};
	public static Block[] wool;
	public static Block[] stoneBrick;
	public static Block[] stoneBrickCarved;
	public static Block[] stoneBrickCracked;
	public static Block[] obsidian;
	public static Block[] stone;
	public static Block[] cobble;
	public static Block[] soulsand;
	public static Block[] quartz;

	public static Block[] clay;
	public static Block[] coal;
	public static Block[] glowstone;
	public static Block[] lapis;
	public static Block[] redstone;

	public static Block[] plank;

	public static Block[] rockwool;
	public static Block[] tulip;
	public static Block[] log;
	public static Block[] leaf;
	public static Block[] sapling;

	public static Block[] glassClear;
	public static Block[] glassFoggy;
	public static Block[] glassClearPane;
	public static Block[] glassFoggyPane;
	public static Block[] brick;
	public static Block[] sand;
	public static Block[] workbench;
    public static Block[] chest;
	public static Block[] hardenedClay;

	public static void initialize()
	{
		int l=colors.length;

		wool=new Block[l];
		stoneBrick = new Block[l];
		stoneBrickCarved = new Block[l];
		stoneBrickCracked = new Block[l];
		stone=new Block[l];
		cobble = new Block[l];
		obsidian = new Block[l];
		soulsand = new Block[l];
		quartz=new Block[l];
		clay=new Block[l];
		coal=new Block[l];
		glowstone=new Block[l];
		lapis = new Block[l];
		redstone = new Block[l];
		plank = new Block[l];

		tulip = new Block[l];
		log = new Block[l];
		leaf = new Block[l];
		sapling = new Block[l];
		glassClear = new Block[l];
		glassFoggy = new Block[l];
		glassClearPane = new Block[l];
		glassFoggyPane = new Block[l];
		brick = new Block[l];
		sand= new Block[l];

		workbench = new Block[l];
        chest = new Block[l];
		hardenedClay = new Block[l];

		for(int i=0;i<colors.length;i++)
		{
			wool[i]=new MDBlockColored(colorStrings[i],info[0],colors[i]);
			stoneBrick[i]=new MDBlockColored(colorStrings[i],info[1],colors[i]);
			stoneBrickCarved[i]=new MDBlockColored(colorStrings[i],info[2],colors[i]);
			stoneBrickCracked[i]=new MDBlockColored(colorStrings[i],info[3],colors[i]);
			stone[i]=new MDBlockColored(colorStrings[i],info[4],colors[i]);
			cobble[i]=new MDBlockColored(colorStrings[i],info[5],colors[i]);
			obsidian[i]=new MDBlockColored(colorStrings[i],info[6],colors[i]);
			soulsand[i]=new MDBlockColored(colorStrings[i],info[7],colors[i]);
			quartz[i]=new MDBlockColoredMulti(colorStrings[i], info[8], colors[i]);
			clay[i]=new MDBlockColored(colorStrings[i],info[9],colors[i]);
			coal[i]=new MDBlockColored(colorStrings[i],info[10],colors[i]);
			glowstone[i]=new MDBlockColored(colorStrings[i],info[11],colors[i]).setLightLevel(1.0f);
			lapis[i]=new MDBlockColored(colorStrings[i],info[12],colors[i]);
			redstone[i]=new MDBlockColoredPowered(colorStrings[i],info[13],colors[i]);
			plank[i]=new MDBlockColored(colorStrings[i],info[14],colors[i]);
			tulip[i]=new MDBlockFlower(colorStrings[i],info[15],colors[i]);
			log[i]=new MDBlockLog(colorStrings[i],info[16],colors[i]);
			leaf[i]=new MDBlockLeaf(colorStrings[i],info[17],colors[i],i);
			sapling[i]=new MDBlockSapling(colorStrings[i],info[18],colors[i],i);
			glassClear[i]=new MDBlockGlass(colorStrings[i],info[19],colors[i],i,"clear");
			glassFoggy[i]=new MDBlockGlass(colorStrings[i],info[19],colors[i],i,"foggy");
			glassClearPane[i]=new MDBlockGlassPane(colorStrings[i],info[19],colors[i],i,"clear");
			glassFoggyPane[i]=new MDBlockGlassPane(colorStrings[i],info[19],colors[i],i,"foggy");
			brick[i]=new MDBlockColored(colorStrings[i],info[21],colors[i]);
			sand[i]=new MDBlockColoredSand(colorStrings[i],info[22],colors[i]);
            workbench[i]=new MDBlockWorkbench(colorStrings[i],info[20],colors[i]);
            chest[i] = new MDColoredChest(colorStrings[i], info[23], colors[i]);
			hardenedClay[i] = new MDBlockColored(colorStrings[i], info[23], colors[i]);
		}
	}
	public static void register()
	{
		for(int i=0;i<wool.length;i++)
		{
			GameRegistry.registerBlock(wool[i], MDItemBlockColored.class,((IBlockColored) wool[i]).getColorSet()+"MixWool");
			GameRegistry.registerBlock(stoneBrick[i], MDItemBlockColored.class,((IBlockColored) stoneBrick[i]).getColorSet()+"MixStonebrick");
			GameRegistry.registerBlock(stoneBrickCarved[i],MDItemBlockColored.class,((IBlockColored) stoneBrickCarved[i]).getColorSet()+"MixStonebrickCarved");
			GameRegistry.registerBlock(stoneBrickCracked[i],MDItemBlockColored.class,((IBlockColored) stoneBrickCracked[i]).getColorSet()+"MixStonebrickCracked");
			GameRegistry.registerBlock(stone[i],MDItemBlockColored.class,((IBlockColored) stone[i]).getColorSet()+"MixStone");
			GameRegistry.registerBlock(cobble[i],MDItemBlockColored.class,((IBlockColored) cobble[i]).getColorSet()+"MixCobble");
			GameRegistry.registerBlock(obsidian[i],MDItemBlockColored.class,((IBlockColored) obsidian[i]).getColorSet()+"MixObsidian");
			GameRegistry.registerBlock(soulsand[i],MDItemBlockColored.class,((IBlockColored) soulsand[i]).getColorSet()+"MixSoulsand");
			GameRegistry.registerBlock(quartz[i],MDItemBlockColored.class,((IBlockColored) quartz[i]).getColorSet()+"MixQuartz");
			GameRegistry.registerBlock(clay[i],MDItemBlockColored.class,((IBlockColored) clay[i]).getColorSet()+"MixClay");
			GameRegistry.registerBlock(coal[i],MDItemBlockColored.class,((IBlockColored) coal[i]).getColorSet()+"MixCoal");
			GameRegistry.registerBlock(glowstone[i],MDItemBlockColored.class,((IBlockColored) glowstone[i]).getColorSet()+"MixGlowstone");
			GameRegistry.registerBlock(lapis[i],MDItemBlockColored.class,((IBlockColored) lapis[i]).getColorSet()+"MixLapis");
			GameRegistry.registerBlock(redstone[i],MDItemBlockColored.class,((IBlockColored) redstone[i]).getColorSet()+"MixRedstone");
			GameRegistry.registerBlock(plank[i],MDItemBlockColored.class,((IBlockColored) plank[i]).getColorSet()+"MixPlank");
			GameRegistry.registerBlock(tulip[i],MDItemBlockColored.class,((IBlockColored) tulip[i]).getColorSet()+"MixTulip");
			GameRegistry.registerBlock(log[i],MDItemBlockColored.class,((IBlockColored) log[i]).getColorSet()+"MixLog");
			GameRegistry.registerBlock(leaf[i],MDItemBlockColored.class,((IBlockColored) leaf[i]).getColorSet()+"MixLeaf");
			GameRegistry.registerBlock(sapling[i],MDItemBlockColored.class,((IBlockColored) sapling[i]).getColorSet()+"MixSapling");
			GameRegistry.registerBlock(glassClear[i],MDItemBlockColored.class,((IBlockColored) glassClear[i]).getColorSet()+"MixGlassClear");
			GameRegistry.registerBlock(glassFoggy[i],MDItemBlockColored.class,((IBlockColored) glassFoggy[i]).getColorSet()+"MixGlassFoggy");
			GameRegistry.registerBlock(glassClearPane[i],MDItemBlockColored.class,((IBlockColored) glassClearPane[i]).getColorSet()+"MixGlassClearPane");
			GameRegistry.registerBlock(glassFoggyPane[i],MDItemBlockColored.class,((IBlockColored) glassFoggyPane[i]).getColorSet()+"MixGlassFoggyPane");
			GameRegistry.registerBlock(brick[i],MDItemBlockColored.class,((IBlockColored) brick[i]).getColorSet()+"MixBrick");
			GameRegistry.registerBlock(sand[i], MDItemBlockColored.class,((IBlockColored) sand[i]).getColorSet()+"MixSand");
            GameRegistry.registerBlock(workbench[i],MDItemBlockColored.class,((IBlockColored) workbench[i]).getColorSet()+"MixWorkbench");
			GameRegistry.registerBlock(hardenedClay[i],MDItemBlockColored.class,((IBlockColored) hardenedClay[i]).getColorSet()+"MixHardenedClay");
		}
	}
	public static void registerTileEntities()
	{

	}
	public static void registerOreDictionary()
	{
		for(int i=0;i<16;i++)
		{
			OreDictionary.registerOre("wool",new ItemStack(Blocks.wool,1,i));
			OreDictionary.registerOre("blockWool",new ItemStack(Blocks.wool,1,i));
			OreDictionary.registerOre("stainedClay",new ItemStack(Blocks.stained_hardened_clay,1,i));
		}
		OreDictionary.registerOre("soulsand",new ItemStack(Blocks.soul_sand,1));
		for(int a=0;a<wool.length;a++)
		{
			for(int i=0;i<=((MDBlockColored)wool[a]).getMaxMeta();i++)
			{

				OreDictionary.registerOre("wool", new ItemStack(wool[a],1,i));
				OreDictionary.registerOre("blockWool", new ItemStack(wool[a],1,i));
				OreDictionary.registerOre("bricksStone", new ItemStack(stoneBrick[a],1,i));
				OreDictionary.registerOre("bricksStoneCracked", new ItemStack(stoneBrickCracked[a],1,i));
				OreDictionary.registerOre("bricksStoneCarved", new ItemStack(stoneBrickCarved[a],1,i));
				OreDictionary.registerOre("blockObsidian", new ItemStack(obsidian[a],1,i));
				OreDictionary.registerOre("stone", new ItemStack(stone[a],1,i));
				OreDictionary.registerOre("cobblestone", new ItemStack(cobble[a],1,i));
				OreDictionary.registerOre("soulsand", new ItemStack(soulsand[a],1,i));
				OreDictionary.registerOre("blockQuartz",new ItemStack(quartz[a],1,i));
				OreDictionary.registerOre("blockRedstone",new ItemStack(redstone[a],1,i));
				OreDictionary.registerOre("blockCoal",new ItemStack(coal[a],1,i));
				OreDictionary.registerOre("plankWood",new ItemStack(plank[a],1,i));
				OreDictionary.registerOre("glowstone",new ItemStack(glowstone[a],1,i));
				OreDictionary.registerOre("lapisBlock",new ItemStack(lapis[a],1,i));
				OreDictionary.registerOre("stainedClay",new ItemStack(clay[a],1,i));
				OreDictionary.registerOre("logWood", new ItemStack(log[a],1,i));
                OreDictionary.registerOre("workbench", new ItemStack(workbench[a], 1, i));
				OreDictionary.registerOre("chest", new ItemStack(chest[a], 1, i));
				OreDictionary.registerOre("hardenedClay", new ItemStack(hardenedClay[a], 1, i));
			}
		}
	}
}
