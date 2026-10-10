package info.kg6jay.moredyes.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Holds the color of a dyed block (see Colors). Every dyed block keeps its color here rather than in its block ID or
 * metadata, the way microblock mods keep many blocks in one block ID, so each kind of dyed block needs only one block
 * ID for all its colors and its metadata stays free for direction, shape and the like.
 */
public class TileEntityDyed extends TileEntity implements IDyedTile {

    private static final String TAG_COLOR = "color";

    private int color;

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    /** Never ticks, so a world full of dyed blocks costs no time per tick. */
    @Override
    public boolean canUpdate() {
        return false;
    }

    /**
     * Keeps the color while the block stays the same. Forge would otherwise replace the tile entity whenever the
     * metadata changes (a slab becoming a double slab, a trapdoor opening, leaves being checked for decay).
     */
    @Override
    public boolean shouldRefresh(int oldID, int newID, int oldMeta, int newMeta, World world, int x, int y, int z) {
        return oldID != newID;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.color = tag.getShort(TAG_COLOR);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setShort(TAG_COLOR, (short) this.color);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setShort(TAG_COLOR, (short) this.color);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }

    /** The color can arrive after the chunk was drawn, so draw the block again. */
    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
        this.color = packet.customParam1.getShort(TAG_COLOR);
        if (this.worldObj != null) {
            this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }
}
