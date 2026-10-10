package info.kg6jay.moredyes.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.Textures;
import info.kg6jay.moredyes.block.BlockDyedChest;
import info.kg6jay.moredyes.block.TileEntityDyedChest;

/**
 * Draws the dyed chests with the vanilla chest models: a grey copy of the vanilla chest texture tinted with the dye
 * color, with the latch left untinted. Also draws the chest items, since the vanilla chest item renderer always draws
 * a plain vanilla chest.
 */
public class DyedChestRenderer extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {

    private final ModelChest modelSingle = new ModelChest();
    private final ModelChest modelDouble = new ModelLargeChest();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (!(tile instanceof TileEntityDyedChest)) {
            return;
        }
        TileEntityDyedChest chest = (TileEntityDyedChest) tile;
        int facing = 3;
        if (chest.func_70309_m()) {
            facing = chest.getBlockMetadata();
            chest.checkForAdjacentChests();
        }
        // The half at the lower coordinate draws the whole double chest.
        if (chest.adjacentChestXNeg != null || chest.adjacentChestZNeg != null) {
            return;
        }
        boolean isDouble = chest.adjacentChestXPos != null || chest.adjacentChestZPosition != null;
        this.bindTextureByName(isDouble ? Textures.LARGE_CHEST : Textures.CHEST);
        float lid = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * partialTicks;
        this.renderModel(isDouble ? this.modelDouble : this.modelSingle, Colors.rgb(chest.getColor()), facing,
            chest.adjacentChestXPos != null, chest.adjacentChestZPosition != null, lid, x, y, z);
    }

    /** Same transforms as the vanilla chest renderer. */
    private void renderModel(ModelChest model, int color, int facing, boolean partnerXPos, boolean partnerZPos,
        float lid, double x, double y, double z) {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        int angle = 0;
        if (facing == 2) {
            angle = 180;
        } else if (facing == 4) {
            angle = 90;
        } else if (facing == 5) {
            angle = -90;
        }
        if (facing == 2 && partnerXPos) {
            GL11.glTranslatef(1.0F, 0.0F, 0.0F);
        }
        if (facing == 5 && partnerZPos) {
            GL11.glTranslatef(0.0F, 0.0F, -1.0F);
        }
        GL11.glRotatef(angle, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        lid = 1.0F - lid;
        lid = 1.0F - lid * lid * lid;
        model.chestLid.rotateAngleX = -(lid * (float) Math.PI / 2.0F);
        model.chestKnob.rotateAngleX = model.chestLid.rotateAngleX;

        // Same parts as ModelChest.renderAll, with the wood tinted and the latch in its natural color.
        GL11.glColor4f((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, 1.0F);
        model.chestLid.render(0.0625F);
        model.chestBelow.render(0.0625F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        model.chestKnob.render(0.0625F);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void renderInventoryBlock(Block block, int color, int modelId, RenderBlocks renderer) {
        // Matches what RenderBlocks does before drawing a vanilla chest item.
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        RenderEngine engine = Minecraft.getMinecraft().renderEngine;
        engine.bindTexture(engine.getTexture(Textures.CHEST));
        this.renderModel(this.modelSingle, Colors.rgb(color), 3, false, false, 0.0F, 0.0D, 0.0D, 0.0D);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        // Item renderers draw the next item expecting the block sheet.
        engine.bindTexture(engine.getTexture(block.getTextureFile()));
    }

    /** Nothing in the chunk mesh; the tile entity renderer draws the chest. */
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return BlockDyedChest.renderId;
    }
}
