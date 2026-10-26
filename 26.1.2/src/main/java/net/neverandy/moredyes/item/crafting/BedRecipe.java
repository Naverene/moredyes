package net.neverandy.moredyes.item.crafting;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.Nullable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;

import net.neverandy.moredyes.Config;
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

    public static final MapCodec<BedRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
            CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
            ShapedRecipePattern.MAP_CODEC.forGetter(o -> o.pattern),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.template))
        .apply(i, BedRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BedRecipe> STREAM_CODEC = StreamCodec.composite(
        Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo,
        CraftingRecipe.CraftingBookInfo.STREAM_CODEC, o -> o.bookInfo,
        ShapedRecipePattern.STREAM_CODEC, o -> o.pattern,
        ItemStackTemplate.STREAM_CODEC, o -> o.template,
        BedRecipe::new);

    private final ItemStackTemplate template;

    public BedRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo,
        ShapedRecipePattern pattern, ItemStackTemplate result) {
        super(commonInfo, bookInfo, pattern, result);
        this.template = result;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        ItemStack result = super.assemble(input);
        Identifier wood = planks(input);
        if (wood == null || !Config.woodBedFrames() || DyedBedBlockEntity.OAK.equals(wood)) {
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
    private static @Nullable Identifier planks(CraftingInput input) {
        Map<Identifier, Integer> counts = new HashMap<>();
        Identifier best = null;
        for (ItemStack stack : input.items()) {
            if (stack.is(ItemTags.PLANKS)) {
                Identifier id = BuiltInRegistries.BLOCK.getKey(Block.byItem(stack.getItem()));
                int count = counts.merge(id, 1, Integer::sum);
                if (best == null || count > counts.get(best)) {
                    best = id;
                }
            }
        }
        return best;
    }

    /** ShapedRecipe's type says its serializer makes ShapedRecipes, which a BedRecipe is. */
    @Override
    @SuppressWarnings("unchecked")
    public RecipeSerializer<ShapedRecipe> getSerializer() {
        return (RecipeSerializer<ShapedRecipe>) (RecipeSerializer<?>) ModRecipes.BED.get();
    }
}
