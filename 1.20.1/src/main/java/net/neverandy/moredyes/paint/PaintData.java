package net.neverandy.moredyes.paint;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import net.neverandy.moredyes.reference.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * The painted blocks of one chunk, saved with the chunk. Each painted position keeps its color and the block that was
 * painted: paint only shows while that block is still there, so a block that is broken, blown up or pushed away
 * loses its paint, and the entry is dropped when the chunk is saved.
 */
public class PaintData implements ICapabilitySerializable<CompoundTag>
{
    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "paint");
    public static final Capability<PaintData> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    /** A block's paint: its color (0xRRGGBB) and the block it was painted on. */
    public record Paint(int rgb, Block block) {}

    private final LevelChunk chunk;
    private final Map<Long, Paint> paints = new HashMap<>();
    private final LazyOptional<PaintData> self = LazyOptional.of(() -> this);

    public PaintData(LevelChunk chunk)
    {
        this.chunk = chunk;
    }

    /** The paint at a position, or null if there is none or the painted block is gone. */
    @Nullable
    public Paint get(BlockPos pos)
    {
        Paint paint = paints.get(pos.asLong());
        return paint != null && chunk.getBlockState(pos).is(paint.block()) ? paint : null;
    }

    public void set(BlockPos pos, Paint paint)
    {
        paints.put(pos.asLong(), paint);
        chunk.setUnsaved(true);
    }

    /** Removes the paint at a position, and says whether there was any. */
    public boolean remove(BlockPos pos)
    {
        boolean removed = paints.remove(pos.asLong()) != null;
        if (removed)
        {
            chunk.setUnsaved(true);
        }
        return removed;
    }

    /** Every paint whose block is still there, by position (BlockPos.asLong). */
    public Map<Long, Paint> all()
    {
        paints.entrySet().removeIf(entry -> !chunk.getBlockState(BlockPos.of(entry.getKey())).is(entry.getValue().block()));
        return paints;
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side)
    {
        return CAPABILITY.orEmpty(capability, self);
    }

    @Override
    public CompoundTag serializeNBT()
    {
        ListTag list = new ListTag();
        for (Map.Entry<Long, Paint> entry : all().entrySet())
        {
            CompoundTag tag = new CompoundTag();
            tag.putLong("pos", entry.getKey());
            tag.putInt("color", entry.getValue().rgb());
            tag.putString("block", String.valueOf(ForgeRegistries.BLOCKS.getKey(entry.getValue().block())));
            list.add(tag);
        }
        CompoundTag tag = new CompoundTag();
        tag.put("paints", list);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        paints.clear();
        ListTag list = nbt.getList("paints", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++)
        {
            CompoundTag tag = list.getCompound(i);
            Block block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(tag.getString("block")));
            // A block from a removed mod comes back as air; its paint goes.
            if (block != null && !block.defaultBlockState().isAir())
            {
                paints.put(tag.getLong("pos"), new Paint(tag.getInt("color"), block));
            }
        }
    }
}
