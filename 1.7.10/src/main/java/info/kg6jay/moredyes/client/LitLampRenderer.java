package info.kg6jay.moredyes.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import info.kg6jay.moredyes.block.MDBlockLamp;
import info.kg6jay.moredyes.utility.ColorUtil;

/**
 * Draws a lit dyed lamp at full brightness, in its color brightened (ColorUtil.glow), and without the darker shading
 * vanilla gives the sides and bottom, so it
 * looks like it is giving off light rather than being lit from outside. This matters most with colored light (RPLE),
 * where a lamp would otherwise be lit only by its own colored glow.
 */
public class LitLampRenderer implements ISimpleBlockRenderingHandler {

    /** Full block light and sky light, packed as the tessellator takes it. */
    private static final int FULL_BRIGHTNESS = 15 << 20 | 15 << 4;

    /** How far the halo reaches past the block on each side. */
    private static final double HALO_SIZE = 0.9375 / 16;

    /** How opaque the halo is, out of 255. */
    private static final int HALO_ALPHA = 72;

    private final int renderId;

    public LitLampRenderer(int renderId) {
        this.renderId = renderId;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        if (MDBlockLamp.renderPass() == 1) {
            return this.renderHalo(world, x, y, z, block, renderer);
        }
        Tessellator tessellator = Tessellator.instance;
        renderer.setRenderBoundsFromBlock(block);
        renderer.enableAO = false;
        tessellator.setBrightness(FULL_BRIGHTNESS);
        tessellator.setColorOpaque_I(ColorUtil.glow(block.colorMultiplier(world, x, y, z)));
        boolean drawn = false;
        for (int side = 0; side < 6; ++side) {
            if (!renderer.renderAllFaces
                && !block.shouldSideBeRendered(world, x + offsetX(side), y + offsetY(side), z + offsetZ(side), side)) {
                continue;
            }
            face(renderer, block, side, x, y, z, renderer.getBlockIcon(block, world, x, y, z, side));
            drawn = true;
        }
        return drawn;
    }

    /**
     * A see-through box a little bigger than the block, in its glow color at full brightness, drawn in the
     * translucent pass so the lamp seems to shine past its edges, like Project Red's lamps. Sides against a solid block
     * are left out, as they can't be seen.
     */
    private boolean renderHalo(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer) {
        if (!(block instanceof MDBlockLamp lamp) || lamp.halo == null) {
            return false;
        }
        Tessellator tessellator = Tessellator.instance;
        renderer.enableAO = false;
        tessellator.setBrightness(FULL_BRIGHTNESS);
        int color = ColorUtil.glow(block.colorMultiplier(world, x, y, z));
        tessellator.setColorRGBA_I(color, HALO_ALPHA);
        boolean drawn = false;
        for (int side = 0; side < 6; ++side) {
            if (world.getBlock(x + offsetX(side), y + offsetY(side), z + offsetZ(side))
                .isOpaqueCube()) {
                continue;
            }
            haloFace(tessellator, side, x, y, z, lamp.halo);
            drawn = true;
        }
        return drawn;
    }

    /**
     * One side of the halo box. Drawn by hand rather than with RenderBlocks, which works the texture coordinates out
     * from the box and so, for a box bigger than the block, would reach past the halo texture into its neighbours.
     */
    private static void haloFace(Tessellator t, int side, int x, int y, int z, IIcon icon) {
        double lo = -HALO_SIZE, hi = 1 + HALO_SIZE;
        double x0 = x + lo, x1 = x + hi, y0 = y + lo, y1 = y + hi, z0 = z + lo, z1 = z + hi;
        double u0 = icon.getMinU(), u1 = icon.getMaxU(), v0 = icon.getMinV(), v1 = icon.getMaxV();
        switch (side) {
            case 0 -> {
                t.addVertexWithUV(x0, y0, z1, u0, v1);
                t.addVertexWithUV(x0, y0, z0, u0, v0);
                t.addVertexWithUV(x1, y0, z0, u1, v0);
                t.addVertexWithUV(x1, y0, z1, u1, v1);
            }
            case 1 -> {
                t.addVertexWithUV(x1, y1, z1, u1, v1);
                t.addVertexWithUV(x1, y1, z0, u1, v0);
                t.addVertexWithUV(x0, y1, z0, u0, v0);
                t.addVertexWithUV(x0, y1, z1, u0, v1);
            }
            case 2 -> {
                t.addVertexWithUV(x0, y1, z0, u1, v0);
                t.addVertexWithUV(x1, y1, z0, u0, v0);
                t.addVertexWithUV(x1, y0, z0, u0, v1);
                t.addVertexWithUV(x0, y0, z0, u1, v1);
            }
            case 3 -> {
                t.addVertexWithUV(x0, y1, z1, u0, v0);
                t.addVertexWithUV(x0, y0, z1, u0, v1);
                t.addVertexWithUV(x1, y0, z1, u1, v1);
                t.addVertexWithUV(x1, y1, z1, u1, v0);
            }
            case 4 -> {
                t.addVertexWithUV(x0, y1, z1, u1, v0);
                t.addVertexWithUV(x0, y1, z0, u0, v0);
                t.addVertexWithUV(x0, y0, z0, u0, v1);
                t.addVertexWithUV(x0, y0, z1, u1, v1);
            }
            default -> {
                t.addVertexWithUV(x1, y0, z1, u0, v1);
                t.addVertexWithUV(x1, y0, z0, u1, v1);
                t.addVertexWithUV(x1, y1, z0, u1, v0);
                t.addVertexWithUV(x1, y1, z1, u0, v0);
            }
        }
    }

    @Override
    public void renderInventoryBlock(Block block, int meta, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        int color = ColorUtil.glow(block.getRenderColor(meta));
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glColor4f((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, 1.0F);
        for (int side = 0; side < 6; ++side) {
            tessellator.startDrawingQuads();
            face(renderer, block, side, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, side, meta));
            tessellator.draw();
        }
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopAttrib();
    }

    private static void face(RenderBlocks renderer, Block block, int side, double x, double y, double z, IIcon icon) {
        switch (side) {
            case 0 -> renderer.renderFaceYNeg(block, x, y, z, icon);
            case 1 -> renderer.renderFaceYPos(block, x, y, z, icon);
            case 2 -> renderer.renderFaceZNeg(block, x, y, z, icon);
            case 3 -> renderer.renderFaceZPos(block, x, y, z, icon);
            case 4 -> renderer.renderFaceXNeg(block, x, y, z, icon);
            default -> renderer.renderFaceXPos(block, x, y, z, icon);
        }
    }

    private static int offsetX(int side) {
        return side == 4 ? -1 : side == 5 ? 1 : 0;
    }

    private static int offsetY(int side) {
        return side == 0 ? -1 : side == 1 ? 1 : 0;
    }

    private static int offsetZ(int side) {
        return side == 2 ? -1 : side == 3 ? 1 : 0;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return this.renderId;
    }
}
