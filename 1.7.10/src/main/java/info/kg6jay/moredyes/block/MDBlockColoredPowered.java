package info.kg6jay.moredyes.block;

import info.kg6jay.moredyes.handler.ConfigHandler;
import info.kg6jay.moredyes.utility.BlockInfo;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.IBlockAccess;

public class MDBlockColoredPowered extends MDBlockColored
{

	public MDBlockColoredPowered(String[] colors,BlockInfo info,String colorSet)
    {
		super(colors, info,colorSet);
	}

    @Override
    public boolean canProvidePower()
    {
        return true;
    }
    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z)
    {
        if(ConfigHandler.overrideDefaultMobSpawning)
        {
            return ConfigHandler.mobSpawnOnBlock;
        }
        else
        {
            return super.canCreatureSpawn(type, world, x, y, z);
        }
    }
    @Override
    public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_)
    {
        return 15;
    }
}
