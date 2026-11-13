package info.kg6jay.moredyes.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import info.kg6jay.moredyes.item.SharedBlocks;

/**
 * Draws the shared dyed block item (see SharedBlocks) the way the block's own item is drawn: with the old item's
 * renderer if it has one, as a 3D block if the block draws as one, and otherwise (plants, panes) not at all, so the
 * vanilla flat icon is drawn from the icons the shared item hands on.
 */
public class SharedBlockItemRenderer implements IItemRenderer {

    /** The 3D block for a stack, or null if it is not drawn as one. */
    private static Block block3d(ItemStack old) {
        if (old == null || !(old.getItem() instanceof ItemBlock)) {
            return null;
        }
        Block block = Block.getBlockFromItem(old.getItem());
        return RenderBlocks.renderItemIn3d(block.getRenderType()) ? block : null;
    }

    private static IItemRenderer custom(ItemStack old, ItemRenderType type) {
        return old == null ? null : MinecraftForgeClient.getItemRenderer(old, type);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        ItemStack old = SharedBlocks.toOld(item);
        if (custom(old, type) != null) {
            return true;
        }
        return type != ItemRenderType.FIRST_PERSON_MAP && block3d(old) != null;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        ItemStack old = SharedBlocks.toOld(item);
        IItemRenderer custom = custom(old, type);
        if (custom != null) {
            return custom.shouldUseRenderHelper(type, old, helper);
        }
        return switch (helper) {
            case ENTITY_ROTATION, ENTITY_BOBBING, BLOCK_3D, INVENTORY_BLOCK, EQUIPPED_BLOCK -> true;
            default -> false;
        };
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        ItemStack old = SharedBlocks.toOld(item);
        IItemRenderer custom = custom(old, type);
        if (custom != null) {
            custom.renderItem(type, old, data);
            return;
        }
        Block block = block3d(old);
        if (block == null || data.length == 0 || !(data[0] instanceof RenderBlocks renderBlocks)) {
            return;
        }
        int meta = old.getItemDamage();
        boolean blend = block.getRenderBlockPass() != 0;
        GL11.glPushMatrix();
        switch (type) {
            case ENTITY -> {
                // Forge scales a dropped item by 0.5 when it cannot see the block; vanilla draws cubes at 0.25.
                int renderType = block.getRenderType();
                if (renderType != 1 && renderType != 19 && renderType != 12 && renderType != 2) {
                    GL11.glScalef(0.5F, 0.5F, 0.5F);
                }
                if (blend) {
                    GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                    GL11.glEnable(GL11.GL_BLEND);
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                }
            }
            case EQUIPPED, EQUIPPED_FIRST_PERSON -> {
                // Forge moves the block by -0.5 for its helper, but renderBlockAsItem centers it itself.
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                if (blend) {
                    GL11.glDepthMask(false);
                }
            }
            case INVENTORY -> {
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                if (blend) {
                    GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                    GL11.glEnable(GL11.GL_BLEND);
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                } else {
                    GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
                    GL11.glDisable(GL11.GL_BLEND);
                }
            }
            default -> {}
        }
        renderBlocks.renderBlockAsItem(block, meta, 1.0F);
        if (blend) {
            if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
                GL11.glDepthMask(true);
            } else if (type == ItemRenderType.ENTITY) {
                GL11.glDisable(GL11.GL_BLEND);
            }
        }
        GL11.glPopMatrix();
    }
}
