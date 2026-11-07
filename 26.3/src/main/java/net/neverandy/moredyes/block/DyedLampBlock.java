package net.neverandy.moredyes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import org.jetbrains.annotations.Nullable;

/**
 * A dyed redstone lamp, like the colored lamps of RedPower and Project Red. A normal lamp lights up while it is powered,
 * like vanilla's; an inverted one is lit until it is powered. Like vanilla, a lamp turns on at once and turns off four
 * ticks later, so a short pulse doesn't make it flicker. It gives light through the vanilla lamp's properties, which it
 * copies.
 */
public class DyedLampBlock extends Block {

    public static final BooleanProperty LIT = RedstoneLampBlock.LIT;

    private final boolean inverted;

    public DyedLampBlock(Properties properties, boolean inverted) {
        // Lit, it draws at full brightness (its models also skip the side shading), so it looks like the light
        // comes from it.
        super(properties.emissiveRendering(state -> state.getValue(LIT)));
        this.inverted = inverted;
        registerDefaultState(defaultBlockState().setValue(LIT, inverted));
    }

    public boolean isInverted() {
        return inverted;
    }

    /** Whether the lamp should be lit with the power it gets at pos. */
    private boolean shouldBeLit(Level level, BlockPos pos) {
        return level.hasNeighborSignal(pos) != inverted;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(LIT, shouldBeLit(context.getLevel(), context.getClickedPos()));
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean isMoving) {
        if (level.isClientSide()) {
            return;
        }
        boolean lit = state.getValue(LIT);
        if (lit != shouldBeLit(level, pos)) {
            if (lit) {
                level.scheduleTick(pos, this, 4);
            } else {
                level.setBlock(pos, state.setValue(LIT, true), Block.UPDATE_CLIENTS);
            }
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT) && !shouldBeLit(level, pos)) {
            level.setBlock(pos, state.setValue(LIT, false), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
