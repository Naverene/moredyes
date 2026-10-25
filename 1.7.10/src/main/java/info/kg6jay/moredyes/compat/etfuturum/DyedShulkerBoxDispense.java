package info.kg6jay.moredyes.compat.etfuturum;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/** A dispenser places a dyed shulker box in front of it, as Et Futurum's dispensers do with its boxes. */
public class DyedShulkerBoxDispense extends BehaviorDefaultDispenseItem {

    @Override
    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        EnumFacing facing = BlockDispenser.func_149937_b(source.getBlockMetadata());
        World world = source.getWorld();
        int x = source.getXInt() + facing.getFrontOffsetX();
        int y = source.getYInt() + facing.getFrontOffsetY();
        int z = source.getZInt() + facing.getFrontOffsetZ();
        if (!world.getBlock(x, y, z)
            .isReplaceable(world, x, y, z) || !world.setBlock(x, y, z, EtFuturumCompat.block)) {
            return super.dispenseStack(source, stack);
        }
        EtFuturumCompat.block.onBlockPlacedBy(world, x, y, z, null, stack);
        if (world.getTileEntity(x, y, z) instanceof DyedShulkerBoxTile box) {
            box.facing = facing != EnumFacing.UP && world.getBlock(x, y - 1, z) == Blocks.air ? (byte) facing.ordinal()
                : 1;
        }
        stack.stackSize--;
        return stack;
    }
}
