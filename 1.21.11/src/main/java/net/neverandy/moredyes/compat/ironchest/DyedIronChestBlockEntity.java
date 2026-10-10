package net.neverandy.moredyes.compat.ironchest;

import com.progwml6.ironchest.common.block.regular.entity.AbstractIronChestBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

/** The inventory of a dyed Iron Chests chest: the size and screen of its tier. */
public class DyedIronChestBlockEntity extends AbstractIronChestBlockEntity {

    private final IronChestCompat.Tier tier;

    public DyedIronChestBlockEntity(IronChestCompat.Tier tier, BlockPos pos, BlockState state) {
        super(tier.entity(), pos, state, tier.type, tier::block);
        this.tier = tier;
    }

    public IronChestCompat.Tier tier() {
        return tier;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return tier.menu.create(id, inventory, this);
    }
}
