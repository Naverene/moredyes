package net.neverandy.moredyes.compat.ironchest;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import net.neverandy.moredyes.color.MixColors;

/**
 * An Iron Chests chest in a More Dyes color. The item keeps the color in its {@code minecraft:block_state} component,
 * which the placed block takes over. Like Iron Chests' own chests it is a chest block that never joins its neighbor
 * into a double chest.
 */
public class DyedIronChestBlock extends ChestBlock {

    /** The color's position in {@link MixColors#ALL}. */
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, MixColors.ALL.size() - 1);

    private final IronChestCompat.Tier tier;

    public DyedIronChestBlock(IronChestCompat.Tier tier, Properties properties) {
        super(properties, tier::entity);
        this.tier = tier;
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
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DyedIronChestBlockEntity(tier, pos, state);
    }

    /** Drops itself in its color, with the chest's name. */
    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        ItemStack stack = IronChestCompat.stack(tier, state.getValue(COLOR));
        if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof BaseContainerBlockEntity chest
            && chest.getCustomName() != null) {
            stack.set(DataComponents.CUSTOM_NAME, chest.getCustomName());
        }
        return List.of(stack);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return IronChestCompat.stack(tier, state.getValue(COLOR));
    }
}
