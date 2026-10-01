package net.neverandy.moredyes.utility;
import net.neverandy.moredyes.MoreDyes;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockInfo
{
	public String blockName;
	public float hardness;
	public SoundType sound;
	public String harvestTool;
	public int harvestLevel;
	public float resistance;
	public Material blockMaterial;
	public CreativeTabs tab;
	public BlockInfo(String blockName,Material mat,float h,SoundType t, String s, int hL,float r,CreativeTabs tab)
	{
		this.blockName=blockName;
		this.blockMaterial=mat;
		this.hardness=h;
		this.sound=t;
		this.harvestTool=s;
		this.harvestLevel=hL;
		this.resistance = r;
		this.tab=tab;
	}
	public BlockInfo(String blockName,Material mat,float h,SoundType t, String s, int hL,float r)
	{
		this(blockName,mat,h,t,s,hL,r, MoreDyes.tabBlocks);
	}
	public BlockInfo(String blockName,Material mat,float h,SoundType t, String s, int hL)
	{
		this(blockName,mat,h,t,s,hL,1.0F);
	}
	public BlockInfo()
	{
		this("",Material.ROCK,1.0f,SoundType.STONE,"pickaxe",1);
	}
}