package info.kg6jay.moredyes.compat.chisel;

import java.util.ArrayList;
import java.util.List;

import com.cricketcraft.chisel.api.carving.ICarvingGroup;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;

public class CarvingGroup implements ICarvingGroup
{
	String sound="";
	String name="";
	String oreDictName="";
	List<ICarvingVariation> variations;
	public CarvingGroup(String oreDictName,String name,String sound)
	{
		this.sound=sound;
		this.oreDictName=oreDictName;
		this.name=name;
		variations = new ArrayList();
	}
	public CarvingGroup(String oreDictName,String name)
	{
		this(oreDictName,name,"");
	}
	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getSound()
	{
		return sound;
	}

	@Override
	public void setSound(String sound)
	{
		this.sound=sound;
		
	}

	@Override
	public String getOreName()
	{
		return oreDictName;
	}

	@Override
	public void setOreName(String oreName)
	{
		this.oreDictName=oreName;
	}

	@Override
	public List<ICarvingVariation> getVariations()
	{
		return variations;
	}

	@Override
	public void addVariation(ICarvingVariation variation)
	{
		variations.add(variation);
	}

	@Override
	public boolean removeVariation(ICarvingVariation variation)
	{
		return variations.remove(variation);
	}

}
