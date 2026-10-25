package info.kg6jay.moredyes.compat.etfuturum;

import java.lang.reflect.Field;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

import ganymedes01.etfuturum.tileentities.TileEntityShulkerBox;

/**
 * A dyed shulker box: an Et Futurum Requiem shulker box (of any of its tiers) that also keeps its More Dyes color (see
 * ColorIndex). Et Futurum's own 16 colors are not used: its color field stays 0.
 */
public class DyedShulkerBoxTile extends TileEntityShulkerBox {

    private static final String TAG_COLOR = "MoreDyesColor";

    private int dye;

    public int getDye() {
        return this.dye;
    }

    /** Sets the color. The caller saves and sends the change (markDirty, markBlockForUpdate) if the box is placed. */
    public void setDye(int dye) {
        this.dye = dye;
        this.color = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.dye = nbt.getShort(TAG_COLOR);
        this.color = 0;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setShort(TAG_COLOR, (short) this.dye);
    }

    /**
     * Et Futurum's packet (tier, facing, the items a crystal box shows) with the color added. The packet's tag can only
     * be read on the client, so it is found by its type.
     */
    @Override
    public Packet getDescriptionPacket() {
        Packet packet = super.getDescriptionPacket();
        NBTTagCompound tag = packetTag(packet);
        if (tag != null) {
            tag.setShort(TAG_COLOR, (short) this.dye);
        }
        return packet;
    }

    private static NBTTagCompound packetTag(Packet packet) {
        if (!(packet instanceof S35PacketUpdateTileEntity)) {
            return null;
        }
        for (Field field : S35PacketUpdateTileEntity.class.getDeclaredFields()) {
            if (field.getType() == NBTTagCompound.class) {
                try {
                    field.setAccessible(true);
                    return (NBTTagCompound) field.get(packet);
                } catch (ReflectiveOperationException | RuntimeException e) {
                    return null;
                }
            }
        }
        return null;
    }

    /** The dropped box: Et Futurum's item stack (contents, tier and name) as a dyed box of this color. */
    @Override
    public ItemStack createItemStack(EntityPlayer player) {
        ItemStack plain = super.createItemStack(player);
        if (plain == null) {
            return null;
        }
        ItemStack stack = new ItemStack(EtFuturumCompat.block, 1, this.dye);
        stack.setTagCompound(plain.getTagCompound());
        EtFuturumCompat.clearVanillaColor(stack);
        return stack;
    }
}
