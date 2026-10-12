package info.kg6jay.moredyes.block;

import java.util.ArrayList;

import net.minecraft.block.BlockSign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import info.kg6jay.moredyes.block.tileentity.TileEntityMDSign;
import info.kg6jay.moredyes.item.MDItem;
import info.kg6jay.moredyes.reference.Reference;

/**
 * A dyed sign in every color, standing or on a wall, like the two vanilla sign blocks. The color is kept in the sign's
 * tile entity with its text, and the sign item's damage is the color's number (see ColorIndex).
 * TileEntityMDSignRenderer
 * draws it.
 */
public class MDBlockDyedSign extends BlockSign {

    public MDBlockDyedSign(boolean standing, String name) {
        super(TileEntityMDSign.class, standing);
        this.setHardness(1.0F);
        this.setStepSound(soundTypeWood);
        this.disableStats();
        this.setBlockName(Reference.MOD_ID + "." + name);
    }

    public static int getColor(IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return te instanceof TileEntityMDSign sign ? sign.getColor() : 0;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(MDItem.sign, 1, getColor(world, x, y, z)));
        return drops;
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        return willHarvest || super.removedByPlayer(world, player, x, y, z, false);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
        super.harvestBlock(world, player, x, y, z, meta);
        world.setBlockToAir(x, y, z);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(MDItem.sign, 1, getColor(world, x, y, z));
    }
}
