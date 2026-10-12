package net.neverandy.moredyes.client;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.block.TileColor;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityDyedSign;

/**
 * Draws a dyed sign: the vanilla sign renderer, with a grey sign texture multiplied by the sign's color. The text keeps
 * its vanilla look.
 */
@SideOnly(Side.CLIENT)
public class DyedSignRenderer extends TileEntitySpecialRenderer<TileEntityDyedSign>
{
	private static final ResourceLocation SIGN_TEXTURE=new ResourceLocation(Reference.MOD_ID,"textures/entity/sign.png");
	private final ModelSign model=new ModelSign();

	@Override
	public void render(TileEntityDyedSign te,double x,double y,double z,float partialTicks,int destroyStage,float alpha)
	{
		GlStateManager.pushMatrix();
		if(te.getBlockType()==MDBlock.standingSign)
		{
			GlStateManager.translate((float)x+0.5f,(float)y+0.5f,(float)z+0.5f);
			GlStateManager.rotate(-(te.getBlockMetadata()*360)/16.0f,0.0f,1.0f,0.0f);
			this.model.signStick.showModel=true;
		}
		else
		{
			int facing=te.getBlockMetadata();
			float angle=0.0f;
			if(facing==2)
			{
				angle=180.0f;
			}
			if(facing==4)
			{
				angle=90.0f;
			}
			if(facing==5)
			{
				angle=-90.0f;
			}
			GlStateManager.translate((float)x+0.5f,(float)y+0.5f,(float)z+0.5f);
			GlStateManager.rotate(-angle,0.0f,1.0f,0.0f);
			GlStateManager.translate(0.0f,-0.3125f,-0.4375f);
			this.model.signStick.showModel=false;
		}
		if(destroyStage>=0)
		{
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0f,2.0f,1.0f);
			GlStateManager.translate(0.0625f,0.0625f,0.0625f);
			GlStateManager.matrixMode(5888);
		}
		else
		{
			this.bindTexture(SIGN_TEXTURE);
			int rgb=TileColor.rgb(te.getColor());
			GlStateManager.color((rgb>>16&255)/255.0f,(rgb>>8&255)/255.0f,(rgb&255)/255.0f,1.0f);
		}
		GlStateManager.enableRescaleNormal();
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.6666667f,-0.6666667f,-0.6666667f);
		this.model.renderSign();
		GlStateManager.popMatrix();
		GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
		FontRenderer font=this.getFontRenderer();
		GlStateManager.translate(0.0f,0.33333334f,0.046666667f);
		GlStateManager.scale(0.010416667f,-0.010416667f,0.010416667f);
		GlStateManager.glNormal3f(0.0f,0.0f,-0.010416667f);
		GlStateManager.depthMask(false);
		if(destroyStage<0)
		{
			for(int line=0;line<te.signText.length;line++)
			{
				if(te.signText[line]!=null)
				{
					List<ITextComponent> parts=GuiUtilRenderComponents.splitText(te.signText[line],90,font,false,true);
					String text=parts!=null&&!parts.isEmpty()?parts.get(0).getFormattedText():"";
					if(line==te.lineBeingEdited)
					{
						text="> "+text+" <";
					}
					font.drawString(text,-font.getStringWidth(text)/2,line*10-te.signText.length*5,0);
				}
			}
		}
		GlStateManager.depthMask(true);
		GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
		GlStateManager.popMatrix();
		if(destroyStage>=0)
		{
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
	}
}
