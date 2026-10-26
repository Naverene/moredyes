package net.neverandy.moredyes.block;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

/**
 * A More Dyes bed, which remembers the planks it was made from: its block entity (DyedBedBlockEntity) keeps them, and
 * its wood property picks the model its frame is drawn with.
 */
public class DyedBedBlock extends BedBlock implements EntityBlock {

    public static final EnumProperty<BedWood> WOOD = EnumProperty.create("wood", BedWood.class);

    public DyedBedBlock(DyeColor color, Properties properties) {
        super(color, properties);
        registerDefaultState(defaultBlockState().setValue(WOOD, BedWood.OAK));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WOOD);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DyedBedBlockEntity(pos, state);
    }

    /** The foot gets the wood here; vanilla places the head as a copy of it. */
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null
            : state.setValue(WOOD, BedWood.of(DyedBedBlockEntity.woodOf(context.getItemInHand())));
    }

    /** Vanilla places the head here; both halves' block entities get the planks. */
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

    /** Picking the bed gives one of the same wood. */
    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack stack = super.getCloneItemStack(level, pos, state, includeData);
        if (level.getBlockEntity(pos) instanceof DyedBedBlockEntity bed) {
            DyedBedBlockEntity.setWood(stack, bed.getWood());
        }
        return stack;
    }
}
