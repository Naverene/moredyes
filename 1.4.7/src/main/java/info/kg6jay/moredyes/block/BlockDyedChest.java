package info.kg6jay.moredyes.block;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import info.kg6jay.moredyes.Textures;

/**
 * A dyed chest. The metadata holds the facing, like a vanilla chest (2 north, 3 south, 4 west, 5 east), and the color
 * is in the tile entity. Two chests of the same color next to each other make a double chest. The chest itself is
 * drawn by the tile entity renderer; the plank texture is only for the breaking particles, as with vanilla chests.
 */
public class BlockDyedChest extends BlockDyed {

    public static int renderId;

    private static final int[][] HORIZONTAL = { { 0, -1 }, { 0, 1 }, { -1, 0 }, { 1, 0 } };

    public BlockDyedChest(int id, BlockInfo info) {
        super(id, info, Faces.all(Textures.PLANK));
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return new TileEntityDyedChest();
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

    /** True if there is a dyed chest of this color at this position. */
    private boolean isSameChest(IBlockAccess world, int x, int y, int z, int color) {
        return world.getBlockId(x, y, z) == this.blockID && Dyed.get(world, x, y, z) == color;
    }

    private boolean isPartOfDoubleChest(World world, int x, int y, int z, int color) {
        for (int[] d : HORIZONTAL) {
            if (this.isSameChest(world, x + d[0], y, z + d[1], color)) {
                return true;
            }
        }
        return false;
    }

    /** Like vanilla, a chest may join at most one other chest. Checked by the item, which knows the color. */
    public boolean canPlaceChestAt(World world, int x, int y, int z, int color) {
        int neighbours = 0;
        for (int[] d : HORIZONTAL) {
            if (this.isSameChest(world, x + d[0], y, z + d[1], color)) {
                if (this.isPartOfDoubleChest(world, x + d[0], y, z + d[1], color)) {
                    return false;
                }
                ++neighbours;
            }
        }
        return neighbours <= 1;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int color = Dyed.get(world, x, y, z);
        if (this.isSameChest(world, x, y, z - 1, color)) {
            this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
        } else if (this.isSameChest(world, x, y, z + 1, color)) {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
        } else if (this.isSameChest(world, x - 1, y, z, color)) {
            this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        } else if (this.isSameChest(world, x + 1, y, z, color)) {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
        } else {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        }
    }

    /** Called by the item once the color is set. Faces the player, and turns a double chest's halves the same way. */
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving placer) {
        int color = Dyed.get(world, x, y, z);
        int facing;
        switch (MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3) {
            case 0:
                facing = 2;
                break;
            case 1:
                facing = 5;
                break;
            case 2:
                facing = 3;
                break;
            default:
                facing = 4;
                break;
        }
        boolean partnerOnX = this.isSameChest(world, x - 1, y, z, color) || this.isSameChest(world, x + 1, y, z, color);
        boolean partnerOnZ = this.isSameChest(world, x, y, z - 1, color) || this.isSameChest(world, x, y, z + 1, color);
        if (partnerOnX && facing != 2 && facing != 3) {
            facing = 3;
        } else if (partnerOnZ && facing != 4 && facing != 5) {
            facing = 5;
        }
        world.setBlockMetadataWithNotify(x, y, z, facing);
        for (int[] d : HORIZONTAL) {
            if (this.isSameChest(world, x + d[0], y, z + d[1], color)) {
                world.setBlockMetadataWithNotify(x + d[0], y, z + d[1], facing);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int neighbor) {
        super.onNeighborBlockChange(world, x, y, z, neighbor);
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityDyedChest) {
            te.updateContainingBlockInfo();
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int id, int meta) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof TileEntityDyedChest && !world.isRemote) {
            TileEntityDyedChest chest = (TileEntityDyedChest) te;
            for (int i = 0; i < chest.getSizeInventory(); ++i) {
                ItemStack stack = chest.getStackInSlot(i);
                if (stack != null) {
                    EntityItem item = new EntityItem(world, x + world.rand.nextFloat() * 0.8F + 0.1F,
                        y + world.rand.nextFloat() * 0.8F + 0.1F, z + world.rand.nextFloat() * 0.8F + 0.1F,
                        stack.copy());
                    item.motionX = world.rand.nextGaussian() * 0.05D;
                    item.motionY = world.rand.nextGaussian() * 0.05D + 0.2D;
                    item.motionZ = world.rand.nextGaussian() * 0.05D;
                    world.spawnEntityInWorld(item);
                    chest.setInventorySlotContents(i, null);
                }
            }
        }
        super.breakBlock(world, x, y, z, id, meta);
    }

    /** The inventory to open here (both halves for a double chest), or null if a block above keeps it shut. */
    public IInventory getInventory(World world, int x, int y, int z) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (!(te instanceof TileEntityDyedChest) || world.isBlockSolidOnSide(x, y + 1, z, ForgeDirection.DOWN)) {
            return null;
        }
        IInventory inventory = (IInventory) te;
        int color = Dyed.get(world, x, y, z);
        for (int[] d : HORIZONTAL) {
            int nx = x + d[0], nz = z + d[1];
            if (!this.isSameChest(world, nx, y, nz, color)) {
                continue;
            }
            if (world.isBlockSolidOnSide(nx, y + 1, nz, ForgeDirection.DOWN)) {
                return null;
            }
            TileEntity other = world.getBlockTileEntity(nx, y, nz);
            if (other instanceof TileEntityDyedChest) {
                // Same order as vanilla: the half at the lower coordinate is the top of the screen.
                if (d[0] < 0 || d[1] < 0) {
                    inventory = new InventoryLargeChest("container.chestDouble", (IInventory) other, inventory);
                } else {
                    inventory = new InventoryLargeChest("container.chestDouble", inventory, (IInventory) other);
                }
            }
        }
        return inventory;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
        float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }
        IInventory inventory = this.getInventory(world, x, y, z);
        if (inventory != null) {
            player.displayGUIChest(inventory);
        }
        return true;
    }

    /** Passes the "players using it" count from openChest and closeChest on to the tile entity. */
    @Override
    public void onBlockEventReceived(World world, int x, int y, int z, int id, int param) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te != null) {
            te.receiveClientEvent(id, param);
        }
    }
}
