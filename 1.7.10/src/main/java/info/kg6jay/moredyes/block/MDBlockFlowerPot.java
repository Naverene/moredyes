package info.kg6jay.moredyes.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import info.kg6jay.moredyes.block.tileentity.TileEntityMDFlowerPot;
import info.kg6jay.moredyes.reference.Reference;

/**
 * A flower pot holding a dyed flower or sapling. The vanilla flower pot only takes vanilla plants and draws them
 * untinted, so using a dyed plant on an empty vanilla pot turns it into this block, which keeps the plant in its tile
 * entity and draws it tinted (see client/FlowerPotRenderer). Breaking it drops the pot and the plant.
 */
public class MDBlockFlowerPot extends BlockFlowerPot {

    public MDBlockFlowerPot() {
        this.setHardness(0.0F);
        this.setStepSound(soundTypeStone);
        this.setBlockName(Reference.MOD_ID + ".flowerPot");
        this.setBlockTextureName("flower_pot");
    }

    /** True if this stack is a dyed plant that fits in a flower pot. */
    public static boolean canPot(ItemStack stack) {
        return stack != null && Block.getBlockFromItem(stack.getItem()) instanceof MDBlockColoredPlant;
    }

    /** True if the block at this position is an empty vanilla flower pot. */
    public static boolean isEmptyPot(World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return world.getBlock(x, y, z) == Blocks.flower_pot && te instanceof TileEntityFlowerPot pot
            && pot.getFlowerPotItem() == null;
    }

    /**
     * Puts a dyed plant into the empty vanilla flower pot at this position, if it is one. Called when a player uses a
     * dyed plant on a block; returns true if the plant was potted.
     */
    public static boolean pot(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side) {
        if (!canPot(stack) || !isEmptyPot(world, x, y, z) || !player.canPlayerEdit(x, y, z, side, stack)) {
            return false;
        }
        if (!world.isRemote) {
            world.setBlock(x, y, z, MDBlock.flowerPot, 0, 3);
            if (world.getTileEntity(x, y, z) instanceof TileEntityMDFlowerPot pot) {
                pot.setPlant(stack);
            }
            if (!player.capabilities.isCreativeMode) {
                --stack.stackSize;
            }
        }
        return true;
    }

    /** The potted plant, or null. */
    public static ItemStack getPlant(IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return te instanceof TileEntityMDFlowerPot pot ? pot.getPlant() : null;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityMDFlowerPot();
    }

    @Override
    public int getRenderType() {
        return RenderIds.flowerPot;
    }

    /** Nothing else goes into a pot that already has a dyed plant. */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
        float hitY, float hitZ) {
        return false;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(Items.flower_pot));
        ItemStack plant = getPlant(world, x, y, z);
        if (plant != null) {
            drops.add(plant.copy());
        }
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
        ItemStack plant = getPlant(world, x, y, z);
        return plant != null ? plant.copy() : new ItemStack(Items.flower_pot);
    }
}
