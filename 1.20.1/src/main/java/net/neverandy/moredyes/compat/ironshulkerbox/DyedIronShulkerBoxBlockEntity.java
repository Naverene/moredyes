package net.neverandy.moredyes.compat.ironshulkerbox;

import com.progwml6.ironshulkerbox.common.block.entity.AbstractIronShulkerBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/** The inventory of a dyed Iron Shulker Boxes box: the size and screen of its tier. */
public class DyedIronShulkerBoxBlockEntity extends AbstractIronShulkerBoxBlockEntity
{
    private final IronShulkerBoxCompat.Tier tier;

    public DyedIronShulkerBoxBlockEntity(IronShulkerBoxCompat.Tier tier, BlockPos pos, BlockState state)
    {
        super(tier.entity.get(), pos, state, null, tier.type);
        this.tier = tier;
    }

    public IronShulkerBoxCompat.Tier tier()
    {
        return tier;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory)
    {
        return tier.menu.create(id, inventory, this);
    }

    @Override
    public Block getBlockToUse()
    {
        return tier.block.get();
    }
}
