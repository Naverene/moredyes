package info.kg6jay.moredyes.compat.storagedrawers;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersStandard;

/**
 * Storage Drawers' standard drawer that also keeps its color (see ColorIndex). Storage Drawers sends its whole tag to
 * clients, so the color reaches them with it.
 */
public class DyedDrawersTile extends TileEntityDrawersStandard {

    private static final String TAG_COLOR = "MoreDyesColor";

    private int color;

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
        this.markDirty();
    }

    /** Keep this tile entity while the block stays the same (Forge's default replaces it when the metadata changes). */
    @Override
    public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y,
        int z) {
        return oldBlock != newBlock;
    }

    /** The color belongs to the block, not to what a taped drawer carries, so it is in the fixed part of the tag. */
    @Override
    protected void readFromFixedNBT(NBTTagCompound tag) {
        super.readFromFixedNBT(tag);
        this.color = tag.getShort(TAG_COLOR);
    }

    @Override
    protected void writeToFixedNBT(NBTTagCompound tag) {
        super.writeToFixedNBT(tag);
        tag.setShort(TAG_COLOR, (short) this.color);
    }
}
