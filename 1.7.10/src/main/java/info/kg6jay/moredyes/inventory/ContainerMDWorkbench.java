package info.kg6jay.moredyes.inventory;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.world.World;

import info.kg6jay.moredyes.block.MDBlockDyedWorkbenchSlab;
import info.kg6jay.moredyes.block.MDBlockWorkbench;

/**
 * The vanilla workbench container closes itself on the next tick unless the block is exactly
 * Blocks.crafting_table, so the dyed crafting tables and crafting table slabs need their own check.
 */
public class ContainerMDWorkbench extends ContainerWorkbench {

    private final World world;
    private final int x, y, z;

    public ContainerMDWorkbench(InventoryPlayer inventory, World world, int x, int y, int z) {
        super(inventory, world, x, y, z);
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        Block block = this.world.getBlock(this.x, this.y, this.z);
        return (block instanceof MDBlockWorkbench || block instanceof MDBlockDyedWorkbenchSlab)
            && player.getDistanceSq(this.x + 0.5D, this.y + 0.5D, this.z + 0.5D) <= 64.0D;
    }
}
