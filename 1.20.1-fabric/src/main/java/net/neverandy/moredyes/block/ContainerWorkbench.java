package net.neverandy.moredyes.block;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.block.Block;

public class ContainerWorkbench extends CraftingMenu
{
    private final ContainerLevelAccess access;

    public ContainerWorkbench(int id, Inventory inventory, ContainerLevelAccess access)
    {
        super(id, inventory, access);
        this.access = access;
    }

    /** Open while the player is near a dyed crafting table or crafting table slab, as vanilla does for its own table. */
    @Override
    public boolean stillValid(Player player)
    {
        return access.evaluate((level, pos) -> isWorkbench(level.getBlockState(pos).getBlock())
                && player.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D, true);
    }

    private static boolean isWorkbench(Block block)
    {
        return block instanceof WorkbenchBlock || block instanceof WorkbenchSlabBlock;
    }
}
