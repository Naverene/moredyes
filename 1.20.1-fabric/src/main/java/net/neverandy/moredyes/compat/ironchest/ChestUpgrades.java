package net.neverandy.moredyes.compat.ironchest;

import anner.ironchest.blocks.ChestTypes;
import anner.ironchest.items.UpgradeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neverandy.moredyes.block.BlockChest;
import net.neverandy.moredyes.block.MDBlock;

import java.util.Arrays;
import java.util.Locale;

/**
 * Iron Chests' upgrade items keep the color: an iron-to-gold upgrade on a dyed iron chest gives a dyed gold chest of the
 * same color, and a wood-to-iron upgrade on a dyed vanilla chest gives a dyed iron chest. Upgrades to a tier that has
 * no dyed version (emerald, crystal, obsidian, Christmas) give Iron Chests' plain chest of that tier, as Iron Chests
 * itself would. The chest keeps its contents, name and facing. This runs before the item is used, so Iron Chests' own
 * upgrade code, which only knows plain chests, doesn't get to it.
 */
final class ChestUpgrades
{
    private ChestUpgrades() {}

    static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hit)
    {
        if (player.isSpectator())
        {
            return InteractionResult.PASS;
        }
        ItemStack stack = player.getItemInHand(hand);
        UpgradeTypes upgrade = upgradeOf(stack);
        if (upgrade == null)
        {
            return InteractionResult.PASS;
        }
        BlockPos pos = hit.getBlockPos();
        BlockState old = level.getBlockState(pos);
        int color = colorOf(old, upgrade);
        if (color < 0)
        {
            // Iron Chests' wood upgrades take any chest block, so used while sneaking they would turn a dyed chest of any
            // tier into a plain one, dropping whatever doesn't fit. Without sneaking the chest just opens.
            return old.getBlock() instanceof DyedIronChestBlock && player.isSecondaryUseActive() ? InteractionResult.FAIL : InteractionResult.PASS;
        }
        if (!level.isClientSide)
        {
            upgrade(level, pos, old, upgrade.target, color, player, stack);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /** The upgrade an item makes, from its name: ironchest:iron_gold_upgrade is IRON_TO_GOLD. */
    private static UpgradeTypes upgradeOf(ItemStack stack)
    {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (!IronChestCompat.MOD_ID.equals(id.getNamespace()) || !id.getPath().endsWith("_upgrade"))
        {
            return null;
        }
        String[] tiers = id.getPath().split("_");
        if (tiers.length != 3)
        {
            return null;
        }
        try
        {
            return UpgradeTypes.valueOf((tiers[0] + "_TO_" + tiers[1]).toUpperCase(Locale.ROOT));
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }

    /** The color of a chest this upgrade applies to, or -1 if it isn't a dyed chest of the upgrade's source tier. */
    private static int colorOf(BlockState state, UpgradeTypes upgrade)
    {
        if (state.getBlock() instanceof DyedIronChestBlock block)
        {
            return upgrade.canUpgrade(block.tier().type) ? state.getValue(DyedIronChestBlock.COLOR) : -1;
        }
        if (state.getBlock() instanceof BlockChest && upgrade.canUpgrade(ChestTypes.WOOD))
        {
            return Arrays.asList(MDBlock.chestArray).indexOf(state.getBlock());
        }
        return -1;
    }

    private static void upgrade(Level level, BlockPos pos, BlockState old, ChestTypes target, int color, Player player, ItemStack upgrade)
    {
        BlockEntity entity = level.getBlockEntity(pos);
        if (!(entity instanceof BaseContainerBlockEntity container) || !container.canOpen(player) || isOpen(level, pos, entity))
        {
            return;
        }
        IronChestCompat.Tier tier = IronChestCompat.Tier.of(target);
        BlockState state = tier != null
                ? tier.block().defaultBlockState().setValue(DyedIronChestBlock.COLOR, color)
                : ChestTypes.get(target).defaultBlockState();
        state = state.setValue(ChestBlock.FACING, old.getValue(ChestBlock.FACING))
                .setValue(ChestBlock.WATERLOGGED, old.getValue(ChestBlock.WATERLOGGED));

        // Every upgrade is to a larger chest, so everything fits.
        NonNullList<ItemStack> items = NonNullList.withSize(Math.min(container.getContainerSize(), target.size), ItemStack.EMPTY);
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
        if (entity instanceof DyedIronChestBlockEntity chest)
        {
            return chest.isOpen();
        }
        return entity instanceof ChestBlockEntity && ChestBlockEntity.getOpenCount(level, pos) > 0;
    }
}
