package net.neverandy.moredyes.item.crafting;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neverandy.moredyes.block.DyedBedBlock;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.tileentity.DyedBedBlockEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * A bed recipe (minecraft:crafting_shaped JSON) whose bed remembers the planks it was made from, so its frame is drawn
 * in that wood. It also replaces vanilla's bed recipes: a vanilla bed made from planks other than oak becomes the More
 * Dyes bed in the same color, which can remember them.
 */
public class BedRecipe extends ShapedRecipe
{
    public static final RecipeSerializer<BedRecipe> SERIALIZER = new Serializer();
    public static final String ID = "crafting_bed";

    public BedRecipe(ShapedRecipe base)
    {
        super(base.getId(), base.getGroup(), base.category(), base.getWidth(), base.getHeight(), base.getIngredients(),
                base.getResultItem(RegistryAccess.EMPTY), base.showNotification());
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess access)
    {
        ItemStack result = super.assemble(container, access);
        ResourceLocation wood = planks(container);
        if (wood == null || DyedBedBlockEntity.OAK.equals(wood))
        {
            return result;
        }
        if (result.getItem() instanceof BedItem item && !(item.getBlock() instanceof DyedBedBlock))
        {
            BedBlock bed = MDBlock.vanillaColorBed(((BedBlock) item.getBlock()).getColor());
            if (bed == null)
            {
                return result;
            }
            ItemStack swapped = new ItemStack(bed, result.getCount());
            swapped.setTag(result.getTag());
            result = swapped;
        }
        DyedBedBlockEntity.setWood(result, wood);
        return result;
    }

    /** The planks used the most in the grid (the first of them on a tie), by block id. */
    private static ResourceLocation planks(CraftingContainer container)
    {
        Map<ResourceLocation, Integer> counts = new HashMap<>();
        ResourceLocation best = null;
        for (int i = 0; i < container.getContainerSize(); i++)
        {
            ItemStack stack = container.getItem(i);
            if (stack.is(ItemTags.PLANKS))
            {
                ResourceLocation id = BuiltInRegistries.BLOCK.getKey(Block.byItem(stack.getItem()));
                int count = counts.merge(id, 1, Integer::sum);
                if (best == null || count > counts.get(best))
                {
                    best = id;
                }
            }
        }
        return best;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return SERIALIZER;
    }

    private static final class Serializer implements RecipeSerializer<BedRecipe>
    {
        @Override
        public BedRecipe fromJson(ResourceLocation id, JsonObject json)
        {
            return new BedRecipe(RecipeSerializer.SHAPED_RECIPE.fromJson(id, json));
        }

        @Override
        public BedRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer)
        {
            return new BedRecipe(RecipeSerializer.SHAPED_RECIPE.fromNetwork(id, buffer));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BedRecipe recipe)
        {
            RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
        }
    }
}
