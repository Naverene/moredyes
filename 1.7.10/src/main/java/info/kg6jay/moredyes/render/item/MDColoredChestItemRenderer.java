package info.kg6jay.moredyes.render.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.kg6jay.moredyes.reference.Reference;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static info.kg6jay.moredyes.block.MDBlock.colors;

@SideOnly(Side.CLIENT)
public class MDColoredChestItemRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation[] DOUBLE_CHEST = new ResourceLocation[colors.length];
    private static final ResourceLocation[] NORMAL_CHEST = new ResourceLocation[colors.length];
    private ModelChest model = new ModelChest();
    private ModelChest large_model = new ModelLargeChest();
    private boolean field_147509_j;

    public MDColoredChestItemRenderer()
    {
        for (int i = 0; i < colors.length; i++)
        {
            DOUBLE_CHEST[i] = new ResourceLocation(Reference.MOD_ID + ":textures/blocks/chest/double/" + colors[i] + ".png"); //g
            NORMAL_CHEST[i] = new ResourceLocation(Reference.MOD_ID + ":textures/blocks/chest/normal/" + colors[i] + ".png"); //d
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_)
    {

    }

    @Override
    protected void bindTexture(ResourceLocation p_147499_1_)
    {
        TextureManager texturemanager = this.field_147501_a.field_147553_e;

        if (texturemanager != null)
        {
            texturemanager.bindTexture(p_147499_1_);
        }
    }
}