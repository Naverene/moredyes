package info.kg6jay.moredyes.compat.dynmap;

import org.dynmap.hdmap.renderer.FenceWallBlockRenderer;

/** The map's own walls (type=wall), asking for the color tile entity field as well. */
public class TileColorWallRenderer extends FenceWallBlockRenderer {

    @Override
    public String[] getTileEntityFieldsNeeded() {
        return TileColorFields.FIELDS;
    }
}
