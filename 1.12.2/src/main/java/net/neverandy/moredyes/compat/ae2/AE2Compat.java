package net.neverandy.moredyes.compat.ae2;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.LogHelper;

/**
 * Applied Energistics 2's cables, paint balls and Color Applicator only know the 16 vanilla colors, so each More Dyes
 * dye counts as the vanilla color it looks closest to ({@link NearestDye}). Written against AE2 rv6.
 * <ul>
 * <li>Every dye is in the ore dictionary as "moredyes" and its vanilla color, such as moredyesLightBlue. Not as
 * AE2's dyeLightBlue: our own dye mixes (CraftManager) take those, so two of our dyes would mix into a third.</li>
 * <li>Recipes like AE2's own take those names: eight fluix cables (glass, covered, smart, dense covered or dense
 * smart) around a dye make eight cables of its color, and eight matter balls make eight paint balls.</li>
 * <li>The Color Applicator finds a dye's color through a private map from ore name to AE2 color; the names are added
 * to it, so it takes our dyes too.</li>
 * </ul>
 * More Dyes doesn't compile against AE2: AE2's items are looked up by name and its map is reached by reflection. Only
 * used when AE2 is installed.
 */
public class AE2Compat
{
	public static final String MOD_ID="appliedenergistics2";

	/** The vanilla colors in wool metadata order, which is also AE2's color order (its item damage values). */
	private static final String[] COLORS={"White","Orange","Magenta","LightBlue","Yellow","Lime","Pink","Gray",
			"LightGray","Cyan","Purple","Blue","Brown","Green","Red","Black"};
	/** AE2's AEColor constants, in the same order. */
	private static final String[] AE_COLORS={"WHITE","ORANGE","MAGENTA","LIGHT_BLUE","YELLOW","LIME","PINK","GRAY",
			"LIGHT_GRAY","CYAN","PURPLE","BLUE","BROWN","GREEN","RED","BLACK"};
	/** AE2's part damage of the white cable of each kind; the next 15 are the other colors, then the fluix one. */
	private static final int[] CABLES={0,20,40,60,500};
	private static final String[] CABLE_NAMES={"glass","covered","smart","dense_smart","dense_covered"};
	private static final int FLUIX=16;
	private static final int MATTER_BALL=6;

	private static String oreName(int color)
	{
		return Reference.MOD_ID+COLORS[color];
	}

	public static void registerRecipes(IForgeRegistry<IRecipe> registry)
	{
		for(int i=0;i<ColorStrings.ALL.length;i++)
		{
			OreDictionary.registerOre(oreName(NearestDye.of(ColorStrings.ALL[i])),MDItem.dyeStack(i,1));
		}
		Item part=ForgeRegistries.ITEMS.getValue(new ResourceLocation(MOD_ID,"part"));
		Item material=ForgeRegistries.ITEMS.getValue(new ResourceLocation(MOD_ID,"material"));
		Item paintBall=ForgeRegistries.ITEMS.getValue(new ResourceLocation(MOD_ID,"paint_ball"));
		if(part==null||material==null||paintBall==null)
		{
			LogHelper.warn("Applied Energistics 2's items weren't found, so More Dyes dyes can't color its cables");
			return;
		}
		for(int color=0;color<COLORS.length;color++)
		{
			String name=AE_COLORS[color].toLowerCase();
			for(int c=0;c<CABLES.length;c++)
			{
				ring(registry,"cable_"+CABLE_NAMES[c]+"_"+name,new ItemStack(part,8,CABLES[c]+color),
						new ItemStack(part,1,CABLES[c]+FLUIX),color);
			}
			ring(registry,"paint_ball_"+name,new ItemStack(paintBall,8,color),new ItemStack(material,1,MATTER_BALL),color);
		}
	}

	/** Eight of {@code around} around a dye of a color make {@code result}. */
	private static void ring(IForgeRegistry<IRecipe> registry,String name,ItemStack result,ItemStack around,int color)
	{
		ResourceLocation id=new ResourceLocation(Reference.MOD_ID,"ae2/"+name);
		registry.register(new ShapedOreRecipe(id,result,"aaa","aba","aaa",'a',around,'b',oreName(color))
				.setRegistryName(id));
	}

	/** Lets the Color Applicator take our dyes. */
	@SuppressWarnings({"unchecked","rawtypes"})
	public static void postInit()
	{
		try
		{
			Class<? extends Enum> aeColor=(Class<? extends Enum>)Class.forName("appeng.api.util.AEColor");
			Field field=Class.forName("appeng.items.tools.powered.ToolColorApplicator").getDeclaredField("ORE_TO_COLOR");
			field.setAccessible(true);
			Map<Integer,Object> oreToColor=(Map<Integer,Object>)field.get(null);
			for(int color=0;color<COLORS.length;color++)
			{
				oreToColor.put(OreDictionary.getOreID(oreName(color)),Enum.valueOf(aeColor,AE_COLORS[color]));
			}
			LogHelper.info("Applied Energistics 2's Color Applicator takes More Dyes dyes");
		}
		catch(ReflectiveOperationException|RuntimeException e)
		{
			LogHelper.warn("Couldn't add More Dyes dyes to Applied Energistics 2's Color Applicator: "+e);
		}
	}
}
