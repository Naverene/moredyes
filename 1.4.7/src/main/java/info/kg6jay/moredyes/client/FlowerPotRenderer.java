package info.kg6jay.moredyes.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import info.kg6jay.moredyes.Textures;
import info.kg6jay.moredyes.block.BlockDyedFlowerPot;
import info.kg6jay.moredyes.block.ILayeredBlock;
import info.kg6jay.moredyes.block.ILayeredBlock.RenderLayer;

/**
 * Draws a dyed flower pot like the vanilla flower pot renderer, with the pot and soil textures copied into the mod's
 * sheet, and the dyed plant in it drawn layer by layer: its colorMultiplier reads the pot's color.
 */
public class FlowerPotRenderer implements ISimpleBlockRenderingHandler {

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        renderer.renderStandardBlock(block, x, y, z);

        // The inside of the pot and the soil, as in RenderBlocks.renderBlockFlowerpot.
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        float wall = 0.1865F;
        int pot = Textures.FLOWER_POT;
        renderer.renderSouthFace(block, x - 0.5F + wall, y, z, pot);
        renderer.renderNorthFace(block, x + 0.5F - wall, y, z, pot);
        renderer.renderWestFace(block, x, y, z - 0.5F + wall, pot);
        renderer.renderEastFace(block, x, y, z + 0.5F - wall, pot);
        renderer.renderTopFace(block, x, y - 0.5F + wall + 0.1875F, z, Textures.FLOWER_POT_DIRT);

        Block plant = BlockDyedFlowerPot.plant(world.getBlockMetadata(x, y, z));
        int layers = plant instanceof ILayeredBlock ? ((ILayeredBlock) plant).getLayerCount() : 1;
        tessellator.addTranslation(0.0F, 0.25F, 0.0F);
        try {
            for (int layer = 0; layer < layers; ++layer) {
                RenderLayer.current = layer;
                renderer.renderCrossedSquares(plant, x, y, z);
            }
        } finally {
            RenderLayer.current = -1;
            tessellator.addTranslation(0.0F, -0.25F, 0.0F);
        }
        return true;
    }

    /** The pot has no item of its own. */
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {}

    @Override
    public boolean shouldRender3DInInventory() {
        return false;
    }

    @Override
    public int getRenderId() {
        return BlockDyedFlowerPot.renderId;
    }
}
