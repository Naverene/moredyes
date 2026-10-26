package net.neverandy.moredyes.handler;

import net.neverandy.moredyes.tileentity.DyedBedTileEntity;
import net.neverandy.moredyes.block.MDBlock;
import net.minecraft.item.Items;
import net.minecraft.item.DyeColor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.BannerItem;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.neverandy.moredyes.reference.Reference;

import java.util.Optional;

/**
 * Right-clicking a cauldron that has water in it with a stack of dyed blocks washes the whole stack back to the
 * vanilla block and uses up one level of water, like washing dyed leather armor. The vanilla block is the result of
 * the block's "washing/" crafting recipe (dyed block plus water bucket), so a data pack that changes that recipe
 * changes the cauldron too.
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public final class CauldronWashing
{
    private CauldronWashing() {}

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        World world = event.getWorld();
        BlockState state = world.getBlockState(event.getPos());
        ItemStack held = event.getItemStack();
        if (!state.matchesBlock(Blocks.CAULDRON) || held.isEmpty())
        {
            return;
        }
        // Dyed shulker boxes and patterned banners are washed by vanilla's cauldron code, which keeps their contents
        // and patterns.
        if (Block.getBlockFromItem(held.getItem()) instanceof ShulkerBoxBlock
                || held.getItem() instanceof BannerItem && BannerTileEntity.getPatterns(held) > 0)
        {
            return;
        }
        int water = state.get(CauldronBlock.LEVEL);
        ItemStack washed = washed(world, held);
        if (water <= 0 || washed.isEmpty())
        {
            return;
        }

        // Cancelled on both sides so the held block is not placed; the client still sends the click to the server.
        event.setCanceled(true);
        event.setCancellationResult(ActionResultType.func_233537_a_(world.isRemote));
        if (world.isRemote)
        {
            return;
        }
        PlayerEntity player = event.getPlayer();
        ItemStack result = washed.copy();
        ResourceLocation wood = DyedBedTileEntity.woodOf(held);
        if (wood != null && result.getItem() == Items.WHITE_BED && MDBlock.vanillaColorBed(DyeColor.WHITE) != null)
        {
            // A bed keeps the planks it was made from, so it washes to the More Dyes white bed that remembers them.
            result = new ItemStack(MDBlock.vanillaColorBed(DyeColor.WHITE));
            DyedBedTileEntity.setWood(result, wood);
        }
        result.setCount(held.getCount());
        player.setHeldItem(event.getHand(), result);
        ((CauldronBlock) Blocks.CAULDRON).setWaterLevel(world, event.getPos(), state, water - 1);
        player.addStat(Stats.USE_CAULDRON);
    }

    /** The vanilla block a dyed block washes back into, or an empty stack if it isn't a dyed block. */
    private static ItemStack washed(World world, ItemStack dyed)
    {
        ResourceLocation item = dyed.getItem().getRegistryName();
        if (item == null || !Reference.MOD_ID.equals(item.getNamespace()))
        {
            return ItemStack.EMPTY;
        }
        // One recipe per type, such as "washing/wool" for "wool_334c59".
        String path = item.getPath();
        int color = path.lastIndexOf('_');
        String type = color < 0 ? path : path.substring(0, color);
        Optional<? extends IRecipe<?>> recipe = world.getRecipeManager()
                .getRecipe(new ResourceLocation(Reference.MOD_ID, "washing/" + type));
        return recipe.map(IRecipe::getRecipeOutput).orElse(ItemStack.EMPTY);
    }
}
