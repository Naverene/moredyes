package net.neverandy.moredyes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neverandy.moredyes.utility.BlockInfo;

/** A dyed glass pane. It connects to every other pane and to walls, like vanilla panes. */
public class BlockGlassPane extends IronBarsBlock
{
    private final float[] beaconColor;

    /** hex is the dye color in the registry name, such as "334c59". */
    public BlockGlassPane(BlockInfo info, String hex)
    {
        super(BlockBehaviour.Properties.of(info.blockMaterial)
                .strength(info.hardness, info.resistance)
                .sound(info.sound)
                .noOcclusion());
        this.beaconColor = BlockGlass.beaconColor(hex);
    }

    /** Tints a beacon beam passing through it in its own color, like vanilla stained glass panes. */
    @Override
    public float[] getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos)
    {
        return this.beaconColor;
    }
}
