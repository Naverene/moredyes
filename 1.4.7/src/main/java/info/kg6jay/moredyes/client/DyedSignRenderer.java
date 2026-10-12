package info.kg6jay.moredyes.client;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import info.kg6jay.moredyes.Colors;
import info.kg6jay.moredyes.Textures;
import info.kg6jay.moredyes.block.BlockDyedSign;
import info.kg6jay.moredyes.block.TileEntityDyedSign;

/**
 * Draws the dyed signs like the vanilla sign renderer, with a grey copy of the sign texture tinted with the dye color
 * and the text in black. The vanilla renderer cannot be reused: it only stands a sign up if it is the vanilla
 * standing sign.
 */
public class DyedSignRenderer extends TileEntitySpecialRenderer {

    private final ModelSign model = new ModelSign();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (!(tile instanceof TileEntityDyedSign)) {
            return;
        }
        TileEntityDyedSign sign = (TileEntityDyedSign) tile;
        Block block = sign.getBlockType();
        float scale = 0.6666667F;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.75F * scale, (float) z + 0.5F);
        if (block instanceof BlockDyedSign && ((BlockDyedSign) block).isStanding()) {
            GL11.glRotatef(-(sign.getBlockMetadata() * 360) / 16.0F, 0.0F, 1.0F, 0.0F);
            this.model.signStick.showModel = true;
        } else {
            int meta = sign.getBlockMetadata();
            float rotation = meta == 2 ? 180.0F : meta == 4 ? 90.0F : meta == 5 ? -90.0F : 0.0F;
            GL11.glRotatef(-rotation, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.3125F, -0.4375F);
            this.model.signStick.showModel = false;
        }

        this.bindTextureByName(Textures.SIGN);
        int rgb = Colors.rgb(sign.getColor());
        GL11.glColor4f((rgb >> 16 & 255) / 255.0F, (rgb >> 8 & 255) / 255.0F, (rgb & 255) / 255.0F, 1.0F);
        GL11.glPushMatrix();
        GL11.glScalef(scale, -scale, -scale);
        this.model.renderSign();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        FontRenderer font = this.getFontRenderer();
        float textScale = 0.016666668F * scale;
        GL11.glTranslatef(0.0F, 0.5F * scale, 0.07F * scale);
        GL11.glScalef(textScale, -textScale, textScale);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F * textScale);
        GL11.glDepthMask(false);
        for (int i = 0; i < sign.signText.length; ++i) {
            String line = i == sign.lineBeingEdited ? "> " + sign.signText[i] + " <" : sign.signText[i];
            font.drawString(line, -font.getStringWidth(line) / 2, i * 10 - sign.signText.length * 5, 0);
        }
        GL11.glDepthMask(true);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
}
