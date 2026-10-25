package net.neverandy.moredyes.compat.ironshulkerbox;

import com.progwml6.ironshulkerbox.common.block.GenericIronShulkerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Washing a dyed box in a cauldron gives Iron Shulker Boxes' purple box of the same tier, keeping its contents and
 * name. Iron Shulker Boxes has no undyed box on 1.16.5, and purple is the color of the plain vanilla shulker box.
 */
public final class ShulkerBoxWashing
{
    private ShulkerBoxWashing() {}

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        World world = event.getWorld();
        BlockState state = world.getBlockState(event.getPos());
        ItemStack held = event.getItemStack();
        if (!state.matchesBlock(Blocks.CAULDRON) || !(held.getItem() instanceof DyedIronShulkerBoxItem) || state.get(CauldronBlock.LEVEL) <= 0)
        {
            return;
        }
        event.setCanceled(true);
        event.setCancellationResult(ActionResultType.func_233537_a_(world.isRemote));
        if (world.isRemote)
        {
            return;
        }
        IronShulkerBoxCompat.Tier tier = ((DyedIronShulkerBoxItem) held.getItem()).tier();
        ItemStack washed = new ItemStack(GenericIronShulkerBlock.getBlockByColor(DyeColor.PURPLE, tier.type));
        if (held.getTag() != null)
        {
            washed.setTag(held.getTag().copy());
            washed.removeChildTag("BlockStateTag");
        }
        event.getPlayer().setHeldItem(event.getHand(), washed);
        ((CauldronBlock) Blocks.CAULDRON).setWaterLevel(world, event.getPos(), state, state.get(CauldronBlock.LEVEL) - 1);
        event.getPlayer().addStat(Stats.CLEAN_SHULKER_BOX);
    }
}
