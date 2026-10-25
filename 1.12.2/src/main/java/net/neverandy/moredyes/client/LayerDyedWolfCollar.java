package net.neverandy.moredyes.client;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.client.renderer.entity.layers.LayerWolfCollar;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.handler.DyedCollarHandler;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.utility.ColorUtil;

/** The vanilla wolf collar layer, drawing a collar with a mod color (see DyedCollarHandler) in that color. */
@SideOnly(Side.CLIENT)
public class LayerDyedWolfCollar extends LayerWolfCollar
{
	private static final ResourceLocation TEXTURE=new ResourceLocation("textures/entity/wolf/wolf_collar.png");
	private final RenderWolf wolfRenderer;

	public LayerDyedWolfCollar(RenderWolf wolfRenderer)
	{
		super(wolfRenderer);
		this.wolfRenderer=wolfRenderer;
	}
	@Override
	public void doRenderLayer(EntityWolf wolf,float limbSwing,float limbSwingAmount,float partialTicks,float ageInTicks,float netHeadYaw,float headPitch,float scale)
	{
		int index=DyedCollarHandler.getColor(wolf);
		if(index<0)
		{
			super.doRenderLayer(wolf,limbSwing,limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch,scale);
			return;
		}
		if(wolf.isTamed()&&!wolf.isInvisible())
		{
			this.wolfRenderer.bindTexture(TEXTURE);
			int color=ColorUtil.fromHex(ColorStrings.ALL[index]);
			GlStateManager.color((color>>16&255)/255.0F,(color>>8&255)/255.0F,(color&255)/255.0F);
			this.wolfRenderer.getMainModel().render(wolf,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scale);
		}
	}
}
