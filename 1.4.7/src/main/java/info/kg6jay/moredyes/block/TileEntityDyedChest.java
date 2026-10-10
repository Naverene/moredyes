package info.kg6jay.moredyes.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

/**
 * Tile entity for the dyed chests: the vanilla chest inventory and lid, plus the color. A dyed chest only joins a dyed
 * chest of the same color, and the vanilla code that opens and closes the lid only works for the vanilla chest block,
 * so both are done here.
 */
public class TileEntityDyedChest extends TileEntityChest implements IDyedTile {

    private static final String TAG_COLOR = "color";

    private int color;

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
        this.adjacentChestChecked = false;
    }

    @Override
    public String getInvName() {
        return "container.chest";
    }

    @Override
    public void checkForAdjacentChests() {
        if (this.adjacentChestChecked || this.worldObj == null) {
            return;
        }
        this.adjacentChestChecked = true;
        this.adjacentChestXNeg = this.findPartner(this.xCoord - 1, this.yCoord, this.zCoord);
        this.adjacentChestXPos = this.findPartner(this.xCoord + 1, this.yCoord, this.zCoord);
        this.adjacentChestZNeg = this.findPartner(this.xCoord, this.yCoord, this.zCoord - 1);
        this.adjacentChestZPosition = this.findPartner(this.xCoord, this.yCoord, this.zCoord + 1);
        // Same as vanilla: the neighbours check again, so a half that was placed or removed is noticed on both sides.
        this.notifyNeighbour(this.xCoord - 1, this.yCoord, this.zCoord);
        this.notifyNeighbour(this.xCoord + 1, this.yCoord, this.zCoord);
        this.notifyNeighbour(this.xCoord, this.yCoord, this.zCoord - 1);
        this.notifyNeighbour(this.xCoord, this.yCoord, this.zCoord + 1);
    }

    private TileEntityDyedChest findPartner(int x, int y, int z) {
        if (this.isInvalid() || this.worldObj.getBlockId(x, y, z) != this.worldObj.getBlockId(this.xCoord,
            this.yCoord, this.zCoord)) {
            return null;
        }
        TileEntity te = this.worldObj.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityDyedChest && !te.isInvalid() && ((TileEntityDyedChest) te).color == this.color) {
            return (TileEntityDyedChest) te;
        }
        return null;
    }

    private void notifyNeighbour(int x, int y, int z) {
        TileEntity te = this.worldObj.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityDyedChest) {
            TileEntityDyedChest chest = (TileEntityDyedChest) te;
            boolean linked = chest.adjacentChestXNeg == this || chest.adjacentChestXPos == this
                || chest.adjacentChestZNeg == this || chest.adjacentChestZPosition == this;
            if (linked != (chest.findPartner(this.xCoord, this.yCoord, this.zCoord) == this)) {
                chest.adjacentChestChecked = false;
            }
        }
    }

    @Override
    public void openChest() {
        ++this.numUsingPlayers;
        this.sendUsers();
    }

    @Override
    public void closeChest() {
        --this.numUsingPlayers;
        this.sendUsers();
    }

    /** Tells the clients how many players have the chest open, which opens or closes the lid. */
    private void sendUsers() {
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord,
            this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord), 1, this.numUsingPlayers);
    }

    /** Keeps the inventory while the block stays the same; the metadata (the facing) may change. */
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

    /** Sends the color; the contents only go to players who open the chest. */
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setShort(TAG_COLOR, (short) this.color);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }

    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
        this.setColor(packet.customParam1.getShort(TAG_COLOR));
    }
}
