package net.neverandy.moredyes.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A dyed shulker box. It is a vanilla shulker box with no dye color (like the purple one), so it shares the vanilla
 * block entity and keeps its contents the same way; client/DyedShulkerBoxRenderer draws it in its own color.
 */
public class DyedShulkerBoxBlock extends ShulkerBoxBlock
{
    public DyedShulkerBoxBlock(BlockBehaviour.Properties properties)
    {
        super(null, properties);
    }

    /** Vanilla's shulker box properties that BlockBehaviour.Properties.copy leaves out. */
    public static BlockBehaviour.Properties properties(BlockBehaviour.Properties properties)
    {
        BlockBehaviour.StatePredicate closed = (state, level, pos) ->
                !(level.getBlockEntity(pos) instanceof ShulkerBoxBlockEntity box) || box.isClosed();
        return properties.isSuffocating(closed).isViewBlocking(closed).isRedstoneConductor((state, level, pos) -> true);
    }

    /**
     * A full box broken in creative drops itself with its contents. Vanilla's code would drop an undyed box, as it
     * picks the item by dye color, so the box is dropped here and emptied before vanilla's code sees it.
     */
    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
    {
        if (!level.isClientSide && player.isCreative()
                && level.getBlockEntity(pos) instanceof ShulkerBoxBlockEntity box && !box.isEmpty())
        {
            ItemStack stack = new ItemStack(this);
            box.saveToItem(stack);
            if (box.hasCustomName())
            {
                stack.setHoverName(box.getCustomName());
            }
            ItemEntity item = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
            item.setDefaultPickUpDelay();
            level.addFreshEntity(item);
            box.clearContent();
        }
        super.playerWillDestroy(level, pos, state, player);
    }
}
