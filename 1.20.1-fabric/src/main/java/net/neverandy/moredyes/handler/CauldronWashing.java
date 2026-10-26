package net.neverandy.moredyes.handler;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.DyedBedBlockEntity;

/**
 * Right-clicking a cauldron that has water in it with a stack of dyed blocks washes the whole stack back to the
 * vanilla block and uses up one level of water, like washing dyed leather armor. The vanilla block is the result of
 * the block's "washing/" crafting recipe (dyed block plus water bucket), so a data pack that changes that recipe
 * changes the cauldron too.
 */
public final class CauldronWashing
{
    private CauldronWashing() {}

    public static void register()
    {
        UseBlockCallback.EVENT.register((player, level, hand, hit) -> onRightClickBlock(player, level, hand, hit.getBlockPos()));
    }

    private static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockPos pos)
    {
        BlockState state = level.getBlockState(pos);
        ItemStack held = player.getItemInHand(hand);
        if (player.isSpectator() || !state.is(Blocks.WATER_CAULDRON) || held.isEmpty())
        {
            return InteractionResult.PASS;
        }
        // Dyed shulker boxes and patterned banners are washed by vanilla's cauldron code (see block/Furnishings), which
        // keeps their contents and patterns.
        if (Block.byItem(held.getItem()) instanceof ShulkerBoxBlock
                || held.getItem() instanceof BannerItem && BannerBlockEntity.getPatternCount(held) > 0)
        {
            return InteractionResult.PASS;
        }
        ItemStack washed = washed(level, held);
        if (washed.isEmpty())
        {
            return InteractionResult.PASS;
        }

        // Handled on both sides so the held block is not placed; the client still sends the click to the server.
        if (level.isClientSide)
        {
            return InteractionResult.SUCCESS;
        }
        ItemStack result = washed.copy();
        ResourceLocation wood = DyedBedBlockEntity.woodOf(held);
        if (wood != null && result.is(Items.WHITE_BED) && MDBlock.vanillaColorBed(DyeColor.WHITE) != null)
        {
            // A bed keeps the planks it was made from, so it washes to the More Dyes white bed that remembers them.
            result = new ItemStack(MDBlock.vanillaColorBed(DyeColor.WHITE));
            DyedBedBlockEntity.setWood(result, wood);
        }
        result.setCount(held.getCount());
        player.setItemInHand(hand, result);
        LayeredCauldronBlock.lowerFillLevel(state, level, pos);
        player.awardStat(Stats.USE_CAULDRON);
        return InteractionResult.CONSUME;
    }

    /** The vanilla block a dyed block washes back into, or an empty stack if it isn't a dyed block. */
    private static ItemStack washed(Level level, ItemStack dyed)
    {
        ResourceLocation item = BuiltInRegistries.ITEM.getKey(dyed.getItem());
        if (!Reference.MOD_ID.equals(item.getNamespace()))
        {
            return ItemStack.EMPTY;
        }
        // One recipe per type, such as "washing/wool" for "wool_334c59".
        String path = item.getPath();
        int color = path.lastIndexOf('_');
        String type = color < 0 ? path : path.substring(0, color);
        return level.getRecipeManager().byKey(new ResourceLocation(Reference.MOD_ID, "washing/" + type))
                .map((Recipe<?> recipe) -> recipe.getResultItem(level.registryAccess())).orElse(ItemStack.EMPTY);
    }
}
