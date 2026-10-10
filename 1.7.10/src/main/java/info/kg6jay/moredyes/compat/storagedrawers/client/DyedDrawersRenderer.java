package info.kg6jay.moredyes.compat.storagedrawers.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.client.renderer.DrawersRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.block.RenderIds;
import info.kg6jay.moredyes.compat.storagedrawers.DyedDrawersBlock;
import info.kg6jay.moredyes.compat.storagedrawers.DyedDrawersTile;
import info.kg6jay.moredyes.utility.ColorIndex;

/**
 * Draws dyed drawers in the world: Storage Drawers' own drawer renderer, with a box renderer that multiplies the drawer
 * body by the dye color (see TintedBoxRenderer). Its indicators, locks, tape and upgrade overlays are drawn untinted as
 * usual.
 */
@SideOnly(Side.CLIENT)
public class DyedDrawersRenderer extends DrawersRenderer {

    private final TintedBoxRenderer box = TintedBoxRenderer.install(this, DrawersRenderer.class);

    /** The dye color as the multipliers Storage Drawers' renderer takes. */
    static float[] color(int index) {
        int rgb = ColorIndex.rgb(index);
        return new float[] { (rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F };
    }

    @Override
    protected void renderBaseBlock(IBlockAccess world, TileEntityDrawers tile, int x, int y, int z, BlockDrawers block,
        RenderBlocks renderer) {
        if (this.box != null) {
            this.box.setTint(color(tile instanceof DyedDrawersTile dyed ? dyed.getColor() : 0));
        }
        super.renderBaseBlock(world, tile, x, y, z, block, renderer);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        return block instanceof DyedDrawersBlock && super.renderWorldBlock(world, x, y, z, block, modelId, renderer);
    }

    @Override
    public int getRenderId() {
        return RenderIds.dyedDrawers;
    }
}
