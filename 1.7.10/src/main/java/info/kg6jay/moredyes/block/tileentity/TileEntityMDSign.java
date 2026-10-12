package info.kg6jay.moredyes.block.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntitySign;

/**
 * A dyed sign: a vanilla sign with its color (see ColorIndex). The client is sent the color together with the text,
 * so it is a full tile entity update rather than the vanilla sign packet.
 */
public class TileEntityMDSign extends TileEntitySign {

    private static final String TAG_COLOR = "color";

    private int color;

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
        this.markDirty();
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
        this.writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        NBTTagCompound tag = packet.func_148857_g();
        for (int i = 0; i < this.signText.length; ++i) {
            this.signText[i] = tag.getString("Text" + (i + 1));
        }
        this.color = tag.getShort(TAG_COLOR);
    }
}
