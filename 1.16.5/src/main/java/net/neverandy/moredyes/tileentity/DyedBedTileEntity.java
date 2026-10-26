package net.neverandy.moredyes.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.BedTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * A More Dyes bed's tile entity: vanilla's, plus the planks the bed was made from, which client/DyedBedRenderer draws
 * its frame in. Both halves of the bed hold it. The item keeps it as {@code BlockEntityTag.Wood}, the planks' block id;
 * a bed without it (or made from oak) has vanilla's oak frame.
 */
public class DyedBedTileEntity extends BedTileEntity
{
    public static final String WOOD = "Wood";
    public static final ResourceLocation OAK = new ResourceLocation("oak_planks");
    private static final String BLOCK_ENTITY_TAG = "BlockEntityTag";

    @Nullable
    private ResourceLocation wood;

    @Override
    public TileEntityType<?> getType()
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
        markDirty();
    }

    @Override
    public void read(BlockState state, CompoundNBT tag)
    {
        super.read(state, tag);
        wood = tag.contains(WOOD) ? ResourceLocation.tryCreate(tag.getString(WOOD)) : null;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        super.write(tag);
        if (wood != null)
        {
            tag.putString(WOOD, wood.toString());
        }
        return tag;
    }

    /** Sent to the client, which draws the frame. */
    @Override
    public CompoundNBT getUpdateTag()
    {
        return write(new CompoundNBT());
    }

    /** The planks a bed item was made from, or null for oak. */
    @Nullable
    public static ResourceLocation woodOf(ItemStack stack)
    {
        CompoundNBT tag = stack.getChildTag(BLOCK_ENTITY_TAG);
        return tag != null && tag.contains(WOOD) ? ResourceLocation.tryCreate(tag.getString(WOOD)) : null;
    }

    /** Records the planks on a bed item; oak (or null) leaves the item plain, so it stacks with other beds. */
    public static void setWood(ItemStack stack, @Nullable ResourceLocation wood)
    {
        if (wood == null || OAK.equals(wood))
        {
            CompoundNBT tag = stack.getChildTag(BLOCK_ENTITY_TAG);
            if (tag != null)
            {
                tag.remove(WOOD);
                if (tag.isEmpty())
                {
                    stack.removeChildTag(BLOCK_ENTITY_TAG);
                }
            }
            return;
        }
        stack.getOrCreateChildTag(BLOCK_ENTITY_TAG).putString(WOOD, wood.toString());
    }
}
