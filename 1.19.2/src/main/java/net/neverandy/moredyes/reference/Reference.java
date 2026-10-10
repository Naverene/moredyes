package net.neverandy.moredyes.reference;

public class Reference
{
    public static final String MOD_ID = "moredyes";
    public static final String MOD_NAME = "More Dyes";

    /**
     * The Iron Chests tiers that have dyed versions (compat/ironchest), for code that must not load Iron Chests' classes,
     * such as data generation.
     */
    public static final String[] IRON_CHEST_TIERS = {"iron", "gold", "diamond", "copper"};

    /** The Storage Drawers sizes that have dyed versions (compat/storagedrawers), named as Storage Drawers names them. */
    public static final String[] DRAWER_SIZES = {"full_drawers_1", "full_drawers_2", "full_drawers_4", "half_drawers_1", "half_drawers_2", "half_drawers_4"};
}
