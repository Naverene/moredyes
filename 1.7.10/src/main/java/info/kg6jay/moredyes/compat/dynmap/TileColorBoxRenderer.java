package info.kg6jay.moredyes.compat.dynmap;

import java.util.ArrayList;
import java.util.Map;

import org.dynmap.renderer.CustomRenderer;
import org.dynmap.renderer.MapDataContext;
import org.dynmap.renderer.RenderPatch;
import org.dynmap.renderer.RenderPatchFactory;

/**
 * A box from xmin, ymin, zmin to xmax, ymax, zmax (0 to 1, the whole block by default) for slabs, trapdoors, pistons
 * and chests. Its faces are patches 0 to 5, numbered like Minecraft's sides: bottom, top, north, south, west, east.
 * It asks the map for the color tile entity field, which DyeColorMultiplier reads.
 */
public class TileColorBoxRenderer extends CustomRenderer {

    /** The patch of each face, in the order addBox takes them: bottom, top, west, east, north, south. */
    private static final int[] PATCHES = { 0, 1, 4, 5, 2, 3 };

    private RenderPatch[] model;

    @Override
    public boolean initializeRenderer(RenderPatchFactory factory, int blockId, int dataMask,
        Map<String, String> params) {
        if (!super.initializeRenderer(factory, blockId, dataMask, params)) {
            return false;
        }
        ArrayList<RenderPatch> list = new ArrayList<>();
        addBox(
            factory,
            list,
            param(params, "xmin", 0.0),
            param(params, "xmax", 1.0),
            param(params, "ymin", 0.0),
            param(params, "ymax", 1.0),
            param(params, "zmin", 0.0),
            param(params, "zmax", 1.0),
            PATCHES);
        this.model = list.toArray(new RenderPatch[0]);
        return true;
    }

    private static double param(Map<String, String> params, String name, double fallback) {
        String value = params.get(name);
        return value == null ? fallback : Math.max(0.0, Math.min(1.0, Double.parseDouble(value)));
    }

    @Override
    public int getMaximumTextureCount() {
        return PATCHES.length;
    }

    @Override
    public String[] getTileEntityFieldsNeeded() {
        return TileColorFields.FIELDS;
    }

    @Override
    public RenderPatch[] getRenderPatchList(MapDataContext context) {
        return this.model;
    }
}
