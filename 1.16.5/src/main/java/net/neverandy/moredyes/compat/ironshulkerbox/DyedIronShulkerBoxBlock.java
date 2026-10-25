package net.neverandy.moredyes.compat.ironshulkerbox;

import com.progwml6.ironshulkerbox.common.block.GenericIronShulkerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.neverandy.moredyes.reference.ColorStrings;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * An Iron Shulker Boxes box in a More Dyes color. COLOR is the color's position in ColorStrings.ALL. Opening, the
 * contents, the shape while open and the tooltip are Iron Shulker Boxes' own.
 */
public class DyedIronShulkerBoxBlock extends GenericIronShulkerBlock
{
    public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, ColorStrings.ALL.length - 1);

    private final IronShulkerBoxCompat.Tier tier;

    public DyedIronShulkerBoxBlock(IronShulkerBoxCompat.Tier tier, Properties properties)
    {
        super(null, properties, tier.type);
        this.tier = tier;
    }

    public IronShulkerBoxCompat.Tier tier()
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
        return new DyedIronShulkerBoxTileEntity(tier);
    }

    /** The box in its color with what it holds and its name, as a vanilla shulker box drops. */
    public ItemStack stack(BlockState state, @Nullable TileEntity entity)
    {
        ItemStack stack = IronShulkerBoxCompat.stack(tier, state.get(COLOR));
        if (entity instanceof DyedIronShulkerBoxTileEntity)
        {
            DyedIronShulkerBoxTileEntity box = (DyedIronShulkerBoxTileEntity) entity;
            CompoundNBT contents = box.saveToNbt(new CompoundNBT());
            if (!contents.isEmpty())
            {
                stack.setTagInfo("BlockEntityTag", contents);
            }
            if (box.hasCustomName())
            {
                stack.setDisplayName(box.getCustomName());
            }
        }
        return stack;
    }

    /** Drops itself in its color, holding its contents. A loot table can't, as the color is a property with 118 values. */
    @Override
    @SuppressWarnings("deprecation")
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
    {
        return Collections.singletonList(stack(state, builder.get(LootParameters.BLOCK_ENTITY)));
    }

    /**
     * A full box broken in creative drops itself with its contents. Iron Shulker Boxes' code would look its item up by
     * a vanilla dye color, which this box doesn't have, so the box is dropped here and emptied before its code runs.
     */
    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player)
    {
        TileEntity entity = world.getTileEntity(pos);
        if (!world.isRemote && player.isCreative() && entity instanceof DyedIronShulkerBoxTileEntity
                && !((DyedIronShulkerBoxTileEntity) entity).isEmpty())
        {
            ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack(state, entity));
            item.setDefaultPickupDelay();
            world.addEntity(item);
            ((DyedIronShulkerBoxTileEntity) entity).clear();
        }
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state)
    {
        return stack(state, world.getTileEntity(pos));
    }
}
