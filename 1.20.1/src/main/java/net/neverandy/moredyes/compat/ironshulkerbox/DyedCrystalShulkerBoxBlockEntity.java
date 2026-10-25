package net.neverandy.moredyes.compat.ironshulkerbox;

import com.progwml6.ironshulkerbox.common.block.entity.AbstractIronShulkerBoxBlockEntity;
import com.progwml6.ironshulkerbox.common.block.entity.ICrystalShulkerBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * A dyed crystal box. Like Iron Shulker Boxes' crystal box, it keeps a sorted list of the items it shows on top and
 * sends it to players nearby, whose renderer draws them.
 */
public class DyedCrystalShulkerBoxBlockEntity extends DyedIronShulkerBoxBlockEntity implements ICrystalShulkerBox
{
    private NonNullList<ItemStack> topStacks = NonNullList.withSize(8, ItemStack.EMPTY);
    private boolean inventoryTouched;
    private boolean hadStuff;

    public DyedCrystalShulkerBoxBlockEntity(BlockPos pos, BlockState state)
    {
        super(IronShulkerBoxCompat.Tier.CRYSTAL, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AbstractIronShulkerBoxBlockEntity entity)
    {
        AbstractIronShulkerBoxBlockEntity.tick(level, pos, state, entity);
        if (entity instanceof DyedCrystalShulkerBoxBlockEntity box && !level.isClientSide && box.inventoryTouched)
        {
            box.inventoryTouched = false;
            box.sortTopStacks();
        }
    }

    @Override
    public void setItems(NonNullList<ItemStack> items)
    {
        super.setItems(items);
        inventoryTouched = true;
    }

    @Override
    public ItemStack getItem(int index)
    {
        inventoryTouched = true;
        return super.getItem(index);
    }

    @Override
    public NonNullList<ItemStack> getTopItems()
    {
        return topStacks;
    }

    @Nullable
    @Override
    public Level getChestLevel()
    {
        return level;
    }

    @Override
    public BlockPos getChestWorldPosition()
    {
        return worldPosition;
    }

    @Override
    public void receiveMessageFromServer(NonNullList<ItemStack> stacks)
    {
        topStacks = stacks;
    }

    @Override
    public NonNullList<ItemStack> getCurrentItems()
    {
        return getItems();
    }

    @Override
    public void setHadStuff(boolean hadStuff)
    {
        this.hadStuff = hadStuff;
    }

    @Override
    public boolean getHadStuff()
    {
        return hadStuff;
    }
}
