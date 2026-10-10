package info.kg6jay.moredyes.block;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.item.MDItems;

/**
 * Dyed leaves. They drop saplings and dyes of their color, and decay once no dyed or vanilla log is near.
 * <p>
 * Vanilla leaves look for a log every now and then. These only check when a nearby log is removed (logs call
 * beginLeavesDecay) and pass the check on to their neighbours when they decay, so a whole canopy falls apart once its
 * tree is gone, and leaves used for building never decay unless a log next to them is removed.
 */
public class BlockDyedLeaves extends BlockDyed implements IShearable {

    private static final int DECAY_RANGE = 4;
    private static final int DECAY_DELAY_MIN = 10;
    private static final int DECAY_DELAY_RANDOM = 100;
    private static final int[][] NEIGHBOURS = { { 1, 0, 0 }, { -1, 0, 0 }, { 0, 1, 0 }, { 0, -1, 0 }, { 0, 0, 1 },
        { 0, 0, -1 } };

    public BlockDyedLeaves(int id, BlockInfo info, Faces faces) {
        super(id, info, faces);
        this.setLightOpacity(1);
    }

    @Override
    public void beginLeavesDecay(World world, int x, int y, int z) {
        world.scheduleBlockUpdate(x, y, z, this.blockID, DECAY_DELAY_MIN + world.rand.nextInt(DECAY_DELAY_RANDOM));
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (world.isRemote || this.isSupported(world, x, y, z)) {
            return;
        }
        this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        world.setBlockWithNotify(x, y, z, 0);
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                for (int dz = -1; dz <= 1; ++dz) {
                    Block neighbour = Block.blocksList[world.getBlockId(x + dx, y + dy, z + dz)];
                    if (neighbour != null && neighbour.isLeaves(world, x + dx, y + dy, z + dz)) {
                        neighbour.beginLeavesDecay(world, x + dx, y + dy, z + dz);
                    }
                }
            }
        }
    }

    /** True if a log can be reached within DECAY_RANGE steps through leaves, the same rule vanilla leaves use. */
    private boolean isSupported(World world, int x, int y, int z) {
        int r = DECAY_RANGE;
        if (!world.checkChunksExist(x - r - 1, y - r - 1, z - r - 1, x + r + 1, y + r + 1, z + r + 1)) {
            return true;
        }
        int size = 2 * r + 1;
        boolean[] visited = new boolean[size * size * size];
        ArrayDeque<int[]> queue = new ArrayDeque<int[]>();
        queue.add(new int[] { x, y, z, 0 });
        visited[(r * size + r) * size + r] = true;
        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            for (int[] d : NEIGHBOURS) {
                int nx = pos[0] + d[0], ny = pos[1] + d[1], nz = pos[2] + d[2];
                int ox = nx - x + r, oy = ny - y + r, oz = nz - z + r;
                if (ox < 0 || oy < 0 || oz < 0 || ox >= size || oy >= size || oz >= size) {
                    continue;
                }
                int index = (ox * size + oy) * size + oz;
                if (visited[index]) {
                    continue;
                }
                visited[index] = true;
                Block block = Block.blocksList[world.getBlockId(nx, ny, nz)];
                if (block == null) {
                    continue;
                }
                if (block.canSustainLeaves(world, nx, ny, nz)) {
                    return true;
                }
                if (pos[3] + 1 < r && block.isLeaves(world, nx, ny, nz)) {
                    queue.add(new int[] { nx, ny, nz, pos[3] + 1 });
                }
            }
        }
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        if (world.canLightningStrikeAt(x, y + 1, z) && !world.doesBlockHaveSolidTopSurface(x, y - 1, z)
            && rand.nextInt(15) == 1) {
            world.spawnParticle("dripWater", x + rand.nextFloat(), y - 0.05D, z + rand.nextFloat(), 0.0D, 0.0D,
                0.0D);
        }
    }

    /** A sapling about one time in ten and a dye one time in four, both of the leaves' color. */
    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        int color = Dyed.get(world, x, y, z);
        int chance = 20;
        if (fortune > 0) {
            chance = Math.max(10, chance - (2 << fortune));
        }
        if (world.rand.nextInt(chance) <= 1) {
            drops.add(new ItemStack(MDBlocks.sapling, 1, color));
        }
        chance = 200;
        if (fortune > 0) {
            chance = Math.max(40, chance - (10 << fortune));
        }
        if (world.rand.nextInt(chance) <= 50) {
            drops.add(new ItemStack(MDItems.dye, 1, color));
        }
        return drops;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isShearable(ItemStack item, World world, int x, int y, int z) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, World world, int x, int y, int z, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(Dyed.stack(this, world, x, y, z, 1));
        return drops;
    }

    @Override
    public boolean isLeaves(World world, int x, int y, int z) {
        return true;
    }
}
