package info.kg6jay.moredyes.render.tileEntity;

import cpw.mods.fml.common.FMLLog;
import info.kg6jay.moredyes.block.MDColoredChest;
import info.kg6jay.moredyes.block.tileEntity.TileEntityColoredChest;
import info.kg6jay.moredyes.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;

import static info.kg6jay.moredyes.block.MDBlock.colors;

public class TileEntityColoredChestRender //extends TileEntitySpecialRenderer implements IItemRenderer
{
    /*
    //private static final ResourceLocation[] DOUBLE_CHEST = new ResourceLocation[colors.length];
    //private static final ResourceLocation[] NORMAL_CHEST = new ResourceLocation[colors.length];

    private static ResourceLocation DOUBLE_CHEST, NORMAL_CHEST;
    private ModelChest model, large_model;
    private ArrayList<ModelChest> moddelArray;
    private ArrayList<ModelLargeChest> largeChestArray;

    public TileEntityColoredChestRender()
    {
        model = new ModelChest();
        large_model = new ModelLargeChest();

        //for (int i = 0; i < colors.length; i++)
        //{
            DOUBLE_CHEST = new ResourceLocation(Reference.MOD_ID + ":textures/blocks/chest/double/4c266.png"); //g
            NORMAL_CHEST = new ResourceLocation(Reference.MOD_ID + ":textures/blocks/chest/normal/4c2c66.png");//d
        //}
    }
/*
    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float p_147500_8_)
    {
        int i;

        if (!te.hasWorldObj())
        {
            i = 0;
        } else
        {
            MDColoredChest block = (MDColoredChest) te.getBlockType();
            i = te.getBlockMetadata();

            if (block instanceof MDColoredChest && i == 0)
            {
                try
                {
                    block.func_149954_e(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord);
                } catch (ClassCastException e)
                {
                    FMLLog.severe("Attempted to render a chest at %d,  %d, %d that was not a chest", te.xCoord, te.yCoord, te.zCoord);
                }
                i = te.blockMetadata;
            }

            //chest.checkForAdjacentChests();
        }
        if (((TileEntityColoredChest) te).adjacentChestZNeg == null && ((TileEntityColoredChest) te).adjacentChestXNeg == null)
        {
            ModelChest mc;

            if (((TileEntityColoredChest) te).adjacentChestXPos == null && ((TileEntityColoredChest) te).adjacentChestZPos == null)
            {
                mc = this.model;
                if (((TileEntityColoredChest) te).adjacentChestXPos == null && ((TileEntityColoredChest) te).adjacentChestZPos == null)
                {
                    this.bindTexture(DOUBLE_CHEST);
                } else
                {
                    mc = this.large_model;
                    this.bindTexture(NORMAL_CHEST);
                }

                GL11.glPushMatrix();
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
                GL11.glScalef(1.0F, -1.0F, -1.0F);
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
                short short1 = 0;

                if (i == 2)
                {
                    short1 = 180;
                }

                if (i == 3)
                {
                    short1 = 0;
                }

                if (i == 4)
                {
                    short1 = 90;
                }

                if (i == 5)
                {
                    short1 = -90;
                }

                if (i == 2 && ((TileEntityColoredChest) te).adjacentChestXPos != null)
                {
                    GL11.glTranslatef(1.0F, 0.0F, 0.0F);
                }

                if (i == 5 && ((TileEntityColoredChest) te).adjacentChestZPos != null)
                {
                    GL11.glTranslatef(0.0F, 0.0F, -1.0F);
                }

                GL11.glRotatef(short1, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                float f1 = ((TileEntityColoredChest) te).prevLidAngle + (((TileEntityColoredChest) te).lidAngle - ((TileEntityColoredChest) te).prevLidAngle) * p_147500_8_;
                float f2;

                if (((TileEntityColoredChest) te).adjacentChestZNeg != null)
                {
                    f2 = ((TileEntityColoredChest) te).adjacentChestZNeg.prevLidAngle + (((TileEntityColoredChest) te).adjacentChestZNeg.lidAngle - ((TileEntityColoredChest) te).adjacentChestZNeg.prevLidAngle) * p_147500_8_;

                    if (f2 > f1)
                    {
                        f1 = f2;
                    }
                }

                if (((TileEntityColoredChest) te).adjacentChestXNeg != null)
                {
                    f2 = ((TileEntityColoredChest) te).adjacentChestXNeg.prevLidAngle + (((TileEntityColoredChest) te).adjacentChestXNeg.lidAngle - ((TileEntityColoredChest) te).adjacentChestXNeg.prevLidAngle) * p_147500_8_;

                    if (f2 > f1)
                    {
                        f1 = f2;
                    }
                }

                f1 = 1.0F - f1;
                f1 = 1.0F - f1 * f1 * f1;
                mc.chestLid.rotateAngleX = -(f1 * (float) Math.PI / 2.0F);
                mc.renderAll();
                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
                GL11.glPopMatrix();
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_)
    {
        int i;

        if (!p_147500_1_.hasWorldObj())
        {
            i = 0;
        }
        else
        {
            Block block = p_147500_1_.getBlockType();
            i = p_147500_1_.getBlockMetadata();

            if (block instanceof BlockChest && i == 0)
            {
                try
                {
                    ((BlockChest)block).func_149954_e(p_147500_1_.getWorldObj(), p_147500_1_.xCoord, p_147500_1_.yCoord, p_147500_1_.zCoord);
                }
                catch (ClassCastException e)
                {
                    FMLLog.severe("Attempted to render a chest at %d,  %d, %d that was not a chest", p_147500_1_.xCoord, p_147500_1_.yCoord, p_147500_1_.zCoord);
                }
                i = p_147500_1_.getBlockMetadata();
            }

            ((TileEntityColoredChest) p_147500_1_).checkForAdjacentChests();
        }

        if (((TileEntityColoredChest) p_147500_1_).adjacentChestZNeg == null && ((TileEntityColoredChest)p_147500_1_).adjacentChestXNeg == null)
        {
            ModelChest modelchest;

            if (((TileEntityColoredChest)p_147500_1_).adjacentChestXPos == null && ((TileEntityColoredChest)p_147500_1_).adjacentChestZPos == null)
            {
                modelchest = this.field_147510_h;

                if (p_147500_1_.func_145980_j() == 1)
                {
                    //this.bindTexture(field_147506_e);
                }
                else if (this.field_147509_j)
                {
                    //this.bindTexture(field_147503_f);
                }
                else
                {
                    this.bindTexture(DOUBLE_CHEST);
                }
            }
            else
            {
                modelchest = this.field_147511_i;

                if (p_147500_1_.func_145980_j() == 1)
                {
                    this.bindTexture(field_147507_b);
                }
                else if (this.field_147509_j)
                {
                    this.bindTexture(field_147508_c);
                }
                else
                {
                    this.bindTexture(NORMAL_CHEST);
                }
            }

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float)p_147500_2_, (float)p_147500_4_ + 1.0F, (float)p_147500_6_ + 1.0F);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            short short1 = 0;

            if (i == 2)
            {
                short1 = 180;
            }

            if (i == 3)
            {
                short1 = 0;
            }

            if (i == 4)
            {
                short1 = 90;
            }

            if (i == 5)
            {
                short1 = -90;
            }

            if (i == 2 && p_147500_1_.adjacentChestXPos != null)
            {
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
            }

            if (i == 5 && p_147500_1_.adjacentChestZPos != null)
            {
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            }

            GL11.glRotatef((float)short1, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float f1 = p_147500_1_.prevLidAngle + (p_147500_1_.lidAngle - p_147500_1_.prevLidAngle) * p_147500_8_;
            float f2;

            if (p_147500_1_.adjacentChestZNeg != null)
            {
                f2 = p_147500_1_.adjacentChestZNeg.prevLidAngle + (p_147500_1_.adjacentChestZNeg.lidAngle - p_147500_1_.adjacentChestZNeg.prevLidAngle) * p_147500_8_;

                if (f2 > f1)
                {
                    f1 = f2;
                }
            }

            if (p_147500_1_.adjacentChestXNeg != null)
            {
                f2 = p_147500_1_.adjacentChestXNeg.prevLidAngle + (p_147500_1_.adjacentChestXNeg.lidAngle - p_147500_1_.adjacentChestXNeg.prevLidAngle) * p_147500_8_;

                if (f2 > f1)
                {
                    f1 = f2;
                }
            }

            f1 = 1.0F - f1;
            f1 = 1.0F - f1 * f1 * f1;
            modelchest.chestLid.rotateAngleX = -(f1 * (float)Math.PI / 2.0F);
            modelchest.renderAll();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {

    }
    */
}

