package info.kg6jay.moredyes.block;

import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.block.ILayeredBlock.RenderLayer;
import info.kg6jay.moredyes.client.TintedTextures;
import info.kg6jay.moredyes.utility.BlockInfo;
import info.kg6jay.moredyes.utility.ColorUtil;

/**
 * Dyed bookshelf: the wood takes the dye color, the books keep their own. Drawn in two layers: layer 0 is the whole
 * tinted bookshelf, layer 1 the untinted books on its sides.
 */
public class MDBlockBookshelf extends MDBlockColoredMulti implements ILayeredBlock {

    @SideOnly(Side.CLIENT)
    protected IIcon booksIcon;

    public MDBlockBookshelf(String[] colors, BlockInfo info, String colorSet) {
        super(colors, info, colorSet);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.topIcon = TintedTextures.register(iconRegister, "plank");
        this.bottomIcon = this.topIcon;
        this.sideIcon = TintedTextures.register(iconRegister, "bookshelf");
        this.booksIcon = TintedTextures.register(iconRegister, "bookshelf/books");
    }

    /** On the layer being drawn, that layer's texture; elsewhere (particles, other mods) the tinted bookshelf. */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (RenderLayer.current >= 0) {
            IIcon icon = this.getLayerIcon(side, meta, RenderLayer.current);
            if (icon != null) {
                return icon;
            }
        }
        return super.getIcon(side, meta);
    }

    @Override
    public int getRenderType() {
        return RenderIds.layeredCube;
    }

    @Override
    public int getLayerCount() {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getLayerIcon(int side, int meta, int layer) {
        boolean end = side == 0 || side == 1;
        if (layer == 0) {
            return end ? this.topIcon : this.sideIcon;
        }
        return end ? null : this.booksIcon;
    }

    @Override
    public int getLayerColor(int meta, int layer) {
        return layer == 0 ? ColorUtil.shade(this.blockColors, meta) : ColorUtil.WHITE;
    }

    @Override
    public boolean isPlant() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return this.getLayerColor(world.getBlockMetadata(x, y, z), Math.max(RenderLayer.current, 0));
    }

    /** Skips the sides that are not part of the layer being drawn. */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        if (RenderLayer.current >= 0) {
            int meta = world.getBlockMetadata(
                x - Facing.offsetsXForSide[side],
                y - Facing.offsetsYForSide[side],
                z - Facing.offsetsZForSide[side]);
            if (this.getLayerIcon(side, meta, RenderLayer.current) == null) {
                return false;
            }
        }
        return super.shouldSideBeRendered(world, x, y, z, side);
    }

    /** Counts toward an enchanting table's power, like a vanilla bookshelf. */
    @Override
    public float getEnchantPowerBonus(World world, int x, int y, int z) {
        return 1.0F;
    }

    @Override
    public int quantityDropped(Random random) {
        return 3;
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return Items.book;
    }
}
