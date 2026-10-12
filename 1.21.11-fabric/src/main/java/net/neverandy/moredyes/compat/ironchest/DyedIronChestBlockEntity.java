package net.neverandy.moredyes.compat.ironchest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The inventory of a dyed Iron Chests chest: the size and screen of its tier. It is a vanilla chest block entity, as
 * Iron Chests' own are, so hoppers, comparators and the lid animation work as for them. Iron Chests' mixins make the
 * vanilla count of who has a chest open recognize its screen.
 */
public class DyedIronChestBlockEntity extends ChestBlockEntity {

    private final IronChestCompat.Tier tier;

    public DyedIronChestBlockEntity(IronChestCompat.Tier tier, BlockPos pos, BlockState state) {
        super(tier.entity(), pos, state);
        this.tier = tier;
        setItems(NonNullList.withSize(getContainerSize(), ItemStack.EMPTY));
    }

    public IronChestCompat.Tier tier() {
        return tier;
    }

    @Override
    public int getContainerSize() {
        return tier.type.size;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return IronChestCompat.menu(tier, id, inventory, level, worldPosition);
    }

    /** "C56685 Iron Chest", like the item. */
    @Override
    protected Component getDefaultName() {
        BlockState state = getBlockState();
        return DyedIronChestItem.name(tier, state.hasProperty(DyedIronChestBlock.COLOR) ? state.getValue(DyedIronChestBlock.COLOR) : 0);
    }
}
