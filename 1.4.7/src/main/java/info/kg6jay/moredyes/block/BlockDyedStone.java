package info.kg6jay.moredyes.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/** Dyed stone drops dyed cobblestone of the same color, like vanilla stone. Silk touch gives the stone itself. */
public class BlockDyedStone extends BlockDyed {

    public BlockDyedStone(int id, BlockInfo info, Faces faces) {
        super(id, info, faces);
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int fortune) {
        return Dyed.drops(MDBlocks.cobble, world, x, y, z, 1);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
        if (EnchantmentHelper.getSilkTouchModifier(player)) {
            Silk.harvest(this, world, player, x, y, z);
        } else {
            super.harvestBlock(world, player, x, y, z, meta);
        }
    }

    /** Silk touch harvesting for the dyed blocks that drop something else normally. */
    static final class Silk {

        private Silk() {}

        static void harvest(Block block, World world, EntityPlayer player, int x, int y, int z) {
            player.addExhaustion(0.025F);
            if (!world.isRemote) {
                ItemStack stack = Dyed.stack(block, world, x, y, z, 1);
                float spread = 0.7F;
                double dx = world.rand.nextFloat() * spread + (1.0F - spread) * 0.5D;
                double dy = world.rand.nextFloat() * spread + (1.0F - spread) * 0.5D;
                double dz = world.rand.nextFloat() * spread + (1.0F - spread) * 0.5D;
                net.minecraft.entity.item.EntityItem item = new net.minecraft.entity.item.EntityItem(world, x + dx,
                    y + dy, z + dz, stack);
                item.delayBeforeCanPickup = 10;
                world.spawnEntityInWorld(item);
            }
        }
    }
}
