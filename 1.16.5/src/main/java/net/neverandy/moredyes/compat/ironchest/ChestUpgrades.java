package net.neverandy.moredyes.compat.ironchest;

import com.progwml6.ironchest.common.block.GenericIronChestBlock;
import com.progwml6.ironchest.common.block.IronChestsTypes;
import com.progwml6.ironchest.common.block.tileentity.GenericIronChestTileEntity;
import com.progwml6.ironchest.common.item.ChestUpgradeItem;
import com.progwml6.ironchest.common.item.IronChestsItems;
import com.progwml6.ironchest.common.item.IronChestsUpgradeType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.neverandy.moredyes.block.BlockChest;
import net.neverandy.moredyes.block.MDBlock;

import java.util.Arrays;
import java.util.Map;

/**
 * Iron Chests' upgrade items keep the color: an iron-to-gold upgrade on a dyed iron chest gives a dyed gold chest of the
 * same color, and a wood-to-iron upgrade on a dyed vanilla chest gives a dyed iron chest. Iron Chests' own upgrade only
 * accepts its own chests (and would turn a dyed wooden chest into a plain one), so the click is handled here first.
 * Upgrades to a tier that has no dyed version (crystal, obsidian) give Iron Chests' plain chest of that tier.
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
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState old = world.getBlockState(pos);
        int color = colorOf(old, upgrade);
        if (color < 0)
        {
            return;
        }
        event.setCanceled(true);
        event.setCancellationResult(ActionResultType.SUCCESS);
        if (!world.isRemote)
        {
            upgrade(world, pos, old, upgrade.target, color, event.getPlayer(), event.getItemStack());
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

    /** The color of a chest this upgrade applies to, or -1 if it isn't a dyed chest of the upgrade's source tier. */
    private static int colorOf(BlockState state, IronChestsUpgradeType upgrade)
    {
        if (state.getBlock() instanceof DyedIronChestBlock)
        {
            DyedIronChestBlock block = (DyedIronChestBlock) state.getBlock();
            return upgrade.canUpgrade(block.tier().type) ? state.get(DyedIronChestBlock.COLOR) : -1;
        }
        if (state.getBlock() instanceof BlockChest && upgrade.canUpgrade(IronChestsTypes.WOOD))
        {
            return Arrays.asList(MDBlock.chestArray).indexOf(state.getBlock());
        }
        return -1;
    }

    private static void upgrade(World world, BlockPos pos, BlockState old, IronChestsTypes target, int color, PlayerEntity player, ItemStack upgrade)
    {
        TileEntity entity = world.getTileEntity(pos);
        if (!(entity instanceof LockableTileEntity) || !((LockableTileEntity) entity).canOpen(player) || isOpen(world, pos, entity))
        {
            return;
        }
        LockableTileEntity container = (LockableTileEntity) entity;
        NonNullList<ItemStack> items = NonNullList.withSize(target.size, ItemStack.EMPTY);
        for (int slot = 0; slot < Math.min(container.getSizeInventory(), items.size()); slot++)
        {
            items.set(slot, container.getStackInSlot(slot));
        }
        ITextComponent name = container.getCustomName();

        IronChestCompat.Tier tier = IronChestCompat.Tier.of(target);
        BlockState state = tier != null
                ? tier.block.get().getDefaultState().with(DyedIronChestBlock.COLOR, color)
                : IronChestsTypes.get(target).getDefaultState();
        state = state.with(GenericIronChestBlock.FACING, old.get(BlockStateProperties.HORIZONTAL_FACING))
                .with(GenericIronChestBlock.WATERLOGGED, old.get(BlockStateProperties.WATERLOGGED));
        // Removing the tile entity first keeps the old chest from dropping what it held.
        world.removeTileEntity(pos);
        world.removeBlock(pos, false);
        world.setBlockState(pos, state, 3);
        TileEntity placed = world.getTileEntity(pos);
        if (placed instanceof GenericIronChestTileEntity)
        {
            GenericIronChestTileEntity chest = (GenericIronChestTileEntity) placed;
            chest.setItems(items);
            if (name != null)
            {
                chest.setCustomName(name);
            }
        }
        if (!player.abilities.isCreativeMode)
        {
            upgrade.shrink(1);
        }
    }

    private static boolean isOpen(World world, BlockPos pos, TileEntity entity)
    {
        if (entity instanceof GenericIronChestTileEntity)
        {
            return GenericIronChestTileEntity.getPlayersUsing(world, pos) > 0;
        }
        return entity instanceof ChestTileEntity && ChestTileEntity.getPlayersUsing(world, pos) > 0;
    }
}
