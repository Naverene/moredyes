package net.neverandy.moredyes.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.GlassBlock;
import net.neverandy.moredyes.api.MoreDyesAPI;

/** Dyed glass. A beacon beam passing through it takes its color, like vanilla stained glass. */
public class DyedGlassBlock extends GlassBlock implements DyedBeaconBeamBlock
{
    private final float[] beaconColor;
    private final DyeColor nearest;

    /** hex is the dye color in the registry name, such as "334c59". */
    public DyedGlassBlock(Properties properties, String hex)
    {
        super(properties);
        this.beaconColor = beaconColor(hex);
        this.nearest = MoreDyesAPI.nearestVanillaColor(Integer.parseInt(hex, 16));
    }

    @Override
    public float[] getBeaconColor()
    {
        return this.beaconColor;
    }

    @Override
    public DyeColor getColor()
    {
        return this.nearest;
    }

    /** The color a dye hex is drawn in, as the red, green and blue fractions a beacon beam is tinted with. */
    public static float[] beaconColor(String hex)
    {
        int rgb = MoreDyesAPI.displayColor(Integer.parseInt(hex, 16));
        return new float[]{(rgb >> 16 & 0xFF) / 255.0F, (rgb >> 8 & 0xFF) / 255.0F, (rgb & 0xFF) / 255.0F};
    }
}
