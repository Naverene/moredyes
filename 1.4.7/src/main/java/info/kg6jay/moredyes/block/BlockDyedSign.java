package info.kg6jay.moredyes.block;

import java.util.ArrayList;

import net.minecraft.block.BlockSign;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * A dyed oak sign, standing or on a wall: the vanilla sign with its color in a TileEntityDyedSign. Both drop the dyed
 * sign item (the standing sign's ItemBlockDyedSign). DyedSignRenderer draws the board tinted.
 */
public class BlockDyedSign extends BlockSign implements IDyedBlock {

    private final BlockInfo info;
    private final boolean standing;

    public BlockDyedSign(int id, BlockInfo info, boolean standing) {
        super(id, TileEntityDyedSign.class, standing);
        this.info = info;
        this.standing = standing;
        info.apply(this);
        this.disableStats();
    }

    public boolean isStanding() {
        return this.standing;
    }

    @Override
    public String getDyedName() {
        return this.info.displayName;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int id, int meta) {
        Dyed.remember(world, x, y, z);
        super.breakBlock(world, x, y, z, id, meta);
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int fortune) {
        return Dyed.drops(MDBlocks.sign, world, x, y, z, 1);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return Dyed.stack(MDBlocks.sign, world, x, y, z, 1);
    }
}
