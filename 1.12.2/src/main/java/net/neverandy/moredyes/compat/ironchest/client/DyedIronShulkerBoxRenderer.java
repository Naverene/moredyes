package net.neverandy.moredyes.compat.ironchest.client;

import com.google.common.primitives.SignedBytes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.neverandy.moredyes.compat.ironchest.BlockDyedIronShulkerBox;
import net.neverandy.moredyes.compat.ironchest.IronShulkerBoxCompat;
import net.neverandy.moredyes.compat.ironchest.TileEntityDyedIronShulkerBox;
import net.neverandy.moredyes.reference.ColorStrings;
import net.neverandy.moredyes.utility.ColorUtil;

/**
 * Draws a dyed Iron Chests shulker box as Iron Chests' TileEntityIronShulkerBoxRenderer draws its own, but twice: once
 * with the grey shell texture multiplied by the dye color and once with the metal trim in its own colors (see
 * DyedIronShulkerBoxTextures). A crystal box also shows the items it holds.
 */
@SideOnly(Side.CLIENT)
public class DyedIronShulkerBoxRenderer extends TileEntitySpecialRenderer<TileEntityDyedIronShulkerBox>
{
	/** Where a crystal box shows its top items, as in Iron Chests. */
	private static final float[][] SHIFTS={{0.3F,0.45F,0.3F},{0.7F,0.45F,0.3F},{0.3F,0.45F,0.7F},{0.7F,0.45F,0.7F},
			{0.3F,0.1F,0.3F},{0.7F,0.1F,0.3F},{0.3F,0.1F,0.7F},{0.7F,0.1F,0.7F},{0.5F,0.32F,0.5F}};

	private final ModelShulker model=new ModelShulker();
	private RenderEntityItem itemRenderer;
	private EntityItem customItem;

	@Override
	public void render(TileEntityDyedIronShulkerBox box,double x,double y,double z,float partialTicks,int destroyStage,float alpha)
	{
		if(box==null||box.isInvalid())
		{
			return;
		}
		IronShulkerBoxCompat.Tier tier=IronShulkerBoxCompat.Tier.of(box.getType());
		if(tier==null)
		{
			return;
		}
		EnumFacing facing=EnumFacing.UP;
		if(box.hasWorld()&&this.getWorld().getBlockState(box.getPos()).getBlock() instanceof BlockDyedIronShulkerBox)
		{
			facing=box.getFacing();
		}
		float progress=box.getProgress(partialTicks);

		GlStateManager.enableDepth();
		GlStateManager.depthFunc(515);
		GlStateManager.depthMask(true);
		GlStateManager.disableCull();
		if(destroyStage>=0)
		{
			this.bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F,4.0F,1.0F);
			GlStateManager.translate(0.0625F,0.0625F,0.0625F);
			GlStateManager.matrixMode(5888);
			this.draw(x,y,z,facing,progress);
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
		else
		{
			ResourceLocation shell=DyedIronShulkerBoxTextures.INSTANCE.shell(tier);
			if(shell!=null)
			{
				int rgb=ColorUtil.fromHex(ColorStrings.ALL[box.getDyeIndex()]);
				this.bindTexture(shell);
				GlStateManager.color((rgb>>16&255)/255.0F,(rgb>>8&255)/255.0F,(rgb&255)/255.0F,alpha);
				this.draw(x,y,z,facing,progress);
			}
			this.bindTexture(DyedIronShulkerBoxTextures.INSTANCE.trim(tier));
			GlStateManager.color(1.0F,1.0F,1.0F,alpha);
			this.draw(x,y,z,facing,progress);
		}
		GlStateManager.enableCull();
		GlStateManager.color(1.0F,1.0F,1.0F,1.0F);

		if(tier.type.isTransparent()&&box.hasWorld()
				&&box.getDistanceSq(this.rendererDispatcher.entityX,this.rendererDispatcher.entityY,this.rendererDispatcher.entityZ)<128.0D)
		{
			this.renderItems(box,x,y,z,partialTicks);
		}
	}

	/** The box model, facing its way, with the lid as far open as it is. */
	private void draw(double x,double y,double z,EnumFacing facing,float progress)
	{
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.translate((float)x+0.5F,(float)y+1.5F,(float)z+0.5F);
		GlStateManager.scale(1.0F,-1.0F,-1.0F);
		GlStateManager.translate(0.0F,1.0F,0.0F);
		GlStateManager.scale(0.9995F,0.9995F,0.9995F);
		GlStateManager.translate(0.0F,-1.0F,0.0F);
		switch(facing)
		{
			case DOWN:
				GlStateManager.translate(0.0F,2.0F,0.0F);
				GlStateManager.rotate(180.0F,1.0F,0.0F,0.0F);
				break;
			case NORTH:
				GlStateManager.translate(0.0F,1.0F,1.0F);
				GlStateManager.rotate(90.0F,1.0F,0.0F,0.0F);
				GlStateManager.rotate(180.0F,0.0F,0.0F,1.0F);
				break;
			case SOUTH:
				GlStateManager.translate(0.0F,1.0F,-1.0F);
				GlStateManager.rotate(90.0F,1.0F,0.0F,0.0F);
				break;
			case WEST:
				GlStateManager.translate(-1.0F,1.0F,0.0F);
				GlStateManager.rotate(90.0F,1.0F,0.0F,0.0F);
				GlStateManager.rotate(-90.0F,0.0F,0.0F,1.0F);
				break;
			case EAST:
				GlStateManager.translate(1.0F,1.0F,0.0F);
				GlStateManager.rotate(90.0F,1.0F,0.0F,0.0F);
				GlStateManager.rotate(90.0F,0.0F,0.0F,1.0F);
				break;
			default:
				break;
		}
		this.model.base.render(0.0625F);
		GlStateManager.translate(0.0F,-progress*0.5F,0.0F);
		GlStateManager.rotate(270.0F*progress,0.0F,1.0F,0.0F);
		this.model.lid.render(0.0625F);
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	/** The crystal box's top items, turning, as Iron Chests draws them. */
	private void renderItems(TileEntityDyedIronShulkerBox box,double x,double y,double z,float partialTicks)
	{
		int shift=0;
		float blockScale=0.7F;
		float time=(float)(360.0D*(System.currentTimeMillis()&16383L)/16383.0D)-partialTicks;
		if(box.getTopItems().get(1).isEmpty())
		{
			shift=8;
			blockScale=0.85F;
		}
		if(this.customItem==null)
		{
			this.customItem=new EntityItem(this.getWorld());
		}
		this.customItem.hoverStart=0.0F;
		if(this.itemRenderer==null)
		{
			this.itemRenderer=new RenderEntityItem(Minecraft.getMinecraft().getRenderManager(),Minecraft.getMinecraft().getRenderItem())
			{
				@Override
				public int getModelCount(ItemStack stack)
				{
					return SignedBytes.saturatedCast(Math.min(stack.getCount()/32,15)+1);
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
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x,(float)y,(float)z);
		for(ItemStack item:box.getTopItems())
		{
			if(shift>=SHIFTS.length)
			{
				break;
			}
			if(item.isEmpty())
			{
				shift++;
				continue;
			}
			float[] at=SHIFTS[shift++];
			GlStateManager.pushMatrix();
			GlStateManager.translate(at[0],at[1],at[2]);
			GlStateManager.rotate(time,0.0F,1.0F,0.0F);
			GlStateManager.scale(blockScale,blockScale,blockScale);
			this.customItem.setItem(item);
			this.itemRenderer.doRender(this.customItem,0.0D,0.0D,0.0D,0.0F,partialTicks);
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}
}
