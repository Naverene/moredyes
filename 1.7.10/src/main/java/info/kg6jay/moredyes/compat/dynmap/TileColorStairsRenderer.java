package info.kg6jay.moredyes.compat.dynmap;

import org.dynmap.hdmap.renderer.StairBlockRenderer;

/** The map's own stairs, asking for the color tile entity field as well. */
public class TileColorStairsRenderer extends StairBlockRenderer {

    @Override
    public String[] getTileEntityFieldsNeeded() {
        return TileColorFields.FIELDS;
    }
}
