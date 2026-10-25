package net.neverandy.moredyes.compat.ironshulkerbox;

import com.progwml6.ironshulkerbox.common.block.GenericIronShulkerBlock;
import com.progwml6.ironshulkerbox.common.block.IronShulkerBoxesTypes;
import com.progwml6.ironshulkerbox.common.block.tileentity.GenericIronShulkerBoxTileEntity;
import com.progwml6.ironshulkerbox.common.items.IronShulkerBoxesItems;
import com.progwml6.ironshulkerbox.common.items.IronShulkerBoxesUpgradeType;
import com.progwml6.ironshulkerbox.common.items.ShulkerBoxUpgradeItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.ShulkerBoxTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.neverandy.moredyes.block.MDBlock;

import java.util.Arrays;
import java.util.Map;

/**
 * Iron Shulker Boxes' upgrade items keep the color: an iron-to-gold upgrade on a dyed iron box gives a dyed gold box of
 * the same color, and a vanilla-to-iron upgrade on a dyed vanilla shulker box gives a dyed iron box. Iron Shulker
 * Boxes' own code would give one of its 16 colors, so the click is handled here first. The box keeps its contents, name
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
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState old = world.getBlockState(pos);
        int color = colorOf(old, upgrade);
        IronShulkerBoxCompat.Tier target = IronShulkerBoxCompat.Tier.of(upgrade.target);
        if (color < 0 || target == null)
        {
            return;
        }
        event.setCanceled(true);
        event.setCancellationResult(ActionResultType.func_233537_a_(world.isRemote));
        if (!world.isRemote)
        {
            BlockState state = target.block.get().getDefaultState()
                    .with(DyedIronShulkerBoxBlock.COLOR, color)
                    .with(GenericIronShulkerBlock.FACING, old.get(ShulkerBoxBlock.FACING));
            upgrade(world, pos, state, event.getPlayer(), event.getItemStack());
        }
    }

    private static IronShulkerBoxesUpgradeType upgradeOf(ItemStack stack)
    {
        for (Map.Entry<IronShulkerBoxesUpgradeType, RegistryObject<ShulkerBoxUpgradeItem>> entry : IronShulkerBoxesItems.UPGRADES.entrySet())
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
        if (state.getBlock() instanceof DyedIronShulkerBoxBlock)
        {
            DyedIronShulkerBoxBlock block = (DyedIronShulkerBoxBlock) state.getBlock();
            return upgrade.canUpgrade(block.tier().type) ? state.get(DyedIronShulkerBoxBlock.COLOR) : -1;
        }
        if (upgrade.canUpgrade(IronShulkerBoxesTypes.VANILLA))
        {
            return Arrays.asList(MDBlock.shulkerBoxArray).indexOf(state.getBlock());
        }
        return -1;
    }

    private static void upgrade(World world, BlockPos pos, BlockState state, PlayerEntity player, ItemStack upgrade)
    {
        TileEntity entity = world.getTileEntity(pos);
        if (!(entity instanceof LockableTileEntity) || !((LockableTileEntity) entity).canOpen(player) || isOpen(entity))
        {
            return;
        }
        LockableTileEntity container = (LockableTileEntity) entity;
        NonNullList<ItemStack> items = NonNullList.withSize(container.getSizeInventory(), ItemStack.EMPTY);
        for (int slot = 0; slot < items.size(); slot++)
        {
            items.set(slot, container.getStackInSlot(slot));
        }
        ITextComponent name = container.getCustomName();

        // Emptying and removing the old box first keeps it from dropping what it held.
        container.clear();
        world.removeTileEntity(pos);
        world.removeBlock(pos, false);
        world.setBlockState(pos, state, 3);
        TileEntity placed = world.getTileEntity(pos);
        if (placed instanceof DyedIronShulkerBoxTileEntity)
        {
            // Every upgrade is to a larger box, so everything fits.
            DyedIronShulkerBoxTileEntity box = (DyedIronShulkerBoxTileEntity) placed;
            box.setItems(items);
            if (name != null)
            {
                box.setCustomName(name);
            }
        }
        if (!player.abilities.isCreativeMode)
        {
            upgrade.shrink(1);
        }
    }

    private static boolean isOpen(TileEntity entity)
    {
        if (entity instanceof GenericIronShulkerBoxTileEntity)
        {
            return ((GenericIronShulkerBoxTileEntity) entity).getAnimationStatus() != GenericIronShulkerBoxTileEntity.AnimationStatus.CLOSED;
        }
        return entity instanceof ShulkerBoxTileEntity
                && ((ShulkerBoxTileEntity) entity).getAnimationStatus() != ShulkerBoxTileEntity.AnimationStatus.CLOSED;
    }
}
