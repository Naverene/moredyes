package net.neverandy.moredyes.compat.ironchest.client;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.compat.ironchest.IronChestCompat;
import net.neverandy.moredyes.compat.ironchest.TileEntityDyedIronChest;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.utility.ColorUtil;

/**
 * Draws a dyed Iron Chests chest like Iron Chests draws its own (TileEntityIronChestRenderer): the vanilla chest model,
 * drawn twice, once with the grey panels multiplied by the dye color and once with the rest of the texture in its own
 * colors (see DyedIronChestTextures). A crystal chest also shows the items it holds most of.
 */
@SideOnly(Side.CLIENT)
public class DyedIronChestRenderer extends TileEntitySpecialRenderer<TileEntityDyedIronChest>
{
	private static final float[][] ITEM_SPOTS={{0.3F,0.45F,0.3F},{0.7F,0.45F,0.3F},{0.3F,0.45F,0.7F},{0.7F,0.45F,0.7F},
			{0.3F,0.1F,0.3F},{0.7F,0.1F,0.3F},{0.3F,0.1F,0.7F},{0.7F,0.1F,0.7F},{0.5F,0.32F,0.5F}};

	private final ModelChest model=new ModelChest();
	private final Random random=new Random();
	private RenderEntityItem itemRenderer;
	private EntityItem shownItem;

	@Override
	public void render(TileEntityDyedIronChest chest,double x,double y,double z,float partialTicks,int destroyStage,float alpha)
	{
		if(chest==null||chest.isInvalid())
		{
			return;
		}
		IronChestCompat.Tier tier=chest.getTier();
		// An item has no world to face in, and is shown from the front.
		EnumFacing facing=chest.hasWorld()?chest.getFacing():EnumFacing.SOUTH;
		boolean crystal=tier.type.isTransparent();

		GlStateManager.enableDepth();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		GlStateManager.pushMatrix();
		if(crystal)
		{
			GlStateManager.disableCull();
		}
		GlStateManager.translate((float)x,(float)y+1.0F,(float)z+1.0F);
		GlStateManager.scale(1.0F,-1.0F,-1.0F);
		GlStateManager.translate(0.5F,0.5F,0.5F);
		GlStateManager.rotate(angle(facing),0.0F,1.0F,0.0F);
		GlStateManager.translate(-0.5F,-0.5F,-0.5F);
		if(crystal)
		{
			GlStateManager.scale(1.0F,0.99F,1.0F);
		}
		float lid=chest.prevLidAngle+(chest.lidAngle-chest.prevLidAngle)*partialTicks;
		lid=1.0F-lid;
		lid=1.0F-lid*lid*lid;
		this.model.chestLid.rotateAngleX=-(lid*((float)Math.PI/2F));
		this.model.chestKnob.rotateAngleX=this.model.chestLid.rotateAngleX;

		if(destroyStage>=0)
		{
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F,4.0F,1.0F);
			GlStateManager.translate(0.0625F,0.0625F,0.0625F);
			GlStateManager.matrixMode(5888);
			this.model.renderAll();
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
		else
		{
			ResourceLocation panels=DyedIronChestTextures.INSTANCE.panels(tier);
			if(panels!=null)
			{
				int color=ColorUtil.fromHex(ColorStrings.ALL[chest.getColor()]);
				this.bindTexture(panels);
				GlStateManager.color((color>>16&255)/255.0F,(color>>8&255)/255.0F,(color&255)/255.0F,1.0F);
				this.model.renderAll();
			}
			this.bindTexture(DyedIronChestTextures.INSTANCE.rest(tier));
			GlStateManager.color(1.0F,1.0F,1.0F,1.0F);
			this.model.renderAll();
		}
		if(crystal)
		{
			GlStateManager.enableCull();
		}
		GlStateManager.popMatrix();
		GlStateManager.color(1.0F,1.0F,1.0F,1.0F);

		if(crystal&&chest.hasWorld()&&chest.getDistanceSq(this.rendererDispatcher.entityX,this.rendererDispatcher.entityY,this.rendererDispatcher.entityZ)<128.0D)
		{
			this.renderItems(chest,x,y,z,partialTicks);
		}
	}

	private static float angle(EnumFacing facing)
	{
		switch(facing)
		{
		case NORTH:
			return 180.0F;
		case WEST:
			return 90.0F;
		case EAST:
			return 270.0F;
		default:
			return 0.0F;
		}
	}

	/** The items a crystal chest holds most of, turning inside it, as Iron Chests shows them. */
	private void renderItems(TileEntityDyedIronChest chest,double x,double y,double z,float partialTicks)
	{
		this.random.setSeed(254L);
		int spot=0;
		float scale=0.7F;
		float turn=(float)(360.0D*(System.currentTimeMillis()&0x3FFFL)/0x3FFFL)-partialTicks;
		if(chest.getTopItems().get(1).isEmpty())
		{
			spot=8;
			scale=0.85F;
		}
		if(this.shownItem==null)
		{
			this.shownItem=new EntityItem(this.getWorld());
		}
		if(this.itemRenderer==null)
		{
			this.itemRenderer=new RenderEntityItem(Minecraft.getMinecraft().getRenderManager(),Minecraft.getMinecraft().getRenderItem())
			{
				@Override
				public int getModelCount(ItemStack stack)
				{
					return Math.min(stack.getCount()/32,15)+1;
				}
				@Override
				public boolean shouldBob()
				{
					return false;
				}
				@Override
				public boolean shouldSpreadItems()
				{
					return true;
				}
			};
		}
		this.shownItem.hoverStart=0.0F;
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x,(float)y,(float)z);
		for(ItemStack stack:chest.getTopItems())
		{
			if(spot>=ITEM_SPOTS.length)
			{
				break;
			}
			if(stack.isEmpty())
			{
				spot++;
				continue;
			}
			float[] at=ITEM_SPOTS[spot++];
			GlStateManager.pushMatrix();
			GlStateManager.translate(at[0],at[1],at[2]);
			GlStateManager.rotate(turn,0.0F,1.0F,0.0F);
			GlStateManager.scale(scale,scale,scale);
			this.shownItem.setItem(stack);
			this.itemRenderer.doRender(this.shownItem,0.0D,0.0D,0.0D,0.0F,partialTicks);
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}
}
