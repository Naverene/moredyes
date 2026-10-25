package net.neverandy.moredyes.client;

import net.minecraft.client.model.ModelLlama;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLlama;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.entity.LlamaCarpets;
import net.neverandy.moredyes.reference.Reference;

/**
 * Draws the dyed carpet a llama wears, like vanilla's LayerLlamaDecor: a grey carpet multiplied by its color, then its
 * stripes over it in their own colors. The server sends the color (see entity/LlamaCarpets).
 */
@SideOnly(Side.CLIENT)
public class LayerDyedLlamaDecor implements LayerRenderer<EntityLlama>
{
	private static final ResourceLocation CARPET=new ResourceLocation(Reference.MOD_ID,"textures/entity/llama/decor/dyed.png");
	private static final ResourceLocation STRIPES=new ResourceLocation(Reference.MOD_ID,"textures/entity/llama/decor/dyed_stripes.png");
	private final RenderLlama renderer;
	private final ModelLlama model=new ModelLlama(0.5f);

	public LayerDyedLlamaDecor(RenderLlama renderer)
	{
		this.renderer=renderer;
	}
	@Override
	public void doRenderLayer(EntityLlama llama,float limbSwing,float limbSwingAmount,float partialTicks,float ageInTicks,float netHeadYaw,float headPitch,float scale)
	{
		int rgb=LlamaCarpets.clientColor(llama);
		if(rgb<0)
		{
			return;
		}
		this.model.setModelAttributes(this.renderer.getMainModel());
		this.renderer.bindTexture(CARPET);
		GlStateManager.color((rgb>>16&255)/255.0f,(rgb>>8&255)/255.0f,(rgb&255)/255.0f,1.0f);
		this.model.render(llama,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scale);
		this.renderer.bindTexture(STRIPES);
		GlStateManager.color(1.0f,1.0f,1.0f,1.0f);
		this.model.render(llama,limbSwing,limbSwingAmount,ageInTicks,netHeadYaw,headPitch,scale);
	}
	@Override
	public boolean shouldCombineTextures()
	{
		return false;
	}
}
