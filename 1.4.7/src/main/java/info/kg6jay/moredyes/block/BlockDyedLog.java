package info.kg6jay.moredyes.block;

import net.minecraft.block.Block;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.Textures;

/**
 * Dyed log: the rings on the top and bottom take the dye color, the bark keeps its natural color. Drawn in two
 * layers: layer 0 is the tinted top and bottom, layer 1 the untinted bark.
 */
public class BlockDyedLog extends BlockDyed implements ILayeredBlock {

    public static int renderId;

    public BlockDyedLog(int id, BlockInfo info) {
        super(id, info, Faces.of(Textures.LOG_TOP, Textures.LOG_BARK, Textures.LOG_TOP));
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int id, int meta) {
        int range = 4;
        int loaded = range + 1;
        if (world.checkChunksExist(x - loaded, y - loaded, z - loaded, x + loaded, y + loaded, z + loaded)) {
            for (int dx = -range; dx <= range; ++dx) {
                for (int dy = -range; dy <= range; ++dy) {
                    for (int dz = -range; dz <= range; ++dz) {
                        Block block = Block.blocksList[world.getBlockId(x + dx, y + dy, z + dz)];
                        if (block != null && block.isLeaves(world, x + dx, y + dy, z + dz)) {
                            block.beginLeavesDecay(world, x + dx, y + dy, z + dz);
                        }
                    }
                }
            }
        }
        super.breakBlock(world, x, y, z, id, meta);
    }

    @Override
    public boolean canSustainLeaves(World world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean isWood(World world, int x, int y, int z) {
        return true;
    }

    @Override
    public int getRenderType() {
        return renderId;
    }

    @Override
    public int getLayerCount() {
        return 2;
    }

    @Override
    public int getLayerTexture(int side, int layer) {
        boolean end = side == 0 || side == 1;
        if (layer == 0) {
            return end ? Textures.LOG_TOP : -1;
        }
        return end ? -1 : Textures.LOG_BARK;
    }

    @Override
    public int getLayerColor(int color, int layer) {
        return layer == 0 ? Colors.rgb(color) : 0xFFFFFF;
    }

    @Override
    public boolean isPlant() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int color) {
        return this.getLayerColor(color, RenderLayer.layer());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return this.getLayerColor(Dyed.get(world, x, y, z), RenderLayer.layer());
    }

    /** Skips the sides that are not part of the layer being drawn. */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        if (RenderLayer.current >= 0 && this.getLayerTexture(side, RenderLayer.current) < 0) {
            return false;
        }
        return super.shouldSideBeRendered(world, x, y, z, side);
    }
}
