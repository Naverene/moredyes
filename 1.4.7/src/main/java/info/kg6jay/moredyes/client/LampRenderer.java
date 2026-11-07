package info.kg6jay.moredyes.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import info.kg6jay.moredyes.Textures;
import info.kg6jay.moredyes.block.BlockDyedLamp;

/**
 * Draws a dyed lamp. Unlit it is an ordinary cube. Lit it is drawn at full brightness, in its color brightened
 * (glow), and without the darker shading
 * vanilla gives the sides and bottom, so it looks like it is giving off light rather than being lit from outside.
 */
public class LampRenderer implements ISimpleBlockRenderingHandler {

    /** Full block light and sky light, packed as the tessellator takes it. */
    private static final int FULL_BRIGHTNESS = 15 << 20 | 15 << 4;

    /** How far the halo around a lit lamp reaches past the block on each side. */
    private static final double HALO_SIZE = 0.9375 / 16;

    /** How opaque the halo is, out of 255. */
    private static final int HALO_ALPHA = 72;

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        boolean lit = block instanceof BlockDyedLamp && BlockDyedLamp.isLit(world.getBlockMetadata(x, y, z));
        if (BlockDyedLamp.renderPass() == 1) {
            return lit && this.renderHalo(world, x, y, z, block, renderer);
        }
        if (!lit) {
            return renderer.renderStandardBlock(block, x, y, z);
        }
        Tessellator tessellator = Tessellator.instance;
        renderer.setRenderBoundsFromBlock(block);
        renderer.enableAO = false;
        tessellator.setBrightness(FULL_BRIGHTNESS);
        tessellator.setColorOpaque_I(glow(block.colorMultiplier(world, x, y, z)));
        boolean drawn = false;
        for (int side = 0; side < 6; ++side) {
            if (!renderer.renderAllFaces && !block.shouldSideBeRendered(world, x + offsetX(side), y + offsetY(side),
                z + offsetZ(side), side)) {
                continue;
            }
            face(renderer, block, side, x, y, z, block.getBlockTexture(world, x, y, z, side));
            drawn = true;
        }
        return drawn;
    }

    /**
     * A see-through box a little bigger than the block, in its glow color at full brightness, drawn in the second
     * (translucent) pass so the lamp seems to shine past its edges, like Project Red's lamps. Sides against a solid
     * block are left out, as they can't be seen.
     */
    private boolean renderHalo(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        renderer.enableAO = false;
        tessellator.setBrightness(FULL_BRIGHTNESS);
        tessellator.setColorRGBA_I(glow(block.colorMultiplier(world, x, y, z)), HALO_ALPHA);
        boolean drawn = false;
        for (int side = 0; side < 6; ++side) {
            Block neighbor = Block.blocksList[world.getBlockId(x + offsetX(side), y + offsetY(side), z + offsetZ(side))];
            if (neighbor != null && neighbor.isOpaqueCube()) {
                continue;
            }
            haloFace(tessellator, side, x, y, z, Textures.LAMP_HALO);
            drawn = true;
        }
        return drawn;
    }

    /**
     * One side of the halo box. Drawn by hand rather than with RenderBlocks, which works the texture coordinates out
     * from the box and so, for a box bigger than the block, would reach past the halo tile into its neighbours.
     */
    private static void haloFace(Tessellator t, int side, int x, int y, int z, int texture) {
        double lo = -HALO_SIZE, hi = 1 + HALO_SIZE;
        double x0 = x + lo, x1 = x + hi, y0 = y + lo, y1 = y + hi, z0 = z + lo, z1 = z + hi;
        double u0 = (texture % 16 * 16 + 0.5) / 256.0, u1 = (texture % 16 * 16 + 15.5) / 256.0;
        double v0 = (texture / 16 * 16 + 0.5) / 256.0, v1 = (texture / 16 * 16 + 15.5) / 256.0;
        switch (side) {
            case 0:
                t.addVertexWithUV(x0, y0, z1, u0, v1);
                t.addVertexWithUV(x0, y0, z0, u0, v0);
                t.addVertexWithUV(x1, y0, z0, u1, v0);
                t.addVertexWithUV(x1, y0, z1, u1, v1);
                break;
            case 1:
                t.addVertexWithUV(x1, y1, z1, u1, v1);
                t.addVertexWithUV(x1, y1, z0, u1, v0);
                t.addVertexWithUV(x0, y1, z0, u0, v0);
                t.addVertexWithUV(x0, y1, z1, u0, v1);
                break;
            case 2:
                t.addVertexWithUV(x0, y1, z0, u1, v0);
                t.addVertexWithUV(x1, y1, z0, u0, v0);
                t.addVertexWithUV(x1, y0, z0, u0, v1);
                t.addVertexWithUV(x0, y0, z0, u1, v1);
                break;
            case 3:
                t.addVertexWithUV(x0, y1, z1, u0, v0);
                t.addVertexWithUV(x0, y0, z1, u0, v1);
                t.addVertexWithUV(x1, y0, z1, u1, v1);
                t.addVertexWithUV(x1, y1, z1, u1, v0);
                break;
            case 4:
                t.addVertexWithUV(x0, y1, z1, u1, v0);
                t.addVertexWithUV(x0, y1, z0, u0, v0);
                t.addVertexWithUV(x0, y0, z0, u0, v1);
                t.addVertexWithUV(x0, y0, z1, u1, v1);
                break;
            default:
                t.addVertexWithUV(x1, y0, z1, u0, v1);
                t.addVertexWithUV(x1, y0, z0, u1, v1);
                t.addVertexWithUV(x1, y1, z0, u1, v0);
                t.addVertexWithUV(x1, y1, z1, u0, v0);
                break;
        }
    }

    /** The item's damage is its color. An inverted lamp's item shows it lit, so it is drawn unlit by the GUI light. */
    @Override
    public void renderInventoryBlock(Block block, int color, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        boolean lit = block instanceof BlockDyedLamp && ((BlockDyedLamp) block).isInverted();
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        if (lit) {
            GL11.glDisable(GL11.GL_LIGHTING);
        }
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        int rgb = lit ? glow(block.getRenderColor(color)) : block.getRenderColor(color);
        GL11.glColor4f((rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F, 1.0F);
        for (int side = 0; side < 6; ++side) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(offsetX(side), offsetY(side), offsetZ(side));
            face(renderer, block, side, 0, 0, 0, block.getBlockTextureFromSideAndMetadata(side, color));
            tessellator.draw();
        }
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopAttrib();
    }

    /**
     * The color a lit lamp is drawn in: the dye color brightened, keeping its hue, until its brightest channel is full
     * or it is 60% brighter. Dark colors stay darker than light ones, so lamps of different shades still differ.
     */
    static int glow(int rgb) {
        int r = rgb >> 16 & 0xFF, g = rgb >> 8 & 0xFF, b = rgb & 0xFF;
        int max = Math.max(1, Math.max(r, Math.max(g, b)));
        int percent = Math.min(160, 255 * 100 / max);
        return r * percent / 100 << 16 | g * percent / 100 << 8 | b * percent / 100;
    }

    private static void face(RenderBlocks renderer, Block block, int side, double x, double y, double z,
        int texture) {
        switch (side) {
            case 0:
                renderer.renderBottomFace(block, x, y, z, texture);
                break;
            case 1:
                renderer.renderTopFace(block, x, y, z, texture);
                break;
            case 2:
                renderer.renderEastFace(block, x, y, z, texture);
                break;
            case 3:
                renderer.renderWestFace(block, x, y, z, texture);
                break;
            case 4:
                renderer.renderNorthFace(block, x, y, z, texture);
                break;
            default:
                renderer.renderSouthFace(block, x, y, z, texture);
                break;
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
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return BlockDyedLamp.renderId;
    }
}
