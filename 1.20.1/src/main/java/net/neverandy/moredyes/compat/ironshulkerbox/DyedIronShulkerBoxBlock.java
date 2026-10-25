package net.neverandy.moredyes.compat.ironshulkerbox;

import com.progwml6.ironshulkerbox.common.block.AbstractIronShulkerBoxBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neverandy.moredyes.reference.ColorStrings;

import javax.annotation.Nullable;
import java.util.List;

/**
 * An Iron Shulker Boxes box in a More Dyes color. COLOR is the color's position in ColorStrings.ALL. Opening, the
 * contents, the shape while open and the tooltip are Iron Shulker Boxes' own.
 */
public class DyedIronShulkerBoxBlock extends AbstractIronShulkerBoxBlock
{
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, ColorStrings.ALL.length - 1);

    private final IronShulkerBoxCompat.Tier tier;

    public DyedIronShulkerBoxBlock(IronShulkerBoxCompat.Tier tier, Properties properties)
    {
        super(properties, null, () -> tier.entity.get(), tier.type);
        this.tier = tier;
    }

    public IronShulkerBoxCompat.Tier tier()
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
        return tier == IronShulkerBoxCompat.Tier.CRYSTAL ? new DyedCrystalShulkerBoxBlockEntity(pos, state)
                : new DyedIronShulkerBoxBlockEntity(tier, pos, state);
    }

    /** A crystal box also keeps the items it shows on top up to date. */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
    {
        if (tier == IronShulkerBoxCompat.Tier.CRYSTAL)
        {
            return createTickerHelper(type, tier.entity.get(), DyedCrystalShulkerBoxBlockEntity::tick);
        }
        return super.getTicker(level, state, type);
    }

    /** The box in its color with what it holds and its name, as a vanilla shulker box drops. */
    public ItemStack stack(BlockState state, @Nullable BlockEntity entity)
    {
        ItemStack stack = IronShulkerBoxCompat.stack(tier, state.getValue(COLOR));
        if (entity instanceof DyedIronShulkerBoxBlockEntity box)
        {
            box.saveToItem(stack);
            if (box.hasCustomName())
            {
                stack.setHoverName(box.getCustomName());
            }
        }
        return stack;
    }

    /** Drops itself in its color, holding its contents. A loot table can't, as the color is a property with 118 values. */
    @Override
    @SuppressWarnings("deprecation")
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params)
    {
        return List.of(stack(state, params.getOptionalParameter(LootContextParams.BLOCK_ENTITY)));
    }

    /**
     * A full box broken in creative drops itself with its contents. Iron Shulker Boxes' code would drop its own box, as
     * it picks the item by tier and dye color, so the box is dropped here and emptied before its code sees it.
     */
    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
    {
        if (!level.isClientSide && player.isCreative()
                && level.getBlockEntity(pos) instanceof DyedIronShulkerBoxBlockEntity box && !box.isEmpty())
        {
            ItemEntity item = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack(state, box));
            item.setDefaultPickUpDelay();
            level.addFreshEntity(item);
            box.clearContent();
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state)
    {
        return stack(state, level.getBlockEntity(pos));
    }
}
