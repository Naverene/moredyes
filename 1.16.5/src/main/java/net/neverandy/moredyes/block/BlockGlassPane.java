package net.neverandy.moredyes.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.PaneBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.neverandy.moredyes.utility.BlockInfo;

/** A dyed glass pane. It connects to every other pane and to walls, like vanilla panes. */
public class BlockGlassPane extends PaneBlock
{
    private final float[] beaconColor;

    /** hex is the dye color in the registry name, such as "334c59". */
    public BlockGlassPane(BlockInfo info, String hex)
    {
        super(AbstractBlock.Properties.create(info.blockMaterial)
                .hardnessAndResistance(info.hardness, info.resistance)
                .sound(info.sound)
                .notSolid());
        this.beaconColor = BlockGlass.beaconColor(hex);
    }

    /** Tints a beacon beam passing through it in its own color, like vanilla stained glass panes. */
    @Override
    public float[] getBeaconColorMultiplier(BlockState state, IWorldReader world, BlockPos pos, BlockPos beaconPos)
    {
        return this.beaconColor;
    }
}
