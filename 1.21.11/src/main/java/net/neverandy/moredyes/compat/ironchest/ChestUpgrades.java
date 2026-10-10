package net.neverandy.moredyes.compat.ironchest;

import java.util.Map;
import java.util.function.Supplier;

import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.regular.AbstractIronChestBlock;
import com.progwml6.ironchest.common.block.regular.entity.AbstractIronChestBlockEntity;
import com.progwml6.ironchest.common.item.IronChestsItems;
import com.progwml6.ironchest.common.item.IronChestsUpgradeType;

import org.jspecify.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import net.neverandy.moredyes.block.Kind;
import net.neverandy.moredyes.color.MixColors;
import net.neverandy.moredyes.registry.ModBlocks;

/**
 * Iron Chests' upgrade items keep the color: an iron-to-gold upgrade on a dyed iron chest gives a dyed gold chest of the
 * same color, and a wood-to-iron upgrade on a More Dyes dyed chest gives a dyed iron chest. An upgrade to a tier with
 * no dyed version (crystal, obsidian) gives Iron Chests' plain chest, as Iron Chests would. Iron Chests' own upgrade
 * code only knows its own chests and plain chests, so this runs first, before the item is used.
 */
final class ChestUpgrades {

    private static final Map<Supplier<? extends Item>, IronChestsUpgradeType> UPGRADES = Map.of(
        IronChestsItems.IRON_TO_GOLD_CHEST_UPGRADE, IronChestsUpgradeType.IRON_TO_GOLD,
        IronChestsItems.GOLD_TO_DIAMOND_CHEST_UPGRADE, IronChestsUpgradeType.GOLD_TO_DIAMOND,
        IronChestsItems.COPPER_TO_IRON_CHEST_UPGRADE, IronChestsUpgradeType.COPPER_TO_IRON,
        IronChestsItems.DIAMOND_TO_CRYSTAL_CHEST_UPGRADE, IronChestsUpgradeType.DIAMOND_TO_CRYSTAL,
        IronChestsItems.WOOD_TO_IRON_CHEST_UPGRADE, IronChestsUpgradeType.WOOD_TO_IRON,
        IronChestsItems.WOOD_TO_COPPER_CHEST_UPGRADE, IronChestsUpgradeType.WOOD_TO_COPPER,
        IronChestsItems.DIAMOND_TO_OBSIDIAN_CHEST_UPGRADE, IronChestsUpgradeType.DIAMOND_TO_OBSIDIAN);

    private ChestUpgrades() {}

    static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        IronChestsUpgradeType upgrade = upgradeOf(event.getItemStack());
        if (upgrade == null) {
            return;
        }
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState old = level.getBlockState(pos);
        int color = colorOf(old, upgrade);
        if (color < 0) {
            return;
        }
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
        if (!level.isClientSide()) {
            upgrade(level, pos, old, upgrade.target, color, event.getEntity(), event.getItemStack());
        }
    }

    private static @Nullable IronChestsUpgradeType upgradeOf(ItemStack stack) {
        for (Map.Entry<Supplier<? extends Item>, IronChestsUpgradeType> entry : UPGRADES.entrySet()) {
            if (stack.is(entry.getKey().get())) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * The color of a chest this upgrade applies to, by its position in {@link MixColors#ALL}, or -1 if it isn't a
     * dyed chest of the upgrade's source tier.
     */
    private static int colorOf(BlockState state, IronChestsUpgradeType upgrade) {
        if (state.getBlock() instanceof DyedIronChestBlock block) {
            return upgrade.canUpgrade(block.tier().type) ? state.getValue(DyedIronChestBlock.COLOR) : -1;
        }
        if (upgrade.canUpgrade(IronChestsTypes.WOOD) && ModBlocks.isKind(state, Kind.CHEST)) {
            return MixColors.of(BuiltInRegistries.BLOCK.getKey(state.getBlock())).map(MixColors.ALL::indexOf).orElse(-1);
        }
        return -1;
    }

    private static void upgrade(Level level, BlockPos pos, BlockState old, IronChestsTypes target, int color,
        Player player, ItemStack upgrade) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (!(entity instanceof BaseContainerBlockEntity container) || !container.canOpen(player) || isOpen(level, pos, entity)) {
            return;
        }
        IronChestCompat.Tier tier = IronChestCompat.Tier.of(target);
        BlockState state = tier != null
            ? tier.block().defaultBlockState().setValue(DyedIronChestBlock.COLOR, color)
            : IronChestsTypes.get(target).get(0).defaultBlockState();
        state = state.setValue(AbstractIronChestBlock.FACING, old.getValue(BlockStateProperties.HORIZONTAL_FACING))
            .setValue(AbstractIronChestBlock.WATERLOGGED, old.getValue(BlockStateProperties.WATERLOGGED));

        NonNullList<ItemStack> items = NonNullList.withSize(target.size, ItemStack.EMPTY);
        for (int slot = 0; slot < Math.min(container.getContainerSize(), items.size()); slot++) {
            items.set(slot, container.getItem(slot));
        }
        Component name = container.getCustomName();
        // Removing the block entity first keeps the old chest from dropping what it held.
        level.removeBlockEntity(pos);
        level.removeBlock(pos, false);
        level.setBlock(pos, state, 3);
        if (level.getBlockEntity(pos) instanceof AbstractIronChestBlockEntity chest) {
            // Applying the name as a component also empties the chest, so it comes before the items.
            if (name != null) {
                chest.applyComponents(DataComponentMap.builder().set(DataComponents.CUSTOM_NAME, name).build(),
                    DataComponentPatch.EMPTY);
            }
            chest.setItems(items);
            chest.setChanged();
        }
        if (!player.getAbilities().instabuild) {
            upgrade.shrink(1);
        }
    }

    private static boolean isOpen(Level level, BlockPos pos, BlockEntity entity) {
        if (entity instanceof AbstractIronChestBlockEntity) {
            return AbstractIronChestBlockEntity.getOpenCount(level, pos) > 0;
        }
        return entity instanceof ChestBlockEntity && ChestBlockEntity.getOpenCount(level, pos) > 0;
    }
}
