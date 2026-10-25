package net.neverandy.moredyes.client;

import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.block.TileColor;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityDyedShulkerBox;

/** Draws a dyed shulker box: the vanilla shulker box renderer, with a grey texture multiplied by the box's color. */
@SideOnly(Side.CLIENT)
public class DyedShulkerBoxRenderer extends TileEntitySpecialRenderer<TileEntityDyedShulkerBox>
{
	private static final ResourceLocation TEXTURE=new ResourceLocation(Reference.MOD_ID,"textures/entity/shulker/dyed.png");
	private final ModelShulker model=new ModelShulker();

	@Override
	public void render(TileEntityDyedShulkerBox te,double x,double y,double z,float partialTicks,int destroyStage,float alpha)
	{
		EnumFacing facing=EnumFacing.UP;
		if(te.hasWorld())
		{
			IBlockState state=this.getWorld().getBlockState(te.getPos());
			if(state.getBlock() instanceof BlockShulkerBox)
			{
				facing=state.getValue(BlockShulkerBox.FACING);
			}
		}
		GlStateManager.enableDepth();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		GlStateManager.disableCull();
		if(destroyStage>=0)
		{
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0f,4.0f,1.0f);
			GlStateManager.translate(0.0625f,0.0625f,0.0625f);
			GlStateManager.matrixMode(5888);
		}
		else
		{
			this.bindTexture(TEXTURE);
		}
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		if(destroyStage<0)
		{
			int rgb=TileColor.rgb(te.getColorIndex());
			GlStateManager.color((rgb>>16&255)/255.0f,(rgb>>8&255)/255.0f,(rgb&255)/255.0f,alpha);
		}
		GlStateManager.translate((float)x+0.5f,(float)y+1.5f,(float)z+0.5f);
		GlStateManager.scale(1.0f,-1.0f,-1.0f);
		GlStateManager.translate(0.0f,1.0f,0.0f);
		GlStateManager.scale(0.9995f,0.9995f,0.9995f);
		GlStateManager.translate(0.0f,-1.0f,0.0f);
		switch(facing)
		{
		case DOWN:
			GlStateManager.translate(0.0f,2.0f,0.0f);
			GlStateManager.rotate(180.0f,1.0f,0.0f,0.0f);
			break;
		case NORTH:
			GlStateManager.translate(0.0f,1.0f,1.0f);
			GlStateManager.rotate(90.0f,1.0f,0.0f,0.0f);
			GlStateManager.rotate(180.0f,0.0f,0.0f,1.0f);
			break;
		case SOUTH:
			GlStateManager.translate(0.0f,1.0f,-1.0f);
			GlStateManager.rotate(90.0f,1.0f,0.0f,0.0f);
			break;
		case WEST:
			GlStateManager.translate(-1.0f,1.0f,0.0f);
			GlStateManager.rotate(90.0f,1.0f,0.0f,0.0f);
			GlStateManager.rotate(-90.0f,0.0f,0.0f,1.0f);
			break;
		case EAST:
			GlStateManager.translate(1.0f,1.0f,0.0f);
			GlStateManager.rotate(90.0f,1.0f,0.0f,0.0f);
			GlStateManager.rotate(90.0f,0.0f,0.0f,1.0f);
			break;
		default:
			break;
		}
		this.model.base.render(0.0625f);
		GlStateManager.translate(0.0f,-te.getProgress(partialTicks)*0.5f,0.0f);
		GlStateManager.rotate(270.0f*te.getProgress(partialTicks),0.0f,1.0f,0.0f);
		this.model.lid.render(0.0625f);
		GlStateManager.enableCull();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
		if(destroyStage>=0)
		{
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
	}
}
