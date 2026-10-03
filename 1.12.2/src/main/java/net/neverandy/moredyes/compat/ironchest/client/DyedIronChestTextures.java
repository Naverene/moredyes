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
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;
import net.neverandy.moredyes.reference.Reference;
import net.neverandy.moredyes.utility.LogHelper;

/**
 * Splits each Iron Chests chest texture into two: the panels, made grey so the renderer can multiply them by the dye
 * color, and everything else (the edges of each side, the latch and the inside), which keeps its own color. They are
 * made from whatever textures are loaded, so they follow resource packs, and none of Iron Chests' art is copied into
 * More Dyes.
 * <p>
 * The 1.12.2 Iron Chests are metal all over, with no wood to dye as on later versions, so the split follows the chest
 * model's layout instead of the colors: every outer side of the lid and the base is a panel inside a one pixel edge.
 * The crystal chest is clear glass inside its edges, so there the edges are what is dyed.
 */
@SideOnly(Side.CLIENT)
public final class DyedIronChestTextures implements IResourceManagerReloadListener
{
	public static final DyedIronChestTextures INSTANCE=new DyedIronChestTextures();

	/**
	 * The outer sides of the vanilla chest model (ModelChest), as {x1,y1,x2,y2} in a 64x64 texture: the top and the
	 * four sides of the lid, then the bottom and the four sides of the base. The rest of the texture is the latch in
	 * the top-left corner and the inside of the lid and the base.
	 */
	private static final int[][] OUTER_SIDES={
			{14,0,28,14},{0,14,14,19},{14,14,28,19},{28,14,42,19},{42,14,56,19},
			{28,19,42,33},{0,33,14,43},{14,33,28,43},{28,33,42,43},{42,33,56,43}};
	/** The latch, {x2,y2} from the top-left corner of a 64x64 texture. */
	private static final int LATCH_WIDTH=6,LATCH_HEIGHT=5;

	private final Map<IronChestCompat.Tier,ResourceLocation[]> textures=new EnumMap<IronChestCompat.Tier,ResourceLocation[]>(IronChestCompat.Tier.class);
	private boolean made;

	private DyedIronChestTextures(){}

	@Override
	public void onResourceManagerReload(IResourceManager resources)
	{
		this.made=false;
	}

	/** The grey panels of a tier, to be tinted, or null if its texture could not be read. */
	public ResourceLocation panels(IronChestCompat.Tier tier)
	{
		this.make();
		ResourceLocation[] pair=this.textures.get(tier);
		return pair==null?null:pair[0];
	}
	/** Everything of a tier's texture but the panels, in its own colors. */
	public ResourceLocation rest(IronChestCompat.Tier tier)
	{
		this.make();
		ResourceLocation[] pair=this.textures.get(tier);
		return pair==null?tier.type.modelTexture:pair[1];
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
		for(IronChestCompat.Tier tier:IronChestCompat.Tier.values())
		{
			BufferedImage source=read(tier.type.modelTexture);
			if(source==null)
			{
				this.textures.remove(tier);
				continue;
			}
			int width=source.getWidth(),height=source.getHeight();
			boolean[] panel=tier==IronChestCompat.Tier.CRYSTAL?opaqueMask(source):panelMask(width,height);
			BufferedImage panels=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			BufferedImage rest=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			for(int y=0;y<height;y++)
			{
				for(int x=0;x<width;x++)
				{
					int argb=source.getRGB(x,y);
					if(panel[y*width+x])
					{
						panels.setRGB(x,y,grey(argb));
					}
					else
					{
						rest.setRGB(x,y,argb);
					}
				}
			}
			ResourceLocation[] pair={
					new ResourceLocation(Reference.MOD_ID,"dynamic/"+tier.id()+"_chest_panels"),
					new ResourceLocation(Reference.MOD_ID,"dynamic/"+tier.id()+"_chest_rest")};
			manager.deleteTexture(pair[0]);
			manager.deleteTexture(pair[1]);
			manager.loadTexture(pair[0],new DynamicTexture(panels));
			manager.loadTexture(pair[1],new DynamicTexture(rest));
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
			LogHelper.warn("Could not read "+location+", so dyed chests of that tier are drawn without their color: "+e);
			return null;
		}
	}

	/** The panels: inside the one pixel edge of each outer side, scaled to the texture's size. */
	private static boolean[] panelMask(int width,int height)
	{
		boolean[] mask=new boolean[width*height];
		int edgeX=Math.max(1,width/64),edgeY=Math.max(1,height/64);
		for(int[] side:OUTER_SIDES)
		{
			int x1=side[0]*width/64+edgeX,x2=side[2]*width/64-edgeX;
			int y1=side[1]*height/64+edgeY,y2=side[3]*height/64-edgeY;
			for(int y=y1;y<y2;y++)
			{
				for(int x=x1;x<x2;x++)
				{
					mask[y*width+x]=true;
				}
			}
		}
		return mask;
	}
	/** For the crystal chest: every pixel that is not clear, but the latch. */
	private static boolean[] opaqueMask(BufferedImage image)
	{
		int width=image.getWidth(),height=image.getHeight();
		int latchWidth=LATCH_WIDTH*width/64,latchHeight=LATCH_HEIGHT*height/64;
		boolean[] mask=new boolean[width*height];
		for(int y=0;y<height;y++)
		{
			for(int x=0;x<width;x++)
			{
				mask[y*width+x]=!(x<latchWidth&&y<latchHeight)&&(image.getRGB(x,y)>>>24)!=0;
			}
		}
		return mask;
	}
	/** The brightness of a pixel as grey, keeping its alpha, as tools/make_assets.py makes the other dyed textures. */
	private static int grey(int argb)
	{
		int r=argb>>16&255,g=argb>>8&255,b=argb&255;
		int lum=Math.min(255,Math.round(0.299F*r+0.587F*g+0.114F*b));
		return argb&0xFF000000|lum<<16|lum<<8|lum;
	}
}
