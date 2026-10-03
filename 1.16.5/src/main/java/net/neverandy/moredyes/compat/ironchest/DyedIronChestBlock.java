package net.neverandy.moredyes.compat.ironchest;

import com.progwml6.ironchest.common.block.GenericIronChestBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.neverandy.moredyes.reference.ColorStrings;

import java.util.Collections;
import java.util.List;

/** An Iron Chests chest in a More Dyes color. COLOR is the color's position in ColorStrings.ALL. */
public class DyedIronChestBlock extends GenericIronChestBlock
{
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, ColorStrings.ALL.length - 1);

    private final IronChestCompat.Tier tier;

    public DyedIronChestBlock(IronChestCompat.Tier tier, Properties properties)
    {
        super(tier.type, () -> tier.tileEntity.get(), properties);
        this.tier = tier;
    }

    public IronChestCompat.Tier tier()
    {
        return tier;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(COLOR);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new DyedIronChestTileEntity(tier);
    }

    /** Drops itself in its color. A loot table can't, as the color is a property with 118 values. */
    @Override
    @SuppressWarnings("deprecation")
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
    {
        return Collections.singletonList(IronChestCompat.stack(tier, state.get(COLOR)));
    }

    @Override
    public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state)
    {
        return IronChestCompat.stack(tier, state.get(COLOR));
    }
}
