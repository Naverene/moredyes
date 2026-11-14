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
public class DyedShulkerBoxBlock extends ShulkerBoxBlock {

    public DyedShulkerBoxBlock(BlockBehaviour.Properties properties) {
        super(null, properties);
    }

    /**
     * A full box broken in creative drops itself with its contents. Vanilla's code would drop a purple box, as it
     * picks the item by dye color, so the box is dropped here and emptied before vanilla's code sees it.
     */
    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide() && player.preventsBlockDrops()
            && level.getBlockEntity(pos) instanceof ShulkerBoxBlockEntity box && !box.isEmpty()) {
            ItemStack stack = new ItemStack(this);
            stack.applyComponents(box.collectComponents());
            ItemEntity item = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
            item.setDefaultPickUpDelay();
            level.addFreshEntity(item);
            box.clearContent();
        }
        return super.playerWillDestroy(level, pos, state, player);
    }
}
