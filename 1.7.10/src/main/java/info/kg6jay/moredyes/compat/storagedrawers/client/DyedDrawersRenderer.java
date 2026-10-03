package info.kg6jay.moredyes.compat.storagedrawers.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.jaquadro.minecraft.storagedrawers.StorageDrawers;
import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.client.renderer.DrawersRenderer;
import com.jaquadro.minecraft.storagedrawers.client.renderer.ModularBoxRenderer;
import com.jaquadro.minecraft.storagedrawers.util.RenderHelper;
import com.jaquadro.minecraft.storagedrawers.util.RenderHelperState;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.block.RenderIds;
import info.kg6jay.moredyes.compat.storagedrawers.DyedDrawersBlock;
import info.kg6jay.moredyes.compat.storagedrawers.DyedDrawersTile;
import info.kg6jay.moredyes.utility.ColorIndex;

/**
 * Draws dyed drawers in the world: Storage Drawers' drawer renderer, with the drawer body multiplied by the dye color.
 * Storage Drawers draws the body in plain white whatever the block's color, so this redoes that part (the same as its
 * renderBaseBlock) with the color. Its indicators, locks, tape and upgrade overlays are drawn untinted as usual.
 */
@SideOnly(Side.CLIENT)
public class DyedDrawersRenderer extends DrawersRenderer {

    private final ModularBoxRenderer boxRenderer = new ModularBoxRenderer();

    /** The dye color as the multipliers Storage Drawers' renderer takes. */
    static float[] color(int index) {
        int rgb = ColorIndex.rgb(index);
        return new float[] { (rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F };
    }

    @Override
    protected void renderBaseBlock(IBlockAccess world, TileEntityDrawers tile, int x, int y, int z, BlockDrawers block,
        RenderBlocks renderer) {
        int side = tile.getDirection();
        int meta = world.getBlockMetadata(x, y, z);
        float[] color = color(tile instanceof DyedDrawersTile dyed ? dyed.getColor() : 0);

        RenderHelper.instance.state.setUVRotation(1, RenderHelperState.ROTATION_BY_FACE_FACE[2][side]);
        this.boxRenderer.setUnit(block.getTrimWidth());
        this.boxRenderer.setColor(color);
        for (int i = 0; i < 6; ++i) {
            this.boxRenderer.setExteriorIcon(block.getIcon(world, x, y, z, i), i);
        }
        this.boxRenderer.setCutIcon(block.getIconTrim(meta));
        this.boxRenderer.setInteriorIcon(block.getIconTrim(meta));
        this.renderExterior(block, x, y, z, side, renderer);
        RenderHelper.instance.state.clearUVRotation(1);

        int maxStorageLevel = tile.getMaxStorageLevel();
        if (maxStorageLevel > 1 && StorageDrawers.config.cache.renderStorageUpgrades && !tile.shouldHideUpgrades()) {
            this.boxRenderer.setColor(ModularBoxRenderer.COLOR_WHITE);
            for (int i = 0; i < 6; ++i) {
                this.boxRenderer.setExteriorIcon(block.getOverlayIcon(world, x, y, z, i, maxStorageLevel), i);
            }
            this.boxRenderer.setCutIcon(block.getOverlayIconTrim(maxStorageLevel));
            this.boxRenderer.setInteriorIcon(block.getOverlayIconTrim(maxStorageLevel));
            this.renderExterior(block, x, y, z, side, renderer);
            this.boxRenderer.setColor(color);
        }

        this.boxRenderer.setUnit(0.0);
        this.boxRenderer.setInteriorIcon(block.getIcon(world, x, y, z, side), ForgeDirection.OPPOSITES[side]);
        this.renderInterior(block, x, y, z, side, renderer);
    }

    /** The same as Storage Drawers' (private) renderExterior. */
    private void renderExterior(BlockDrawers block, int x, int y, int z, int side, RenderBlocks renderer) {
        double depth = block.halfDepth ? 0.5 : 1.0;
        double xMin = 0.0, xMax = 1.0, zMin = 0.0, zMax = 1.0;
        switch (side) {
            case 2 -> zMin = 1.0 - depth;
            case 3 -> zMax = depth;
            case 4 -> xMin = 1.0 - depth;
            case 5 -> xMax = depth;
            default -> {}
        }
        this.boxRenderer.renderExterior(
            renderer.blockAccess,
            block,
            x,
            y,
            z,
            xMin,
            0.0,
            zMin,
            xMax,
            1.0,
            zMax,
            0,
            ModularBoxRenderer.sideCut[side]);
    }

    /** The same as Storage Drawers' (private) renderInterior. */
    private void renderInterior(BlockDrawers block, int x, int y, int z, int side, RenderBlocks renderer) {
        double unit = block.getTrimDepth();
        double depth = block.halfDepth ? 0.5 : 1.0;
        double xMin = 0.0, xMax = 0.0, zMin = 0.0, zMax = 0.0;
        switch (side) {
            case 2 -> {
                xMin = unit;
                xMax = 1.0 - unit;
                zMin = 1.0 - depth;
                zMax = 1.0 - depth + unit;
            }
            case 3 -> {
                xMin = unit;
                xMax = 1.0 - unit;
                zMin = depth - unit;
                zMax = depth;
            }
            case 4 -> {
                xMin = 1.0 - depth;
                xMax = 1.0 - depth + unit;
                zMin = unit;
                zMax = 1.0 - unit;
            }
            case 5 -> {
                xMin = depth - unit;
                xMax = depth;
                zMin = unit;
                zMax = 1.0 - unit;
            }
            default -> {}
        }
        this.boxRenderer.renderInterior(
            renderer.blockAccess,
            block,
            x,
            y,
            z,
            xMin,
            unit,
            zMin,
            xMax,
            1.0 - unit,
            zMax,
            0,
            ModularBoxRenderer.sideCut[side]);
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
