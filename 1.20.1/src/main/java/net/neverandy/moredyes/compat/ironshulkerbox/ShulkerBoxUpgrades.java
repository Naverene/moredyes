package net.neverandy.moredyes.compat.ironshulkerbox;

import com.progwml6.ironshulkerbox.common.block.AbstractIronShulkerBoxBlock;
import com.progwml6.ironshulkerbox.common.block.IronShulkerBoxesTypes;
import com.progwml6.ironshulkerbox.common.block.entity.AbstractIronShulkerBoxBlockEntity;
import com.progwml6.ironshulkerbox.common.item.IronShulkerBoxUpgradeItem;
import com.progwml6.ironshulkerbox.common.item.IronShulkerBoxesUpgradeType;
import com.progwml6.ironshulkerbox.common.registraton.IronShulkerBoxesItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegistryObject;
import net.neverandy.moredyes.block.MDBlock;

import java.util.Arrays;
import java.util.Map;

/**
 * Iron Shulker Boxes' upgrade items keep the color: an iron-to-gold upgrade on a dyed iron box gives a dyed gold box of
 * the same color, and a vanilla-to-iron upgrade on a dyed vanilla shulker box gives a dyed iron box. Iron Shulker
 * Boxes' own code would give its undyed box, as it picks the box by its 16 dye colors. The box keeps its contents, name
 * and facing.
 */
public final class ShulkerBoxUpgrades
{
    private ShulkerBoxUpgrades() {}

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        IronShulkerBoxesUpgradeType upgrade = upgradeOf(event.getItemStack());
        if (upgrade == null)
        {
            return;
        }
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState old = level.getBlockState(pos);
        int color = colorOf(old, upgrade);
        IronShulkerBoxCompat.Tier target = IronShulkerBoxCompat.Tier.of(upgrade.target);
        if (color < 0 || target == null)
        {
            return;
        }
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
        if (!level.isClientSide)
        {
            BlockState state = target.block.get().defaultBlockState()
                    .setValue(DyedIronShulkerBoxBlock.COLOR, color)
                    .setValue(AbstractIronShulkerBoxBlock.FACING, old.getValue(ShulkerBoxBlock.FACING));
            upgrade(level, pos, state, event.getEntity(), event.getItemStack());
        }
    }

    private static IronShulkerBoxesUpgradeType upgradeOf(ItemStack stack)
    {
        for (Map.Entry<IronShulkerBoxesUpgradeType, RegistryObject<IronShulkerBoxUpgradeItem>> entry : IronShulkerBoxesItems.UPGRADES.entrySet())
        {
            if (entry.getValue().get() == stack.getItem())
            {
                return entry.getKey();
            }
        }
        return null;
    }

    /** The color of a box this upgrade applies to, or -1 if it isn't a dyed box of the upgrade's source tier. */
    private static int colorOf(BlockState state, IronShulkerBoxesUpgradeType upgrade)
    {
        if (state.getBlock() instanceof DyedIronShulkerBoxBlock block)
        {
            return upgrade.canUpgrade(block.tier().type) ? state.getValue(DyedIronShulkerBoxBlock.COLOR) : -1;
        }
        if (upgrade.canUpgrade(IronShulkerBoxesTypes.VANILLA))
        {
            return Arrays.asList(MDBlock.shulkerBoxArray).indexOf(state.getBlock());
        }
        return -1;
    }

    private static void upgrade(Level level, BlockPos pos, BlockState state, Player player, ItemStack upgrade)
    {
        BlockEntity entity = level.getBlockEntity(pos);
        if (!(entity instanceof BaseContainerBlockEntity container) || !container.canOpen(player) || isOpen(level, pos, entity))
        {
            return;
        }
        NonNullList<ItemStack> items = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        for (int slot = 0; slot < items.size(); slot++)
        {
            items.set(slot, container.getItem(slot));
        }
        Component name = container.getCustomName();

        // Emptying and removing the old box first keeps it from dropping what it held.
        container.clearContent();
        level.removeBlockEntity(pos);
        level.removeBlock(pos, false);
        level.setBlock(pos, state, 3);
        if (level.getBlockEntity(pos) instanceof DyedIronShulkerBoxBlockEntity box)
        {
            // Every upgrade is to a larger box, so everything fits.
            box.setItems(items);
            if (name != null)
            {
                box.setCustomName(name);
            }
        }
        if (!player.getAbilities().instabuild)
        {
            upgrade.shrink(1);
        }
    }

    private static boolean isOpen(Level level, BlockPos pos, BlockEntity entity)
    {
        if (entity instanceof AbstractIronShulkerBoxBlockEntity)
        {
            return AbstractIronShulkerBoxBlockEntity.getOpenCount(level, pos) > 0;
        }
        return entity instanceof ShulkerBoxBlockEntity box && box.getAnimationStatus() != ShulkerBoxBlockEntity.AnimationStatus.CLOSED;
    }
}
