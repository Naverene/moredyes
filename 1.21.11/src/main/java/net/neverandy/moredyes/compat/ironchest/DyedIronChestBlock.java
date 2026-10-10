package net.neverandy.moredyes.compat.ironchest;

import com.mojang.serialization.MapCodec;
import com.progwml6.ironchest.common.block.regular.AbstractIronChestBlock;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import net.neverandy.moredyes.color.MixColors;

/**
 * An Iron Chests chest in a More Dyes color. The item keeps the color in its {@code minecraft:block_state} component,
 * which the placed block takes over, and the loot table copies it back ({@code minecraft:copy_state}).
 */
public class DyedIronChestBlock extends AbstractIronChestBlock {

    /** The color's position in {@link MixColors#ALL}. */
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, MixColors.ALL.size() - 1);

    private final IronChestCompat.Tier tier;
    private final MapCodec<DyedIronChestBlock> codec;

    public DyedIronChestBlock(IronChestCompat.Tier tier, Properties properties) {
        super(properties, tier::entity, tier.type);
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

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DyedIronChestBlockEntity(tier, pos, state);
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return IronChestCompat.stack(tier, state.getValue(COLOR));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return codec;
    }
}
