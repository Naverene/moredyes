package info.kg6jay.moredyes.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.MoreDyes;

/**
 * The code every dyed block shares: they extend different vanilla blocks (stairs, leaves, sand...), so each calls
 * these from its own overrides. A dyed block keeps its color in its tile entity (see IDyedTile), and its item's
 * damage is the color.
 */
public final class Dyed {

    /**
     * The color of the dyed block that was last removed, kept for its drops: when a player breaks a block, vanilla
     * removes it (and its tile entity) before working out what it drops.
     */
    private static World removedWorld;
    private static int removedX, removedY, removedZ, removedColor;

    private Dyed() {}

    /** The color of the dyed block at this position, or 0 if there is none. */
    public static int get(IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof IDyedTile) {
            return Colors.clamp(((IDyedTile) te).getColor());
        }
        if (world == removedWorld && x == removedX && y == removedY && z == removedZ) {
            return removedColor;
        }
        return 0;
    }

    /** Colors the dyed block at this position and tells the players who can see it. */
    public static void set(World world, int x, int y, int z, int color) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof IDyedTile) {
            ((IDyedTile) te).setColor(color);
            world.markBlockForUpdate(x, y, z);
        }
    }

    /** Places a dyed block of this color, as world generation and growing saplings do. */
    public static void place(World world, int x, int y, int z, Block block, int meta, int color, boolean notify) {
        if (notify) {
            world.setBlockAndMetadataWithNotify(x, y, z, block.blockID, meta);
        } else {
            world.setBlockAndMetadata(x, y, z, block.blockID, meta);
        }
        set(world, x, y, z, color);
    }

    /** Call from breakBlock, before the tile entity goes: keeps the color for the drops. */
    public static void remember(World world, int x, int y, int z) {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if (te instanceof IDyedTile) {
            removedWorld = world;
            removedX = x;
            removedY = y;
            removedZ = z;
            removedColor = Colors.clamp(((IDyedTile) te).getColor());
        }
    }

    public static ItemStack stack(Block block, IBlockAccess world, int x, int y, int z, int count) {
        return new ItemStack(block, count, get(world, x, y, z));
    }

    public static ArrayList<ItemStack> drops(Block block, World world, int x, int y, int z, int count) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        if (count > 0) {
            drops.add(stack(block, world, x, y, z, count));
        }
        return drops;
    }

    /** The tint of the block at this position. */
    public static int rgb(IBlockAccess world, int x, int y, int z) {
        return Colors.rgb(get(world, x, y, z));
    }

    /** One item per color, for the creative menu. */
    @SuppressWarnings("unchecked")
    public static void addSubBlocks(int id, List list) {
        for (int i = 0; i < Colors.COUNT; i++) {
            list.add(new ItemStack(id, 1, i));
        }
    }

    /** Mobs only spawn on dyed blocks when the config allows it. */
    public static boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z) {
        return !MoreDyes.config.preventMobSpawning;
    }
}
