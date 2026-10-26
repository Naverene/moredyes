package info.kg6jay.moredyes.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import info.kg6jay.moredyes.block.ILayeredBlock.RenderLayer;
import info.kg6jay.moredyes.block.MDBlockDyedBed;

/**
 * Draws a dyed bed (MDBlockDyedBed) like the vanilla bed renderer, once per layer: the sheet in the bed's color, then
 * the pillow and the wooden frame over it. The vanilla renderer sets its own shading colors, so it cannot be tinted and
 * is
 * copied here with the tint multiplied in.
 */
public class DyedBedRenderer implements ISimpleBlockRenderingHandler {

    private final int renderId;

    public DyedBedRenderer(int renderId) {
        this.renderId = renderId;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        try {
            for (int layer = 0; layer < MDBlockDyedBed.LAYERS; layer++) {
                RenderLayer.current = layer;
                renderLayer(world, x, y, z, block, renderer);
            }
        } finally {
            RenderLayer.current = -1;
        }
        return true;
    }

    private static void renderLayer(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        int color = block.colorMultiplier(world, x, y, z);
        float r = (color >> 16 & 255) / 255.0F, g = (color >> 8 & 255) / 255.0F, b = (color & 255) / 255.0F;
        if (EntityRenderer.anaglyphEnable) {
            float grey = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
            float ag = (r * 30.0F + g * 70.0F) / 100.0F;
            float ab = (r * 30.0F + b * 70.0F) / 100.0F;
            r = grey;
            g = ag;
            b = ab;
        }
        int direction = block.getBedDirection(world, x, y, z);
        boolean foot = block.isBedFoot(world, x, y, z);
        int brightness = block.getMixedBrightnessForBlock(world, x, y, z);

        // Bottom, raised to the top of the legs
        IIcon icon = icon(renderer, block, world, x, y, z, 0);
        if (icon != null) {
            tessellator.setBrightness(brightness);
            tessellator.setColorOpaque_F(0.5F * r, 0.5F * g, 0.5F * b);
            double u0 = icon.getMinU(), u1 = icon.getMaxU(), v0 = icon.getMinV(), v1 = icon.getMaxV();
            double x0 = x + renderer.renderMinX, x1 = x + renderer.renderMaxX;
            double by = y + renderer.renderMinY + 0.1875D;
            double z0 = z + renderer.renderMinZ, z1 = z + renderer.renderMaxZ;
            tessellator.addVertexWithUV(x0, by, z1, u0, v1);
            tessellator.addVertexWithUV(x0, by, z0, u0, v0);
            tessellator.addVertexWithUV(x1, by, z0, u1, v0);
            tessellator.addVertexWithUV(x1, by, z1, u1, v1);
        }

        // Top, turned to face the way the bed does
        icon = icon(renderer, block, world, x, y, z, 1);
        if (icon != null) {
            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y + 1, z));
            tessellator.setColorOpaque_F(r, g, b);
            double minU = icon.getMinU(), maxU = icon.getMaxU(), minV = icon.getMinV(), maxV = icon.getMaxV();
            double u1 = minU, u2 = maxU, v1 = minV, v2 = minV, u3 = minU, u4 = maxU, v3 = maxV, v4 = maxV;
            if (direction == 0) {
                u2 = minU;
                v1 = maxV;
                u3 = maxU;
                v4 = minV;
            } else if (direction == 2) {
                u1 = maxU;
                v2 = maxV;
                u4 = minU;
                v3 = minV;
            } else if (direction == 3) {
                u1 = maxU;
                v2 = maxV;
                u4 = minU;
                v3 = minV;
                u2 = minU;
                v1 = maxV;
                u3 = maxU;
                v4 = minV;
            }
            double x0 = x + renderer.renderMinX, x1 = x + renderer.renderMaxX;
            double ty = y + renderer.renderMaxY;
            double z0 = z + renderer.renderMinZ, z1 = z + renderer.renderMaxZ;
            tessellator.addVertexWithUV(x1, ty, z1, u3, v3);
            tessellator.addVertexWithUV(x1, ty, z0, u1, v1);
            tessellator.addVertexWithUV(x0, ty, z0, u2, v2);
            tessellator.addVertexWithUV(x0, ty, z1, u4, v4);
        }

        // Sides; the side where the other half is joined is not drawn
        int hidden = Direction.directionToFacing[foot ? Direction.rotateOpposite[direction] : direction];
        int flipped = switch (direction) {
            case 0 -> 5;
            case 1 -> 3;
            case 3 -> 2;
            default -> 4;
        };
        for (int side = 2; side < 6; side++) {
            int nx = x + (side == 4 ? -1 : side == 5 ? 1 : 0), nz = z + (side == 2 ? -1 : side == 3 ? 1 : 0);
            if (side == hidden || !renderer.renderAllFaces && !block.shouldSideBeRendered(world, nx, y, nz, side)) {
                continue;
            }
            icon = icon(renderer, block, world, x, y, z, side);
            if (icon == null) {
                continue;
            }
            boolean inside = side == 2 ? renderer.renderMinZ > 0.0D
                : side == 3 ? renderer.renderMaxZ < 1.0D
                    : side == 4 ? renderer.renderMinZ > 0.0D : renderer.renderMaxZ < 1.0D;
            tessellator.setBrightness(inside ? brightness : block.getMixedBrightnessForBlock(world, nx, y, nz));
            float shade = side < 4 ? 0.8F : 0.6F;
            tessellator.setColorOpaque_F(shade * r, shade * g, shade * b);
            renderer.flipTexture = flipped == side;
            switch (side) {
                case 2 -> renderer.renderFaceZNeg(block, x, y, z, icon);
                case 3 -> renderer.renderFaceZPos(block, x, y, z, icon);
                case 4 -> renderer.renderFaceXNeg(block, x, y, z, icon);
                default -> renderer.renderFaceXPos(block, x, y, z, icon);
            }
        }
        renderer.flipTexture = false;
    }

    /** The block's icon for this side and layer, the breaking texture while it is being broken, or null for none. */
    private static IIcon icon(RenderBlocks renderer, Block block, IBlockAccess world, int x, int y, int z, int side) {
        IIcon icon = block.getIcon(world, x, y, z, side);
        if (renderer.hasOverrideBlockTexture()) {
            // Breaking cracks are drawn once, over the frame layer.
            return RenderLayer.current == MDBlockDyedBed.FRAME ? renderer.overrideBlockTexture : null;
        }
        return icon;
    }

    /** The bed item is a flat icon (MDItemDyedBed), so this is never used. */
    @Override
    public void renderInventoryBlock(Block block, int meta, int modelId, RenderBlocks renderer) {}

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return this.renderId;
    }
}
