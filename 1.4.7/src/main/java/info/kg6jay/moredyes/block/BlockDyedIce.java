package info.kg6jay.moredyes.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Dyed ice: see-through and slippery. Like vanilla ice it drops nothing unless broken with silk touch, and turns to
 * water when broken over a solid block or a liquid. It does not melt, so it can be built with anywhere.
 */
public class BlockDyedIce extends BlockDyedGlass {

    public BlockDyedIce(int id, BlockInfo info, Faces faces) {
        super(id, info, faces, null);
        this.slipperiness = 0.98F;
        this.setLightOpacity(3);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
        super.harvestBlock(world, player, x, y, z, meta);
        if (!EnchantmentHelper.getSilkTouchModifier(player) && !world.provider.isHellWorld) {
            Material below = world.getBlockMaterial(x, y - 1, z);
            if (below.blocksMovement() || below.isLiquid()) {
                world.setBlockWithNotify(x, y, z, Block.waterMoving.blockID);
            }
        }
    }
}
