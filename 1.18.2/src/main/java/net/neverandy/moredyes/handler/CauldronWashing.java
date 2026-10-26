package net.neverandy.moredyes.handler;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.DyedBedBlockEntity;

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
        Level world = event.getWorld();
        BlockState state = world.getBlockState(event.getPos());
        ItemStack held = event.getItemStack();
        if (!state.is(Blocks.WATER_CAULDRON) || held.isEmpty())
        {
            return;
        }
        // Dyed shulker boxes and patterned banners are washed by vanilla's cauldron code (see Furnishings), which keeps
        // their contents and patterns.
        if (Block.byItem(held.getItem()) instanceof ShulkerBoxBlock
                || held.getItem() instanceof BannerItem && BannerBlockEntity.getPatternCount(held) > 0)
        {
            return;
        }
        int water = state.getValue(LayeredCauldronBlock.LEVEL);
        ItemStack washed = washed(world, held);
        if (water <= 0 || washed.isEmpty())
        {
            return;
        }

        // Cancelled on both sides so the held block is not placed; the client still sends the click to the server.
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(world.isClientSide));
        if (world.isClientSide)
        {
            return;
        }
        Player player = event.getPlayer();
        ItemStack result = washed.copy();
        ResourceLocation wood = DyedBedBlockEntity.woodOf(held);
        if (wood != null && result.is(Items.WHITE_BED) && MDBlock.vanillaColorBed(DyeColor.WHITE) != null)
        {
            // A bed keeps the planks it was made from, so it washes to the More Dyes white bed that remembers them.
            result = new ItemStack(MDBlock.vanillaColorBed(DyeColor.WHITE));
            DyedBedBlockEntity.setWood(result, wood);
        }
        result.setCount(held.getCount());
        player.setItemInHand(event.getHand(), result);
        LayeredCauldronBlock.lowerFillLevel(state, world, event.getPos());
        player.awardStat(Stats.USE_CAULDRON);
    }

    /** The vanilla block a dyed block washes back into, or an empty stack if it isn't a dyed block. */
    private static ItemStack washed(Level world, ItemStack dyed)
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
        Optional<? extends Recipe<?>> recipe = world.getRecipeManager()
                .byKey(new ResourceLocation(Reference.MOD_ID, "washing/" + type));
        return recipe.map(Recipe::getResultItem).orElse(ItemStack.EMPTY);
    }
}
