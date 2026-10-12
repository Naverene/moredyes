package info.kg6jay.moredyes.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntitySign;

/**
 * A dyed sign: a vanilla sign that also holds its color, so the sign editor, the text packets and the text limits all
 * work as for vanilla signs. The color and the text go to the players together in one packet.
 */
public class TileEntityDyedSign extends TileEntitySign implements IDyedTile {

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
        for (int i = 0; i < this.signText.length; ++i) {
            tag.setString("Text" + (i + 1), this.signText[i]);
        }
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }

    /**
     * A sign is only written once, right after it is placed, so the text sent before then is empty. That text is not
     * applied, so it cannot wipe what the player is already typing into the sign editor.
     */
    @Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
        NBTTagCompound tag = packet.customParam1;
        this.color = tag.getShort(TAG_COLOR);
        String[] text = new String[this.signText.length];
        boolean empty = true;
        for (int i = 0; i < text.length; ++i) {
            text[i] = tag.getString("Text" + (i + 1));
            empty &= text[i].length() == 0;
        }
        if (!empty) {
            System.arraycopy(text, 0, this.signText, 0, text.length);
        }
    }
}
