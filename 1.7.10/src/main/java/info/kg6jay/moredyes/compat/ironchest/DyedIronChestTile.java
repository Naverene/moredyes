package info.kg6jay.moredyes.compat.ironchest;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.World;

import cpw.mods.ironchest.TileEntityIronChest;

/**
 * The inventory of a dyed Iron Chests chest: an Iron Chests chest of its tier that also keeps its color (see
 * ColorIndex). There is a subclass per tier because Minecraft makes tile entities from their class alone.
 */
public abstract class DyedIronChestTile extends TileEntityIronChest {

    private static final String TAG_COLOR = "color";
    private static final String TAG_FACING = "facing";

    private final IronChestCompat.Tier tier;
    private int color;
    /** Players with the chest open, for the lid. Iron Chests keeps its own count private. */
    private int users;

    protected DyedIronChestTile(IronChestCompat.Tier tier) {
        super(tier.type);
        this.tier = tier;
    }

    public IronChestCompat.Tier getTier() {
        return this.tier;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
        this.markDirty();
    }

    /**
     * Iron Chests tells clients to open the lid with a block event sent for its own block, which Minecraft drops
     * because the block here is a different one. Send the same event for this block as well.
     */
    @Override
    public void openInventory() {
        super.openInventory();
        this.users++;
        this.sendUsers();
    }

    @Override
    public void closeInventory() {
        super.closeInventory();
        this.users = Math.max(0, this.users - 1);
        this.sendUsers();
    }

    /** True if a player has the chest open. */
    public boolean isOpen() {
        return this.users > 0;
    }

    private void sendUsers() {
        if (this.worldObj != null) {
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.users);
        }
    }

    /** Keep this tile entity while the block stays the same (Forge's default replaces it when the metadata changes). */
    @Override
    public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y,
        int z) {
        return oldBlock != newBlock;
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

    /** Sends the color and facing to clients. The contents are synced through the screen when it is opened. */
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setShort(TAG_COLOR, (short) this.color);
        tag.setByte(TAG_FACING, (byte) this.getFacing());
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        NBTTagCompound tag = packet.func_148857_g();
        this.color = tag.getShort(TAG_COLOR);
        this.setFacing(tag.getByte(TAG_FACING));
    }

    public static class Iron extends DyedIronChestTile {

        public Iron() {
            super(IronChestCompat.Tier.IRON);
        }
    }

    public static class Gold extends DyedIronChestTile {

        public Gold() {
            super(IronChestCompat.Tier.GOLD);
        }
    }

    public static class Diamond extends DyedIronChestTile {

        public Diamond() {
            super(IronChestCompat.Tier.DIAMOND);
        }
    }

    public static class Copper extends DyedIronChestTile {

        public Copper() {
            super(IronChestCompat.Tier.COPPER);
        }
    }

    public static class Silver extends DyedIronChestTile {

        public Silver() {
            super(IronChestCompat.Tier.SILVER);
        }
    }
}
