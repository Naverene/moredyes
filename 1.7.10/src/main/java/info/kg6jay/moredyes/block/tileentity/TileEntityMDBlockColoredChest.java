package info.kg6jay.moredyes.block.tileentity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;

public class TileEntityMDBlockColoredChest extends TileEntityChest {

    // Inherit everything from vanilla for now.
    private int facing;

    public void wasPlaced(EntityLivingBase entityliving, ItemStack itemStack) {}

    public int getFacing()
    {
        if (this.worldObj == null) return this.facing; // return stored facing when not in world
        try {
            int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
            // Return the raw metadata value (vanilla chests use 2..5 for facing)
            return meta;
        } catch (Throwable ignored) {
            return this.facing;
        }
    }

    public void setFacing(int facing) {
        if (this.worldObj == null) {
            // store locally so getFacing can return it before world is available
            this.facing = facing;
            return;
        }
        try {
            this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, facing, 3);
        } catch (Throwable ignored) {}
    }

}
