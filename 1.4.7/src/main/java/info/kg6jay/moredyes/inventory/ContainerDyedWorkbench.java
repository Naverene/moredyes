package info.kg6jay.moredyes.inventory;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.world.World;

import info.kg6jay.moredyes.block.BlockDyedWorkbench;
import info.kg6jay.moredyes.block.BlockDyedWorkbenchSlab;

/**
 * The vanilla crafting table container closes itself unless the block is the vanilla crafting table, so the dyed
 * crafting tables and crafting table slabs need their own check.
 */
public class ContainerDyedWorkbench extends ContainerWorkbench {

    private final World world;
    private final int x, y, z;

    public ContainerDyedWorkbench(InventoryPlayer inventory, World world, int x, int y, int z) {
        super(inventory, world, x, y, z);
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        Block block = Block.blocksList[this.world.getBlockId(this.x, this.y, this.z)];
        return (block instanceof BlockDyedWorkbench || block instanceof BlockDyedWorkbenchSlab)
            && player.getDistanceSq(this.x + 0.5D, this.y + 0.5D, this.z + 0.5D) <= 64.0D;
    }
}
