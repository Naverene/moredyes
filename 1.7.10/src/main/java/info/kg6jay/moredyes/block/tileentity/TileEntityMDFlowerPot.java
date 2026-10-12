package info.kg6jay.moredyes.block.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/** The dyed plant in a flower pot (see MDBlockFlowerPot), as the item it was potted from. */
public class TileEntityMDFlowerPot extends TileEntity {

    private static final String TAG_PLANT = "plant";

    private ItemStack plant;

    /** The potted plant, or null if there is none. */
    public ItemStack getPlant() {
        return this.plant;
    }

    public void setPlant(ItemStack plant) {
        this.plant = plant == null ? null : new ItemStack(plant.getItem(), 1, plant.getItemDamage());
        this.markDirty();
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.plant = tag.hasKey(TAG_PLANT) ? ItemStack.loadItemStackFromNBT(tag.getCompoundTag(TAG_PLANT)) : null;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        if (this.plant != null) {
            tag.setTag(TAG_PLANT, this.plant.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }

    /** The plant arrives after the chunk may already have been drawn, so draw the block again. */
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        this.readFromNBT(packet.func_148857_g());
        if (this.worldObj != null) {
            this.worldObj.markBlockRangeForRenderUpdate(
                this.xCoord,
                this.yCoord,
                this.zCoord,
                this.xCoord,
                this.yCoord,
                this.zCoord);
        }
    }
}
