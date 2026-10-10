package info.kg6jay.moredyes.proxy;

/** What only the client does; the server does nothing. */
public class CommonProxy {

    public void registerRenderers() {}

    /** The server sent the color of a sheep. */
    public void onSheepColor(int entityId, int color) {}
}
