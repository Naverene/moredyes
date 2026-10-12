package info.kg6jay.moredyes.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import info.kg6jay.moredyes.Textures;

/**
 * A flower pot holding a dyed flower or sapling. The vanilla flower pot of Minecraft 1.4.7 keeps its plant in its
 * metadata and only knows the vanilla plants, so putting a dyed plant into one (see FlowerPotHandler) swaps it for
 * this block: the metadata is the plant (its index in MDBlocks.POTTABLE) and the tile entity holds its color. The pot
 * keeps its natural color; FlowerPotRenderer draws the plant inside it tinted.
 */
public class BlockDyedFlowerPot extends Block implements IDyedBlock {

    public static int renderId;

    private final BlockInfo info;

    public BlockDyedFlowerPot(int id, BlockInfo info) {
        super(id, Textures.FLOWER_POT, info.material);
        this.info = info;
        info.apply(this);
        this.setTextureFile(Textures.SHEET);
        this.setBlockBoundsForItemRender();
    }

    /** The dyed plant in a pot with this metadata. */
    public static Block plant(int meta) {
        return MDBlocks.POTTABLE.get(meta < 0 || meta >= MDBlocks.POTTABLE.size() ? 0 : meta);
    }

    @Override
    public String getDyedName() {
        return this.info.displayName;
    }

    @Override
    public void setBlockBoundsForItemRender() {
        float size = 0.375F;
        float half = size / 2.0F;
        this.setBlockBounds(0.5F - half, 0.0F, 0.5F - half, 0.5F + half, size, 0.5F + half);
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
    public int getRenderType() {
        return renderId;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y, z) && world.doesBlockHaveSolidTopSurface(x, y - 1, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighbor) {
        if (!world.doesBlockHaveSolidTopSurface(x, y - 1, z)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockWithNotify(x, y, z, 0);
        }
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityDyed();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int id, int meta) {
        Dyed.remember(world, x, y, z);
        super.breakBlock(world, x, y, z, id, meta);
    }

    /** The vanilla flower pot and the dyed plant. */
    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(Item.flowerPot));
        drops.add(Dyed.stack(plant(meta), world, x, y, z, 1));
        return drops;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return Dyed.stack(plant(world.getBlockMetadata(x, y, z)), world, x, y, z, 1);
    }
}
