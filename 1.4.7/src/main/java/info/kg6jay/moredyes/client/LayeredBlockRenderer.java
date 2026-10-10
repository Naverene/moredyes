package info.kg6jay.moredyes.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import info.kg6jay.moredyes.block.ILayeredBlock;
import info.kg6jay.moredyes.block.ILayeredBlock.RenderLayer;

/**
 * Draws an ILayeredBlock once per layer with the vanilla cube or crossed-squares renderer. The block reads
 * RenderLayer.current to return that layer's texture and tint.
 */
public class LayeredBlockRenderer implements ISimpleBlockRenderingHandler {

    private final int renderId;
    private final boolean plant;

    public LayeredBlockRenderer(int renderId, boolean plant) {
        this.renderId = renderId;
        this.plant = plant;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        if (!(block instanceof ILayeredBlock)) {
            return false;
        }
        ILayeredBlock layered = (ILayeredBlock) block;
        boolean drawn = false;
        try {
            for (int layer = 0; layer < layered.getLayerCount(); ++layer) {
                RenderLayer.current = layer;
                if (this.plant) {
                    drawn |= renderer.renderCrossedSquares(block, x, y, z);
                } else {
                    drawn |= renderer.renderStandardBlock(block, x, y, z);
                }
            }
        } finally {
            RenderLayer.current = -1;
        }
        return drawn;
    }

    /** Only used for cubes; plants are drawn as flat items by ItemBlockDyed. */
    @Override
    public void renderInventoryBlock(Block block, int color, int modelId, RenderBlocks renderer) {
        if (!(block instanceof ILayeredBlock)) {
            return;
        }
        ILayeredBlock layered = (ILayeredBlock) block;
        Tessellator tessellator = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        for (int layer = 0; layer < layered.getLayerCount(); ++layer) {
            int rgb = layered.getLayerColor(color, layer);
            GL11.glColor4f((rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F, 1.0F);
            for (int side = 0; side < 6; ++side) {
                int texture = layered.getLayerTexture(side, layer);
                if (texture < 0) {
                    continue;
                }
                tessellator.startDrawingQuads();
                switch (side) {
                    case 0:
                        tessellator.setNormal(0.0F, -1.0F, 0.0F);
                        renderer.renderBottomFace(block, 0.0D, 0.0D, 0.0D, texture);
                        break;
                    case 1:
                        tessellator.setNormal(0.0F, 1.0F, 0.0F);
                        renderer.renderTopFace(block, 0.0D, 0.0D, 0.0D, texture);
                        break;
                    case 2:
                        tessellator.setNormal(0.0F, 0.0F, -1.0F);
                        renderer.renderEastFace(block, 0.0D, 0.0D, 0.0D, texture);
                        break;
                    case 3:
                        tessellator.setNormal(0.0F, 0.0F, 1.0F);
                        renderer.renderWestFace(block, 0.0D, 0.0D, 0.0D, texture);
                        break;
                    case 4:
                        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
                        renderer.renderNorthFace(block, 0.0D, 0.0D, 0.0D, texture);
                        break;
                    default:
                        tessellator.setNormal(1.0F, 0.0F, 0.0F);
                        renderer.renderSouthFace(block, 0.0D, 0.0D, 0.0D, texture);
                        break;
                }
                tessellator.draw();
            }
        }
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return !this.plant;
    }

    @Override
    public int getRenderId() {
        return this.renderId;
    }
}
