package info.kg6jay.moredyes.block.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

import info.kg6jay.moredyes.block.BedWood;
import info.kg6jay.moredyes.item.MDItem;

/**
 * A half of a dyed bed: its color, the planks it was made from, and whether it has the vanilla bedding (see BedWood).
 */
public class TileEntityMDBed extends TileEntityMDColor {

    private String wood;
    private boolean vanilla;

    /** The planks the bed was made from, or null for oak. */
    public String getWood() {
        return this.wood;
    }

    public boolean isVanilla() {
        return this.vanilla;
    }

    /** Takes the color, wood and bedding of a bed item. */
    public void setItemValues(ItemStack stack) {
        this.wood = BedWood.woodOf(stack);
        this.vanilla = BedWood.isVanilla(stack);
        this.setColor(stack.getItemDamage());
    }

    /** The bed item this half drops. */
    public ItemStack getItemStack() {
        ItemStack stack = this.vanilla ? BedWood.vanillaBed() : new ItemStack(MDItem.bed, 1, this.getColor());
        BedWood.setWood(stack, this.wood);
        return stack;
    }

    private void readExtra(NBTTagCompound tag) {
        this.wood = tag.hasKey(BedWood.TAG, 8) ? tag.getString(BedWood.TAG) : null;
        this.vanilla = tag.getBoolean(BedWood.VANILLA_TAG);
    }

    private void writeExtra(NBTTagCompound tag) {
        if (this.wood != null) {
            tag.setString(BedWood.TAG, this.wood);
        }
        if (this.vanilla) {
            tag.setBoolean(BedWood.VANILLA_TAG, true);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.readExtra(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        this.writeExtra(tag);
    }

    @Override
    public Packet getDescriptionPacket() {
        // The packet's tag can only be read on the client, so it is built here rather than taken from the parent's.
        NBTTagCompound tag = new NBTTagCompound();
        tag.setShort("color", (short) this.getColor());
        this.writeExtra(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        this.readExtra(packet.func_148857_g());
        super.onDataPacket(net, packet);
    }
}
