package info.kg6jay.moredyes.proxy;

public class CommonProxy {

    public void preInit() {}

    public void registerRenderThings() {}

    /**
     * Gives a sheep, or a wolf's collar, in the player's world the More Dyes shade the server sent. Nothing to do on a
     * server.
     */
    public void setSheepColor(int entityId, int color) {}
}
