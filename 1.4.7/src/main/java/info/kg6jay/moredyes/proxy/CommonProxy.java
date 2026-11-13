package info.kg6jay.moredyes.proxy;

/** What only the client does; the server does nothing. */
public class CommonProxy {

    public void registerRenderers() {}

    /** Called once the items exist. */
    public void registerItemRenderers() {}

    /** The server sent the color of a sheep, or of a wolf's collar. */
    public void onSheepColor(int entityId, int color) {}
}
