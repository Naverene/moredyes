package net.neverandy.moredyes.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.neverandy.moredyes.api.MoreDyesAPI;
import net.neverandy.moredyes.utility.BlockInfo;

public class BlockGlass extends AbstractGlassBlock
{
    public String blockName;
    private final float[] beaconColor;

    /** hex is the dye color in the registry name, such as "334c59". */
    public BlockGlass(BlockInfo info, String hex)
    {
        super(AbstractBlock.Properties.create(info.blockMaterial)
                .hardnessAndResistance(info.hardness,info.resistance)
                .harvestLevel(info.harvestLevel)
                .harvestTool(info.harvestTool)
                .sound(info.sound)
                .setLightLevel(value -> info.lightlevel)
                .notSolid().variableOpacity());
        this.beaconColor = beaconColor(hex);
    }

    /** Tints a beacon beam passing through it in its own color, like vanilla stained glass. */
    @Override
    public float[] getBeaconColorMultiplier(BlockState state, IWorldReader world, BlockPos pos, BlockPos beaconPos)
    {
        return this.beaconColor;
    }

    /** The color a dye hex is drawn in, as the red, green and blue fractions a beacon beam is tinted with. */
    public static float[] beaconColor(String hex)
    {
        int rgb = MoreDyesAPI.displayColor(Integer.parseInt(hex, 16));
        return new float[]{(rgb >> 16 & 0xFF) / 255.0F, (rgb >> 8 & 0xFF) / 255.0F, (rgb & 0xFF) / 255.0F};
    }
}
