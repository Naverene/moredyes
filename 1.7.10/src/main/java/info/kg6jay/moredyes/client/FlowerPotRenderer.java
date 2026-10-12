package info.kg6jay.moredyes.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import info.kg6jay.moredyes.block.ILayeredBlock;
import info.kg6jay.moredyes.block.MDBlockFlowerPot;

/**
 * Draws a flower pot holding a dyed plant (MDBlockFlowerPot): the vanilla pot, then the plant in it the way the
 * vanilla pot draws its plants, once per layer with that layer's tint.
 */
public class FlowerPotRenderer implements ISimpleBlockRenderingHandler {

    /** How far up the plant sits in the pot, as in the vanilla renderer. */
    private static final float PLANT_OFFSET = 4.0F / 16.0F;

    private final int renderId;

    public FlowerPotRenderer(int renderId) {
        this.renderId = renderId;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        // There is no vanilla pot tile entity here, so this draws the empty pot.
        renderer.renderBlockFlowerpot((BlockFlowerPot) block, x, y, z);

        ItemStack plant = MDBlockFlowerPot.getPlant(world, x, y, z);
        if (plant == null || !(Block.getBlockFromItem(plant.getItem()) instanceof ILayeredBlock layered)) {
            return true;
        }
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        tessellator.addTranslation(0.0F, PLANT_OFFSET, 0.0F);
        for (int layer = 0; layer < layered.getLayerCount(); ++layer) {
            IIcon icon = layered.getLayerIcon(0, plant.getItemDamage(), layer);
            if (icon != null) {
                tessellator.setColorOpaque_I(layered.getLayerColor(plant.getItemDamage(), layer));
                renderer.drawCrossedSquares(icon, x, y, z, 0.75F);
            }
        }
        tessellator.addTranslation(0.0F, -PLANT_OFFSET, 0.0F);
        return true;
    }

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
