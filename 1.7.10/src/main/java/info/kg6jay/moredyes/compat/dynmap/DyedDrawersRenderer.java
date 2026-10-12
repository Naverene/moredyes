package info.kg6jay.moredyes.compat.dynmap;

import java.util.ArrayList;
import java.util.Map;

import org.dynmap.renderer.CustomRenderer;
import org.dynmap.renderer.MapDataContext;
import org.dynmap.renderer.RenderPatch;
import org.dynmap.renderer.RenderPatchFactory;

/**
 * A dyed Storage Drawers drawer (compat/storagedrawers): a box turned the way the drawer faces, which Storage Drawers
 * keeps in its tile entity, as deep as the depth param (0.5 for the half drawers). Its textures are 0 for the top and
 * bottom, 1 for the left and right sides, 2 for the back and 3 for the front, as Storage Drawers draws them. It also
 * asks the map for the drawer's color, which DyeColorMultiplier reads.
 */
public class DyedDrawersRenderer extends CustomRenderer {

    /** Storage Drawers' field for the side the drawer faces (2 north, 3 south, 4 west, 5 east). */
    private static final String DIRECTION = "Dir";
    private static final String[] FIELDS = { TileColorFields.DRAWER_COLOR, DIRECTION };

    /** The model for each side the drawer can face, by side number. */
    private final RenderPatch[][] models = new RenderPatch[6][];

    @Override
    public boolean initializeRenderer(RenderPatchFactory factory, int blockId, int dataMask,
        Map<String, String> params) {
        if (!super.initializeRenderer(factory, blockId, dataMask, params)) {
            return false;
        }
        String value = params.get("depth");
        double depth = value == null ? 1.0 : Double.parseDouble(value);
        for (int facing = 2; facing < 6; facing++) {
            double xmin = facing == 5 ? 1.0 - depth : 0.0, xmax = facing == 4 ? depth : 1.0;
            double zmin = facing == 3 ? 1.0 - depth : 0.0, zmax = facing == 2 ? depth : 1.0;
            // In the order addBox takes the faces: bottom, top, west, east, north, south.
            int[] sides = { 0, 1, 4, 5, 2, 3 };
            int[] textures = new int[6];
            for (int i = 0; i < 6; i++) {
                int side = sides[i];
                textures[i] = side < 2 ? 0 : side == facing ? 3 : side == (facing ^ 1) ? 2 : 1;
            }
            ArrayList<RenderPatch> list = new ArrayList<>();
            addBox(factory, list, xmin, xmax, 0.0, 1.0, zmin, zmax, textures);
            this.models[facing] = list.toArray(new RenderPatch[0]);
        }
        this.models[0] = this.models[1] = this.models[2];
        return true;
    }

    @Override
    public int getMaximumTextureCount() {
        return 4;
    }

    @Override
    public String[] getTileEntityFieldsNeeded() {
        return FIELDS;
    }

    @Override
    public RenderPatch[] getRenderPatchList(MapDataContext context) {
        Object facing = context.getBlockTileEntityField(DIRECTION);
        int side = facing instanceof Number number ? number.intValue() : 2;
        return this.models[side >= 0 && side < 6 ? side : 2];
    }
}
