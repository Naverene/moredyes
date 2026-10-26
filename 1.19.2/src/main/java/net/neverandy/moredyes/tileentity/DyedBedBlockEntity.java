package net.neverandy.moredyes.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * A More Dyes bed's block entity: vanilla's, plus the planks the bed was made from, which client/DyedBedRenderer draws
 * its frame in. Both halves of the bed hold it. The item keeps it as {@code BlockEntityTag.Wood}, the planks' block id;
 * a bed without it (or made from oak) has vanilla's oak frame.
 */
public class DyedBedBlockEntity extends BedBlockEntity
{
    public static final String WOOD = "Wood";
    private static final String BLOCK_ENTITY_TAG = "BlockEntityTag";
    public static final ResourceLocation OAK = new ResourceLocation("oak_planks");

    @Nullable
    private ResourceLocation wood;

    public DyedBedBlockEntity(BlockPos pos, BlockState state)
    {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType()
    {
        return ModTileEntities.BED.get();
    }

    @Nullable
    public ResourceLocation getWood()
    {
        return wood;
    }

    public void setWood(@Nullable ResourceLocation wood)
    {
        this.wood = OAK.equals(wood) ? null : wood;
        setChanged();
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        wood = tag.contains(WOOD) ? ResourceLocation.tryParse(tag.getString(WOOD)) : null;
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        if (wood != null)
        {
            tag.putString(WOOD, wood.toString());
        }
    }

    /** Sent to the client, which draws the frame. */
    @Override
    public CompoundTag getUpdateTag()
    {
        return saveWithoutMetadata();
    }

    /** The planks a bed item was made from, or null for oak. */
    @Nullable
    public static ResourceLocation woodOf(ItemStack stack)
    {
        CompoundTag tag = BlockItem.getBlockEntityData(stack);
        return tag != null && tag.contains(WOOD) ? ResourceLocation.tryParse(tag.getString(WOOD)) : null;
    }

    /** Records the planks on a bed item; oak (or null) leaves the item plain, so it stacks with other beds. */
    public static void setWood(ItemStack stack, @Nullable ResourceLocation wood)
    {
        if (wood == null || OAK.equals(wood))
        {
            CompoundTag tag = stack.getTagElement(BLOCK_ENTITY_TAG);
            if (tag != null)
            {
                tag.remove(WOOD);
                if (tag.isEmpty())
                {
                    stack.removeTagKey(BLOCK_ENTITY_TAG);
                }
            }
            return;
        }
        stack.getOrCreateTagElement(BLOCK_ENTITY_TAG).putString(WOOD, wood.toString());
    }
}
