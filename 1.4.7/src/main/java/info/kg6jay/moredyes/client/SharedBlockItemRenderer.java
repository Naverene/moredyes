package info.kg6jay.moredyes.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import info.kg6jay.moredyes.item.SharedBlocks;

/**
 * Draws the shared dyed block item (see SharedBlocks) as its block, the way Minecraft draws a block's own item: Forge
 * only draws items that are not block items flat. Flat ones (plants, panes, trapdoors...) are left to Minecraft.
 */
public class SharedBlockItemRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
        Block block = SharedBlocks.sharedBlock(stack);
        return block != null && type != ItemRenderType.FIRST_PERSON_MAP
            && RenderBlocks.renderItemIn3d(block.getRenderType());
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack stack, ItemRendererHelper helper) {
        return helper == ItemRendererHelper.ENTITY_ROTATION || helper == ItemRendererHelper.ENTITY_BOBBING
            || helper == ItemRendererHelper.INVENTORY_BLOCK || helper == ItemRendererHelper.EQUIPPED_BLOCK;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        Block block = SharedBlocks.sharedBlock(stack);
        if (block == null || data.length == 0 || !(data[0] instanceof RenderBlocks)) {
            return;
        }
        RenderBlocks renderBlocks = (RenderBlocks) data[0];
        int color = SharedBlocks.sharedColor(stack);
        GL11.glPushMatrix();
        if (type == ItemRenderType.ENTITY) {
            // Forge scales items that are not block items by 0.5; Minecraft draws most blocks at 0.25.
            int renderType = block.getRenderType();
            if (renderType != 1 && renderType != 19 && renderType != 12 && renderType != 2) {
                GL11.glScalef(0.5F, 0.5F, 0.5F);
            }
        } else if (type == ItemRenderType.EQUIPPED) {
            // Forge moves a block in hand by -0.5 for renderers that draw from the corner; renderBlockAsItem
            // draws around the middle.
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
        renderBlocks.renderBlockAsItem(block, color, 1.0F);
        GL11.glPopMatrix();
    }
}
