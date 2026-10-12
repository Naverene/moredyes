package net.neverandy.moredyes.compat.ironchest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The inventory of a dyed Iron Chests chest: the size and screen of its tier. It is a vanilla chest block entity, as
 * Iron Chests' own are, so hoppers, comparators and the lid animation work as for them.
 */
public class DyedIronChestBlockEntity extends ChestBlockEntity {

    private final IronChestCompat.Tier tier;
    /**
     * Who has the chest open. Vanilla's own count only recognizes its chest screen, and would close the lid while Iron
     * Chests' screen is still open, so this one counts any screen showing this chest's slots.
     */
    private final ContainerOpenersCounter openers = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState state) {
            sound(level, pos, SoundEvents.CHEST_OPEN);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            sound(level, pos, SoundEvents.CHEST_CLOSE);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int count, int openCount) {
            signalOpenCount(level, pos, state, count, openCount);
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            for (Slot slot : player.containerMenu.slots) {
                if (slot.container == DyedIronChestBlockEntity.this) {
                    return true;
                }
            }
            return false;
        }
    };

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

    @Override
    public void startOpen(Player player) {
        if (!remove && !player.isSpectator()) {
            openers.incrementOpeners(player, getLevel(), getBlockPos(), getBlockState());
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!remove && !player.isSpectator()) {
            openers.decrementOpeners(player, getLevel(), getBlockPos(), getBlockState());
        }
    }

    @Override
    public void recheckOpen() {
        if (!remove) {
            openers.recheckOpeners(getLevel(), getBlockPos(), getBlockState());
        }
    }

    /** Whether anyone has the chest open. */
    public boolean isOpen() {
        return openers.getOpenerCount() > 0;
    }

    private static void sound(Level level, BlockPos pos, SoundEvent sound) {
        level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, sound, SoundSource.BLOCKS, 0.5F,
            level.random.nextFloat() * 0.1F + 0.9F);
    }
}
