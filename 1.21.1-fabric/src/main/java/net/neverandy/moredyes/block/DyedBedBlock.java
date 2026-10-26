package net.neverandy.moredyes.block;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/** A More Dyes bed, which remembers the planks it was made from (see DyedBedBlockEntity). */
public class DyedBedBlock extends BedBlock {

    public DyedBedBlock(DyeColor color, Properties properties) {
        super(color, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DyedBedBlockEntity(pos, state);
    }

    /** Vanilla places the head here; both halves get the planks. */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
        ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        for (BlockPos part : new BlockPos[] {pos, pos.relative(state.getValue(FACING))}) {
            if (level.getBlockEntity(part) instanceof DyedBedBlockEntity bed) {
                bed.setWood(DyedBedBlockEntity.woodOf(stack));
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        if (level.getBlockEntity(pos) instanceof DyedBedBlockEntity bed) {
            DyedBedBlockEntity.setWood(stack, bed.getWood());
        }
        return stack;
    }
}
