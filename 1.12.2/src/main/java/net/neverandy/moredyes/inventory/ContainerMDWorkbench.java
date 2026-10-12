package net.neverandy.moredyes.inventory;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.neverandy.moredyes.block.BlockColoredWorkbench;
import net.neverandy.moredyes.block.BlockDyedWorkbenchSlab;

/** The vanilla crafting grid, staying open next to a dyed crafting table or crafting table slab instead of the vanilla one. */
public class ContainerMDWorkbench extends ContainerWorkbench
{
	private final World world;
	private final BlockPos pos;

	public ContainerMDWorkbench(InventoryPlayer inventory,World world,BlockPos pos)
	{
		super(inventory,world,pos);
		this.world=world;
		this.pos=pos;
	}
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		Block block=this.world.getBlockState(this.pos).getBlock();
		return (block instanceof BlockColoredWorkbench||block instanceof BlockDyedWorkbenchSlab)
				&&player.getDistanceSq(this.pos.getX()+0.5D,this.pos.getY()+0.5D,this.pos.getZ()+0.5D)<=64.0D;
	}
}
