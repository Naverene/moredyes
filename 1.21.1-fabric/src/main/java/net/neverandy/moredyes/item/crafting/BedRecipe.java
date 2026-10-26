package net.neverandy.moredyes.item.crafting;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;

import net.neverandy.moredyes.block.DyedBedBlock;
import net.neverandy.moredyes.block.DyedBedBlockEntity;
import net.neverandy.moredyes.registry.ModBlocks;
import net.neverandy.moredyes.registry.ModRecipes;

/**
 * A bed recipe ({@code minecraft:crafting_shaped} JSON) whose bed remembers the planks it was made from, so its frame
 * is drawn in that wood. A vanilla bed made from planks other than oak becomes the More Dyes bed in the same color,
 * which can remember them.
 */
public class BedRecipe extends ShapedRecipe {

    public static final MapCodec<BedRecipe> CODEC = RecipeSerializer.SHAPED_RECIPE.codec().xmap(BedRecipe::new,
        recipe -> recipe);
    public static final StreamCodec<RegistryFriendlyByteBuf, BedRecipe> STREAM_CODEC = RecipeSerializer.SHAPED_RECIPE
        .streamCodec().map(BedRecipe::new, recipe -> recipe);

    public BedRecipe(ShapedRecipe base) {
        // ShapedRecipe.getResultItem only returns the result; it doesn't use the registries.
        super(base.getGroup(), base.category(),
            new ShapedRecipePattern(base.getWidth(), base.getHeight(), base.getIngredients(), Optional.empty()),
            base.getResultItem(null), base.showNotification());
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack result = super.assemble(input, registries);
        ResourceLocation wood = planks(input);
        if (wood == null || DyedBedBlockEntity.OAK.equals(wood)) {
            return result;
        }
        if (result.getItem() instanceof BedItem item && !(item.getBlock() instanceof DyedBedBlock)) {
            Block bed = ModBlocks.vanillaColorBed(((BedBlock) item.getBlock()).getColor());
            if (bed == null) {
                return result;
            }
            result = result.transmuteCopy(bed, result.getCount());
        }
        DyedBedBlockEntity.setWood(result, wood);
        return result;
    }

    /** The planks used the most in the grid (the first of them on a tie), by block id. */
    private static ResourceLocation planks(CraftingInput input) {
        Map<ResourceLocation, Integer> counts = new HashMap<>();
        ResourceLocation best = null;
        for (ItemStack stack : input.items()) {
            if (stack.is(ItemTags.PLANKS)) {
                ResourceLocation id = BuiltInRegistries.BLOCK.getKey(Block.byItem(stack.getItem()));
                int count = counts.merge(id, 1, Integer::sum);
                if (best == null || count > counts.get(best)) {
                    best = id;
                }
            }
        }
        return best;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.BED;
    }
}
