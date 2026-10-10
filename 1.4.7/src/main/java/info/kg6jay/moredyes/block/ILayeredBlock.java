package info.kg6jay.moredyes.block;

/**
 * A block drawn in several layers, each with its own texture and tint, for blocks where only part of the texture
 * takes the dye color: the petals of a flower but not its stem, or the rings of a log but not its bark.
 */
public interface ILayeredBlock {

    int getLayerCount();

    /** The texture for this side on this layer, or -1 if the side is not drawn on this layer. */
    int getLayerTexture(int side, int layer);

    /** RGB tint of this layer for the given color. */
    int getLayerColor(int color, int layer);

    /** True if the layers are drawn as crossed squares (like flowers) instead of as a cube. */
    boolean isPlant();

    /**
     * The layer currently being drawn by the layered block renderer, read by getBlockTextureFromSideAndMetadata,
     * colorMultiplier and shouldSideBeRendered. -1 outside of that renderer.
     */
    final class RenderLayer {

        public static int current = -1;

        private RenderLayer() {}

        public static int layer() {
            return current < 0 ? 0 : current;
        }
    }
}
