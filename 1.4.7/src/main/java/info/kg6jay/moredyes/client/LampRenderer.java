package info.kg6jay.moredyes.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import info.kg6jay.moredyes.block.BlockDyedLamp;

/**
 * Draws a dyed lamp. Unlit it is an ordinary cube. Lit it is drawn at full brightness, in its color brightened
 * (glow), and without the darker shading
 * vanilla gives the sides and bottom, so it looks like it is giving off light rather than being lit from outside.
 */
public class LampRenderer implements ISimpleBlockRenderingHandler {

    /** Full block light and sky light, packed as the tessellator takes it. */
    private static final int FULL_BRIGHTNESS = 15 << 20 | 15 << 4;

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        if (!(block instanceof BlockDyedLamp) || !BlockDyedLamp.isLit(world.getBlockMetadata(x, y, z))) {
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
