package net.neverandy.moredyes.compat.ironchest.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.compat.ironchest.IronShulkerBoxCompat;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.LogHelper;

/**
 * Splits each white Iron Chests shulker box texture into two: the shell, made grey so the renderer can multiply it by
 * the dye color, and the metal trim, which keeps its own colors. The shell is the part that differs between the white
 * and the red box of a tier; the trim is the same on both. They are made from whatever textures are loaded, so they
 * follow resource packs, and none of Iron Chests' art is copied into More Dyes.
 */
@SideOnly(Side.CLIENT)
public final class DyedIronShulkerBoxTextures implements IResourceManagerReloadListener
{
	public static final DyedIronShulkerBoxTextures INSTANCE=new DyedIronShulkerBoxTextures();

	private final Map<IronShulkerBoxCompat.Tier,ResourceLocation[]> textures=new EnumMap<IronShulkerBoxCompat.Tier,ResourceLocation[]>(IronShulkerBoxCompat.Tier.class);
	private boolean made;

	private DyedIronShulkerBoxTextures(){}

	@Override
	public void onResourceManagerReload(IResourceManager resources)
	{
		this.made=false;
	}

	/** The grey shell of a tier, to be tinted, or null if its textures could not be read. */
	public ResourceLocation shell(IronShulkerBoxCompat.Tier tier)
	{
		this.make();
		ResourceLocation[] pair=this.textures.get(tier);
		return pair==null?null:pair[0];
	}
	/** The trim of a tier, in its own colors; Iron Chests' white box if the textures could not be read. */
	public ResourceLocation trim(IronShulkerBoxCompat.Tier tier)
	{
		this.make();
		ResourceLocation[] pair=this.textures.get(tier);
		return pair==null?original(tier,"white"):pair[1];
	}

	private static ResourceLocation original(IronShulkerBoxCompat.Tier tier,String color)
	{
		return new ResourceLocation("ironchest","textures/model/shulker/"+color+"/shulker_"+color+tier.type.modelTexture);
	}

	/** Makes the textures from the loaded ones, the first time they are drawn after resources (re)load. */
	private void make()
	{
		if(this.made)
		{
			return;
		}
		this.made=true;
		TextureManager manager=Minecraft.getMinecraft().getTextureManager();
		for(IronShulkerBoxCompat.Tier tier:IronShulkerBoxCompat.Tier.values())
		{
			BufferedImage white=read(original(tier,"white"));
			BufferedImage red=read(original(tier,"red"));
			if(white==null)
			{
				this.textures.remove(tier);
				continue;
			}
			int width=white.getWidth(),height=white.getHeight();
			// Without a red texture to compare with (a resource pack that changes the sizes), all of it is shell.
			boolean compare=red!=null&&red.getWidth()==width&&red.getHeight()==height;
			BufferedImage shell=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			BufferedImage trim=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			for(int y=0;y<height;y++)
			{
				for(int x=0;x<width;x++)
				{
					int argb=white.getRGB(x,y);
					if(!compare||red.getRGB(x,y)!=argb)
					{
						shell.setRGB(x,y,grey(argb));
					}
					else
					{
						trim.setRGB(x,y,argb);
					}
				}
			}
			ResourceLocation[] pair={
					new ResourceLocation(Reference.MOD_ID,"dynamic/"+tier.id()+"_shulker_box_shell"),
					new ResourceLocation(Reference.MOD_ID,"dynamic/"+tier.id()+"_shulker_box_trim")};
			manager.deleteTexture(pair[0]);
			manager.deleteTexture(pair[1]);
			manager.loadTexture(pair[0],new DynamicTexture(shell));
			manager.loadTexture(pair[1],new DynamicTexture(trim));
			this.textures.put(tier,pair);
		}
	}

	private static BufferedImage read(ResourceLocation location)
	{
		try(IResource resource=Minecraft.getMinecraft().getResourceManager().getResource(location);InputStream stream=resource.getInputStream())
		{
			return TextureUtil.readBufferedImage(stream);
		}
		catch(IOException e)
		{
			LogHelper.warn("Could not read "+location+", so dyed shulker boxes of that tier are drawn without their color: "+e);
			return null;
		}
	}

	/** The brightness of a pixel as grey, keeping its alpha. */
	private static int grey(int argb)
	{
		int r=argb>>16&255,g=argb>>8&255,b=argb&255;
		int lum=Math.min(255,Math.round(0.299F*r+0.587F*g+0.114F*b));
		return argb&0xFF000000|lum<<16|lum<<8|lum;
	}
}
