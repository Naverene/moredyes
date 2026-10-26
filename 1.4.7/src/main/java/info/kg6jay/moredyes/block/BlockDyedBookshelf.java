package info.kg6jay.moredyes.block;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.Textures;

/**
 * Dyed bookshelf: drops three books, like a vanilla bookshelf. The wood takes the dye color and the books keep their
 * own, so it is drawn in two layers: layer 0 is the whole tinted bookshelf, layer 1 the untinted books on its sides.
 */
public class BlockDyedBookshelf extends BlockDyed implements ILayeredBlock {

    public BlockDyedBookshelf(int id, BlockInfo info, Faces faces) {
        super(id, info, faces);
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(Item.book, 3));
        return drops;
    }

    /** Drawn by the same layered cube renderer as the dyed log. */
    @Override
    public int getRenderType() {
        return BlockDyedLog.renderId;
    }

    @Override
    public int getLayerCount() {
        return 2;
    }

    @Override
    public int getLayerTexture(int side, int layer) {
        boolean end = side == 0 || side == 1;
        if (layer == 0) {
            return end ? Textures.PLANK : Textures.BOOKSHELF;
        }
        return end ? -1 : Textures.BOOKSHELF_BOOKS;
    }

    @Override
    public int getLayerColor(int color, int layer) {
        return layer == 0 ? Colors.rgb(color) : 0xFFFFFF;
    }

    @Override
    public boolean isPlant() {
        return false;
    }

    /** On the layer being drawn, that layer's texture; elsewhere (particles, other mods) the tinted bookshelf. */
    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta) {
        if (RenderLayer.current >= 0) {
            int texture = this.getLayerTexture(side, RenderLayer.current);
            if (texture >= 0) {
                return texture;
            }
        }
        return super.getBlockTextureFromSideAndMetadata(side, meta);
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
