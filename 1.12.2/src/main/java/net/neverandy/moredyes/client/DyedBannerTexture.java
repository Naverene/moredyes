package net.neverandy.moredyes.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.utility.LogHelper;

/**
 * The texture of one banner, put together like vanilla's LayeredColorMaskTexture: each pattern's mask cut out of the
 * banner texture and multiplied by its color. Vanilla's only takes the 16 dye colors; this one takes any color, for
 * the base of a dyed banner.
 */
@SideOnly(Side.CLIENT)
public class DyedBannerTexture extends AbstractTexture
{
	private static final ResourceLocation BANNER=new ResourceLocation("textures/entity/banner_base.png");

	private final List<String> layers;
	private final int[] colors;

	/** The mask textures, and the color (0xRRGGBB) of each. */
	public DyedBannerTexture(List<String> layers,int[] colors)
	{
		this.layers=layers;
		this.colors=colors;
	}
	@Override
	public void loadTexture(IResourceManager resources) throws IOException
	{
		this.deleteGlTexture();
		BufferedImage result;
		try(IResource resource=resources.getResource(BANNER))
		{
			BufferedImage base=TextureUtil.readBufferedImage(resource.getInputStream());
			int type=base.getType()==0?BufferedImage.TYPE_4BYTE_ABGR:base.getType();
			result=new BufferedImage(base.getWidth(),base.getHeight(),type);
			result.getGraphics().drawImage(base,0,0,null);
			for(int i=0;i<this.layers.size()&&i<this.colors.length&&i<17;i++)
			{
				BufferedImage mask=MinecraftForgeClient.getImageLayer(new ResourceLocation(this.layers.get(i)),resources);
				if(mask.getWidth()!=result.getWidth()||mask.getHeight()!=result.getHeight()||mask.getType()!=BufferedImage.TYPE_4BYTE_ABGR)
				{
					continue;
				}
				for(int y=0;y<mask.getHeight();y++)
				{
					for(int x=0;x<mask.getWidth();x++)
					{
						int pixel=mask.getRGB(x,y);
						if((pixel&0xFF000000)!=0)
						{
							int alpha=(pixel&0xFF0000)<<8&0xFF000000;
							int color=MathHelper.multiplyColor(base.getRGB(x,y),this.colors[i])&0xFFFFFF;
							mask.setRGB(x,y,alpha|color);
						}
					}
				}
				result.getGraphics().drawImage(mask,0,0,null);
			}
		}
		catch(IOException e)
		{
			LogHelper.error("Couldn't make a dyed banner texture: "+e);
			return;
		}
		TextureUtil.uploadTextureImage(this.getGlTextureId(),result);
	}
}
