package info.kg6jay.moredyes.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import info.kg6jay.moredyes.block.BlockDyedSand;
import info.kg6jay.moredyes.block.Dyed;

/**
 * Falling dyed sand or concrete powder. The vanilla falling sand only carries a block ID and metadata, so it would
 * lose the color; this one carries the color too and gives it back to the block it lands as.
 */
public class EntityFallingDyed extends Entity implements IEntityAdditionalSpawnData {

    public int blockID, metadata, color;
    private int fallTime;

    public EntityFallingDyed(World world) {
        super(world);
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
    }

    public EntityFallingDyed(World world, int x, int y, int z, int blockID, int metadata, int color) {
        this(world);
        this.blockID = blockID;
        this.metadata = metadata;
        this.color = color;
        this.setPosition(x + 0.5D, y + 0.5D, z + 0.5D);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    }

    @Override
    protected void entityInit() {}

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    /** Same as vanilla falling sand, but the block it lands as gets the color back. */
    @Override
    public void onUpdate() {
        Block block = this.blockID > 0 && this.blockID < Block.blocksList.length ? Block.blocksList[this.blockID]
            : null;
        if (block == null) {
            this.setDead();
            return;
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        ++this.fallTime;
        this.motionY -= 0.04D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.98D;
        this.motionY *= 0.98D;
        this.motionZ *= 0.98D;
        if (this.worldObj.isRemote) {
            return;
        }
        int x = MathHelper.floor_double(this.posX);
        int y = MathHelper.floor_double(this.posY);
        int z = MathHelper.floor_double(this.posZ);
        if (this.fallTime == 1) {
            if (this.worldObj.getBlockId(x, y, z) != this.blockID) {
                this.setDead();
                return;
            }
            this.worldObj.setBlockWithNotify(x, y, z, 0);
        }
        if (this.onGround) {
            this.motionX *= 0.7D;
            this.motionZ *= 0.7D;
            this.motionY *= -0.5D;
            if (this.worldObj.getBlockId(x, y, z) != Block.pistonMoving.blockID) {
                this.setDead();
                if (this.worldObj.canPlaceEntityOnSide(this.blockID, x, y, z, true, 1, null)
                    && !BlockSand.canFallBelow(this.worldObj, x, y - 1, z)
                    && this.worldObj.setBlockAndMetadataWithNotify(x, y, z, this.blockID, this.metadata)) {
                    Dyed.set(this.worldObj, x, y, z, this.color);
                    if (block instanceof BlockDyedSand) {
                        ((BlockDyedSand) block).onLanded(this.worldObj, x, y, z);
                    }
                } else {
                    this.entityDropItem(new ItemStack(this.blockID, 1, this.color), 0.0F);
                }
            }
        } else if (this.fallTime > 100 && (y < 1 || y > 256) || this.fallTime > 600) {
            this.entityDropItem(new ItemStack(this.blockID, 1, this.color), 0.0F);
            this.setDead();
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {
        tag.setShort("Tile", (short) this.blockID);
        tag.setByte("Data", (byte) this.metadata);
        tag.setShort("Color", (short) this.color);
        tag.setShort("Time", (short) this.fallTime);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {
        this.blockID = tag.getShort("Tile");
        this.metadata = tag.getByte("Data") & 15;
        this.color = tag.getShort("Color");
        this.fallTime = tag.getShort("Time");
    }

    @Override
    public void writeSpawnData(ByteArrayDataOutput data) {
        data.writeShort(this.blockID);
        data.writeByte(this.metadata);
        data.writeShort(this.color);
    }

    @Override
    public void readSpawnData(ByteArrayDataInput data) {
        this.blockID = data.readShort();
        this.metadata = data.readByte();
        this.color = data.readShort();
    }

    @Override
    public float getShadowSize() {
        return 0.0F;
    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }
}
