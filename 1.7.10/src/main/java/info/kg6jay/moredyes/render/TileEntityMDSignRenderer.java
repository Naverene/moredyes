package info.kg6jay.moredyes.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.block.MDBlock;
import info.kg6jay.moredyes.block.tileentity.TileEntityMDSign;
import info.kg6jay.moredyes.client.TintedTextures;
import info.kg6jay.moredyes.utility.ColorIndex;

/**
 * Draws the dyed signs like the vanilla sign renderer, with a grey copy of the vanilla sign texture tinted with the
 * sign's color. The text stays black.
 */
@SideOnly(Side.CLIENT)
public class TileEntityMDSignRenderer extends TileEntitySpecialRenderer {

    private final ModelSign model = new ModelSign();
    private final ResourceLocation texture = TintedTextures.modelTexture("sign/model");

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (!(tile instanceof TileEntityMDSign sign)) {
            return;
        }
        GL11.glPushMatrix();
        float scale = 2.0F / 3.0F;
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.75F * scale, (float) z + 0.5F);
        if (sign.getBlockType() == MDBlock.standingSign) {
            GL11.glRotatef(-(sign.getBlockMetadata() * 360) / 16.0F, 0.0F, 1.0F, 0.0F);
            this.model.signStick.showModel = true;
        } else {
            float angle = switch (sign.getBlockMetadata()) {
                case 2 -> 180.0F;
                case 4 -> 90.0F;
                case 5 -> -90.0F;
                default -> 0.0F;
            };
            GL11.glRotatef(-angle, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.3125F, -0.4375F);
            this.model.signStick.showModel = false;
        }

        this.bindTexture(this.texture);
        int color = ColorIndex.rgb(sign.getColor());
        GL11.glColor4f((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, 1.0F);
        GL11.glPushMatrix();
        GL11.glScalef(scale, -scale, -scale);
        this.model.renderSign();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        FontRenderer font = this.func_147498_b();
        float textScale = 0.016666668F * scale;
        GL11.glTranslatef(0.0F, 0.5F * scale, 0.07F * scale);
        GL11.glScalef(textScale, -textScale, textScale);
        GL11.glNormal3f(0.0F, 0.0F, -textScale);
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
