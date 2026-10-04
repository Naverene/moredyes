package net.neverandy.moredyes.api;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.Loader;
import net.neverandy.moredyes.block.IColoredBlock;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;
import net.neverandy.moredyes.compat.ironchest.ItemDyedIronChest;
import net.neverandy.moredyes.compat.ironchest.TileEntityDyedIronChest;
import net.neverandy.moredyes.compat.storagedrawers.StorageDrawersCompat;
import net.neverandy.moredyes.handler.DyedSheepHandler;
import net.neverandy.moredyes.item.MDItem;
import net.neverandy.moredyes.item.MDItemDye;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.utility.ColorUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;

/**
 * The public API other mods can use to work with More Dyes colors. Everything else in More Dyes is internal and may
 * change between releases; this class keeps its methods.
 *
 * <p>A More Dyes color is identified by its RGB value, 0xRRGGBB, which is also the hex code in its dye's name
 * ("334C59 Dye"). The dyes are items named "moredyes:dye_&lt;vanilla color&gt;", one per vanilla color they are mixed
 * from, with the shades as metadata, so use {@link #getColor(ItemStack)} rather than the item and metadata.
 *
 * <p>Every dye is in the ore dictionary as {@link #ORE_DYES} ("moredyesDye") and "dye".
 */
public final class MoreDyesAPI
{
	/** Goes up when methods are added. */
	public static final int API_VERSION=1;

	/** Ore dictionary name of every More Dyes dye. */
	public static final String ORE_DYES="moredyesDye";

	private static final List<Integer> COLORS;

	static
	{
		List<Integer> colors=new ArrayList<>();
		for(String hex:ColorStrings.ALL)
		{
			colors.add(ColorUtil.fromHex(hex));
		}
		COLORS=Collections.unmodifiableList(colors);
	}

	private MoreDyesAPI(){}

	/** Every More Dyes color as 0xRRGGBB, in the mod's own order (the order of the creative tab). */
	public static List<Integer> colors()
	{
		return COLORS;
	}
	/** Whether a color, 0xRRGGBB, is one of More Dyes' colors. */
	public static boolean isColor(int rgb)
	{
		return COLORS.contains(rgb);
	}
	/** Whether the stack is a More Dyes dye. */
	public static boolean isDye(ItemStack stack)
	{
		return stack.getItem() instanceof MDItemDye;
	}
	/** The dye of a More Dyes color, or an empty stack if the color is not one of More Dyes' colors. */
	public static ItemStack getDye(int rgb,int count)
	{
		int index=COLORS.indexOf(rgb);
		return index<0?ItemStack.EMPTY:MDItem.dyeStack(index,count);
	}
	/**
	 * The More Dyes color of a dye or of any dyed More Dyes item, such as dyed wool, planks, chests or (with Iron
	 * Chests or Storage Drawers) dyed iron chests and drawers. Empty if the stack has no More Dyes color.
	 */
	public static OptionalInt getColor(ItemStack stack)
	{
		Item item=stack.getItem();
		int meta=stack.getMetadata();
		if(item instanceof MDItemDye)
		{
			return color(((MDItemDye)item).getColorIndex(meta));
		}
		if(item instanceof ItemBlock&&((ItemBlock)item).getBlock() instanceof IColoredBlock)
		{
			return color(((IColoredBlock)((ItemBlock)item).getBlock()).getColorIndex(meta));
		}
		if(Loader.isModLoaded(IronChestCompat.MOD_ID)&&item instanceof ItemDyedIronChest)
		{
			return color(IronChestCompat.clampColor(meta));
		}
		if(Loader.isModLoaded(StorageDrawersCompat.MOD_ID)&&item==StorageDrawersCompat.drawersItem)
		{
			return color(StorageDrawersCompat.colorOf(stack));
		}
		return OptionalInt.empty();
	}
	/**
	 * The More Dyes color of a placed block, or empty if it is not a dyed More Dyes block. Dyed iron chests and drawers
	 * keep their color in their tile entity, so for them use {@link #getColor(IBlockAccess,BlockPos)}.
	 */
	public static OptionalInt getColor(IBlockState state)
	{
		Block block=state.getBlock();
		return block instanceof IColoredBlock?color(((IColoredBlock)block).getColorIndex(block.getMetaFromState(state))):OptionalInt.empty();
	}
	/** The More Dyes color of the block at a position, or empty if it is not a dyed More Dyes block. */
	public static OptionalInt getColor(IBlockAccess world,BlockPos pos)
	{
		IBlockState state=world.getBlockState(pos);
		OptionalInt color=getColor(state);
		if(color.isPresent())
		{
			return color;
		}
		TileEntity tile=world.getTileEntity(pos);
		if(Loader.isModLoaded(IronChestCompat.MOD_ID)&&tile instanceof TileEntityDyedIronChest)
		{
			return color(((TileEntityDyedIronChest)tile).getColor());
		}
		if(Loader.isModLoaded(StorageDrawersCompat.MOD_ID)&&state.getBlock()==StorageDrawersCompat.drawers&&tile instanceof TileEntityDrawers)
		{
			return color(StorageDrawersCompat.colorOf((TileEntityDrawers)tile));
		}
		return OptionalInt.empty();
	}
	/** The More Dyes color of a sheep's wool, or empty if it has a vanilla color. */
	public static OptionalInt getSheepColor(EntitySheep sheep)
	{
		return color(DyedSheepHandler.getColor(sheep));
	}
	/**
	 * Dyes a sheep's wool a More Dyes color, like using the dye on it. Call it on the server; players who can see the
	 * sheep are told. Returns false, and does nothing, if the color is not one of More Dyes' colors.
	 */
	public static boolean setSheepColor(EntitySheep sheep,int rgb)
	{
		int index=COLORS.indexOf(rgb);
		if(index>=0)
		{
			DyedSheepHandler.setColor(sheep,index);
		}
		return index>=0;
	}
	/** Takes a sheep's More Dyes color away, so its wool shows its vanilla color again. Call it on the server. */
	public static void clearSheepColor(EntitySheep sheep)
	{
		DyedSheepHandler.setColor(sheep,-1);
	}
	/** The color a More Dyes color is drawn in. On 1.12.2 that is the color itself. */
	public static int displayColor(int rgb)
	{
		return rgb;
	}
	/**
	 * The vanilla dye color that looks closest to a color, 0xRRGGBB (by CIEDE2000 distance), the same one the newer
	 * versions of More Dyes use.
	 */
	public static EnumDyeColor nearestVanillaColor(int rgb)
	{
		return EnumDyeColor.byMetadata(NearestColor.of(rgb));
	}

	private static OptionalInt color(int index)
	{
		return index>=0&&index<COLORS.size()?OptionalInt.of(COLORS.get(index)):OptionalInt.empty();
	}
}
