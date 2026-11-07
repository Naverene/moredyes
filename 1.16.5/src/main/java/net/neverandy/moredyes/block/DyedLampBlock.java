package net.neverandy.moredyes.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

/**
 * A dyed redstone lamp, like the colored lamps of RedPower and Project Red. A normal lamp lights up while it is powered,
 * like vanilla's; an inverted one is lit until it is powered. Like vanilla, a lamp turns on at once and turns off four
 * ticks later, so a short pulse doesn't make it flicker.
 */
public class DyedLampBlock extends Block
{
    public static final BooleanProperty LIT = RedstoneLampBlock.LIT;

    private final boolean inverted;

    public DyedLampBlock(Properties properties, boolean inverted)
    {
        // Lit, it draws at full brightness (its models also skip the side shading), so it looks like the light
        // comes from it.
        super(properties.setEmmisiveRendering((state, world, pos) -> state.get(LIT)));
        this.inverted = inverted;
        setDefaultState(getDefaultState().with(LIT, inverted));
    }

    public boolean isInverted()
    {
        return inverted;
    }

    /** Whether the lamp should be lit with the power it gets at pos. */
    private boolean shouldBeLit(World world, BlockPos pos)
    {
        return world.isBlockPowered(pos) != inverted;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return getDefaultState().with(LIT, shouldBeLit(context.getWorld(), context.getPos()));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
    {
        if (world.isRemote)
        {
            return;
        }
        boolean lit = state.get(LIT);
        if (lit != shouldBeLit(world, pos))
        {
            if (lit)
            {
                world.getPendingBlockTicks().scheduleTick(pos, this, 4);
            }
            else
            {
                world.setBlockState(pos, state.with(LIT, true), 2);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random)
    {
        if (state.get(LIT) && !shouldBeLit(world, pos))
        {
            world.setBlockState(pos, state.with(LIT, false), 2);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(LIT);
    }
}
