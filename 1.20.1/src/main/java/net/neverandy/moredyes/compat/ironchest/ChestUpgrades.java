package net.neverandy.moredyes.compat.ironchest;

import com.progwml6.ironchest.common.block.IronChestsBlocks;
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
import net.minecraft.world.level.block.ChestBlock;
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
 * no dyed version (crystal, obsidian) give Iron Chests' plain chest of that tier, as Iron Chests itself would. The
 * chest keeps its contents, name and facing.
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
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState old = level.getBlockState(pos);
        BlockState state = upgraded(old, upgrade);
        if (state == null)
        {
            return;
        }
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
        if (!level.isClientSide)
        {
            upgrade(level, pos, state, event.getEntity(), event.getItemStack());
        }
    }

    /**
     * The chest a dyed chest becomes with this upgrade: the dyed chest of the upgrade's tier in the same color, or, for a
     * tier with no dyed version (crystal, obsidian), Iron Chests' own chest of that tier. Null if the upgrade doesn't
     * apply, which leaves it to Iron Chests.
     */
    private static BlockState upgraded(BlockState old, IronChestsUpgradeType upgrade)
    {
        int color = colorOf(old, upgrade);
        if (color < 0)
        {
            return null;
        }
        IronChestCompat.Tier target = tierOf(upgrade.target);
        BlockState state;
        if (target != null)
        {
            state = target.block.get().defaultBlockState().setValue(DyedIronChestBlock.COLOR, color);
        }
        else if (upgrade.target == IronChestsTypes.CRYSTAL)
        {
            state = IronChestsBlocks.CRYSTAL_CHEST.get().defaultBlockState();
        }
        else if (upgrade.target == IronChestsTypes.OBSIDIAN)
        {
            state = IronChestsBlocks.OBSIDIAN_CHEST.get().defaultBlockState();
        }
        else
        {
            return null;
        }
        return state.setValue(AbstractIronChestBlock.FACING, old.getValue(BlockStateProperties.HORIZONTAL_FACING))
                .setValue(AbstractIronChestBlock.WATERLOGGED, old.getValue(BlockStateProperties.WATERLOGGED));
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

        // Removing the block entity first keeps the old chest from dropping what it held.
        level.removeBlockEntity(pos);
        level.removeBlock(pos, false);
        level.setBlock(pos, state, 3);
        if (level.getBlockEntity(pos) instanceof BaseContainerBlockEntity chest)
        {
            // Every upgrade is to a larger chest, so everything fits.
            for (int slot = 0; slot < Math.min(items.size(), chest.getContainerSize()); slot++)
            {
                chest.setItem(slot, items.get(slot));
            }
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
