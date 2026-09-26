package info.kg6jay.moredyes.block.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

/**
 * Tile entity for the dyed chests. Reuses the vanilla chest inventory and lid animation, but stores the facing itself
 * (the block metadata holds the color) and only joins with a chest of the same block and shade.
 */
public class TileEntityMDBlockColoredChest extends TileEntityChest {

    private static final String TAG_FACING = "facing";

    /** Same values as the vanilla chest metadata: 2 = north, 3 = south, 4 = west, 5 = east. */
    private int facing = 3;

    public int getFacing() {
        return this.facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
        this.markDirty();
    }

    @Override
    public void checkForAdjacentChests() {
        if (this.adjacentChestChecked) {
            return;
        }
        this.adjacentChestChecked = true;
        this.adjacentChestZNeg = this.findPartner(this.xCoord, this.yCoord, this.zCoord - 1);
        this.adjacentChestZPos = this.findPartner(this.xCoord, this.yCoord, this.zCoord + 1);
        this.adjacentChestXNeg = this.findPartner(this.xCoord - 1, this.yCoord, this.zCoord);
        this.adjacentChestXPos = this.findPartner(this.xCoord + 1, this.yCoord, this.zCoord);

        // Vanilla does the same: tell the neighbours to re-check, so a newly placed or removed half is noticed on
        // the client as well (neighbour-change notifications only happen on the server).
        this.notifyNeighbour(this.xCoord, this.yCoord, this.zCoord - 1);
        this.notifyNeighbour(this.xCoord, this.yCoord, this.zCoord + 1);
        this.notifyNeighbour(this.xCoord - 1, this.yCoord, this.zCoord);
        this.notifyNeighbour(this.xCoord + 1, this.yCoord, this.zCoord);
    }

    private TileEntityMDBlockColoredChest findPartner(int x, int y, int z) {
        if (this.worldObj == null || this.isInvalid()) {
            return null;
        }
        if (this.worldObj.getBlock(x, y, z) != this.getBlockType()
            || this.worldObj.getBlockMetadata(x, y, z) != this.getBlockMetadata()) {
            return null;
        }
        TileEntity te = this.worldObj.getTileEntity(x, y, z);
        return te instanceof TileEntityMDBlockColoredChest chest ? chest : null;
    }

    private void notifyNeighbour(int x, int y, int z) {
        if (this.worldObj == null) {
            return;
        }
        TileEntity te = this.worldObj.getTileEntity(x, y, z);
        if (te instanceof TileEntityMDBlockColoredChest chest && chest.adjacentChestChecked) {
            boolean linkedToThis = chest.adjacentChestZNeg == this || chest.adjacentChestZPos == this
                || chest.adjacentChestXNeg == this
                || chest.adjacentChestXPos == this;
            boolean shouldLink = !this.isInvalid() && this.findPartner(x, y, z) == chest;
            if (linkedToThis != shouldLink) {
                chest.adjacentChestChecked = false;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey(TAG_FACING)) {
            this.facing = tag.getByte(TAG_FACING);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setByte(TAG_FACING, (byte) this.facing);
    }

    /** Sends the facing to clients; the inventory is synced separately through the container when opened. */
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte(TAG_FACING, (byte) this.facing);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        this.facing = packet.func_148857_g()
            .getByte(TAG_FACING);
    }
}
