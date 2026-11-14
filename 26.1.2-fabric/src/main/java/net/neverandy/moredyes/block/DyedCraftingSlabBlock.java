package net.neverandy.moredyes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/** Half a dyed crafting table. Using it opens the crafting grid, like a whole one. */
public class DyedCraftingSlabBlock extends SlabBlock {

    private static final Component CONTAINER_TITLE = Component.translatable("container.crafting");

    public DyedCraftingSlabBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
        BlockHitResult hit) {
        if (!level.isClientSide()) {
            player.openMenu(new SimpleMenuProvider((containerId, inventory, p) -> new DyedCraftingTableBlock.Menu(
                containerId, inventory, ContainerLevelAccess.create(level, pos), this), CONTAINER_TITLE));
            player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
        }
        return InteractionResult.SUCCESS;
    }
}
