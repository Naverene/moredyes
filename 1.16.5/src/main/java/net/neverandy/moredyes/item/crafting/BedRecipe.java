package net.neverandy.moredyes.item.crafting;

import com.google.gson.JsonObject;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BedItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.neverandy.moredyes.ConfigHandler;
import net.neverandy.moredyes.block.DyedBedBlock;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.tileentity.DyedBedTileEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * A bed recipe (minecraft:crafting_shaped JSON) whose bed remembers the planks it was made from, so its frame is drawn
 * in that wood. It also replaces vanilla's bed recipes: a vanilla bed made from planks other than oak becomes the More
 * Dyes bed in the same color, which can remember them.
 */
public class BedRecipe extends ShapedRecipe
{
    public static final IRecipeSerializer<BedRecipe> SERIALIZER = new Serializer();
    public static final String ID = "crafting_bed";

    public BedRecipe(ShapedRecipe base)
    {
        super(base.getId(), base.getGroup(), base.getRecipeWidth(), base.getRecipeHeight(), base.getIngredients(), base.getRecipeOutput());
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory container)
    {
        ItemStack result = super.getCraftingResult(container);
        ResourceLocation wood = planks(container);
        if (wood == null || !ConfigHandler.woodBedFrames() || DyedBedTileEntity.OAK.equals(wood))
        {
            return result;
        }
        if (result.getItem() instanceof BedItem && !(((BedItem) result.getItem()).getBlock() instanceof DyedBedBlock))
        {
            BedBlock bed = MDBlock.vanillaColorBed(((BedBlock) ((BedItem) result.getItem()).getBlock()).getColor());
            if (bed == null)
            {
                return result;
            }
            ItemStack swapped = new ItemStack(bed, result.getCount());
            swapped.setTag(result.getTag());
            result = swapped;
        }
        DyedBedTileEntity.setWood(result, wood);
        return result;
    }

    /** The planks used the most in the grid (the first of them on a tie), by block id. */
    private static ResourceLocation planks(CraftingInventory container)
    {
        Map<ResourceLocation, Integer> counts = new HashMap<>();
        ResourceLocation best = null;
        for (int i = 0; i < container.getSizeInventory(); i++)
        {
            ItemStack stack = container.getStackInSlot(i);
            if (stack.getItem().isIn(ItemTags.PLANKS))
            {
                ResourceLocation id = Block.getBlockFromItem(stack.getItem()).getRegistryName();
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
    public IRecipeSerializer<?> getSerializer()
    {
        return SERIALIZER;
    }

    private static final class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BedRecipe>
    {
        @Override
        public BedRecipe read(ResourceLocation id, JsonObject json)
        {
            return new BedRecipe(IRecipeSerializer.CRAFTING_SHAPED.read(id, json));
        }

        @Override
        public BedRecipe read(ResourceLocation id, PacketBuffer buffer)
        {
            return new BedRecipe(IRecipeSerializer.CRAFTING_SHAPED.read(id, buffer));
        }

        @Override
        public void write(PacketBuffer buffer, BedRecipe recipe)
        {
            IRecipeSerializer.CRAFTING_SHAPED.write(buffer, recipe);
        }
    }
}
