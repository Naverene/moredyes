package net.neverandy.moredyes.client;

import net.minecraft.client.model.ModelBed;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.block.TileColor;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityDyedBed;

/**
 * Draws a dyed bed: the vanilla bed renderer, drawn twice. First the grey bedding multiplied by the bed's color, then
 * the wooden frame over it in its own colors (the bedding is clear in that texture).
 */
@SideOnly(Side.CLIENT)
public class DyedBedRenderer extends TileEntitySpecialRenderer<TileEntityDyedBed>
{
	private static final ResourceLocation BEDDING=new ResourceLocation(Reference.MOD_ID,"textures/entity/bed/dyed.png");
	private static final ResourceLocation FRAME=new ResourceLocation(Reference.MOD_ID,"textures/entity/bed/dyed_frame.png");
	private ModelBed model=new ModelBed();
	private int version=this.model.getModelVersion();

	@Override
	public void render(TileEntityDyedBed te,double x,double y,double z,float partialTicks,int destroyStage,float alpha)
	{
		if(this.version!=this.model.getModelVersion())
		{
			this.model=new ModelBed();
			this.version=this.model.getModelVersion();
		}
		boolean inWorld=te.getWorld()!=null;
		int facing=inWorld?te.getBlockMetadata()&3:0;
		if(destroyStage>=0)
		{
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0f,4.0f,1.0f);
			GlStateManager.translate(0.0625f,0.0625f,0.0625f);
			GlStateManager.matrixMode(5888);
			this.renderBed(te,inWorld,x,y,z,facing);
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
			return;
		}
		int rgb=TileColor.rgb(te.getColorIndex());
		this.bindTexture(BEDDING);
		GlStateManager.color((rgb>>16&255)/255.0f,(rgb>>8&255)/255.0f,(rgb&255)/255.0f,alpha);
		this.renderBed(te,inWorld,x,y,z,facing);
		this.bindTexture(FRAME);
		GlStateManager.color(1.0f,1.0f,1.0f,alpha);
		this.renderBed(te,inWorld,x,y,z,facing);
		GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
	}
	/** In the world, the half this tile entity is; as an item, both halves. */
	private void renderBed(TileEntityDyedBed te,boolean inWorld,double x,double y,double z,int facing)
	{
		if(inWorld)
		{
			this.renderPiece(te.isHeadPiece(),x,y,z,facing);
		}
		else
		{
			this.renderPiece(true,x,y,z,facing);
			this.renderPiece(false,x,y,z-1.0,facing);
		}
	}
	private void renderPiece(boolean head,double x,double y,double z,int facing)
	{
		this.model.preparePiece(head);
		GlStateManager.pushMatrix();
		float angle=0.0f;
		float dx=0.0f;
		float dz=0.0f;
		if(facing==EnumFacing.SOUTH.getHorizontalIndex())
		{
			angle=180.0f;
			dx=1.0f;
			dz=1.0f;
		}
		else if(facing==EnumFacing.WEST.getHorizontalIndex())
		{
			angle=-90.0f;
			dz=1.0f;
		}
		else if(facing==EnumFacing.EAST.getHorizontalIndex())
		{
			angle=90.0f;
			dx=1.0f;
		}
		GlStateManager.translate((float)x+dx,(float)y+0.5625f,(float)z+dz);
		GlStateManager.rotate(90.0f,1.0f,0.0f,0.0f);
		GlStateManager.rotate(angle,0.0f,0.0f,1.0f);
		GlStateManager.enableRescaleNormal();
		GlStateManager.pushMatrix();
		this.model.render();
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();
	}
}
