package net.neverandy.moredyes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neverandy.moredyes.api.MoreDyesAPI;
import net.neverandy.moredyes.utility.BlockInfo;

public class BlockGlass extends AbstractGlassBlock
{
    public String blockName;
    private final float[] beaconColor;

    /** hex is the dye color in the registry name, such as "334c59". */
    public BlockGlass(BlockInfo info, String hex)
    {
        super(BlockBehaviour.Properties.of(info.blockMaterial)
                .strength(info.hardness,info.resistance)
                .sound(info.sound)
                .lightLevel(value -> info.lightlevel)
                .noOcclusion().dynamicShape());
        this.beaconColor = beaconColor(hex);
    }

    /** Tints a beacon beam passing through it in its own color, like vanilla stained glass. */
    @Override
    public float[] getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos)
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
