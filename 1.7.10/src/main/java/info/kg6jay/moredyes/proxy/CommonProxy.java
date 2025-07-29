package info.kg6jay.moredyes.proxy;

public abstract class CommonProxy implements IProxy {

    public int addArmor(String armor) {
        return 0;
    }

    public void registerBlocks() {

    }
    /*
     * public void registerTileEntities()
     * {
     * GameRegistry.registerTileEntity(TileEntityColoredChest.class, Reference.MOD_ID);
     * }
     */

    public abstract void registerRenderThings();
}
