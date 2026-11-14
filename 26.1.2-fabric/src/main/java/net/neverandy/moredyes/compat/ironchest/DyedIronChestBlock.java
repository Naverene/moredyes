package net.neverandy.moredyes.compat.ironchest;

import com.mojang.serialization.MapCodec;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;

import net.neverandy.moredyes.color.MixColors;

/**
 * An Iron Chests chest in a More Dyes color. The item keeps the color in its {@code minecraft:block_state} component,
 * which the placed block takes over, and the loot table copies it back ({@code minecraft:copy_state}). Like Iron
 * Chests' own chests it is a chest block that never joins its neighbor into a double chest.
 */
public class DyedIronChestBlock extends ChestBlock {

    /** The color's position in {@link MixColors#ALL}. */
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, MixColors.ALL.size() - 1);

    private final IronChestCompat.Tier tier;
    private final MapCodec<DyedIronChestBlock> codec;

    public DyedIronChestBlock(IronChestCompat.Tier tier, Properties properties) {
        super(tier::entity, tier.openSound(), tier.closeSound(), properties);
        this.tier = tier;
        this.codec = simpleCodec(p -> new DyedIronChestBlock(tier, p));
    }

    public IronChestCompat.Tier tier() {
        return tier;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(COLOR);
    }

    /** Always a single chest. The color comes from the item afterwards. */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite())
            .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public boolean chestCanConnectTo(BlockState state) {
        return false;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
        Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random)
            .setValue(TYPE, ChestType.SINGLE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DyedIronChestBlockEntity(tier, pos, state);
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return IronChestCompat.stack(tier, state.getValue(COLOR));
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapCodec<? extends ChestBlock> codec() {
        return codec;
    }
}
