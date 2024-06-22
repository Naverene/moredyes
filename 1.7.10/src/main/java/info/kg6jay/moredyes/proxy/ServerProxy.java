package info.kg6jay.moredyes.proxy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ServerProxy extends CommonProxy
{

    @Override
    @SideOnly(Side.SERVER)
    public void registerRenderThings()
    {

    }
}
