package info.kg6jay.moredyes.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.item.MDItem;
import info.kg6jay.moredyes.utility.BlockInfo;

public class MDBlockLeaf extends MDBlockColored implements IShearable {

    int blockIndex;
    int[] field_150128_a;

    public MDBlockLeaf(String[] colors, BlockInfo info, String colorSet, int blockIndex) {
        super(colors, info, colorSet);
        this.setTickRandomly(true);
        this.setLightOpacity(1);
        this.blockIndex = blockIndex;
    }

    public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_) {
        byte b0 = 1;
        int i1 = b0 + 1;

        if (world.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1)) {
            for (int j1 = -b0; j1 <= b0; ++j1) {
                for (int k1 = -b0; k1 <= b0; ++k1) {
                    for (int l1 = -b0; l1 <= b0; ++l1) {
                        Block b = world.getBlock(x + j1, y + k1, z + l1);
                        if (b.isLeaves(world, x + j1, y + k1, z + l1)) {
                            b.beginLeavesDecay(world, x + j1, y + k1, z + l1);
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates. Args: blockAccess, x, y, z, side
     */
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess blockAccess, int x, int y, int z, int side) {
        return super.shouldSideBeRendered(blockAccess, x, y, z, side);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (!world.isRemote) {
            int meta = world.getBlockMetadata(x, y, z);

            if ((meta & 8) != 0 && (meta & 4) == 0) {
                int range = 4;
                int i1 = range + 1;
                byte b1 = 32;
                int j1 = b1 * b1;
                int k1 = b1 / 2;

                if (this.field_150128_a == null) {
                    this.field_150128_a = new int[b1 * b1 * b1];
                }

                int xOffset;

                if (world.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1)) {
                    int i2;
                    int j2;

                    for (xOffset = -range; xOffset <= range; ++xOffset) {
                        for (i2 = -range; i2 <= range; ++i2) {
                            for (j2 = -range; j2 <= range; ++j2) {
                                Block block = world.getBlock(x + xOffset, y + i2, z + j2);

                                if (!block.canSustainLeaves(world, x + xOffset, y + i2, z + j2)) {
                                    if (block.isLeaves(world, x + xOffset, y + i2, z + j2)) {
                                        this.field_150128_a[(xOffset + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -2;
                                    } else {
                                        this.field_150128_a[(xOffset + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -1;
                                    }
                                } else {
                                    this.field_150128_a[(xOffset + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = 0;
                                }
                            }
                        }
                    }

                    for (xOffset = 1; xOffset <= 4; ++xOffset) {
                        for (i2 = -range; i2 <= range; ++i2) {
                            for (j2 = -range; j2 <= range; ++j2) {
                                for (int k2 = -range; k2 <= range; ++k2) {
                                    if (this.field_150128_a[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1] == xOffset - 1) {
                                        if (this.field_150128_a[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2) {
                                            this.field_150128_a[(i2 + k1 - 1) * j1 + (j2 + k1) * b1
                                                + k2
                                                + k1] = xOffset;
                                        }

                                        if (this.field_150128_a[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2) {
                                            this.field_150128_a[(i2 + k1 + 1) * j1 + (j2 + k1) * b1
                                                + k2
                                                + k1] = xOffset;
                                        }

                                        if (this.field_150128_a[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] == -2) {
                                            this.field_150128_a[(i2 + k1) * j1 + (j2 + k1 - 1) * b1
                                                + k2
                                                + k1] = xOffset;
                                        }

                                        if (this.field_150128_a[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] == -2) {
                                            this.field_150128_a[(i2 + k1) * j1 + (j2 + k1 + 1) * b1
                                                + k2
                                                + k1] = xOffset;
                                        }

                                        if (this.field_150128_a[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)]
                                            == -2) {
                                            this.field_150128_a[(i2 + k1) * j1 + (j2 + k1) * b1
                                                + (k2 + k1 - 1)] = xOffset;
                                        }

                                        if (this.field_150128_a[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] == -2) {
                                            this.field_150128_a[(i2 + k1) * j1 + (j2 + k1) * b1
                                                + k2
                                                + k1
                                                + 1] = xOffset;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                xOffset = this.field_150128_a[k1 * j1 + k1 * b1 + k1];

                if (xOffset >= 0) {
                    world.setBlockMetadataWithNotify(x, y, z, meta & -9, 4);
                } else {
                    this.removeLeaves(world, x, y, z);
                }
            }
        }
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        if (world.canLightningStrikeAt(x, y + 1, z) && !World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)
            && rand.nextInt(15) == 1) {
            double d0 = (double) ((float) x + rand.nextFloat());
            double d1 = (double) y - 0.05D;
            double d2 = (double) ((float) z + rand.nextFloat());
            world.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    private void removeLeaves(World world, int x, int y, int z) {
        this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        world.setBlockToAir(x, y, z);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random rand) {
        return rand.nextInt(10) == 0 ? 1 : 0;
    }

    public Item getItemDropped(int meta, Random rand, int fortune) {

        return Item.getItemFromBlock(MDBlock.sapling[this.blockIndex]);
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int meta, float p_149690_6_, int chance) {
        super.dropBlockAsItemWithChance(world, x, y, z, meta, 1.0f, chance);
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
        super.harvestBlock(world, player, x, y, z, meta);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube? This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube() {
        return false;
    }

    /**
     * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
    protected ItemStack createStackedBlock(int meta) {
        return new ItemStack(Item.getItemFromBlock(this), 1, meta);
    }

    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z)));
        return ret;
    }

    @Override
    public void beginLeavesDecay(World world, int x, int y, int z) {
        world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z), 4);
    }

    @Override
    public boolean isLeaves(IBlockAccess world, int x, int y, int z) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        int chance = 20;

        if (fortune > 0) {
            chance -= 2 << fortune;
            if (chance < 10) chance = 10;
        }

        if (world.rand.nextInt(chance) <= 1)
            ret.add(new ItemStack(this.getItemDropped(metadata, world.rand, fortune), 1, this.damageDropped(metadata)));

        chance = 200;
        if (fortune > 0) {
            chance -= 10 << fortune;
            if (chance < 40) chance = 40;
        }
        if (world.rand.nextInt(chance) <= 50) {
            ret.add(new ItemStack(MDItem.dye[this.blockIndex], 1, metadata));
        }
        this.captureDrops(true);
        ret.addAll(this.captureDrops(false));
        return ret;
    }
}
