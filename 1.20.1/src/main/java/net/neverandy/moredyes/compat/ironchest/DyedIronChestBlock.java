package net.neverandy.moredyes.compat.ironchest;

import com.progwml6.ironchest.common.block.regular.AbstractIronChestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.neverandy.moredyes.reference.ColorStrings;

import java.util.List;

/** An Iron Chests chest in a More Dyes color. COLOR is the color's position in ColorStrings.ALL. */
public class DyedIronChestBlock extends AbstractIronChestBlock
{
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, ColorStrings.ALL.length - 1);

    private final IronChestCompat.Tier tier;

    public DyedIronChestBlock(IronChestCompat.Tier tier, Properties properties)
    {
        super(properties, () -> tier.entity.get(), tier.type);
        this.tier = tier;
    }

    public IronChestCompat.Tier tier()
    {
        return tier;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(COLOR);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DyedIronChestBlockEntity(tier, pos, state);
    }

    /** Drops itself in its color. A loot table can't, as the color is a property with 118 values. */
    @Override
    @SuppressWarnings("deprecation")
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params)
    {
        return List.of(IronChestCompat.stack(tier, state.getValue(COLOR)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state)
    {
        return IronChestCompat.stack(tier, state.getValue(COLOR));
    }
}
