package net.neverandy.moredyes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** Half a dyed crafting table. Using it opens the crafting grid, like a whole one. */
public class WorkbenchSlabBlock extends SlabBlock
{
    private static final Component CONTAINER_NAME = Component.translatable("container.crafting");

    public WorkbenchSlabBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if (level.isClientSide)
        {
            return InteractionResult.SUCCESS;
        }
        player.openMenu(new SimpleMenuProvider((id, inventory, p) ->
                new ContainerWorkbench(id, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_NAME));
        player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
        return InteractionResult.CONSUME;
    }
}
