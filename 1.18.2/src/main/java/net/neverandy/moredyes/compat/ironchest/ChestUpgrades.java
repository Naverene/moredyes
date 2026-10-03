package net.neverandy.moredyes.compat.ironchest;

import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.regular.AbstractIronChestBlock;
import com.progwml6.ironchest.common.block.regular.entity.AbstractIronChestBlockEntity;
import com.progwml6.ironchest.common.item.ChestUpgradeItem;
import com.progwml6.ironchest.common.item.IronChestsItems;
import com.progwml6.ironchest.common.item.IronChestsUpgradeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegistryObject;
import net.neverandy.moredyes.block.BlockChest;
import net.neverandy.moredyes.block.MDBlock;

import java.util.Arrays;
import java.util.Map;

/**
 * Iron Chests' upgrade items keep the color: an iron-to-gold upgrade on a dyed iron chest gives a dyed gold chest of the
 * same color, and a wood-to-iron upgrade on a dyed vanilla chest gives a dyed iron chest. Upgrades to a tier that has
 * no dyed version (crystal, obsidian) are left to Iron Chests, which gives its plain chest.
 *
 * <p>This runs on the right-click event, which comes before the upgrade item's own onItemUseFirst.
 */
public final class ChestUpgrades
{
    private ChestUpgrades() {}

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        IronChestsUpgradeType upgrade = upgradeOf(event.getItemStack());
        if (upgrade == null)
        {
            return;
        }
        IronChestCompat.Tier target = tierOf(upgrade.target);
        Level level = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState old = level.getBlockState(pos);
        int color = colorOf(old, upgrade);
        if (target == null || color < 0)
        {
            return;
        }
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
        if (!level.isClientSide)
        {
            upgrade(level, pos, old, target, color, event.getPlayer(), event.getItemStack());
        }
    }

    private static IronChestsUpgradeType upgradeOf(ItemStack stack)
    {
        for (Map.Entry<IronChestsUpgradeType, RegistryObject<ChestUpgradeItem>> entry : IronChestsItems.UPGRADES.entrySet())
        {
            if (entry.getValue().get() == stack.getItem())
            {
                return entry.getKey();
            }
        }
        return null;
    }

    private static IronChestCompat.Tier tierOf(IronChestsTypes type)
    {
        for (IronChestCompat.Tier tier : IronChestCompat.Tier.values())
        {
            if (tier.type == type)
            {
                return tier;
            }
        }
        return null;
    }

    /** The color of a chest this upgrade applies to, or -1 if it isn't a dyed chest of the upgrade's source tier. */
    private static int colorOf(BlockState state, IronChestsUpgradeType upgrade)
    {
        if (state.getBlock() instanceof DyedIronChestBlock block)
        {
            return upgrade.canUpgrade(block.tier().type) ? state.getValue(DyedIronChestBlock.COLOR) : -1;
        }
        if (state.getBlock() instanceof BlockChest && upgrade.canUpgrade(IronChestsTypes.WOOD))
        {
            return Arrays.asList(MDBlock.chestArray).indexOf(state.getBlock());
        }
        return -1;
    }

    private static void upgrade(Level level, BlockPos pos, BlockState old, IronChestCompat.Tier target, int color, Player player, ItemStack upgrade)
    {
        BlockEntity entity = level.getBlockEntity(pos);
        if (!(entity instanceof BaseContainerBlockEntity container) || !container.canOpen(player) || isOpen(level, pos, entity))
        {
            return;
        }
        NonNullList<ItemStack> items = NonNullList.withSize(target.type.size, ItemStack.EMPTY);
        for (int slot = 0; slot < Math.min(container.getContainerSize(), items.size()); slot++)
        {
            items.set(slot, container.getItem(slot));
        }
        Component name = container.getCustomName();

        BlockState state = target.block.get().defaultBlockState()
                .setValue(DyedIronChestBlock.COLOR, color)
                .setValue(AbstractIronChestBlock.FACING, old.getValue(BlockStateProperties.HORIZONTAL_FACING))
                .setValue(AbstractIronChestBlock.WATERLOGGED, old.getValue(BlockStateProperties.WATERLOGGED));
        // Removing the block entity first keeps the old chest from dropping what it held.
        level.removeBlockEntity(pos);
        level.removeBlock(pos, false);
        level.setBlock(pos, state, 3);
        if (level.getBlockEntity(pos) instanceof DyedIronChestBlockEntity chest)
        {
            chest.setItems(items);
            if (name != null)
            {
                chest.setCustomName(name);
            }
        }
        if (!player.getAbilities().instabuild)
        {
            upgrade.shrink(1);
        }
    }

    private static boolean isOpen(Level level, BlockPos pos, BlockEntity entity)
    {
        if (entity instanceof AbstractIronChestBlockEntity)
        {
            return AbstractIronChestBlockEntity.getOpenCount(level, pos) > 0;
        }
        return entity instanceof ChestBlockEntity && ChestBlockEntity.getOpenCount(level, pos) > 0;
    }
}
