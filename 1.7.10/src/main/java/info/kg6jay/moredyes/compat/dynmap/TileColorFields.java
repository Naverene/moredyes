package info.kg6jay.moredyes.compat.dynmap;

/** The tile entity fields that hold a block's color, for the renderers that ask for them. */
final class TileColorFields {

    /** TileEntityMDColor's and the dyed Iron Chests' (DyedIronChestTile). */
    static final String COLOR = "color";
    /** The dyed Storage Drawers' (DyedDrawersTile). */
    static final String DRAWER_COLOR = "MoreDyesColor";

    static final String[] FIELDS = { COLOR };

    private TileColorFields() {}
}
