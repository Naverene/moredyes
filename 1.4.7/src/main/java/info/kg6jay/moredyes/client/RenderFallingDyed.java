package info.kg6jay.moredyes.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.entity.EntityFallingDyed;

/** Draws falling dyed sand and concrete powder in their color, like the vanilla falling sand renderer. */
public class RenderFallingDyed extends Render {

    /** How bright each side is, as in the vanilla renderer: bottom, top, then the four sides. */
    private static final float[] SHADE = { 0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F };

    private final RenderBlocks renderBlocks = new RenderBlocks();

    public RenderFallingDyed() {
        this.shadowSize = 0.5F;
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        EntityFallingDyed falling = (EntityFallingDyed) entity;
        Block block = falling.blockID > 0 && falling.blockID < Block.blocksList.length
            ? Block.blocksList[falling.blockID] : null;
        if (block == null) {
            return;
        }
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        this.loadTexture(block.getTextureFile());
        GL11.glDisable(GL11.GL_LIGHTING);
        int rgb = Colors.rgb(Colors.clamp(falling.color));
        float r = (rgb >> 16 & 255) / 255.0F, g = (rgb >> 8 & 255) / 255.0F, b = (rgb & 255) / 255.0F;
        this.renderBlocks.setRenderBoundsFromBlock(block);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setBrightness(block.getMixedBrightnessForBlock(falling.worldObj,
            MathHelper.floor_double(falling.posX), MathHelper.floor_double(falling.posY),
            MathHelper.floor_double(falling.posZ)));
        for (int side = 0; side < 6; side++) {
            float shade = SHADE[side];
            tessellator.setColorOpaque_F(r * shade, g * shade, b * shade);
            int texture = block.getBlockTextureFromSideAndMetadata(side, falling.metadata);
            switch (side) {
                case 0:
                    this.renderBlocks.renderBottomFace(block, -0.5D, -0.5D, -0.5D, texture);
                    break;
                case 1:
                    this.renderBlocks.renderTopFace(block, -0.5D, -0.5D, -0.5D, texture);
                    break;
                case 2:
                    this.renderBlocks.renderEastFace(block, -0.5D, -0.5D, -0.5D, texture);
                    break;
                case 3:
                    this.renderBlocks.renderWestFace(block, -0.5D, -0.5D, -0.5D, texture);
                    break;
                case 4:
                    this.renderBlocks.renderNorthFace(block, -0.5D, -0.5D, -0.5D, texture);
                    break;
                default:
                    this.renderBlocks.renderSouthFace(block, -0.5D, -0.5D, -0.5D, texture);
                    break;
            }
        }
        tessellator.draw();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
