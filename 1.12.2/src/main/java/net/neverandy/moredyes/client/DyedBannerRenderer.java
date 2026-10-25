package net.neverandy.moredyes.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.block.MDBlock;
import net.neverandy.moredyes.block.TileColor;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.tileentity.TileEntityDyedBanner;

/**
 * Draws a dyed banner: the vanilla banner renderer, with a texture whose base is the banner's color (see
 * DyedBannerTexture). Like vanilla, each look is put together once and kept while it is in use.
 */
@SideOnly(Side.CLIENT)
public class DyedBannerRenderer extends TileEntitySpecialRenderer<TileEntityDyedBanner>
{
	private static final int CACHE_SIZE=256;
	private static final long UNUSED_MILLIS=5000L;
	private static final Map<String,CacheEntry> CACHE=new LinkedHashMap<String,CacheEntry>();

	private final ModelBanner model=new ModelBanner();

	private static class CacheEntry
	{
		ResourceLocation texture;
		long lastUse;
	}

	@Override
	public void render(TileEntityDyedBanner te,double x,double y,double z,float partialTicks,int destroyStage,float alpha)
	{
		boolean inWorld=te.getWorld()!=null;
		boolean standing=!inWorld||te.getBlockType()==MDBlock.standingBanner;
		int meta=inWorld?te.getBlockMetadata():0;
		long time=inWorld?te.getWorld().getTotalWorldTime():0L;
		GlStateManager.pushMatrix();
		if(standing)
		{
			GlStateManager.translate((float)x+0.5f,(float)y+0.5f,(float)z+0.5f);
			GlStateManager.rotate(-(meta*360/16.0f),0.0f,1.0f,0.0f);
			this.model.bannerStand.showModel=true;
		}
		else
		{
			float angle=0.0f;
			if(meta==2)
			{
				angle=180.0f;
			}
			if(meta==4)
			{
				angle=90.0f;
			}
			if(meta==5)
			{
				angle=-90.0f;
			}
			GlStateManager.translate((float)x+0.5f,(float)y-0.16666667f,(float)z+0.5f);
			GlStateManager.rotate(-angle,0.0f,1.0f,0.0f);
			GlStateManager.translate(0.0f,-0.3125f,-0.4375f);
			this.model.bannerStand.showModel=false;
		}
		BlockPos pos=te.getPos();
		float sway=pos.getX()*7+pos.getY()*9+pos.getZ()*13+(float)time+partialTicks;
		this.model.bannerSlate.rotateAngleX=(-0.0125f+0.01f*MathHelper.cos(sway*(float)Math.PI*0.02f))*(float)Math.PI;
		GlStateManager.enableRescaleNormal();
		ResourceLocation texture=texture(te);
		if(texture!=null)
		{
			this.bindTexture(texture);
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.6666667f,-0.6666667f,-0.6666667f);
			this.model.renderBanner();
			GlStateManager.popMatrix();
		}
		GlStateManager.color(1.0f,1.0f,1.0f,alpha);
		GlStateManager.popMatrix();
	}

	/** The texture of a banner's color and patterns, or null while it has none. */
	private static ResourceLocation texture(TileEntityDyedBanner te)
	{
		String patterns=te.getPatternResourceLocation();
		if(patterns==null||patterns.isEmpty())
		{
			return null;
		}
		List<BannerPattern> patternList=te.getPatternList();
		List<EnumDyeColor> colorList=te.getColorList();
		// Like vanilla's key, with the dyed color in place of the base color.
		StringBuilder key=new StringBuilder(ColorStrings.ALL[TileColor.clamp(te.getColorIndex())]);
		for(int i=1;i<patternList.size();i++)
		{
			key.append(patternList.get(i).getHashname()).append(colorList.get(i).getDyeDamage());
		}
		CacheEntry entry=CACHE.get(key.toString());
		if(entry==null)
		{
			if(CACHE.size()>=CACHE_SIZE&&!freeSlot())
			{
				return null;
			}
			List<String> layers=new ArrayList<String>();
			int[] colors=new int[patternList.size()];
			for(int i=0;i<patternList.size();i++)
			{
				layers.add("textures/entity/banner/"+patternList.get(i).getFileName()+".png");
				colors[i]=i==0?TileColor.rgb(te.getColorIndex()):colorList.get(i).getColorValue();
			}
			entry=new CacheEntry();
			entry.texture=new ResourceLocation(Reference.MOD_ID,"dyed_banner/"+key);
			Minecraft.getMinecraft().getTextureManager().loadTexture(entry.texture,new DyedBannerTexture(layers,colors));
			CACHE.put(key.toString(),entry);
		}
		entry.lastUse=System.currentTimeMillis();
		return entry.texture;
	}
	/** Forgets a texture no banner has used for a while, like vanilla's banner cache. */
	private static boolean freeSlot()
	{
		long now=System.currentTimeMillis();
		Iterator<CacheEntry> entries=CACHE.values().iterator();
		while(entries.hasNext())
		{
			CacheEntry entry=entries.next();
			if(now-entry.lastUse>UNUSED_MILLIS)
			{
				Minecraft.getMinecraft().getTextureManager().deleteTexture(entry.texture);
				entries.remove();
				return true;
			}
		}
		return CACHE.size()<CACHE_SIZE;
	}
}
