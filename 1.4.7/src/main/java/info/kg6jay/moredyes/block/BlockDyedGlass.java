package info.kg6jay.moredyes.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Dyed glass, clear or foggy. Like vanilla glass it drops nothing unless broken with silk touch. The faces between
 * two glass blocks of the same kind and color are hidden, so a wall of one color looks like one pane.
 */
public class BlockDyedGlass extends BlockDyed {

    private final String variant;

    public BlockDyedGlass(int id, BlockInfo info, Faces faces, String variant) {
        super(id, info, faces);
        this.variant = variant;
    }

    @Override
    public String getDyedName() {
        return this.variant + " " + this.info.displayName;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int fortune) {
        return new ArrayList<ItemStack>();
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
        if (EnchantmentHelper.getSilkTouchModifier(player)) {
            BlockDyedStone.Silk.harvest(this, world, player, x, y, z);
        } else {
            super.harvestBlock(world, player, x, y, z, meta);
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public boolean canRenderInPass(int pass) {
        return pass == 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        if (world.getBlockId(x, y, z) == this.blockID && Dyed.get(world, x, y, z) == Dyed.get(world,
            x - Facing.offsetsXForSide[side], y - Facing.offsetsYForSide[side], z - Facing.offsetsZForSide[side])) {
            return false;
        }
        return super.shouldSideBeRendered(world, x, y, z, side);
    }
}
