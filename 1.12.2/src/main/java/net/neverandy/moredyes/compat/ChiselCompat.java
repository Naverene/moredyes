package net.neverandy.moredyes.compat;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.neverandy.moredyes.block.IColoredBlock;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.LogHelper;

/**
 * Makes the dyed blocks chiselable with Chisel, like the 1.7.10 version does. Every shade joins the carving group of
 * its vanilla block: Chisel's own group where it has one, otherwise a group of ours that also holds the vanilla
 * block. So a chisel turns the vanilla block into any dyed shade and back, and the Auto Chisel does too.
 * <p>
 * Everything goes through Chisel's IMC messages ("add_variation"), so this uses no Chisel classes and needs no
 * dependency; the messages must be sent before Chisel reads them, between init and postInit. The Chisel group names
 * are the ones Chisel 1.0.2 for 1.12.2 gives its vanilla blocks (Features.java). Coal and lapis blocks have no IMC
 * group: Chisel groups those by their ore dictionary names (blockCoal, blockLapis), which the dyed ones have too.
 * <p>
 * Rechiseled needs no code: it reads assets/moredyes/chiseling_recipes, written by tools/make_chisel_recipes.py.
 */
public class ChiselCompat
{
	public static final String CHISEL="chisel";

	private static int messages;

	public static void sendIMC()
	{
		messages=0;
		// Groups Chisel already has, holding the vanilla block.
		existing("stonebrick",MDBlock.stone,MDBlock.stonebrick,MDBlock.stonebrickCracked,MDBlock.stonebrickCarved);
		existing("cobblestone",MDBlock.cobble);
		existing("diorite",MDBlock.diorite);
		existing("obsidian",MDBlock.obsidian);
		existing("quartz",MDBlock.quartz);
		existing("glowstone",MDBlock.glowstone);
		existing("redstone",MDBlock.redstone);
		existing("sandstoneyellow",MDBlock.sandstone);
		existing("planks-oak",MDBlock.plank);
		existing("wool_white",MDBlock.wool);
		existing("hardenedclay",MDBlock.hardenedClay);
		existing("bricks",MDBlock.brick);
		existing("glass",MDBlock.glass);
		existing("glasspane",MDBlock.glassPane);
		existing("bookshelf_oak",MDBlock.bookshelf);

		// Groups of our own, each starting with the vanilla block.
		own("soul_sand",Blocks.SOUL_SAND,0,MDBlock.soulsand);
		own("sand",Blocks.SAND,0,MDBlock.sand);
		own("stained_hardened_clay",Blocks.STAINED_HARDENED_CLAY,0,MDBlock.clay);
		own("chest",Blocks.CHEST,0,MDBlock.chest);
		LogHelper.info("Sent "+messages+" chisel variations to Chisel");
	}

	private static void existing(String group,Block[]... dyed)
	{
		for(Block[] blocks:dyed)
		{
			for(Block block:blocks)
			{
				for(int meta=0;meta<((IColoredBlock)block).getShadeCount();meta++)
				{
					addVariation(group,block,meta);
				}
			}
		}
	}

	private static void own(String name,Block vanilla,int vanillaMeta,Block[]... dyed)
	{
		String group=Reference.MOD_ID+":"+name;
		addVariation(group,vanilla,vanillaMeta);
		existing(group,dyed);
	}

	/** Chisel adds the variations of each group in the order they are sent, after its own ones. */
	private static void addVariation(String group,Block block,int meta)
	{
		NBTTagCompound tag=new NBTTagCompound();
		tag.setString("group",group);
		tag.setString("block",block.getRegistryName().toString());
		tag.setInteger("meta",meta);
		FMLInterModComms.sendMessage(CHISEL,"add_variation",tag);
		messages++;
	}
}
